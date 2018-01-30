package br.unb.cic.bionimbuz.rest.action;

import br.unb.bionimbuz.storage.bucket.BioBucket;
import br.unb.bionimbuz.storage.bucket.CloudStorageMethods;
import br.unb.bionimbuz.storage.bucket.PeriodicCheckerBuckets;
import br.unb.bionimbuz.storage.bucket.methods.CloudMethodsAmazonGoogle;
import java.io.File;
import java.io.IOException;

import javax.ws.rs.client.Client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.model.FileInfo;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.request.UploadRequest;
import br.unb.cic.bionimbuz.rest.response.UploadResponse;
import br.unb.cic.bionimbuz.security.HashUtil;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * Since there is a bug in Resteasy upload method (documented here
 * https://issues.jboss.org/browse/RESTEASY-1201), the implementation of this
 * Action is made with Apache HttpClient.
 *
 * @author Vinicius (with Edrward's help)
 */
public class Upload extends Action {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Upload.class);
    private static final String SERVICE_URL = "/rest/file/upload";
    private final String requestUrl;
    
    // --------------------------------------------------------------
    // Constructors.
    // --------------------------------------------------------------
    public Upload() {
        super();
        this.requestUrl = super.bionimbuzAddress + SERVICE_URL;
    }
    
    // --------------------------------------------------------------
    // * @see
    // br.unb.cic.bionimbuz.rest.action.Action#setup(javax.ws.rs.client.Client,
    // br.unb.cic.bionimbuz.rest.request.RequestInfo)
    // --------------------------------------------------------------
    @Override
    public void setup(Client client, RequestInfo requestInfo) {
        this.request = requestInfo;
    }
    // --------------------------------------------------------------
    // * @see br.unb.cic.bionimbuz.rest.action.Action#prepareTarget()
    // --------------------------------------------------------------
    @Override
    public void prepareTarget() {
        // Nothing to do
    }
    // --------------------------------------------------------------
    // * @see br.unb.cic.bionimbuz.rest.action.Action#execute()
    // --------------------------------------------------------------
    @Override
    public UploadResponse execute() {
        final UploadRequest req = (UploadRequest) this.request;
        final FileInfo fileInfo = req.getFileInfo();
        final File tempFile = new File(ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + fileInfo.getName());
        try (final CloseableHttpClient httpClient = HttpClients.createDefault();) {
            if (ConfigurationRepository.getConfig().getStorageMode().equalsIgnoreCase("1")) { // Cloud Storage
                // Compute and store the hash on metadata object
                final String computedHash = HashUtil.computeNativeSHA3(tempFile.getAbsolutePath());
                fileInfo.setHash(computedHash);
                
                //TODO: Colocar para setar depois que enviou o arquivo, para gravar no banco o no znode,
                ///depois que enviar o arquivo para o bucket PAleativo
                fileInfo.setBucket("bionimbuz-a-br2");
                
               // Transforms the metadata object into a json string
                final String jsonFileInfo = new ObjectMapper().writeValueAsString(fileInfo);
                // Config the http request
                final HttpPost post = new HttpPost(this.requestUrl);
                final MultipartEntityBuilder multpartBuilder = MultipartEntityBuilder.create();
                multpartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
               // multpartBuilder.addPart("file", new FileBody(tempFile, ContentType.DEFAULT_BINARY));
                multpartBuilder.addPart("file_info", new StringBody(jsonFileInfo, ContentType.APPLICATION_JSON));
                final HttpEntity requestEntity = multpartBuilder.build();
                post.setEntity(requestEntity);
                try (final CloseableHttpResponse response = httpClient.execute(post);) {
                    // Handle the http response
                    final HttpEntity responseEntity = response.getEntity();
                    String destName = IOUtils.toString(responseEntity.getContent(), "UTF-8");
                    if (responseEntity != null) {
                        EntityUtils.consume(responseEntity);
                        if (response.getStatusLine() != null) {
                            LOGGER.info("Upload request got status code: " + response.getStatusLine().getStatusCode());
//                        HttpEntity entity = response.getClass().toString();
                            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//                                FileOutputStream fos = new FileOutputStream(ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + "/" + req.getFileInfo().getName());
//                                fos.write(req.getFileInfo().getPayload());
//                                fos.close();
                                PeriodicCheckerBuckets instance = new PeriodicCheckerBuckets();
                                instance.start();
                                BioBucket dest = PeriodicCheckerBuckets.getBucket(destName);

                                LOGGER.info("Uploading file to bucket: " + dest.getName());

                                CloudStorageMethods methodsInstance = new CloudMethodsAmazonGoogle();
                                try {
                                    methodsInstance.StorageAuth(dest.getProvider());
                                    methodsInstance.StorageUploadFile(dest, "/data-folder/", ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + "/", req.getFileInfo().getName());
                                } catch (Throwable t) {
                                    LOGGER.error(t.getMessage());
                                }
                                File aux = new File(ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + "/" + req.getFileInfo().getName());
                                aux.delete();

                                return new UploadResponse(true);
                            }
                            return new UploadResponse(false);
                        }
                    }
                    LOGGER.error("Response from Server is null!");
                }
            } else {
                // Compute and store the hash on metadata object
                final String computedHash = HashUtil.computeNativeSHA3(tempFile.getAbsolutePath());
                fileInfo.setHash(computedHash);
                // Transforms the metadata object into a json string
                final String jsonFileInfo = new ObjectMapper().writeValueAsString(fileInfo);
                // Config the http request
                final HttpPost post = new HttpPost(this.requestUrl);
                final MultipartEntityBuilder multpartBuilder = MultipartEntityBuilder.create();
                multpartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                multpartBuilder.addPart("file", new FileBody(tempFile, ContentType.DEFAULT_BINARY));
                multpartBuilder.addPart("file_info", new StringBody(jsonFileInfo, ContentType.APPLICATION_JSON));
                final HttpEntity requestEntity = multpartBuilder.build();
                post.setEntity(requestEntity);
                try (final CloseableHttpResponse response = httpClient.execute(post);) {
                    // Handle the http response
                    final HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        EntityUtils.consume(responseEntity);
                        if (response.getStatusLine() != null) {
                            LOGGER.info("Upload request got status code: " + response.getStatusLine().getStatusCode());
//                        HttpEntity entity = response.getClass().toString();
                            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                                return new UploadResponse(true);
                            }
                            return new UploadResponse(false);
                        }
                    }
                    LOGGER.error("Response from Server is null!");
                }
            }
        } catch (final InterruptedException | IOException e) {
            LOGGER.error("HTTP request error!", e);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
            return new UploadResponse(false);
        }
}