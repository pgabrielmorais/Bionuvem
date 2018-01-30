package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.LogoutResponse;

public class Logout extends Action {

    private static final String REST_LOGOUT_URL = "/rest/logout";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_LOGOUT_URL);
    }

    @Override
    public LogoutResponse execute() {
        logAction(REST_LOGOUT_URL, Logout.class);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), Response.class);

        return new LogoutResponse(response.readEntity(boolean.class));
    }

}
