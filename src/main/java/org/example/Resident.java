package org.example;


import java.time.LocalDate;

public class Resident {
    private String nomComplet;
    private String courriel;
    private String motDePasse;
    private String dateDeNaissance;
    private String telephone;
    private String adresseResidentielle;

    public Resident(String nomComplet, String courriel, String motDePasse, String dateDeNaissance, String telephone, String adresseResidentielle) {
        this.nomComplet = nomComplet;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
        this.dateDeNaissance = dateDeNaissance;
        this.telephone = telephone;
        this.adresseResidentielle = adresseResidentielle;
    }

    // Getters et setters
    public String getNomComplet() {
        return nomComplet;
    }

    public String getCourriel() {
        return courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresseResidentielle() {
        return adresseResidentielle;
    }
}
