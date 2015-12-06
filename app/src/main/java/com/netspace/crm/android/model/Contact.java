package com.netspace.crm.android.model;

/**
 * Created by Oleh Kolomiets
 */
public class Contact {

    private String name;
    private String position;

    public Contact(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }
}
