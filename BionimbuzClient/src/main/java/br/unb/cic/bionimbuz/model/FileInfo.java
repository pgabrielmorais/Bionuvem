package br.unb.cic.bionimbuz.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FileInfo {
    
    private String id = UUID.randomUUID().toString();
    private String name;
    private long size;
    private long userId;
    private String uploadTimestamp;
    private String hash;
    private String bucket;
    @JsonProperty("payload")
    private byte[] payload;
    
    // --------------------------------------------------------------
    // Constructors
    // --------------------------------------------------------------
    public FileInfo() {
        super();
    }
    
    // --------------------------------------------------------------
    // get/set
    // --------------------------------------------------------------
    public FileInfo(String id) {
        this.id = id;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getPayload() {
        return this.payload;
    }
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public String getUploadTimestamp() {
        return this.uploadTimestamp;
    }
    public void setUploadTimestamp(String uploadedTimestamp) {
        this.uploadTimestamp = uploadedTimestamp;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getHash() {
        return this.hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public String getBucket() {
        return this.bucket;
    }
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
