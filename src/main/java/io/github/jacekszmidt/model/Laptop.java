package io.github.jacekszmidt.model;

public class Laptop {
    private String model;
    private String serialNumber;
    private String cleanData;
    private String otherInfo;
    private String problemDescription;

    public Laptop(String model, String serialNumber, String otherInfo, String problemDescription, String cleanData) {
        this.model = model;
        this.serialNumber = serialNumber;
        this.otherInfo = otherInfo;
        this.problemDescription = problemDescription;
        this.cleanData = cleanData;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
        return "Laptop{" +
                "model='" + model + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", cleanData='" + cleanData + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", problemDescription='" + problemDescription + '\'' +
                '}';
    }
}