/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbuz.elasticity;

/**
 *
 * @author guilherme
 */

//import br.unb.cic.bionimbuz.elasticity.AmazonAPI;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
 
@ManagedBean
public class ButtonView {
     
    public void buttonAction(ActionEvent actionEvent) throws IOException {
        //AmazonAPI.createinstance();
        InstanceService service = new InstanceService();
        //service.createInstance("t2.micro");
        
        addMessage("Máquina Virtual Criada");
    }
    
    public void buttonActionCredential(ActionEvent actionEvent) {
        addMessage("Credenciais Aceitas!!");
    }
     
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
