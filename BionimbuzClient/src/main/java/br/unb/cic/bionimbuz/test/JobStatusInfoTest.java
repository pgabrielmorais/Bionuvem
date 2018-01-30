package br.unb.cic.bionimbuz.test;

import java.util.List;

import br.unb.cic.bionimbuz.model.FileInfo;

public class JobStatusInfoTest {

    private String idJob;
    private List<FileInfo> fileList;
    private List<String> workflow;

    public JobStatusInfoTest() {
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }

    public List<String> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<String> workflow) {
        this.workflow = workflow;
    }

}
