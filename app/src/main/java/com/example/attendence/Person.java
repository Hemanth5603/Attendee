package com.example.attendence;

public class Person {
    private String roll = "";
    private boolean isSelected = false;

    public Person(String roll, boolean isSelected) {
        this.roll = roll;
        this.isSelected = isSelected;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String toString(){
        return "Person{ '" + "Roll = " + roll + '\'' + ", isSelected=" + isSelected + '}';
    }
}