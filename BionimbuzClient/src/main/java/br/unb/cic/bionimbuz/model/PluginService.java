package br.unb.cic.bionimbuz.model;

import java.util.List;

/**
 * Defines a PluginService (mapped in BioNimbuZ core)
 *
 * @author Vinicius
 */
public class PluginService {

    private String id;

    private String path;

    private Double presetMode = null;

    // Not used
    private String name;

    private List<String> arguments;

    private List<String> input;

    private List<String> output;

    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPresetMode() {
        return presetMode;
    }

    public void setPresetMode(Double presetMode) {
        this.presetMode = presetMode;
    }

    // Not used
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Not used
    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    // Not used
    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }

    // Not used
    public List<String> getOutput() {
        return output;
    }

    public void setOutput(List<String> output) {
        this.output = output;
    }

    // Not used
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
