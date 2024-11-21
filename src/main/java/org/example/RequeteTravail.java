package org.example;

public class RequeteTravail {

    // Champs pour la requête
    private String titreTravail;
    private String descriptionDetaillee;
    private String typeTravaux;
    private String dateDebutEsperee;

    // Constructeur
    public RequeteTravail(String titreTravail, String descriptionDetaillee, String typeTravaux, String dateDebutEsperee) {
        this.titreTravail = titreTravail;
        this.descriptionDetaillee = descriptionDetaillee;
        this.typeTravaux = typeTravaux;
        this.dateDebutEsperee = dateDebutEsperee;
    }

    // Getters et Setters
    public String getTitreTravail() {
        return titreTravail;
    }

    public void setTitreTravail(String titreTravail) {
        this.titreTravail = titreTravail;
    }

    public String getDescriptionDetaillee() {
        return descriptionDetaillee;
    }

    public void setDescriptionDetaillee(String descriptionDetaillee) {
        this.descriptionDetaillee = descriptionDetaillee;
    }

    public String getTypeTravaux() {
        return typeTravaux;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public String getDateDebutEsperee() {
        return dateDebutEsperee;
    }

    public void setDateDebutEsperee(String dateDebutEsperee) {
        this.dateDebutEsperee = dateDebutEsperee;
    }

    // Méthode pour afficher les détails de la requête
    public void afficherRequete() {
        System.out.println("Détails de la requête de travail résidentiel:");
        System.out.println("Titre du travail: " + titreTravail);
        System.out.println("Description détaillée: " + descriptionDetaillee);
        System.out.println("Type de travaux: " + typeTravaux);
        System.out.println("Date de début espérée: " + dateDebutEsperee);
    }
}
