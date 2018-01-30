package br.unb.cic.bionimbuz.rest.request;

import br.unb.cic.bionimbuz.model.User;

/**
 * Class that defines a logout request to the BioNimbuZ core
 *
 * @author usuario
 *
 */
public class LogoutRequest extends BaseRequest {

    private User user;

    public LogoutRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
