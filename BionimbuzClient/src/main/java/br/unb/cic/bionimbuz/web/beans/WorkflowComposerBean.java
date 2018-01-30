package br.unb.cic.bionimbuz.web.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.exception.ServerNotReachableException;
import br.unb.cic.bionimbuz.model.DiagramElement;
import br.unb.cic.bionimbuz.model.FileInfo;
import br.unb.cic.bionimbuz.model.Instance;
import br.unb.cic.bionimbuz.model.Job;
import br.unb.cic.bionimbuz.model.PluginService;
import br.unb.cic.bionimbuz.model.Prediction;
import br.unb.cic.bionimbuz.model.SLA;
import br.unb.cic.bionimbuz.model.User;
import br.unb.cic.bionimbuz.model.Workflow;
import br.unb.cic.bionimbuz.model.WorkflowDiagram;
import br.unb.cic.bionimbuz.model.WorkflowStatus;
import br.unb.cic.bionimbuz.rest.service.RestService;

/**
 * Controls workflow_composer.xhtml page
 *
 * @author Vinicius
 */
@Named
@SessionScoped
public class WorkflowComposerBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final long ONE_HOUR_MILLES = 36 * 100000;
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowComposerBean.class);
    private static final String PROVIDER = "BioNimbuZ";
    String provider = PROVIDER;
    String objetive;
    @Inject
    private SessionBean sessionBean;

    // @Inject
    // private SlaComposerBean slacomp;
    private final RestService restService;
    private final List<PluginService> servicesList;
    private List<PluginService> selectedservicesList;
    private List<String> idServiceSelecteds;
    private ArrayList<DiagramElement> elements;
    private WorkflowDiagram workflowDiagram;
    private String workflowDescription;
    private boolean suspendEvent;
    private String clickedElementId;
    private String inputURL;
    private String arguments;
    private String dependency;
    private String currentJobOutput = "";
    private ArrayList<FileInfo> inputFiles = new ArrayList<>();
    private final ArrayList<String> references;
    private String chosenReference = "";
    private DiagramElement clickedElement;
    private final List<String> supportedFormats;
    private String fileFormat;
    private SLA sla;

    // ----------------------- SLA Declarations------------------
    private String panel1 = "Hide-Panel1";
    private boolean limitation = false;
    private Integer limitationType;
    private Double limitationValueExecutionTime;
    private Double limitationValueExecutionCost;
    private Double exceedValueExecutionCost;
    private List<Instance> instances;
    private List<Instance> selectedInstances;
    private Instance instance;
    private String chosenInstanceId;
    // private Integer quantity;
    private Integer objective;
    private boolean agreeContract;
    // private Double minToHour = 0.0;
    private String firstname;
    private Instance itest;
    Long execuTime = null;
    // ---------------------------------------------------------

    // --------------------------Prediction Declarations ---
    private Double preco;
    private int number2 = 0;
    private boolean agreePrediction;
    private List<Prediction> solutions;

    // Used by the user to download a workflow
    private StreamedContent workflowToDownload;

    // Logged user
    private User loggedUser;

    public WorkflowComposerBean() {
        this.restService = new RestService();
        this.elements = new ArrayList<>();
        this.instances = new ArrayList<>();
        this.servicesList = ConfigurationRepository.getSupportedServices();
        this.references = ConfigurationRepository.getReferences();
        this.supportedFormats = ConfigurationRepository.getSupportedFormats();
        this.instances.addAll(ConfigurationRepository.getInstances());
    }

    @PostConstruct
    public void init() {
        this.loggedUser = this.sessionBean.getLoggedUser();

        // minToHour = 0.0;
        // ------------- SLA inicialization------------------
        this.selectedInstances = new ArrayList<>();
        this.selectedservicesList = new ArrayList<>();
        this.idServiceSelecteds = new ArrayList<>();
        this.solutions = new ArrayList<>();
        // instances.add(new Instance("Micro", 0.03, 10, "Brazil", 1.0, 3.3, "Xeon", 1, 20.0, "sata"));
        // instances.add(new Instance("Macro", 0.24, 5, "us-west", 4.0, 3.3, "Xeon", 4, 120.0, "sata"));
        // instances.add(new Instance("Large", 0.41, 3, "us-west", 8.0, 3.3, "Xeon", 8, 240.0, "sata"));
        this.limitation = false;
        this.itest = new Instance();
        this.limitationValueExecutionTime = null;
        this.limitationValueExecutionCost = null;
        this.exceedValueExecutionCost = null;
        this.itest.setId("Default");
        this.itest.setType("localhost");
        this.itest.setCostPerHour(0D);
        this.itest.setLocality("Brasil, Brasilia");
        this.itest.setMemoryTotal(7.7);
        this.itest.setCpuHtz(2.6);
        this.itest.setCpuType("Intel(R) Core(TM) i7-6700HQ");
        this.itest.setNumCores(8);
        this.itest.setCpuArch("64");
        this.itest.setProvider("UnB");
        this.itest.setCreationTimer(System.currentTimeMillis());
        this.itest.setDelay(0);
        this.itest.setTimetocreate(System.currentTimeMillis());
        this.itest.setIdUser(this.loggedUser.getId());
        this.itest.setIp("127.0.0.1");

        this.instances.add(this.itest);
        // --------------------------------------------------
    }

    /**
     * Adds an element to the chosen programs list
     *
     * @param service
     */
    public void addElement(PluginService service) {
        this.elements.add(new DiagramElement(service));
        this.selectedservicesList.add(service);
        this.idServiceSelecteds.add(service.getId());
        this.showMessage("Elemento " + service.getName() + " adicionado");
    }

    /**
     * Removes an element from the chosen programs list
     *
     * @param element
     */
    public void removeElement(DiagramElement element) {
        this.elements.remove(element);
        PluginService serveaut = new PluginService();
        for (final PluginService service1 : this.selectedservicesList) {
            if (service1.getName().equals(element.getName())) {
                serveaut = service1;
                this.idServiceSelecteds.remove(service1.getId());
                break;
            }
        }
        this.selectedservicesList.remove(serveaut);
        this.showMessage("Elemento " + element.getName() + " removido");
    }

    /**
     * Show message in growl component (View)
     *
     * @param msg
     */
    private void showMessage(String msg) {
        final FacesMessage message = new FacesMessage(msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Controlls Wizard flow. It is linked with the HTML page that renders the
     * diagram and contains Wizard PrimeFaces component.
     *
     * @param event
     * @return
     */
    public String flowController(FlowEvent event) {
        final String currentStep = event.getOldStep();

        final String toGoStep = event.getNewStep();

        // Resets Workflow
        if (toGoStep.equals("element_selection")) {

            // Verifies if the user is entering workflow composer page
        } else if (toGoStep.equals("workflow_design")) {

            if (this.elements.isEmpty()) {
                this.showMessage("Workflow vazio! Favor selecionar elementos");
                return currentStep;
            }
            try {
                // Creates workflow diagram
                this.workflowDiagram = new WorkflowDiagram(this.loggedUser.getId(), this.workflowDescription, this.elements);
            } catch (final MalformedURLException e) {
                LOGGER.error("[MalformedURLException] " + e.getMessage());
            }
        }

        if (currentStep.equals("sla_option") && toGoStep.equals("provisionamento")) {
            if (this.agreePrediction) {
                return "previsao";
            } else {
                return "provisionamento";
            }
        }
        // Módulo Michel Prediction Tab
        if (toGoStep.equals("provisionamento")) {
            System.out.println("Prediction");
            if (this.agreePrediction) {
                final Prediction so = new Prediction();
                Double totalCustWorkflow = 0d;
                Long totalTimeWorkflow = 0l;

                so.setInstance(this.itest);
                so.setCustoService(0.0D);
                so.setTimeService(System.currentTimeMillis());
                so.setIdService(this.selectedservicesList.get(0).getId());
                // Descomentar apos o michel retornar a lista;
                // solutions.addAll(solutions);
                // solution to not put repetead
                if (!this.solutions.contains(so)) {

                    // solutions.put(selectedservicesList.get(1).getId(), instances.get(1));
                    // Interating on Hash map and and set the program for that kind of instance
                    for (final Prediction s1 : this.solutions) {
                        totalTimeWorkflow += s1.getTimeService();
                        totalCustWorkflow += s1.getCustoService() * s1.getTimeService();
                        s1.getInstance().setIdUser(this.sessionBean.getLoggedUser().getId());
                        this.selectedInstances.add(s1.getInstance());
                    }

                }
                this.setLimitationValueExecutionCost(totalCustWorkflow);
                execuTime = totalTimeWorkflow;
                return "sla_option";
            }
        }
        // Provision Tab
        if (toGoStep.equals("sla_option")) {
            System.out.println("Provisionamento");

            // setMinToHour(0.0);
            // TODO: Get HashMap List from Michel Module
        }

        // SLA Tab
        if (toGoStep.equals("workflow_summary")) {
            System.out.println("SLA");
            // System.out.println("peguei: "+slacomp.getSelectedInstancies().get(0).toString());
            // System.out.println(minToHour);
            this.selectedInstances.stream().forEach((f) -> {
                System.out.println(f.getDescription());
            });
            System.out.println("objective: " + this.getObjective());
            System.out.println("getLimitationValueExecutionCost:" + this.getLimitationValueExecutionCost());
            System.out.println("getLimitationValueExecutionTime:" + this.getLimitationValueExecutionTime());
            System.out.println("limitation: " + this.limitation);
            System.out.println("limitationType: " + this.limitationType);
            System.out.println("limitationValueExecutionCost: " + this.limitationValueExecutionCost);
            System.out.println("limitationValueExecutionTime: " + this.limitationValueExecutionTime);
            if (!this.limitation) {
                this.limitationValueExecutionCost = null;
                this.limitationValueExecutionTime = null;
            }
            // TODO: alterar para o usuário bionimbuz depois da implementação do cadastro de usuário

            // System.out.println(sla.getId());
            // provider = new User("bionimbuz", PBKDF2.generatePassword("@BioNimbuZ!"), "BioNimbuZ", "71004832206", "bionimbuz@gmail.com", "0");
            // try {
            // sla = new SLA(this, loggedUser, getProvider(), this.getServicesList());
            // template = new SLA(restService.startSla(sla, workflowDiagram.getWorkflow()));
            // } catch (ServerNotReachableException ex) {
            // java.util.logging.Logger.getLogger(WorkflowComposerBean.class.getName()).log(Level.SEVERE, null, ex);
            // }
            // sla= new SLA(this,loggedUser,loggedUser,this.getServicesList());
            // System.out.println(sla.getId());
            // try {
            // restService.startSla(sla,workflowDiagram.getWorkflow());
            // } catch (ServerNotReachableException ex) {
            // java.util.logging.Logger.getLogger(WorkflowComposerBean.class.getName()).log(Level.SEVERE, null, ex);
            // }
        }

        return toGoStep;
    }

    /**
     * Server side method called when an element is connected to another
     *
     * @param event
     */
    public void onConnect(ConnectEvent event) {
        final DiagramElement clickedElement = (DiagramElement) event.getTargetElement().getData();

        // Add dependency
        if (((DiagramElement) event.getSourceElement().getData()).getName().equals("Inicio") || ((DiagramElement) event.getSourceElement().getData()).getName().equals("Fim")) {
            this.dependency = null;
        } else {
            this.dependency = ((DiagramElement) event.getSourceElement().getData()).getId();
        }

        if (!this.suspendEvent && !clickedElement.getName().equals("Inicio") && !clickedElement.getName().equals("Fim")) {
            final RequestContext context = RequestContext.getCurrentInstance();

            // Sets clicked element id to be used to set element input file
            this.clickedElementId = ((DiagramElement) event.getTargetElement().getData()).getId();
            this.clickedElement = (DiagramElement) event.getTargetElement().getData();

            // Call input pick painel
            context.execute("PF('file_dlg').show();");

        } else {
            this.suspendEvent = false;
        }
    }

    /**
     * Server side method called when a connection is changed from an element to
     * another
     *
     * @param event
     */
    public void onDisconnect(DisconnectEvent event) {
        final DiagramElement clickedElement = (DiagramElement) event.getTargetElement().getData();

        if (!this.suspendEvent && !clickedElement.getName().equals("Inicio") && !clickedElement.getName().equals("Fim")) {
            final RequestContext context = RequestContext.getCurrentInstance();

            // Sets clicked element id to be used to set element input file
            this.clickedElementId = ((DiagramElement) event.getTargetElement().getData()).getId();

            // Call input pick painel
            context.execute("PF('file_dlg').show();");
        } else {
            this.suspendEvent = false;
        }
    }

    /**
     * Server side method called when a connection is taken to another element
     *
     * @param event
     */
    public void onConnectionChange(ConnectionChangeEvent event) {

        this.suspendEvent = true;
    }

    /**
     * Sets the step fields (like, inputfiles, arguments, ...)
     */
    public void setJobFields() {

        // Sets element input list
        this.workflowDiagram.setJobFields(this.clickedElementId, this.inputFiles, this.chosenReference, this.arguments, this.inputURL, this.dependency, this.clickedElement.getName(), this.fileFormat);

        // Saves current job output filename in case the next job uses it as input
        this.currentJobOutput = this.clickedElement.getName() + "_output_" + this.clickedElementId + this.fileFormat;

        // Resets fields
        this.inputFiles = new ArrayList<>();
        this.arguments = "";
        this.inputURL = "";
        this.fileFormat = "";
    }

    /**
     * Sets job fields when the "jump" button is pressed
     */
    public void setJobFieldsWithOutput() {
        // Creates the input file as the output file from the last Job. Need
        // fake values, because of "null of string" exception of Avro.
        final FileInfo file = new FileInfo();
        file.setName(this.currentJobOutput);
        file.setHash("");
        file.setSize(0l);
        file.setUploadTimestamp("");
        file.setUserId(this.sessionBean.getLoggedUser().getId());

        // Creates the input file list with the file info
        final ArrayList<FileInfo> inputs = new ArrayList<>();
        inputs.add(file);

        // Sets element input list
        this.workflowDiagram.setJobFields(this.clickedElementId, inputs, this.chosenReference, this.arguments, this.inputURL, this.dependency, this.clickedElement.getName(), this.fileFormat);

        // Saves current job output filename in case the next job uses it as input
        this.currentJobOutput = this.clickedElement.getName() + "_output_" + this.clickedElementId + this.fileFormat;

        // Resets fields
        this.inputFiles = new ArrayList<>();
        this.arguments = "";
        this.inputURL = "";
        this.fileFormat = "";
    }

    // private String createInstance(String provider, String type , String nameInstance) throws InterruptedException {
    // AmazonAPI amazonapi = new AmazonAPI();
    // GoogleAPI googleapi = new GoogleAPI();
    // String IP = null;
    // switch (provider) {
    // case "Amazon": {
    // try {
    //// amazonapi.createinstance(type, nameInstance);
    // amazonapi.createinstance("t2.micro", nameInstance);
    // } catch (IOException ex) {
    // java.util.logging.Logger.getLogger(WorkflowComposerBean.class.getName()).log(Level.SEVERE, null, ex);
    // }
    //
    // System.out.println("Amazon IP:" + amazonapi.getIpInstance());
    // while((IP = amazonapi.getIpInstance()) == null) {
    // Thread.sleep(1000);
    // }
    // break;
    // }
    // case "Google": {
    // try {
    //// googleapi.createinstance(type, nameInstance);
    // googleapi.createinstance("n1-standard-1", nameInstance);
    //
    // } catch (IOException ex) {
    // java.util.logging.Logger.getLogger(WorkflowComposerBean.class.getName()).log(Level.SEVERE, null, ex);
    // }
    // System.out.println("Google IP:" + googleapi.getIpInstance());
    // while((IP = googleapi.getIpInstance()) == null) {
    // Thread.sleep(1000);
    // }
    // break;
    // }
    // }
    // return IP;
    // }
    /**
     * Send out workflow to be processed by BioNimbuZ core
     *
     * @return
     */
    public String startWorkflow() {
        try {
            // Calls RestService to send the workflow to core

            final List<String> ips = new ArrayList<>();
            String ipAux;
            int aux = 0;
            if (this.agreePrediction) {
                for (final Instance f : this.selectedInstances) {
                    final List<String> ipsPrediction = new ArrayList<>();
                    // String instanceName =f.getType().substring(25).toLowerCase();
                    // instanceName = getWorkflowDescription()+"-"+instanceName+"-"+aux;
                    final String instanceName = this.getWorkflowDescription() + "-" + aux;
                    if (!f.getProvider().equals("UnB")) {
                        if ((ipAux = this.restService.createelasticity(f.getProvider(), f.getType(), instanceName, "create", "12345")) == null) {
                            return "start_error";
                        }
                        f.setIp(ipAux);
                    }
                    f.setCreationTimer(System.currentTimeMillis());
                    f.setTimetocreate(System.currentTimeMillis());
                    ipAux = f.getIp();
                    ipsPrediction.add(ipAux);
                    // TODO arrumar a logica; somente 1 ip como lista é adicionado no job
                    for (final Job jAux : this.workflowDiagram.getWorkflow().getJobs()) {
                        if (f.getidProgramas().get(0).equals(jAux.getServiceId())) {
                            jAux.setIpjob(ipsPrediction);
                            break;
                        }
                    }
                    aux++;
                }
            } else {
                for (final Instance f : this.selectedInstances) {
                    f.setidProgramas(this.idServiceSelecteds);
                    final String instanceName = this.getWorkflowDescription() + "-" + aux;
                    if (!f.getProvider().equals("UnB")) {
                        if ((ipAux = this.restService.createelasticity(f.getProvider(), f.getType(), instanceName, "create", "teste")) == null) {
                            System.out.println("IP: " + ipAux);
                            return "start_error";
                        }
                        f.setIp(ipAux);
                    }
                    ipAux = f.getIp();
                    f.setCreationTimer(System.currentTimeMillis());
                    f.setTimetocreate(System.currentTimeMillis());
                    ips.add(ipAux);
                    aux++;
                }
                //
                for (final Job jAux : this.workflowDiagram.getWorkflow().getJobs()) {
                    jAux.setIpjob(ips);
                }
            }
            // Setting the instances for that user;
            if (this.loggedUser.getInstances() != null) {
                this.sessionBean.getLoggedUser().addInstances(this.selectedInstances);
            } else {
                this.sessionBean.getLoggedUser().setInstances(this.selectedInstances);
            }
            // Setting the instances for that workflow;
            this.workflowDiagram.getWorkflow().setIntancesWorkflow(this.selectedInstances);
            this.workflowDiagram.getWorkflow().setUserWorkflow(this.loggedUser);

            if (this.getLimitationValueExecutionTime() != null) {
                execuTime = (long) (this.getLimitationValueExecutionTime() * ONE_HOUR_MILLES);
            }

            this.sla = new SLA(PROVIDER, this.workflowDiagram.getWorkflow().getId(), this.getObjective(), 0l, 0D, this.getLimitationType(), execuTime, this.limitationValueExecutionCost,
                    this.agreePrediction, this.solutions, this.limitation, this.exceedValueExecutionCost);
            this.workflowDiagram.getWorkflow().setSla(this.sla);
            if (this.restService.startWorkflow(this.workflowDiagram.getWorkflow())) {
                // Updates user workflow list
                this.workflowDiagram.getWorkflow().setStatus(WorkflowStatus.EXECUTING);
                this.sessionBean.getLoggedUser().getWorkflows().add(this.workflowDiagram.getWorkflow());
            }
            return "start_success";
        } catch (final ServerNotReachableException ex) {
            LOGGER.error("[ServerNotReachableException] " + ex.getMessage());
            java.util.logging.Logger.getLogger(WorkflowComposerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "start_error";
    }

    /**
     * Returns workflow status as a String
     *
     * @return
     */
    public String getWorkflowStatus() {
        return this.workflowDiagram.getWorkflow().getStatus().toString();
    }

    /**
     * Returns the color of the status
     *
     * @return
     */
    public String getWorkflowColor() {
        return this.workflowDiagram.getWorkflow().getStatus().getColor();
    }

    /**
     * Method used for users to download a .json workflow representation
     *
     * @return
     */
    public StreamedContent downloadWorkflow() {
        final ObjectMapper mapper = new ObjectMapper();
        DefaultStreamedContent content = null;
        final String path = ConfigurationRepository.TEMPORARY_WORKFLOW_PATH + this.workflowDiagram.getWorkflow().getId() + ".json";
        File jsonFile = null;
        try {
            jsonFile = new File(path);
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, this.workflowDiagram.getWorkflow());
            final FileInputStream is = new FileInputStream(jsonFile);
            content = new DefaultStreamedContent(is, "application/json", this.workflowDiagram.getWorkflow().getId() + ".flow");
        } catch (final IOException ex) {
            LOGGER.error("[IOException] - " + ex.getMessage());
        } finally {
            jsonFile.delete();
        }

        return content;
    }

    /**
     * Called when an user clicks to import a workflow file to application
     *
     * @param event
     */
    public void handleImportedWorkflow(FileUploadEvent event) {
        try {
            final String path = ConfigurationRepository.TEMPORARY_WORKFLOW_PATH + event.getFile().getFileName();

            final File workflowFile = new File(path);

            try (
                 final BufferedReader br = new BufferedReader(new FileReader(workflowFile));) {

                final StringBuilder builder = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }

                final ObjectMapper mapper = new ObjectMapper();
                final Workflow workflow = mapper.readValue(builder.toString(), Workflow.class);
                System.out.println("Tamanho: " + workflow.getJobs().size());
                System.out.println("Descriçao: " + workflow.getDescription());
                System.out.println("UserId: " + workflow.getUserId());
            }

        } catch (final Exception ex) {
            LOGGER.error("[Exception] - " + ex.getMessage());
        }

    }

    /**
     * COMMENTED BECAUSE IT IS BEEN PASSED DIRECTLY TO THE SERVER (WITHOUT SAVE
     * IN DISK) Saves temporary file in disk
     *
     * @param fileName
     * @param in
     * @return public String saveTempFile(String fileName, InputStream in)
     *         throws FileNotFoundException, IOException { String path =
     *         ConfigurationRepository.TEMPORARY_WORKFLOW_PATH + fileName; // write the
     *         inputStream to a FileOutputStream OutputStream outputStream = new
     *         FileOutputStream(new File(path));
     *
     *         int read = 0; byte[] bytes = new byte[1024];
     *
     *         while ((read = in.read(bytes)) != -1) { outputStream.write(bytes, 0,
     *         read); }
     *
     *         // in.close(); LOGGER.info("Temporary file created [path=" +
     *         ConfigurationRepository.UPLOADED_FILES_PATH + fileName + "]");
     *
     *         return path; }
     */
    public StreamedContent getWorkflowToDownload() {
        return this.workflowToDownload;
    }

    public DefaultDiagramModel getWorkflowModel() {
        return this.workflowDiagram.getWorkflowModel();
    }

    public List<PluginService> getServicesList() {
        return this.servicesList;
    }

    public User getLoggedUser() {
        return this.loggedUser;
    }

    public String getWorkflowDescription() {
        return this.workflowDescription;
    }

    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    public List<DiagramElement> getElements() {
        return this.elements;
    }

    public void setElements(ArrayList<DiagramElement> elements) {
        this.elements = elements;
    }

    public String getInputURL() {
        return this.inputURL;
    }

    public void setInputURL(String inputURL) {
        this.inputURL = inputURL;
    }

    public void setInputFiles(ArrayList<FileInfo> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public ArrayList<FileInfo> getInputFiles() {
        return this.inputFiles;
    }

    public ArrayList<Job> getJobs() {
        return (ArrayList<Job>) this.workflowDiagram.getWorkflow().getJobs();
    }

    public Workflow getWorkflow() {
        return this.workflowDiagram.getWorkflow();
    }

    public String getArguments() {
        return this.arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public ArrayList<String> getReferences() {
        return this.references;
    }

    public String getChosenReference() {
        return this.chosenReference;
    }

    public void setChosenReference(String chosenReference) {
        this.chosenReference = chosenReference;
    }

    public List<String> getSupportedFormats() {
        return this.supportedFormats;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    // -----------------------------SLA Methods---------------------------------
    public void setPanel1(String panel1) {
        this.panel1 = panel1;
    }

    public String getPanel1() {
        return this.panel1;
    }

    public boolean isLimitation() {
        return this.limitation;
    }

    public void setLimitation(boolean limitation) {
        this.limitation = limitation;
    }

    public Integer getLimitationType() {
        return this.limitationType;
    }

    public void setLimitationType(Integer limitationType) {
        this.limitationType = limitationType;
    }

    public List<Instance> getInstancies() {
        return this.instances;
    }

    public void setInstancies(List<Instance> instancies) {
        this.instances = instancies;
    }

    public List<Instance> getSelectedInstancies() {
        return this.selectedInstances;
    }

    public void setSelectedInstancies(List<Instance> selectedInstancies) {
        this.selectedInstances = selectedInstancies;
    }

    public Double getExceedValueExecutionCost() {
        return this.exceedValueExecutionCost;
    }

    public void setExceedValueExecutionCost(Double exceedValueExecutionCost) {
        this.exceedValueExecutionCost = exceedValueExecutionCost;
    }

    public List<String> getListInstancesString() {
        final List<String> instancesString = new ArrayList<>();
        this.instances.stream().forEach((i) -> {
            instancesString.add(i.toString());
        });
        return instancesString;
    }

    public void adSelectedInstance(Instance maq) {
        for (final Instance i : this.instances) {
            if (i.getId().equals(maq.getId()) && !this.instances.isEmpty()) {
                i.setIdUser(this.sessionBean.getLoggedUser().getId());
                this.selectedInstances.add(i);
                // instances.remove(i);
                this.showMessage("Elemento " + i.getType() + " adicionado");
                break;
            } else if (this.selectedInstances.isEmpty()) {
                System.out.println("Not found!!");
            }
        }
    }

    /**
     * Removes an element from the selected instances list
     *
     * @param element
     */
    public void removeElement(Instance element) {
        if (!this.selectedInstances.isEmpty()) {
            this.selectedInstances.remove(element);
            // instances.add(element);
            this.showMessage("Elemento " + element.getType() + " removido");
        } else {
            this.showMessage("Não existem elementos para ser removidos!");
        }
    }

    public Instance getInstance() {
        return this.instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public String getChosenInstanceId() {
        return this.chosenInstanceId;
    }

    public void setChosenInstanceId(String chosenInstanceId) {
        this.chosenInstanceId = chosenInstanceId;
    }

    public Double getLimitationValueExecutionTime() {
        return this.limitationValueExecutionTime;
    }

    public void setLimitationValueExecutionTime(Double limitationValueExecutionTime) {
        this.limitationValueExecutionTime = limitationValueExecutionTime;
    }

    public Double getLimitationValueExecutionCost() {
        return this.limitationValueExecutionCost;
    }

    public void setLimitationValueExecutionCost(Double limitationValueExecutionCost) {
        this.limitationValueExecutionCost = limitationValueExecutionCost;
    }

    // public Integer getQuantity() {
    // return quantity;
    // }
    //
    // public void setQuantity(Integer quantity) {
    // this.quantity = quantity;
    // }
    public Integer getObjective() {
        return this.objective;
    }

    public void setObjective(Integer objective) {
        this.objective = objective;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getObjetive() {
        switch (this.objective) {
            case 1:
                this.objetive = "Desempenho";
                break;
            case 2:
                this.objetive = "Menor Custo";
                break;
            case 3:
                this.objetive = "Custo/Benefício";
                break;
        }
        return this.objetive;
    }

    // public Double getMinToHour() {
    // int decimalPlaces = 2;
    // BigDecimal bd = new BigDecimal(minToHour);
    //
    // // setScale is immutable
    // bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
    // minToHour = bd.doubleValue();
    // return (minToHour / 60.0);
    // }
    //
    // public void setMinToHour(Double minToHour) {
    // this.minToHour = minToHour;
    // }
    public boolean isAgreeContract() {
        return this.agreeContract;
    }

    public void setAgreeContract(boolean agreeContract) {
        this.agreeContract = agreeContract;
        final String message = this.agreeContract ? "Contrato aceito!" : "Contrato Recusado!";
        this.showMessage(message);
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    // -------------------------------------------------------------------------
    // -------------------------------- Prediction functions

    public int getNumber2() {
        return this.number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public void onSlideEnd(SlideEndEvent event) {
        final FacesMessage message = new FacesMessage("Slide Ended", "Value: " + event.getValue());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Double getPreco() {
        return this.preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public boolean isAgreePrediction() {
        return this.agreePrediction;
    }

    public void setAgreePrediction(boolean agreePrediction) {
        this.agreePrediction = agreePrediction;
        final String message = this.agreePrediction ? "Predição aceita!" : "Predição Recusado!";
        this.showMessage(message);
    }

    public List<PluginService> getSelectedservicesList() {
        return this.selectedservicesList;
    }

    public void setSelectedservicesList(List<PluginService> selectedservicesList) {
        this.selectedservicesList = selectedservicesList;
    }

}
