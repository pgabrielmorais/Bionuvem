package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.GetWorkflowStatusResponse;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;

/**
 * Requests an user's workflow status list
 *
 * @author Vinicius
 */
public class GetWorkflowStatus extends Action {

    private static final String REST_WORKFLOW_STATUS_URL = "/rest/workflow/status";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_WORKFLOW_STATUS_URL);

    }

    @Override
    public ResponseInfo execute() {
        logAction(REST_WORKFLOW_STATUS_URL, GetWorkflowStatus.class);

        GetWorkflowStatusResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), GetWorkflowStatusResponse.class);

        return response;
    }

}
