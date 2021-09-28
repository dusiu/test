package io.github.jacekszmidt.model;


import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class ExcelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fileName;
    @Size(min = 6, message = "size must be min 6 characters")
    private String userName;
    @Lob
    private byte[] file;

    public ExcelEntity() {
    }

    public ExcelEntity(String fileName, String userName, byte[] file) {
        this.fileName = fileName;
        this.userName = userName;
        this.file = file;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "ExcelEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
