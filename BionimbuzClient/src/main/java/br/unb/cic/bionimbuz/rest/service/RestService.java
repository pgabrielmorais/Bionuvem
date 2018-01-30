package br.unb.cic.bionimbuz.rest.service;

import br.unb.cic.bionimbuz.rest.response.CreateElasticityResponse;
import java.io.IOException;
import java.util.List;

import br.unb.cic.bionimbuz.communication.RestCommunicator;
import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.model.FileInfo;
import br.unb.cic.bionimbuz.model.SLA;
import br.unb.cic.bionimbuz.model.User;
import br.unb.cic.bionimbuz.model.Workflow;
import br.unb.cic.bionimbuz.rest.action.CreateElasticity;
import br.unb.cic.bionimbuz.rest.action.DeleteFile;
import br.unb.cic.bionimbuz.rest.action.GetConfigurations;
import br.unb.cic.bionimbuz.rest.action.GetWorkflowHistory;
import br.unb.cic.bionimbuz.rest.action.GetWorkflowStatus;
import br.unb.cic.bionimbuz.rest.action.Login;
import br.unb.cic.bionimbuz.rest.action.Logout;
import br.unb.cic.bionimbuz.rest.action.SignUp;
import br.unb.cic.bionimbuz.rest.action.StartSla;
import br.unb.cic.bionimbuz.rest.action.StartWorkflow;
import br.unb.cic.bionimbuz.rest.action.Upload;
import br.unb.cic.bionimbuz.rest.request.DeleteFileRequest;
import br.unb.cic.bionimbuz.rest.request.CreateElasticityRequest;
import br.unb.cic.bionimbuz.rest.request.GetConfigurationsRequest;
import br.unb.cic.bionimbuz.rest.request.GetWorkflowHistoryRequest;
import br.unb.cic.bionimbuz.rest.request.GetWorkflowStatusRequest;
import br.unb.cic.bionimbuz.rest.request.LoginRequest;
import br.unb.cic.bionimbuz.rest.request.LogoutRequest;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.request.SignUpRequest;
import br.unb.cic.bionimbuz.rest.request.StartSlaRequest;
import br.unb.cic.bionimbuz.rest.request.StartWorkflowRequest;
import br.unb.cic.bionimbuz.rest.request.UploadRequest;
import br.unb.cic.bionimbuz.rest.response.DeleteFileResponse;
import br.unb.cic.bionimbuz.rest.response.GetConfigurationsResponse;
import br.unb.cic.bionimbuz.rest.response.GetWorkflowHistoryResponse;
import br.unb.cic.bionimbuz.rest.response.GetWorkflowStatusResponse;
import br.unb.cic.bionimbuz.rest.response.LoginResponse;
import br.unb.cic.bionimbuz.rest.response.LogoutResponse;
import br.unb.cic.bionimbuz.rest.response.SignUpResponse;
import br.unb.cic.bionimbuz.rest.response.StartSlaResponse;
import br.unb.cic.bionimbuz.rest.response.StartWorkflowResponse;
import br.unb.cic.bionimbuz.rest.response.UploadResponse;

/**
 * This links the Web pages to the REST Comunicator
 *
 * @author Vinicius
 */
public class RestService {

    private final RestCommunicator restCommunicator;

