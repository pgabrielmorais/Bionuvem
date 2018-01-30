package br.unb.cic.bionimbuz.web.beans;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.model.FileInfo;
import br.unb.cic.bionimbuz.rest.service.RestService;

@Named
@SessionScoped
public class FileUploadBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String MAX_STORAGE = "25000 Mb";
    private static final Long MAX_STORAGE_SIZE = 25000000000L; // 25 Gb
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadBean.class);
    private final RestService restService;
    private String allowedTypes;
    @Inject
    private SessionBean sessionBean;

    public FileUploadBean() {
        this.restService = new RestService();
        this.allowedTypes = "/(\\.|\\/)(";
        // Concatenate allowed types to form primefaces attribute
        for (final String allowed : ConfigurationRepository.getSupportedFormats()) {
            this.allowedTypes += allowed.replace(".", "") + "|";
        }
        this.allowedTypes += ")$/";
    }

    /**
     * Triggered when an user uploads a file
     *
     * @param event
     */
    public void handleUploadedFile(FileUploadEvent event) {

        // Verifies if the file doesn't overflow the user storage usage
        final UploadedFile uploadedFile = event.getFile();
        final long fileSize = uploadedFile.getSize();
        if (this.sessionBean.getLoggedUser().getStorageUsage() + fileSize > MAX_STORAGE_SIZE) {
            this.showFacesMessage(FacesMessage.SEVERITY_ERROR, "Seu espaco de armazenamento ultrapassou " + MAX_STORAGE + "!");
            return;
        }

        // Verifies if it wasn't uploaded previously
        for (final FileInfo u : this.sessionBean.getLoggedUser().getFiles()) {
            if (uploadedFile.getFileName().equals(u.getName())) {
                this.showFacesMessage(FacesMessage.SEVERITY_ERROR, "Arquivo existente!");
                return;
            }
        }
        final String tempFilePath = ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + uploadedFile.getFileName();
        // Creates a new FileInfo
        final FileInfo fileInfo = new FileInfo();
        try {
            // Set file information
            fileInfo.setName(uploadedFile.getFileName());
            fileInfo.setSize(fileSize);
            fileInfo.setUploadTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
            fileInfo.setUserId(this.sessionBean.getLoggedUser().getId());
            final File tempFile = new File(tempFilePath);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            uploadedFile.write(tempFile.getAbsolutePath());
            // Calls RestService to upload file
            this.restService.uploadFile(fileInfo);
            // If file was uploaded with success, adds file on user file list
            this.sessionBean.getLoggedUser().getFiles().add(fileInfo);
            // Adds the size of the uploaded file to the user storage usage
            this.sessionBean.getLoggedUser().addStorageUsage(fileSize);
            // Shows message to the user
            this.showFacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo enviado com sucesso!");
        } catch (final Exception e) {
            LOGGER.error("[File Upload Error!]", e);
            this.showFacesMessage(FacesMessage.SEVERITY_ERROR, "Erro Interno. Não foi possível enviar o arquivo");
            final File tempFile = new File(tempFilePath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * Saves temporary file in disk
     *
     * @param fileName
     * @param in
     *
     *            public void saveTempFile(String fileName, InputStream in) {
     *            try { //
     *            write the inputStream to a FileOutputStream outputStream = new
     *            FileOutputStream(new File(ConfigurationRepository. +
     *            fileName));
     *
     *            int read = 0; byte[] bytes = new byte[1024];
     *
     *            while ((read = in.read(bytes)) != -1) {
     *            outputStream.write(bytes, 0,
     *            read); }
     *
     *            // in.close(); LOGGER.info("Temporary file created [path=" +
     *            ConfigurationRepository.UPLOADED_FILES_PATH + fileName + "]");
     *
     *            } catch (IOException e) { LOGGER.error("[IOException - " +
     *            e.getMessage()
     *            + "]"); } }
     */
    // Shows JSF Faces Message to the user
    private void showFacesMessage(Severity severity, String msg) {
        final FacesMessage message = new FacesMessage(severity, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Retrieves user's storage usage
     *
     * @return
     */
    public int getStorageUsage() {
        // Gets user storage usage
        final BigDecimal usage = new BigDecimal(this.sessionBean.getLoggedUser().getStorageUsage());
        /**
         * Returns the user storage usage in percentual. returns int value
         * because PrimeFaces component accepts only int value from 0 to 100
         */
        return usage.divide(new BigDecimal(MAX_STORAGE_SIZE)).multiply(new BigDecimal(100)).intValue();
    }

    public String getAllowedTypes() {
        return this.allowedTypes;
    }

    public void setAllowedTypes(String allowedTypes) {
        this.allowedTypes = allowedTypes;
    }
}
