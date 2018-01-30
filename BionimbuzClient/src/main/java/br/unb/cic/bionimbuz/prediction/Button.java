/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbuz.prediction;

/**
 *
 * @author guilherme
 */


import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
 
@ManagedBean
public class Button {
     
    public void buttonAction(ActionEvent actionEvent) throws IOException {
        InstanceService.addInstances();
        //addMessage("Máquina Virtual Criada");
    }

    public void SLAmessage(){  
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"SLA Aceito!",null));  
    }     
    
    public void onTimeout(){  
        FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Máquina Virtual Criada",null));  
    } 
    
    public void createInstance(){  
        
    }

}