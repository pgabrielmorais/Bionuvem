package br.unb.cic.bionimbuz.communication;

import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.rest.action.Action;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.response.ResponseInfo;

/**
 * Defines the interface that a Communicator should implement
 *
 * @author Vinicius
 */
public interface Communicator {

    /**
     * Receives a RequestInfo, make some Action (sending it via REST) and than
     * return a ResponseInfo
     *
     * @param action
     * @param request
     * @return
     * @throws br.unb.cic.bionimbuz.exception.ServerNotReachableException
     */
    public ResponseInfo sendRequest(Action action, RequestInfo request) throws ServerNotReachableException;
}
