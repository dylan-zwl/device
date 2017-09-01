package com.tapc.platform.model.vaplayer;

import java.io.Serializable;
import java.util.List;

public class PlayEntity implements Serializable {
    private static final long serialVersionUID = 4553110282150816252L;
    private String name;
    private String description;
    private String location;
    private String version;
    private String still;
    private String uniqueid;
    private List<String> evtList;
    private String path;
    private double gradient;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStill() {
        return still;
    }

    public void setStill(String still) {
        this.still = still;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public List<String> getEvtList() {
        return evtList;
    }

    public void setEvtList(List<String> evtList) {
        this.evtList = evtList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }
}
