package br.unb.cic.bionimbuz.rest.request;

import br.unb.cic.bionimbuz.model.FileInfo;

public class UploadRequest implements RequestInfo {

    private FileInfo fileInfo;

    public UploadRequest() {
    }

    public UploadRequest(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

}
