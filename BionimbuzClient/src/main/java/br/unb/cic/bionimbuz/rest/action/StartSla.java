/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.request.StartSlaRequest;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;
import br.unb.cic.bionimbuz.rest.response.StartSlaResponse;
/**
 *
 * @author biolabid2
 */
public class StartSla extends Action{
    private static final Logger LOGGER = LoggerFactory.getLogger(StartSla.class);
    private static final String REST_START_SLA_URL = "/rest/sla/start";
    
    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = (StartSlaRequest) reqInfo;
    }

    @Override
    public void prepareTarget() {
         target = target.path(REST_START_SLA_URL);
    }

    @Override
    public ResponseInfo execute() {
        logAction(REST_START_SLA_URL, StartSla.class);
        try{
             StartSlaResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), StartSlaResponse.class);

        return response;
        }catch(Exception e){
            LOGGER.error("[Exception] " + e.getMessage());
            
            return null;
        }
       
    }
    
}
