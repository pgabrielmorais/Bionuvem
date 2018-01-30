package br.unb.cic.bionimbuz.rest.request;

import br.unb.cic.bionimbuz.model.FileInfo;

public class DeleteFileRequest extends BaseRequest {

    private FileInfo fileInfo;

    public DeleteFileRequest(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

}
