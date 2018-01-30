package br.unb.cic.bionimbuz.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.model.Workflow;
import br.unb.cic.bionimbuz.model.WorkflowStatus;
import br.unb.cic.bionimbuz.rest.service.RestService;

/**
 * Controls workflow/status.xhtml page
 *
 * @author Vinicius
 */
@Named
@SessionScoped
public class WorkflowStatusBean implements Serializable {

    private static final long serialVersionUID = 382824367287385072L;

    @Inject
    private SessionBean sessionBean;

    private final RestService restService = new RestService();

    /**
     * Returns the color of a Status
     *
     * @param status
     * @return
     */
    public String getStatusColor(WorkflowStatus status) {
        return status.getColor();
    }

    /**
     * Returns the status text
     *
     * @param status
     * @return
     */
    public String getStatusText(WorkflowStatus status) {
        return status.toString();
    }

    /**
     * Returns user workflow list
     *
     * @return
     * @throws ServerNotReachableException
     */
    public List<Workflow> getUserWorkflows() throws ServerNotReachableException {
        return this.restService.getWorkflowStatus(this.sessionBean.getLoggedUser());
    }
}
