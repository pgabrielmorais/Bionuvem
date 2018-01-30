package br.unb.cic.bionimbuz.web.beans;

import br.unb.bionimbuz.storage.bucket.BioBucket;
import br.unb.bionimbuz.storage.bucket.CloudStorageMethods;
import br.unb.bionimbuz.storage.bucket.PeriodicCheckerBuckets;
import br.unb.bionimbuz.storage.bucket.methods.CloudMethodsAmazonGoogle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.unb.cic.bionimbuz.configuration.BionimbuzClientConfig;
import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.model.Log;
import br.unb.cic.bionimbuz.model.Workflow;
import br.unb.cic.bionimbuz.model.WorkflowOutputFile;
import br.unb.cic.bionimbuz.rest.response.GetWorkflowHistoryResponse;
import br.unb.cic.bionimbuz.rest.service.RestService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controls workflow/history.xhtml page.
 *
 * @author Vinicius
 */
@Named
@SessionScoped
public class WorkflowHistoryBean implements Serializable {

    protected BionimbuzClientConfig config = ConfigurationRepository.getConfig();
    protected static final Logger LOGGER = LoggerFactory.getLogger(WorkflowHistoryBean.class);
    private static final String REST_PATH = "/rest/file/download/";
    private String downloadURL;
    private String restPath;
    private StreamedContent file;
    private RestService restService;
    private Workflow selectedWorkflow;
    private List<Log> history;
    private List<WorkflowOutputFile> workflowOutputFiles;

    @PostConstruct
    public void initialize() {
        restPath = ConfigurationRepository.getConfig().getBionimbuzAddress() + REST_PATH;
        restService = new RestService();
        workflowOutputFiles = new ArrayList<>();
        file = new DefaultStreamedContent();
    }

    /**
     * Returns the color of a Status
     *
     * @return
     */
    public String getStatusColor() {
        return this.selectedWorkflow.getStatus().getColor();
    }

    /**
     * Returns the status text
     *
     * @return
     */
    public String getStatusText() {
        return this.selectedWorkflow.getStatus().toString();
    }

    /**
     * Calls server to retrieve workflow history log
     *
     * @param workflow
     * @return
     */
    public String selectWorkflow(Workflow workflow) {
        this.selectedWorkflow = workflow;

        // Updates rest resource path
        downloadURL = restPath + workflow.getId() + "/";

        try {
            // Fires request
            GetWorkflowHistoryResponse response = restService.getWorkflowHistory(selectedWorkflow.getId());

            // Set result
            this.history = response.getHistory();
            this.workflowOutputFiles = response.getWorkflowOutputFiles();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "workflow_history";
    }

    /**
     * Calls server to refresh workflow history.
     *
     */
    public void updateWorkflowHistory() {
        try {
            GetWorkflowHistoryResponse response = restService.getWorkflowHistory(selectedWorkflow.getId());

            this.history = response.getHistory();
            this.workflowOutputFiles = response.getWorkflowOutputFiles();

            showMessage("Histórico de execução atualizado");
        } catch (Exception ex) {
            ex.printStackTrace();

            showMessage("Erro no processamento. Favor tente mais tarde");
        }
    }

    /**
     * Show message in growl component (View)
     *
     * @param msg
     */
    private void showMessage(String msg) {
        FacesMessage message = new FacesMessage(msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Workflow getSelectedWorkflow() {
        return selectedWorkflow;
    }

    public void setSelectedWorkflow(Workflow selectedWorkflow) {
        this.selectedWorkflow = selectedWorkflow;
    }

    public List<Log> getHistory() {
        return history;
    }

    public void setHistory(List<Log> history) {
        this.history = history;
    }

    public List<WorkflowOutputFile> getWorkflowOutputFiles() {
        return workflowOutputFiles;
    }

    public StreamedContent getDownloadURL(String outputFile) {
        
        System.out.println("[DEBUG] getDownloadURL for file: " + outputFile);
        
        downloadURL = restPath + selectedWorkflow.getId() + "/" + outputFile;
        CloudStorageMethods methodsInstance = new CloudMethodsAmazonGoogle();
        PeriodicCheckerBuckets instance = new PeriodicCheckerBuckets();
        instance.start();
        //TODO: Medida paleativa para pegar o nome do bucket por enquanto 
        //utilizando so o bucket abaixo, mudar para pegar o bucket onde tiver o arquivo
        BioBucket dest = PeriodicCheckerBuckets.getBucket("bionimbuz-a-br2");

        try {
            methodsInstance.StorageAuth(dest.getProvider());
            methodsInstance.StorageDownloadFile(dest, "/data-folder/", 
                    ConfigurationRepository.getConfig().getTemporaryWorkflowFolder() + "/", outputFile);
            String pathName = ConfigurationRepository.getConfig().getTemporaryWorkflowFolder()+ outputFile;
            
            String contentType = Files.probeContentType(Paths.get(pathName));
            InputStream stream = null;
            try{
                stream = new FileInputStream(pathName);
            }catch(FileNotFoundException ex){
                LOGGER.info(ex.getMessage());
            }
//            InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(pathName);
            file = new DefaultStreamedContent(stream, contentType, outputFile);
            
        } catch (Exception t) {
            LOGGER.info(t.getMessage());
            addMessage("get_download_url_error");
        }
        System.out.println("downloadURL: " + downloadURL);
        return file;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
    
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