    /**
     * Constructor that initializes the REST Communicator
     */
    public RestService() {
        this.restCommunicator = new RestCommunicator();
    }
    /**
     * Fires a Login request to the server
     *
     * @param user
     * @return
     * @throws ServerNotReachableException
     */
    public User login(User user) throws ServerNotReachableException {
        final RequestInfo loginRequest = new LoginRequest(user);
        final LoginResponse resp = (LoginResponse) this.restCommunicator.sendRequest(new Login(), loginRequest);
        return resp.getUser();
    }
    /**
     * Communicates an user logout to the server
     *
     * @param user
     * @return
     * @throws ServerNotReachableException
     */
    public boolean logout(User user) throws ServerNotReachableException {
        final RequestInfo logoutRequest = new LogoutRequest(user);
        final LogoutResponse resp = (LogoutResponse) this.restCommunicator.sendRequest(new Logout(), logoutRequest);
        return resp.isLogoutSuccess();
    }
    /**
     * Fires an Upload request to the server
     *
     * @param fileInfo
     * @return
     * @throws IOException
     */
    public boolean uploadFile(FileInfo fileInfo) throws Exception {
        final RequestInfo uploadRequest = new UploadRequest(fileInfo);
        final UploadResponse resp = (UploadResponse) this.restCommunicator.sendRequest(new Upload(), uploadRequest);
        if (!resp.isUploaded()) {
            throw new Exception("Failed to send file");
        }
        return true;
    }
    /**
     * Requests a file exclusion by the server
     *
     * @param fileInfo
     * @return
     * @throws ServerNotReachableException
     */
    public boolean deleteFile(FileInfo fileInfo) throws Exception {
        final RequestInfo deleteFileRequest = new DeleteFileRequest(fileInfo);
        final DeleteFileResponse response = (DeleteFileResponse) this.restCommunicator.sendRequest(new DeleteFile(), deleteFileRequest);
        return response.isDeleted();
    }
    /**
     * Requests an user signup
     *
     * @param user
     * @return
     * @throws ServerNotReachableException
     */
    public boolean signUp(User user) throws ServerNotReachableException {
        final RequestInfo signUpRequest = new SignUpRequest(user);
        final SignUpResponse response = (SignUpResponse) this.restCommunicator.sendRequest(new SignUp(), signUpRequest);
        return response.isAdded();
    }
    /**
     * Sends an user workflow to the BioNimbuZ Core to be processed
     *
     * @param workflow
     * @param sla
     * @return
     * @throws ServerNotReachableException
     */
    public boolean startWorkflow(Workflow workflow) throws ServerNotReachableException {
        final RequestInfo startWorkflowRequest = new StartWorkflowRequest(workflow);
        final StartWorkflowResponse response = (StartWorkflowResponse) this.restCommunicator.sendRequest(new StartWorkflow(), startWorkflowRequest);
        return response.isWorkflowProcessed();
    }
    /**
     * Sends an CreateElasticity operation to the BioNimbuZ Core to be processed
     *
     * @param provider
     * @param type
     * @param instanceName
     * @param operation
     * @param idInstance
     * @return String
     * @throws ServerNotReachableException
     */
    public String createelasticity(String provider, String type,String instanceName, String operation, String idInstance) throws ServerNotReachableException {
        final RequestInfo elasticityRequest = new CreateElasticityRequest(provider, type, instanceName, operation, idInstance);
        final CreateElasticityResponse response = (CreateElasticityResponse) this.restCommunicator.sendRequest(new CreateElasticity(), elasticityRequest);
        return response.getIp();
    }
    /**
     * Sends an user SLA QOS to the BioNimbuZ Core to be processed and returns the SLA template
     *
     * @param sla
     * @param workflow
     * @return
     * @throws ServerNotReachableException
     */
    public SLA startSla(SLA sla, Workflow workflow) throws ServerNotReachableException {
//       final RequestInfo startSlaRequest = new StartSlaRequest(sla, workflow);
       final StartSlaResponse response = (StartSlaResponse) this.restCommunicator.sendRequest(new StartSla(), new StartSlaRequest(sla, workflow));
       return response.getSla();
    }
  
    /**
     * Calls server to inform about the status of the user's workflow list
     *
     * @param user
     * @return
     * @throws ServerNotReachableException
     */
    public List<Workflow> getWorkflowStatus(User user) throws ServerNotReachableException {
        final RequestInfo request = new GetWorkflowStatusRequest(user.getId());
        final GetWorkflowStatusResponse response = (GetWorkflowStatusResponse) this.restCommunicator.sendRequest(new GetWorkflowStatus(), request);
        return response.getUserWorkflows();
    }
    /**
     * Send a request to the server to get the supported services list, instances list, references and supported format list
     *
     * @return
     * @throws ServerNotReachableException
     */
    public GetConfigurationsResponse getServices() throws Exception {
        final GetConfigurationsResponse response = (GetConfigurationsResponse) this.restCommunicator.sendRequest(new GetConfigurations(), new GetConfigurationsRequest());
        return response;
    }
    /**
     * Send a request to the server to get workflow history.
     *
     * @param workflowId
     * @return
     * @throws Exception
     */
    public GetWorkflowHistoryResponse getWorkflowHistory(String workflowId) throws Exception {
        final RequestInfo request = new GetWorkflowHistoryRequest(workflowId);
        final GetWorkflowHistoryResponse response = (GetWorkflowHistoryResponse) this.restCommunicator.sendRequest(new GetWorkflowHistory(), request);
        return response;
    }
    /**
     * Pings BioNimbuZ core.
     *
     * @param address
     * @return
     */
    public static boolean ping() {
        return RestCommunicator.ping();
    }
}
