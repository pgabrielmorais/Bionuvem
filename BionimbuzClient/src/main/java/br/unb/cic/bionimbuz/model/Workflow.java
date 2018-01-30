package br.unb.cic.bionimbuz.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Workflow {

    private final String id = "Workflow" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "-" + UUID.randomUUID().toString().substring(0, 13);
    private final List<Job> jobs;
    private final String creationDatestamp;
    private final Long userId;
    private final String description;
    private WorkflowStatus status;
    private List<Instance> intancesWorkflow;
    private User userWorkflow;
    private SLA sla;
    
    public Workflow() {
        this.jobs = null;
        this.creationDatestamp = null;
        this.userId = null;
        this.description = null;
    }

    public Workflow(Long userId, String description) {
        this.userId = userId;
        this.creationDatestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.jobs = new ArrayList<>();
        this.description = description;
        this.status = WorkflowStatus.PENDING;
    }

    public Workflow(List<Job> jobs, String creationDatestamp, Long userId, String description, WorkflowStatus status, List<Instance> intancesWorkflow, User userWorkflow, SLA sla) {
        this.jobs = jobs;
        this.creationDatestamp = creationDatestamp;
        this.userId = userId;
        this.description = description;
        this.status = status;
        this.intancesWorkflow = intancesWorkflow;
        this.userWorkflow = userWorkflow;
        this.sla = sla;
    }

    public String getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCreationDatestamp() {
        return creationDatestamp;
    }

    public String getDescription() {
        return description;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void addJob(Job job) {
        this.jobs.add(job);
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public List<Instance> getIntancesWorkflow() {
        return intancesWorkflow;
    }

    public void setIntancesWorkflow(List<Instance> intancesWorkflow) {
        this.intancesWorkflow = intancesWorkflow;
    }

    public User getUserWorkflow() {
        return userWorkflow;
    }

    public void setUserWorkflow(User userWorkflow) {
        this.userWorkflow = userWorkflow;
    }
    public SLA getSla() {
        return sla;
    }

    public void setSla(SLA sla) {
        this.sla = sla;
    }
}
