package org.example;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

public class IntervenantMenu {


    public IntervenantMenu() {}

    public void afficherMenu(Scanner scanner) {
        int choice = -1;
        while (choice != 0) {
            System.out.println("--- Menu Intervenant ---");
            System.out.println("Choisissez une option:");
            System.out.println("1. Soumettre un nouveau projet de travaux");
            System.out.println("2. Mettre à jour les informations d'un chantier");
            System.out.println("3. Consulter la liste de requêtes de travail");
            System.out.println("0. Se déconnecter");
            if(scanner.hasNextInt()){
                choice = scanner.nextInt();
                scanner.nextLine();
            }else{
                System.out.println("Veuillez entrer un nombre valide (1, 2 ou 3).");
                scanner.nextLine();
            }

            switch (choice) {
                case 1:
                    soumettreProjet(scanner);
                    break;
                case 2:
                    mettreAJourChantier(scanner);
                    break;
                case 3:
                    consulterRequetes();
                    break;
                case 0:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }

    private void soumettreProjet(Scanner scanner) {
        System.out.println("Soumettre Projet...");
    }


    private void mettreAJourChantier(Scanner scanner) {
       System.out.println("Mettre à jour Chantier");
    }

    private void consulterRequetes() {
        System.out.println("Consulter les requêtes de travail:");

        // URL de l'API où récupérer les requêtes
        String url = "http://localhost:7002/requetes"; // Modifiez l'URL selon l'API réelle

        // Utiliser HttpClientMaVille pour récupérer les requêtes
        HttpResponse<String> response = HttpClientMaVille.get(url);

        // Vérifier la réponse de l'API
        if (response != null && response.statusCode() == 200) {
            String responseBody = response.body();

            // Convertir la réponse JSON en un tableau ou liste de requêtes
            JSONArray requetesArray = new JSONArray(responseBody);

            // Vérifier si des requêtes ont été récupérées
            if (requetesArray.length() == 0) {
                System.out.println("Aucune requête de travail disponible.");
            } else {
                int compteur = 1;
                for (int i = 0; i < requetesArray.length(); i++) {
                    JSONObject requeteJson = requetesArray.getJSONObject(i);

                    // Extraction des données de la requête
                    String titre = requeteJson.getString("titre");
                    String description = requeteJson.getString("description");
                    String type = requeteJson.getString("type");
                    String dateDebut = requeteJson.getString("dateDebut");

                    // Création d'un objet RequeteTravail
                    RequeteTravail requete = new RequeteTravail(titre, description, type, dateDebut);

                    // Afficher les informations de la requête
                    System.out.println("---- Requête " + compteur++ + " ----");
                    System.out.println("Titre : " + requete.getTitreTravail());
                    System.out.println("Description : " + requete.getDescriptionDetaillee());
                    System.out.println("Type de travaux : " + requete.getTypeTravaux());
                    System.out.println("Date début : " + requete.getDateDebutEsperee());
                    System.out.println(); // Ligne vide pour séparer les requêtes
                }
            }
        } else {
            System.out.println("Erreur lors de la récupération des requêtes.");
        }
    }

}

