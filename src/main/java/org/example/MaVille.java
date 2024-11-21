package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaVille {
    public static ArrayList<Resident> residents = new ArrayList<>();
    public static ArrayList<Intervenant> intervenants = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        // Initialisation des listes pour les résidents et intervenants
        ArrayList<Resident> residents = chargerResidentsDepuisJson("src/main/resources/Residents.json");
        ArrayList<Intervenant> intervenants = chargerIntervenantsDepuisJson("src/main/resources/Intervenants.json");
        ArrayList<RequeteTravail> requetesTravail = chargerRequetesTravailDepuisJson("src/main/resources/requetes.json");

        Scanner scanner = new Scanner(System.in);
        menu();

        while (true) {
            int choix = -1;
            if(scanner.hasNextInt()){
                choix = scanner.nextInt();
            }else{
                System.out.println("Veuillez entrer un nombre valide (1, 2 ou 3).");
                scanner.nextLine();
            }
            scanner.nextLine(); // Capturer la ligne suivante
            switch (choix) {
                case 1:
                    // Inscription
                    System.out.println("Entrer rôle: Resident ou Intervenant \n Taper EXIT pour revenir au menu");
                    String role = scanner.nextLine();
                    if (role.equalsIgnoreCase("EXIT")) {
                        menu();
                        continue;
                    }

                    String courriel = "";
                    while (!validerCourriel(courriel)) {
                        System.out.println("Courriel:");
                        courriel = scanner.nextLine();
                        if (!validerCourriel(courriel)) {
                            System.out.println("Adresse courriel invalide. Réessayez.");
                        }
                    }

                    System.out.println("Mot de passe (au moins 8 caractères, une lettre majuscule, et un chiffre):");
                    String mdp = "";
                    while (!validerMotDePasse(mdp)) {
                        mdp = scanner.nextLine();
                        if (!validerMotDePasse(mdp)) {
                            System.out.println("Mot de passe invalide. Assurez-vous qu'il contient au moins 8 caractères, une lettre majuscule, et un chiffre.");
                        }
                    }

                    if (role.equalsIgnoreCase("Resident")) {
                        // Logique d'inscription pour Resident
                        System.out.println("Nom complet:");
                        String nom = scanner.nextLine();

                        System.out.println("Date de naissance (format: yyyy-MM-dd):");
                        String dateNaissance = scanner.nextLine();

                        System.out.println("Téléphone (optionnel):");
                        String telephone = scanner.nextLine();

                        System.out.println("Adresse résidentielle:");
                        String adresse = scanner.nextLine();

                        // Ajout du résident dans la liste
                        residents.add(new Resident(nom, courriel, mdp, dateNaissance, telephone, adresse));
                        System.out.println("Votre compte résident a été enregistré!");
                    } else if (role.equalsIgnoreCase("Intervenant")) {
                        // Logique d'inscription pour Intervenant
                        System.out.println("Nom complet:");
                        String nom = scanner.nextLine();

                        System.out.println("Type (Entreprise publique, Entrepreneur privé, Particulier):");
                        String type = scanner.nextLine();

                        System.out.println("Identifiant de la ville (code à 8 chiffres):");
                        String identifiantVille = scanner.nextLine();

                        // Ajout de l'intervenant dans la liste
                        intervenants.add(new Intervenant(nom, courriel, mdp, type, identifiantVille));
                        System.out.println("Votre compte intervenant a été enregistré!");
                    } else {
                        System.out.println("Rôle non reconnu. Veuillez réessayer.");
                    }

                    menu(); // Pour rester connecter à l'appli et l'acceuil
                    break;

                case 2:
                    // Connexion avec boucle while pour répétition en cas d'échec
                    boolean connexionReussie = false;

                    while (!connexionReussie) {
                        System.out.println("Entrez votre adresse e-mail (ou tapez 'EXIT' pour quitter) :");
                        String emailConnexion = scanner.nextLine();

                        if (emailConnexion.equalsIgnoreCase("EXIT")) {
                            System.out.println("Retour au menu.");
                            menu(); // Retour au menu principal si l'utilisateur veut quitter
                            break; // Quitter la boucle de connexion
                        }

                        System.out.println("Entrez votre mot de passe :");
                        String mdpConnexion = scanner.nextLine();

                        // Vérifier les comptes résidents
                        for (Resident resident : residents) {
                            if (resident.getCourriel().equalsIgnoreCase(emailConnexion) && resident.getMotDePasse().equals(mdpConnexion)) {
                                System.out.println("Connexion réussie en tant que Resident!");
                                connexionReussie = true;
                                ResidentMenu residentMenu = new ResidentMenu();
                                residentMenu.afficherMenu(scanner);
                                break; // Sortir de la boucle de connexion après une réussite
                            }
                        }

                        // Vérifier les comptes intervenants
                        if (!connexionReussie) {
                            for (Intervenant intervenant : intervenants) {
                                if (intervenant.getCourriel().equalsIgnoreCase(emailConnexion) && intervenant.getMotDePasse().equals(mdpConnexion)) {
                                    System.out.println("Connexion réussie en tant qu'Intervenant!");
                                    connexionReussie = true;
                                    IntervenantMenu intervenantMenu = new IntervenantMenu();
                                    intervenantMenu.afficherMenu(scanner);
                                    break; // Sortir de la boucle de connexion après une réussite
                                }
                            }
                        }

                        // Si aucune connexion n'a réussi
                        if (!connexionReussie) {
                            System.out.println("Échec de la connexion. Courriel ou mot de passe incorrect. Essayez à nouveau.");
                        }
                    }

                    menu(); // Retour au menu principal une fois la connexion réussie ou après quitter
                    break;


                case 3:
                    // Quitter l'application
                    System.out.println("Merci d'avoir utilisé MaVilleApp.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
                    menu();
                    break;
            }
        }
    }

    // Méthode pour valider l'adresse courriel
    public static boolean validerCourriel(String courriel) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(courriel);
        return matcher.matches();
    }

    // Méthode pour valider le mot de passe
    public static boolean validerMotDePasse(String motDePasse) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(motDePasse);
        return matcher.matches();
    }

    // Affichage du menu principal
    public static void menu() {
        System.out.println("Bienvenue dans MaVille");
        System.out.println("Menu:");
        System.out.println("Choisir une option:");
        System.out.println("1. S'inscrire");
        System.out.println("2. Se connecter");
        System.out.println("3. Quitter l'application");
    }

    // Méthode pour charger les résidents depuis le fichier JSON
    public static ArrayList<Resident> chargerResidentsDepuisJson(String cheminFichier) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(cheminFichier);
        Type type = new TypeToken<ArrayList<Resident>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    // Méthode pour charger les intervenants depuis le fichier JSON
    public static ArrayList<Intervenant> chargerIntervenantsDepuisJson(String cheminFichier) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(cheminFichier);
        Type type = new TypeToken<ArrayList<Intervenant>>() {}.getType();
        return gson.fromJson(reader, type);
    }
    // Charger les requêtes depuis le fichier Json
    public static ArrayList<RequeteTravail> chargerRequetesTravailDepuisJson(String cheminFichier) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(cheminFichier);
        Type type = new TypeToken<ArrayList<RequeteTravail>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    public static ArrayList<Resident> chargerResidentsDepuisApi() throws IOException {
        String url = "http://localhost:7002/residents"; // L'URL de l'API REST
        HttpResponse<String> response = HttpClientMaVille.get(url);

        if (response != null && response.statusCode() == 200) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Resident>>() {}.getType();
            return gson.fromJson(response.body(), type);
        } else {
            System.out.println("Erreur de récupération des résidents.");
            return new ArrayList<>();
        }
    }

}
