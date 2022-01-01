package com.example.stambapplication;

import java.io.Serializable;

public class GroupModel implements Serializable {
    private int id;
    private int number;
    private String type;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    private int classId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupNumber() {
        return number;
    }

    public void setGroupNumber(int groupNumber) {
        this.number = groupNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
