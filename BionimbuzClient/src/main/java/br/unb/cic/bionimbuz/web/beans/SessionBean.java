package br.unb.cic.bionimbuz.web.beans;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.model.User;
import br.unb.cic.bionimbuz.rest.service.RestService;
import br.unb.cic.bionimbuz.security.PBKDF2;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionBean.class);
    private final RestService restService;
    private boolean serverStatus;
    private User loggedUser = new User();
    private User user = new User();

    // Inicializes session
    public SessionBean() {
        this.restService = new RestService();
    }
    /**
     * Connect to the server to execute a Login request
     *
     * @return redirectView
     */
    public String login() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "";
        User responseUser = null;
        // HttpServletRequest request = (HttpServletRequest)
        // FacesContext.getCurrentInstance().getExternalContext().getRequest();
        // String ipAddres =request.getHeader("X-FORWARDED-FOR");
        //
        // if(ipAddres == null){
        // ipAddres=request.getRemoteAddr();
        // }
        // System.out.println("ip: "+ipAddres);
        /**
         * Hashes password using bCrypt algorithm with computational complexity
         * of 13.
         */
        try {
            password = this.user.getPassword();
            this.user.setPassword("");
            responseUser = this.restService.login(this.user);
        } catch (final Exception e) {
            LOGGER.error("Server is offline (or HTTP 500 - Internal error)");
            e.printStackTrace();
            return "login?faces-redirect=true&internal_error=true";
        }
        // If user.cpf is not null, user sent to the server was found on
        // database and had its data retrieved back to this client
        if (responseUser.getCpf() != null && PBKDF2.validatePassword(password, responseUser.getPassword())) {
            this.loggedUser = responseUser;
            return "success";
        } else {
            // Login not found
            this.user = new User();
            return "login?faces-redirect=true&not_found=true";
        }
    }
    /**
     * Invalidates user session and communicates to the server
     *
     * @return
     */
    public String logout() {
        // Invalidates user session
        final FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance != null) {
            final ExternalContext externalContext = currentInstance.getExternalContext();
            if (externalContext != null) {
                externalContext.invalidateSession();
            }
        }
        // Send a logout message to the server
        try {
            this.restService.logout(this.loggedUser);
        } catch (final ServerNotReachableException e) {
            LOGGER.error("Server logout error", e);
        }
        // Reset user variables
        this.loggedUser = new User();
        this.user = new User();
        return "logout";
    }
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getLoggedUser() {
        return this.loggedUser;
    }
    public boolean isServerStatus() {
        return this.serverStatus;
    }
    public void setServerStatus(boolean serverStatus) {
        this.serverStatus = serverStatus;
    }
}
