package org.example;


import java.util.ArrayList;
import java.util.Scanner;

public class IntervenantMenu {
    private ArrayList<RequeteTravail> requetes = MaVille.requetesTravail;
    private DataManager dataManager = new DataManager();
    private ArrayList<Notification> new_notifications = ResidentMenu.getNotifications();


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
        System.out.println("Titre du projet:");
        String titre = scanner.nextLine();

        System.out.println("Description du projet:");
        String description = scanner.nextLine();

        System.out.println("Type de travaux:");
        String typeTravaux = scanner.nextLine();

        ArrayList<String> quartiersAffectes = new ArrayList<>();
        System.out.println("Entrez les quartiers affectés (tapez 'fin' pour terminer):");
        while (true) {
            String quartier = scanner.nextLine();
            if (quartier.equalsIgnoreCase("fin")) {
                break;
            }
            quartiersAffectes.add(quartier);
        }

        ArrayList<String> ruesAffectees = new ArrayList<>();
        System.out.println("Entrez les rues affectées (tapez 'fin' pour terminer):");
        while (true) {
            String rue = scanner.nextLine();
            if (rue.equalsIgnoreCase("fin")) {
                break;
            }
            ruesAffectees.add(rue);
        }

        System.out.print("Date de début (AAAA-MM-JJ): ");
        String dateDebut = scanner.nextLine();

        System.out.print("Date de fin (AAAA-MM-JJ): ");
        String dateFin = scanner.nextLine();

        System.out.print("Horaire des travaux: ");
        String horaireTravaux = scanner.nextLine();
        String id = dataManager.GenerateId();
        // Créer le projet de travaux
        ProjetTravaux nouveauProjet = new ProjetTravaux(id,titre, description, typeTravaux, quartiersAffectes, ruesAffectees, dateDebut, dateFin, horaireTravaux);
        dataManager.addProjet(nouveauProjet);

        // Créer les entraves associées
        System.out.println("Ajout des entraves associées au projet.");
        while (true) {
            System.out.println("Voulez-vous ajouter une nouvelle entrave ? (oui/non)");
            String reponse = scanner.nextLine();
            if (reponse.equalsIgnoreCase("non")) {
                break;
            }

            System.out.print("Identifiant de la rue (streetId) : ");
            String streetId = scanner.nextLine();

            System.out.print("Nom court de la rue (shortName) : ");
            String shortName = scanner.nextLine();

            System.out.print("Type d'impact sur la rue (streetImpactType) : ");
            String streetImpactType = scanner.nextLine();

            // Créer une entrave
            Entrave nouvelleEntrave = new Entrave(nouveauProjet.getId(), streetId, shortName, streetImpactType);

            // Associer l'entrave au projet
            nouveauProjet.ajouterEntrave(nouvelleEntrave);
        }

        // Ajouter une notification pour les résidents
        String msgNotif = "Projet ajouté\nInformations sur le projet :\n" + nouveauProjet.toString();
        Notification notification = new Notification(msgNotif);
        new_notifications.add(notification);
        ResidentMenu.setNotifications(new_notifications);
    }


    private void mettreAJourChantier(Scanner scanner) {
        if (dataManager.getProjets().isEmpty()) {
            System.out.println("Aucun chantier disponible pour mise à jour.");
            return;
        }

        System.out.println("Choisissez un chantier à mettre à jour:");
        // liste des travaux
        for (int i = 0; i < dataManager.getProjets().size(); i++) {
            System.out.println((i + 1) + ". " + dataManager.getProjets().get(i).getTitre());
        }

        int choixChantier = scanner.nextInt() - 1;// rentrer numéro pour choisir le chantier à mettre à jour
        scanner.nextLine();

        if (choixChantier >= 0 && choixChantier < dataManager.getProjets().size()) {
            ProjetTravaux projet = dataManager.getProjets().get(choixChantier);

            System.out.println("Nouveau titre (laissez vide pour ne pas changer):");
            String nouveauTitre = scanner.nextLine();
            if (!nouveauTitre.isEmpty()) {
                projet.setTitre(nouveauTitre);
            }

            System.out.println("Nouvelle description (laissez vide pour ne pas changer):");
            String nouvelleDescription = scanner.nextLine();
            if (!nouvelleDescription.isEmpty()) {
                projet.setDescription(nouvelleDescription);
            }
            String msgNotif = "Chantier mise à jour\nNouveaux informations du chantier:\n"+ projet.toString();
            Notification notification = new Notification(msgNotif);
            new_notifications.add(notification);
            ResidentMenu.setNotifications(new_notifications);
        } else {
            System.out.println("Chantier invalide.");
        }
    }

    private void consulterRequetes() {
        int compteur = 1;
        if (requetes.isEmpty()) {
            System.out.println("Aucune requête de travail disponible.");
        } else {
            for (RequeteTravail requete : requetes) {
                System.out.println("---- Requete "+ compteur++ +" ----");
                System.out.println(requete.toString());
            }
        }
    }

    public ArrayList<RequeteTravail> getRequetes() {
        return requetes;
    }
}
