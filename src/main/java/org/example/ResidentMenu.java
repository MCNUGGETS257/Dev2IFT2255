package org.example;

import org.example.HttpClientApi;
import org.example.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResidentMenu {
    public ResidentMenu() {}


    public void afficherMenu(Scanner scanner) {
        int choice = -1;
        while (choice != 0) {
            System.out.println("----Menu Résident---");
            System.out.println("Choisissez une option:");
            System.out.println("1. Consulter les travaux en cours ou à venir");
            System.out.println("2.Consulter les entraves associées aux travaux en cours");
            System.out.println("3. Rechercher des travaux");
            System.out.println("4. Recevoir des notifications personnalisées");
            System.out.println("5. Soumettre une requête de travail");
            System.out.println("6. Signaler un problème à la ville");
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
                    consulterTravaux();
                    break;
                case 2:
                    consulterEntraves();
                    break;

                case 3:
                    rechercherTravaux(scanner);
                    break;
                case 4:
                    recevoirNotifs();
                    break;// envoi tous es changements d'infos de chantier et informations sur nouveaux projet soumiss
                case 5:
                    soumettreRequete(scanner);
                    break;
                case 6:
                    signalerProbleme(scanner);
                    break;
                case 7:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
                    break;
            }
        }
    }


    private void consulterTravaux() {
        // ID de la ressource à utiliser pour récupérer les projets de travaux (à ajuster selon votre API)
        String resourceId = "cc41b532-f12d-40fb-9f55-eb58c9a2b12b"; // Remplacez par l'ID réel de la ressource

        // Créer une instance de HttpClientApi pour faire la requête
        HttpClientApi clientApi = new HttpClientApi();

        // Récupérer les données via l'API
        ApiResponse response = clientApi.getData(resourceId);

        // Vérifier si la requête a réussi
        if (response != null && response.getStatusCode() == 200) {
            // Parse le corps de la réponse JSON
            String responseBody = response.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Accéder à la liste des projets de travaux dans la réponse (supposons que c'est dans un champ "records")
            JSONArray records = jsonResponse.getJSONObject("result").getJSONArray("records");

            // Récupérer la date actuelle et la date dans 3 mois pour filtrer les projets
            LocalDate aujourdHui = LocalDate.now();
            LocalDate dansTroisMois = aujourdHui.plusMonths(3);

            // Afficher les projets dans les 3 prochains mois
            int compteur = 1;
            for (int i = 0; i < records.length(); i++) {
                JSONObject projetJson = records.getJSONObject(i);

                // Extraire la date de fin du projet et la convertir en LocalDate
                String dateFinString = projetJson.optString("duration_end_date", "Non spécifié").substring(0, 10);
                LocalDate dateFin = LocalDate.parse(dateFinString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Vérifier si la date de fin est dans les trois prochains mois
                if (dateFin.isBefore(dansTroisMois) && dateFin.isAfter(aujourdHui)) {
                    // Extraire les informations supplémentaires
                    String titre = projetJson.optString("reason_category", "Non spécifié")+" situé à "+projetJson.getString("boroughid");
                    String typeTravaux = projetJson.optString("reason_category", "Non spécifié");
                    String quartier = projetJson.optString("boroughid", "Non spécifié");
                    String rue = projetJson.optString("streetid", "Non spécifié");
                    String intervenant = projetJson.optString("organizationname", "Non spécifié");
                    String statut = projetJson.optString("currentstatus", "Non spécifié");
                    String categorie = projetJson.optString("submittercategory", "Non spécifié");
                    String description = "Type de travaux : " + typeTravaux + ", Quartier : " + quartier + ", Rues affectées : " + rue +
                            ", Intervenant : " + intervenant + ", Statut actuel : " + statut + ", Catégorie : " + categorie;

                    // Créer les listes de quartiers et rues affectées
                    ArrayList<String> quartiersAffectes = new ArrayList<>();
                    quartiersAffectes.add(quartier);

                    ArrayList<String> ruesAffectees = new ArrayList<>();
                    ruesAffectees.add(rue);

                    // Créer l'objet ProjetTravaux
                    ProjetTravaux projet = new ProjetTravaux(
                            projetJson.getString("id"), // ID du projet
                            titre, // Titre
                            description, // Description complète
                            typeTravaux, // Type de travaux
                            quartiersAffectes, // Quartiers affectés
                            ruesAffectees, // Rues affectées
                            projetJson.optString("duration_start_date", "Non spécifié").substring(0, 10), // Date de début
                            dateFinString, // Date de fin
                            projetJson.optString("work_schedule", "Non spécifié") // Horaire de travaux
                    );

                    // Afficher le projet
                    afficherUnProjet(projet, compteur++);
                }
            }

            // Vérifier s'il n'y a aucun projet trouvé dans les trois prochains mois
            if (compteur == 1) {
                System.out.println("Aucun projet prévu dans les trois prochains mois.");
            }
        } else {
            System.out.println("Erreur lors de la récupération des projets de travaux : " + response.getMessage());
        }
    }



    public void consulterEntraves() {
        // ID de la ressource à utiliser pour récupérer les entraves (vous devez le définir selon votre API)
        String resourceId = "a2bc8014-488c-495d-941b-e7ae1999d1bd"; // Remplacez par l'ID réel de la ressource

        // Créer une instance de HttpClientApi pour faire la requête
        HttpClientApi clientApi = new HttpClientApi();

        // Récupérer les données via l'API
        ApiResponse response = clientApi.getData(resourceId);

        // Vérifier si la requête a réussi
        if (response != null && response.getStatusCode() == 200) {
            // Parse le corps de la réponse JSON
            String responseBody = response.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);

            // Accéder à la liste des entraves dans la réponse (supposons que c'est dans un champ "records")
            JSONArray records = jsonResponse.getJSONObject("result").getJSONArray("records");

            // Afficher chaque entrave
            int count = 1;
            for (int i = 0; i < records.length(); i++) {
                JSONObject entraveJson = records.getJSONObject(i);
                // Créer un objet Entrave à partir des données JSON (vous devez adapter ceci en fonction de la structure de votre API)
                Entrave entrave = new Entrave(
                        entraveJson.getString("id_request"), // Remplacez "field1" par les vrais noms des champs
                        entraveJson.getString("streetid"),
                        entraveJson.getString("shortname"),
                        entraveJson.getString("streetimpacttype")
                );
                afficherUneEntrave(entrave, count++);
            }
        } else {
            System.out.println("Erreur lors de la récupération des entraves : " + response.getMessage());
        }
    }



    private void soumettreRequete(Scanner scanner) {
        System.out.println("Soumettre une requête de travail:");

        // Demander les informations à l'utilisateur
        System.out.print("Titre: ");
        String titre = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.println("Quel Type de travaux?");
        String type = scanner.nextLine();

            // Lire la date de début et valider son format
            String dateDebut = "";
            boolean validDate = false;
            while (!validDate) {
                System.out.println("Date debut (YYYY-MM-DD):");
                dateDebut = scanner.nextLine();

                // Vérification du format de la date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate parsedDate = LocalDate.parse(dateDebut, formatter); // Try to parse the date
                    validDate = true; // Si la date est valide, on continue
                } catch (DateTimeParseException e) {
                    System.out.println("Date invalide. Veuillez entrer une date au format YYYY-MM-DD.");
                }
            }

            // Créer l'objet RequeteTravail
            RequeteTravail requete = new RequeteTravail(titre, description, type, dateDebut);

            // Convertir l'objet RequeteTravail en JSON
            String json = String.format("{\"titre\": \"%s\", \"description\": \"%s\", \"type\": \"%s\", \"dateDebut\": \"%s\"}",
                    requete.getTitreTravail(), requete.getDescriptionDetaillee(), requete.getTypeTravaux(), requete.getDateDebutEsperee());

            // URL de l'API où envoyer la requête
            String url = "http://localhost:7002/requetes"; // Modifiez l'URL selon l'API réelle

            // Utiliser HttpClientMaVille pour envoyer la requête
            HttpResponse<String> response = HttpClientMaVille.post(url, json);

            // Vérifier la réponse de l'API
            if (response != null && response.statusCode() == 201) {
                System.out.println("Votre requête a été soumise avec succès.");
            } else {
                System.out.println("Erreur lors de la soumission de la requête.");
            }

    }

    private void rechercherTravaux(Scanner scanner){
      System.out.println("Rechercher Travaux.....");
    };


    // Méthode pour signaler un problème
    private static void signalerProbleme(Scanner scanner) {
        // Recueillir les informations auprès du résident
        System.out.print("Nom du résident : ");
        String nomResident = scanner.nextLine();

        System.out.print("Adresse courriel : ");
        String adresseCourriel = scanner.nextLine();

        System.out.print("Adresse de résidence : ");
        String adresseResidence = scanner.nextLine();

        System.out.print("Type de problème : ");
        String typeProbleme = scanner.nextLine();

        System.out.print("Description du problème : ");
        String descriptionProbleme = scanner.nextLine();

        // Créer un nouvel objet Probleme avec les informations saisies
        Probleme probleme = new Probleme(nomResident, adresseCourriel, adresseResidence, typeProbleme, descriptionProbleme);

        // Afficher les détails du problème signalé
        System.out.println("\nProblème signalé :");
        System.out.println(probleme.toString());
    }


    public void recevoirNotifs(){
        System.out.println("Recevoir Notifications.....");
    };

    public void afficherUnProjet(ProjetTravaux projet , int id) {
                System.out.println("--- Projet "+id+" ---");
                System.out.println("Titre : " + projet.getTitre());
                System.out.println("Description : " + projet.getDescription());
                System.out.println("Quartiers affectés : " + String.join(", ", projet.getQuartiersAffectes()));
                System.out.println("Rues affectées : " + String.join(", ", projet.getRuesAffectees()));
                System.out.println("Dates : " + projet.getDateDebut() + " au " + projet.getDateFin());
                System.out.println("Type de travaux : " + projet.getTypeTravaux());
                System.out.println("Horaire des travaux : " + projet.getHoraireTravaux());
        };
    public void afficherUneEntrave(Entrave entrave,int count) {
                    System.out.println("--- Entrave "+count+" ---");
                    System.out.println("Rue : " + entrave.getShortName());
                    System.out.println("Type d'impact : " + entrave.getStreetImpactType());
                    System.out.println("ID de la rue : " + entrave.getStreetId());
                    System.out.println("Identifiant de travail du projet lié : " + entrave.getIdRequest());
                };

}
