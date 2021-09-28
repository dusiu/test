package io.github.jacekszmidt.model;

public class PersonalComputer {
    private String cpu;
    private String motherBoard;
    private String memoryRam;
    private String psu;
    private String disc;
    private String otherInfo;
    private String problemDescription;
    private String cleanData;

    public PersonalComputer(String cpu, String motherBoard, String memoryRam, String psu, String disc, String otherInfo, String problemDescription, String cleanData) {
        this.cpu = cpu;
        this.motherBoard = motherBoard;
        this.memoryRam = memoryRam;
        this.psu = psu;
        this.disc = disc;
        this.otherInfo = otherInfo;
        this.problemDescription = problemDescription;
        this.cleanData = cleanData;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMotherBoard() {
        return motherBoard;
    }

    public void setMotherBoard(String motherBoard) {
        this.motherBoard = motherBoard;
    }

    public String getMemoryRam() {
        return memoryRam;
    }

    public void setMemoryRam(String memoryRam) {
        this.memoryRam = memoryRam;
    }

    public String getPsu() {
        return psu;
    }

    public void setPsu(String psu) {
        this.psu = psu;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getCleanData() {
        return cleanData;
    }

    public void setCleanData(String cleanData) {
        this.cleanData = cleanData;
    }

    @Override
    public String toString() {
        return "PersonalComputer{" +
                "cpu='" + cpu + '\'' +
                ", motherBoard='" + motherBoard + '\'' +
                ", memoryRam='" + memoryRam + '\'' +
                ", psu='" + psu + '\'' +
                ", disc='" + disc + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", problemDescription='" + problemDescription + '\'' +
                ", cleanData='" + cleanData + '\'' +
                '}';
    }
}



