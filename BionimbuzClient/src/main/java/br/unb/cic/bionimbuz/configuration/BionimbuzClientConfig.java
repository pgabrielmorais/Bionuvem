package br.unb.cic.bionimbuz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Vinicius
 */
public class BionimbuzClientConfig {
    
    @JsonProperty("root-path")
    private String rootPath;
    @JsonProperty("tmp-workflow-folder")
    private String temporaryWorkflowFolder;
    @JsonProperty("address")
    private String address;
    @JsonProperty("bionimbuz-address")
    private String bionimbuzAddress;
    @JsonProperty("buckets-folder")
    private String bucketsFolder;
    @JsonProperty("buckets-auth-folder")
    private String bucketsAuthFolder;
    @JsonProperty("gcloud-folder")
    private String gcloudFolder;
    @JsonProperty("storage-mode")
    private String storageMode;
    
    public String getRootPath() {
        return this.rootPath;
    }
    public String getTemporaryWorkflowFolder() {
        if (!this.temporaryWorkflowFolder.endsWith("/")) {
            this.temporaryWorkflowFolder += "/";
        }
        return this.temporaryWorkflowFolder;
    }
    public void setTemporaryWorkflowFolder(String temporaryWorkflowFolder) {
        this.temporaryWorkflowFolder = temporaryWorkflowFolder;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBionimbuzAddress() {
        return this.bionimbuzAddress;
    }
    public void setBionimbuzAddress(String bionimbuzAddress) {
        this.bionimbuzAddress = bionimbuzAddress;
    }
    public String getBucketsFolder() {
        return this.bucketsFolder;
    }
    public void setBucketsFolder(String bucketsFolder) {
        this.bucketsFolder = bucketsFolder;
    }
    public String getBucketsAuthFolder() {
        return this.bucketsAuthFolder;
    }
    public String getGcloudFolder() {
        return this.gcloudFolder;
    }
    public void setGcloudFolder(String gcloudFolder) {
        this.gcloudFolder = gcloudFolder;
    }
    public String getStorageMode() {
        return this.storageMode;
    }
    public void setStorageMode(String storageMode) {
        this.storageMode = storageMode;
    }
    public void log() {
        final Logger LOGGER = LoggerFactory.getLogger(BionimbuzClientConfig.class);
        LOGGER.info("Web Application configurations:");
        LOGGER.info(" - bionimbuzAddress=" + this.bionimbuzAddress);
        LOGGER.info(" - tmpWorkflowFolder=" + this.temporaryWorkflowFolder);
        LOGGER.info("========================================");
    }
}
