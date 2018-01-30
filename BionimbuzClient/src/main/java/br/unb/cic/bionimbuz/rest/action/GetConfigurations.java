package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.GetConfigurationsResponse;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;

/**
 * Send a request to the server to get the supported Services
 *
 * @author Vinicius
 */
public class GetConfigurations extends Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetConfigurations.class);
    private static final String REST_GET_SERVICES_URL = "/rest/services";

    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_GET_SERVICES_URL);
    }

    @Override
    public ResponseInfo execute() {
        logAction(REST_GET_SERVICES_URL, GetConfigurations.class);

        try {
//            Response r = target
//                    .request(MediaType.APPLICATION_JSON)
//                    .post(Entity.entity(request, MediaType.APPLICATION_JSON), Response.class);
//            
//            List<PluginService> response = r.readEntity(new GenericType<List<PluginService>>() {}); 
            GetConfigurationsResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), GetConfigurationsResponse.class);
            
            return response;
        } catch (Exception e) {
            LOGGER.error("[Exception] " + e.getMessage());
            e.printStackTrace();
            
            return null;
        }

    }
}
