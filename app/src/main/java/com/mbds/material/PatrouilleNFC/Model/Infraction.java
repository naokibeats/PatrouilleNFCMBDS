package com.mbds.material.PatrouilleNFC.Model;

/**
 * Created by Safidimahefa on 15/04/2016.
 */
public class Infraction {
    private int id_infraction;
    private int id_personne;
    private int id_utilisateur;
    private String date_infraction;
    private String remarque;
    private int detention;
    private String libelle;
    private String type;
    private int id_type;
    private double amende;
    private int etat;
    public Infraction(){

    }
    public Infraction(int id_personne, int id_utilisateur,int id_type, String remarque, int detention){
        this.setId_personne(id_personne);
        this.setId_utilisateur(id_utilisateur);
        this.setRemarque(remarque);
        this.setId_type(id_type);
        this.setDetention(detention);
    }
    public int getId_infraction() {
        return id_infraction;
    }

    public void setId_infraction(int id_infraction) {
        this.id_infraction = id_infraction;
    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

    public int getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getDate_infraction() {
        return date_infraction;
    }

    public void setDate_infraction(String date_infraction) {
        this.date_infraction = date_infraction;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public int getDetention() {
        return detention;
    }

    public void setDetention(int detention) {
        this.detention = detention;
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

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
}
