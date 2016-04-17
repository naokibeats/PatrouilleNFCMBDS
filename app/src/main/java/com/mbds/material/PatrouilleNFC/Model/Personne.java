package com.mbds.material.PatrouilleNFC.Model;

import java.io.Serializable;

/**
 * Created by Safidimahefa on 17/04/2016.
 */
public class Personne implements Serializable{
    private int id_personne;
    private String nom;
    private String prenom;
    private int sexe;
    private String date_naiss;
    private String adresse;
    private String image;
    private String lieu_naiss;
    private String immatriculation;
    private String profession;
    private String nom_pere;
    private String nom_mere;
    private String situation_mat;
    private String numero_tag;
    private String statut;
    private String sexecritere;

    public Personne(){

    }
    public Personne(String nom, String prenom, String numero_tag){
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setNumero_tag(numero_tag);
    }
    public Personne(String nom, String prenom, String numero_tag, String cin ,String sexecritere ){
        this.setNom(nom);
        this.setPrenom(prenom);
        this.setNumero_tag(numero_tag);
        this.setImmatriculation(cin);
        this.setSexecritere(sexecritere);

    }

    public int getId_personne() {
        return id_personne;
    }

    public void setId_personne(int id_personne) {
        this.id_personne = id_personne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public String getDate_naiss() {
        return date_naiss;
    }

    public void setDate_naiss(String date_naiss) {
        this.date_naiss = date_naiss;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLieu_naiss() {
        return lieu_naiss;
    }

    public void setLieu_naiss(String lieu_naiss) {
        this.lieu_naiss = lieu_naiss;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getNom_pere() {
        return nom_pere;
    }

    public void setNom_pere(String nom_pere) {
        this.nom_pere = nom_pere;
    }

    public String getNom_mere() {
        return nom_mere;
    }

    public void setNom_mere(String nom_mere) {
        this.nom_mere = nom_mere;
    }

    public String getSituation_mat() {
        return situation_mat;
    }

    public void setSituation_mat(String situation_mat) {
        this.situation_mat = situation_mat;
    }

    public String getNumero_tag() {
        return numero_tag;
    }

    public void setNumero_tag(String numero_tag) {
        this.numero_tag = numero_tag;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSexecritere() {
        return sexecritere;
    }

    public void setSexecritere(String sexecritere) {
        this.sexecritere = sexecritere;
    }
}
