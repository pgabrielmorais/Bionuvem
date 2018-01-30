package br.unb.cic.bionimbuz.rest.action;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;

public abstract class Action {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);
    protected String bionimbuzAddress;
    protected RequestInfo request;
    protected WebTarget target;
    
    // --------------------------------------------------------------
    // Constructors.
    // --------------------------------------------------------------
    public Action() {
        super();
        this.bionimbuzAddress = ConfigurationRepository.getConfig().getBionimbuzAddress();
    }
    
    // --------------------------------------------------------------
    // Abstracts methods.
    // --------------------------------------------------------------
    public abstract void setup(Client client, RequestInfo reqInfo);
    public abstract void prepareTarget();
    public abstract ResponseInfo execute();
    
    /**
     * Logs the request action
     *
     * @param path
     * @param c
     */
    protected static void logAction(String path, Class<? extends Action> c) {
        LOGGER.info("[" + c.getSimpleName() + "] Sending request to BioNimbuZ (path: " + path + ")");
    }
}
