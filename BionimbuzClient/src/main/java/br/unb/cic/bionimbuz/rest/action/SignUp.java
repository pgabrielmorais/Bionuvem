package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;
import br.unb.cic.bionimbuz.rest.response.SignUpResponse;

public class SignUp extends Action {

    private static final String SIGN_UP_URL = "/rest/signup";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(SIGN_UP_URL);
    }

    @Override
    public ResponseInfo execute() {
        logAction(SIGN_UP_URL, SignUp.class);

        SignUpResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), SignUpResponse.class);

        return response;
    }

}
