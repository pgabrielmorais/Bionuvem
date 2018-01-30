package br.unb.cic.bionimbuz.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.model.User;
import br.unb.cic.bionimbuz.rest.service.RestService;
import br.unb.cic.bionimbuz.security.PBKDF2;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@RequestScoped
public class SignUpBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignUpBean.class);

    private User user = new User();
    private final RestService restService;

    public SignUpBean() {
        restService = new RestService();
    }

    /**
     * Class RestService to perform a signUp request
     *
     * @param actionEvent
     */
    public void signUp() {
        boolean result = false;

        try {            
            // Hashes user password using bCrypt algorithm.
            user.setPassword(PBKDF2.generatePassword(user.getPassword()));
            user.setStorageUsage(0L);
            // Send to core
            result = restService.signUp(user);
        } catch (ServerNotReachableException e) {
            addMessage("server_offline");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }
        user = new User();
        if(result)
            addMessage("sign_up_success");
        else
            addMessage("sign_up_error");
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect(context.getRequestContextPath() + "/");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SignUpBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
