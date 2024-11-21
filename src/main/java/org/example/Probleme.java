package org.example;


public class Probleme {
    private String nomResident;
    private String adresseCourriel;
    private String adresseResidence;
    private String typeProbleme;
    private String descriptionProbleme;

    // Constructeur
    public Probleme(String nomResident, String adresseCourriel, String adresseResidence, String typeProbleme, String descriptionProbleme) {
        this.nomResident = nomResident;
        this.adresseCourriel = adresseCourriel;
        this.adresseResidence = adresseResidence;
        this.typeProbleme = typeProbleme;
        this.descriptionProbleme = descriptionProbleme;
    }

    // Getters
    public String getNomResident() {
        return nomResident;
    }

    public String getAdresseCourriel() {
        return adresseCourriel;
    }

    public String getAdresseResidence() {
        return adresseResidence;
    }

    public String getTypeProbleme() {
        return typeProbleme;
    }

    public String getDescriptionProbleme() {
        return descriptionProbleme;
    }

    // Setters
    public void setNomResident(String nomResident) {
        this.nomResident = nomResident;
    }

    public void setAdresseCourriel(String adresseCourriel) {
        this.adresseCourriel = adresseCourriel;
    }

    public void setAdresseResidence(String adresseResidence) {
        this.adresseResidence = adresseResidence;
    }

    public void setTypeProbleme(String typeProbleme) {
        this.typeProbleme = typeProbleme;
    }

    public void setDescriptionProbleme(String descriptionProbleme) {
        this.descriptionProbleme = descriptionProbleme;
    }

    // Méthode pour afficher les détails du problème
    @Override
    public String toString() {
        return "Problème signalé:\n" +
                "Nom du résident: " + nomResident + "\n" +
                "Adresse courriel: " + adresseCourriel + "\n" +
                "Adresse residence" + adresseResidence + "\n" +
                "typeProbleme" + typeProbleme + "\n" +
                "descriptionProbleme" + descriptionProbleme;
    }
}
