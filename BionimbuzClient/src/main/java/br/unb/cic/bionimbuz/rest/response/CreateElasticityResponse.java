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
package br.unb.cic.bionimbuz.rest.response;

/**
 *
 * @author biolabid2
 */
public class CreateElasticityResponse implements ResponseInfo {
    private String ip;
    private boolean done;

    public CreateElasticityResponse() {
    }
    
    public CreateElasticityResponse(boolean done) {
        this.done = done;
    }

    public CreateElasticityResponse(String ip, boolean done) {
        this.ip = ip;
        this.done = done;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
    
    
}
