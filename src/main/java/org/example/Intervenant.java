package org.example;


public class Intervenant {
    private String nomComplet;
    private String courriel;
    private String motDePasse;
    private String type;
    private String identifiantVille;

    public Intervenant(String nomComplet, String courriel, String motDePasse, String type, String identifiantVille) {
        this.nomComplet = nomComplet;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
        this.type = type;
        this.identifiantVille = identifiantVille;
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

    public String getType() {
        return type;
    }

    public String getIdentifiantVille() {
        return identifiantVille;
    }

}

