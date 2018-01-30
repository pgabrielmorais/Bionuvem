/*
 * Copyright (C) 2017 guilherme
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
package br.unb.cic.bionimbuz.elasticity;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author guilherme
 */

@ManagedBean
public class ElasticityButton {
    
    public void createVM(ActionEvent actionEvent) throws IOException {
        AmazonAPI api = new AmazonAPI();
        
        String ip = api.createinstance("m4.large", "ami-ef741383");
        
        addMessage("Máquina Virtual Criada com ip:" + ip);
    }
    
    public void stopInstance(String id){
        AmazonAPI api = new AmazonAPI();
        api.terminate(id);
    }
    
    public void executeElasticity(String id) throws IOException{
        AmazonAPI api = new AmazonAPI();
        //api.executeElasticity(id, id);
    }
    
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
    
}



