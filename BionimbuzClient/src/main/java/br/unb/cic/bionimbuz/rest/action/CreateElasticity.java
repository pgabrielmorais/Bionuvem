/*
 *  BionimbuzClient is a federated cloud platform.
 * Copyright (C) 2017 Developted in Laboratory of Bioinformatics and Data (LaBiD), 
Department of Computer Science, University of Brasilia, Brazil
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import br.unb.cic.bionimbuz.rest.request.CreateElasticityRequest;
import br.unb.cic.bionimbuz.rest.response.CreateElasticityResponse;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author biolabid2
 */
public class CreateElasticity extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateElasticity.class);
    private static final String REST_CREATE_ELASTICITY_URL = "/rest/elasticity/create";
    
    @Override
    public void setup(Client client, RequestInfo reqInfo) {
        this.target = client.target(super.bionimbuzAddress);
        this.request = (CreateElasticityRequest) reqInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_CREATE_ELASTICITY_URL);
    }

    @Override
    public ResponseInfo execute() {
      logAction(REST_CREATE_ELASTICITY_URL, CreateElasticity.class);
        try{
             CreateElasticityResponse response = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON), CreateElasticityResponse.class);

        return response;
        }catch(Exception e){
            LOGGER.error("[Exception] " + e.getMessage());
//            return new CreateElasticityResponse(response.readEntity(boolean.class));
        }
        return null;
    }
    
//        
//    @Override
//    public ResponseInfo execute() {
//        logAction(REST_START_ELASTICITY_URL, CreateElasticity.class);
//
//        Response response = target
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(request, MediaType.APPLICATION_JSON), Response.class);
//
//        return new StartWorkflowResponse(response.readEntity(boolean.class));
//    }

    
}
