package br.unb.cic.bionimbuz.rest.request;

/**
 *
 * @author Vinicius
 */
public class GetConfigurationsRequest implements RequestInfo {

    private boolean request;

    public GetConfigurationsRequest() {
    }

    public GetConfigurationsRequest(boolean request) {
        this.request = request;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }
}
