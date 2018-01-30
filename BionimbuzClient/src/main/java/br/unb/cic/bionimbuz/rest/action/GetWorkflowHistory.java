package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.GetWorkflowHistoryResponse;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;

/**
 *
 * @author Vinicius
 */
public class GetWorkflowHistory extends Action {

    private static final String REST_WORKFLOW_HISTORY_URL = "/rest/workflow/history";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_WORKFLOW_HISTORY_URL);

    }

    @Override
    public ResponseInfo execute() {
        logAction(REST_WORKFLOW_HISTORY_URL, GetWorkflowHistory.class);

        GetWorkflowHistoryResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), GetWorkflowHistoryResponse.class);

        return response;
    }

}
