package com.koshti.titaniam.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class Entities implements Serializable {

	private static final long serialVersionUID = 0;

    public Entities(){}

    public Entities(int id, String name)
    {
        this.id = id;
        this.name= name;
    }

    @Id
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
