package com.mbds.material.PatrouilleNFC.Model;


import java.io.Serializable;

/**
 * Created by Safidimahefa on 16/04/2016.
 */
public class TypeInfraction implements Serializable {
    private int id_type_infraction;
    private String libelle;
    private String type;
    private double amende;
    public TypeInfraction(){

    }


    public int getId_type_infraction() {
        return id_type_infraction;
    }

    public void setId_type_infraction(int id_type_infraction) {
        this.id_type_infraction = id_type_infraction;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmende() {
        return amende;
    }

    public void setAmende(double amende) {
        this.amende = amende;
    }
}
