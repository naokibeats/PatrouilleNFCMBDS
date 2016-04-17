package com.mbds.material.PatrouilleNFC.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Safidimahefa on 15/04/2016.
 */
@SuppressWarnings("serial")
public class InfoPersonne implements Serializable {

    private int id_personne;
    private String nom;
    private String prenom;
    private String sexe;
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
    private String date_permis;
    private int categorie_a;
    private int categorie_b;
    private int categorie_c;
    private int categorie_d;
    private int categorie_e;
    private int categorie_f;
    private List<Infraction> infractions;

    private int avis_recherche;

    public InfoPersonne(){

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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDate_naiss() {
        return date_naiss;
    }

    public void setDate_naiss(String date_naiss) {
        this.date_naiss = date_naiss;
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

    /**
     * ATTRIBUT PERMIS
     */
    public String getDate_permis() {
        return date_permis;
    }

    public void setDate_permis(String date_permis) {
        this.date_permis = date_permis;
    }

    public int getCategorie_a() {
        return categorie_a;
    }

    public void setCategorie_a(int categorie_a) {
        this.categorie_a = categorie_a;
    }

    public int getCategorie_b() {
        return categorie_b;
    }

    public void setCategorie_b(int categorie_b) {
        this.categorie_b = categorie_b;
    }

    public int getCategorie_c() {
        return categorie_c;
    }

    public void setCategorie_c(int categorie_c) {
        this.categorie_c = categorie_c;
    }

    public int getCategorie_d() {
        return categorie_d;
    }

    public void setCategorie_d(int categorie_d) {
        this.categorie_d = categorie_d;
    }

    public int getCategorie_e() {
        return categorie_e;
    }

    public void setCategorie_e(int categorie_e) {
        this.categorie_e = categorie_e;
    }

    public int getCategorie_f() {
        return categorie_f;
    }

    public void setCategorie_f(int categorie_f) {
        this.categorie_f = categorie_f;
    }

    /**
     * ATTRIBUT INFRACTION
     * */
    public List<Infraction> getInfractions() {
        return infractions;
    }

    public void setInfractions(List<Infraction> infractions) {
        this.infractions = infractions;
    }

    public int getAvis_recherche() {
        return avis_recherche;
    }

    public void setAvis_recherche(int avis_recherche) {
        this.avis_recherche = avis_recherche;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
