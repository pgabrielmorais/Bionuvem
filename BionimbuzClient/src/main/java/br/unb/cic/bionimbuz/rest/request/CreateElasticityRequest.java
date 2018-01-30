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
package br.unb.cic.bionimbuz.rest.request;

/**
 *
 * @author biolabid2
 */
public class CreateElasticityRequest implements RequestInfo {
    
    private String provider;
    private String type;
    private String instanceName;
    private String operation;
    private String idInstance;

     public CreateElasticityRequest(){
         
     }
     
    public CreateElasticityRequest(String provider, String type, String instanceName, String operation, String idInstance) {
        this.provider = provider;
        this.type = type;
        this.instanceName = instanceName;
        this.operation = operation;
        this.idInstance = idInstance;
    }
    
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getIdInstance() {
        return idInstance;
    }

    public void setIdInstance(String idInstance) {
        this.idInstance = idInstance;
    }
    
}
