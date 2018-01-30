package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.DeleteFileRequest;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.DeleteFileResponse;

public class DeleteFile extends Action {
    
    private static final String REST_DELETE_FILE_URL = "/rest/file/";
    private String fileId;
    
    // --------------------------------------------------------------
    // Constructors.
    // --------------------------------------------------------------
    public DeleteFile() {
        super();
    }
    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
        this.fileId = ((DeleteFileRequest) this.request).getFileInfo().getId();
    }
    
    @Override
    public void prepareTarget() {
        // Path: /rest/file/{fileId}
        this.target = this.target.path(REST_DELETE_FILE_URL + this.fileId);
    }
    
    @Override
    public DeleteFileResponse execute() {
        logAction(REST_DELETE_FILE_URL, DeleteFile.class);
        this.target.request(MediaType.APPLICATION_JSON).delete();
        return new DeleteFileResponse(true);
    }
    
}
