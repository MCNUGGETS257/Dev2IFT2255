package org.example;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ResidentMenu {
    private DataManager dataManager = new DataManager();

    private static ArrayList<Notification> notifications = new ArrayList<Notification>();
    public ResidentMenu() {}

    public static ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public static void setNotifications(ArrayList<Notification> notifications) {
        ResidentMenu.notifications = notifications;
    }

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
        int compteur = 1;
        LocalDate aujourdHui = LocalDate.now(); // Date actuelle
        LocalDate dansTroisMois = aujourdHui.plusMonths(3); // Date dans trois mois

        for (ProjetTravaux projet : dataManager.getProjets()) {
            LocalDate dateFin = LocalDate.parse(projet.getDateFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (dateFin.isBefore(dansTroisMois) && dateFin.isAfter(aujourdHui)) {
                afficherUnProjet(projet, compteur++);
            }
        }

        if (compteur == 1) {
            System.out.println("Aucun projet prévu dans les trois prochains mois.");
        }
    }

    private void consulterEntraves(){
        int count = 1;
        for(Entrave entrave: dataManager.getEntraves()){
            afficherUneEntrave(entrave,count++);
        }
    };


    private void soumettreRequete(Scanner scanner) {
        System.out.println("Soumettre une requête de travail:");
        System.out.print("Titre: ");
        String titre = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.println("Quel Type de travaux?");
        int count = 0;
        for (String type : dataManager.getTypesTravaux()) {
            System.out.println(count++ + ". " + type);
        }

        while (!scanner.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre valide pour le type de travaux.");
            scanner.nextLine(); // Clear the invalid input
        }
        int indice0 = scanner.nextInt();
        scanner.nextLine(); // Clear the newline after the integer input
        if (indice0 >= 0 && indice0 < dataManager.getTypesTravaux().size()) {
            String type = dataManager.getTypesTravaux().get(indice0);

            // Validate date format (YYYY-MM-DD)
            String dateDebut = "";
            boolean validDate = false;
            while (!validDate) {
                System.out.println("Date debut (YYYY-MM-DD):");
                dateDebut = scanner.nextLine();

                // Check if the date is in the correct format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate parsedDate = LocalDate.parse(dateDebut, formatter); // Try to parse the date
                    validDate = true; // If parsing succeeds, the date is valid
                } catch (DateTimeParseException e) {
                    System.out.println("Date invalide. Veuillez entrer une date au format YYYY-MM-DD.");
                }
            }

            RequeteTravail requete = new RequeteTravail(titre, description, type, dateDebut);
            MaVille.requetesTravail.add(requete);
            System.out.println("Votre requête a été soumise.");
        } else {
            System.out.println("Type de travaux non valide.");
        }
    }
    private void rechercherTravaux(Scanner scanner){
        System.out.println("Choisissez un filtre pour rechercher travaux:\n1.titre\n2.Type travaux\n3.Quartier");
        int choix = scanner.nextInt();
        switch (choix){
            case 1:
                System.out.println("Quel titre?");
                String titre = scanner.nextLine();
                for(ProjetTravaux projet: dataManager.getProjets()){
                    if(projet.getTitre().equalsIgnoreCase(titre)){
                        System.out.println(projet.toString());
                    }
                }
                break;
            case 2:
                System.out.println("Quel Type de travaux??");
                int count = 0;
                for(String Type: dataManager.getTypesTravaux()) {
                    System.out.println(new StringBuilder().append("").append(count++).append(".").append(Type).toString());
                }
                int indice = scanner.nextInt();

                for(ProjetTravaux projet: dataManager.getProjets()){
                    if(projet.getTypeTravaux().equalsIgnoreCase(dataManager.getTypesTravaux().get(indice))){
                        System.out.println(projet.toString());
                    }
                }
                break;
            case 3:
                System.out.println("Quel Quartier ou arrondissement?");
                int count1 = 0;
                for(String quartier: dataManager.getQuartiers()){
                    System.out.println(new StringBuilder().append("").append(count1++).append(".").append(quartier).toString());
                }
                int indice1 = scanner.nextInt();
                for(ProjetTravaux projet: dataManager.getProjets()){
                    if(projet.getQuartiersAffectes().contains(dataManager.getQuartiers().get(indice1))){
                        System.out.println(projet.toString());
                    }
                }
                break;

        }
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
        for(Notification notif: notifications){
            System.out.println(notif.toString());
        }
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

    public ArrayList<ProjetTravaux> getTravaux() {
        return dataManager.getProjets();
    }
}
