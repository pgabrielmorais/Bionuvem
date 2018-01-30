package br.unb.cic.bionimbuz.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.unb.cic.bionimbuz.model.FileInfo;
import br.unb.cic.bionimbuz.rest.action.Upload;
import br.unb.cic.bionimbuz.rest.service.RestService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@RequestScoped
public class DeleteFileBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFileBean.class);
    private final RestService restService;
    
    @Inject
    private SessionBean sessionBean;

    public DeleteFileBean() {
        restService = new RestService();
    }

    /**
     * Handle file delete request by the user
     *
     * @param file
     */
    public void deleteFile(FileInfo file) {
        try {
            boolean returnResult = restService.deleteFile(file);
           
            LOGGER.info("returnResult: " + returnResult);
            
            if (returnResult) {

                // If file was uploaded with success, adds file on user file list
                sessionBean.getLoggedUser().getFiles().remove(file);

                // Adds the size of the uploaded file to the user storage usage
                sessionBean.getLoggedUser().addStorageUsage((-1) * file.getSize());
                
                // Shows message to the user
                showFacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo deletado com sucesso!");
            } else {
                showFacesMessage(FacesMessage.SEVERITY_INFO, "Ocorreu um erro ao deletar o arquivo!!");
            }
            

        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Ocorreu um erro interno... Tente novamente mais tarde", "");
            FacesContext.getCurrentInstance().addMessage(null, message);

            return;
        }
    }

    // Shows JSF Faces Message to the user
    private void showFacesMessage(FacesMessage.Severity severity, String msg) {
        FacesMessage message = new FacesMessage(severity, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
