package br.unb.cic.bionimbuz.communication;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.rest.action.Action;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;
import br.unb.cic.bionimbuz.rest.response.UploadResponse;

/**
 * Executes the REST action. Send the request to the server and wait for a
 * response
 *
 * @author monstrim
 *
 */
public class RestCommunicator implements Communicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestCommunicator.class);
    private static final String PING_URL = ConfigurationRepository.getConfig().getBionimbuzAddress() + "/rest/ping";

    /**
     * Send a request to the Bionimbuz main server to execute a requested
     * operation and send back a ResponseInfo object
     *
     * @param action
     * @param requestInfo
     * @return
     * @throws ServerNotReachableException
     */
    @Override
    public ResponseInfo sendRequest(Action action, RequestInfo requestInfo) throws ServerNotReachableException {
        final Client client = ClientBuilder.newClient();
        action.setup(client, requestInfo);
        action.prepareTarget();
        if (RestCommunicator.ping()) {
            return action.execute();
        }
        return new UploadResponse(false);
    }

    /**
     * Ping server
     *
     * @param hostAddress
     * @return
     * @throws IOException
     */
    public static boolean ping() {
        final HttpGet request = new HttpGet(PING_URL);
        try (
             final CloseableHttpClient httpClient = HttpClients.createDefault();
             final CloseableHttpResponse response = httpClient.execute(request);) {
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (final IOException e) {
            LOGGER.error("Rest Communicator fault: " + e.getMessage());
        }
        return false;
    }
}
