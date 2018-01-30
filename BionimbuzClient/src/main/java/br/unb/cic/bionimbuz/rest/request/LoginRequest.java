package br.unb.cic.bionimbuz.rest.request;

import br.unb.cic.bionimbuz.model.User;

/**
 * Class that defines a login request to the BioNimbuZ core
 *
 * @author usuario
 *
 */
public class LoginRequest extends BaseRequest {

    private User user;

    public LoginRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
