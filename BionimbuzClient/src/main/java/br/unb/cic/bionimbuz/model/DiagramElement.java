package br.unb.cic.bionimbuz.model;

/**
 * Class that defines a diagram element to compose a diagram for the user
 *
 * @author Vinicius
 */
public class DiagramElement {

    private String id;

    private String serviceId;

    private String description;

    private String name;

    private int relativePosition;

    private int xPosition;

    private int yPosition;

    private int width;

    public DiagramElement() {
    }

    public DiagramElement(String name) {
        this.name = name;
    }

    public DiagramElement(PluginService pluginService) {
        this.name = pluginService.getName();
        this.description = pluginService.getInfo();
        this.serviceId = pluginService.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(int relativePosition) {
        this.relativePosition = relativePosition;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
