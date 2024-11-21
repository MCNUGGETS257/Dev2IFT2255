package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetTravaux {
    private String titre;
    private String description;
    private String typeTravaux;
    private ArrayList<String> quartiersAffectes;
    private ArrayList<String> ruesAffectees;
    private String dateDebut;
    private String dateFin;
    private String horaireTravaux;
    private String id; // Identifiant unique du projet
    private ArrayList<Entrave> entraves; // Liste des entraves associées

    // Constructeur
    public ProjetTravaux(String id,String titre, String description, String typeTravaux, ArrayList<String> quartiersAffectes,
                         ArrayList<String> ruesAffectees, String dateDebut, String dateFin,
                         String horaireTravaux) {
        this.titre = titre;
        this.description = description;
        this.typeTravaux = typeTravaux;
        this.quartiersAffectes = quartiersAffectes;
        this.ruesAffectees = ruesAffectees;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.horaireTravaux = horaireTravaux;
        this.id = id;
        this.entraves = new ArrayList<>(); // Initialisation de la liste des entraves
    }

    // Getters
    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getTypeTravaux() {
        return typeTravaux;
    }

    public ArrayList<String> getQuartiersAffectes() {
        return quartiersAffectes;
    }

    public ArrayList<String> getRuesAffectees() {
        return ruesAffectees;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getHoraireTravaux() {
        return horaireTravaux;
    }

    public String getId() {
        return id;
    }

    public List<Entrave> getEntraves() {
        return entraves;
    }

    // Setters
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypeTravaux(String typeTravaux) {
        this.typeTravaux = typeTravaux;
    }

    public void setQuartiersAffectes(ArrayList<String> quartiersAffectes) {
        this.quartiersAffectes = quartiersAffectes;
    }

    public void setRuesAffectees(ArrayList<String> ruesAffectees) {
        this.ruesAffectees = ruesAffectees;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setHoraireTravaux(String horaireTravaux) {
        this.horaireTravaux = horaireTravaux;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Méthode pour ajouter une entrave au projet
    public void ajouterEntrave(Entrave entrave) {
        if (entrave != null) {
            entraves.add(entrave);
        }
    }

    @Override
    public String toString() {
        return "ProjetTravaux{" +
                "titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", typeTravaux='" + typeTravaux + '\'' +
                ", quartiersAffectes=" + quartiersAffectes +
                ", ruesAffectees=" + ruesAffectees +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", horaireTravaux='" + horaireTravaux + '\'' +
                ", id='" + id + '\'' +
                ", entraves=" + entraves +
                '}';
    }
}

