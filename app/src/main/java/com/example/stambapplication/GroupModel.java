package com.example.stambapplication;

public class GroupModel {
    private int id;
    private int groupNumber;
    private String type;

    public GroupModel(int id, int groupNumber, String type) {
        this.id = id;
        this.groupNumber = groupNumber;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
