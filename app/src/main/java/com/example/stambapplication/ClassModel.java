package com.example.stambapplication;


import androidx.annotation.NonNull;

public class ClassModel {
    private int id;
    private String name;
    private String specialty;
    private String module;
    private int grade;
    private int start_year;
    private int end_year;

    public ClassModel(int id, String name) {
        this.id = id;
        this.name = specialty + " " + start_year + "/" + end_year;
    }

    public ClassModel() {
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.specialty + " " + this.start_year + "/" + this.end_year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getStart_year() {
        return start_year;
    }

    public void setStart_year(int start_year) {
        this.start_year = start_year;
    }

    public int getEnd_year() {
        return end_year;
    }

    public void setEnd_year(int end_year) {
        this.end_year = end_year;
    }


}
