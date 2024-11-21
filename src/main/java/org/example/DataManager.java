package org.example;

import java.util.Arrays;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DataManager {
    private ArrayList<ProjetTravaux> projets = new ArrayList<>();
    private ArrayList<Entrave> entraves = new ArrayList<>();
    private HttpClientApi api = new HttpClientApi();
    private String ressourceIdTravaux = "cc41b532-f12d-40fb-9f55-eb58c9a2b12b";// ID du JSON contenant les travaux
    private String ressourceIdEntraves = "a2bc8014-488c-495d-941b-e7ae1999d1bd";
    private ArrayList<String> TypesTravaux = new ArrayList<>();
    private static ArrayList<String> quartiers = new ArrayList<>(
            Arrays.asList(
                    "Ahuntsic-Cartierville",
                    "Anjou",
                    "Côte-des-Neiges–Notre-Dame-de-Grâce",
                    "Lachine",
                    "LaSalle",
                    "Le Plateau-Mont-Royal",
                    "Le Sud-Ouest",
                    "L'Île-Bizard–Sainte-Geneviève",
                    "Mercier–Hochelaga-Maisonneuve",
                    "Montréal-Nord",
                    "Outremont",
                    "Pierrefonds-Roxboro",
                    "Rivière-des-Prairies–Pointe-aux-Trembles",
                    "Rosemont–La Petite-Patrie",
                    "Saint-Laurent",
                    "Saint-Léonard",
                    "Verdun",
                    "Ville-Marie",
                    "Villeray–Saint-Michel–Parc-Extension"
            )
    );



    // Constructeur
    public DataManager() {
        createProjects();
        associerEntravesProjets();
    }

    public JSONArray parseur(String ressourceId) {
        ApiResponse apiResponse = api.getData(ressourceId);
        String jsonData = apiResponse.getBody();
        JSONObject root = new JSONObject(jsonData);

        // Accessing the "records" field under the "result" object
        if (root.has("result") && root.getJSONObject("result").has("records")) {
            //System.out.println(root.getJSONObject("result").getJSONArray("records"));
            return root.getJSONObject("result").getJSONArray("records");
        } else {
            throw new JSONException("Records not found in the API response.");
        }
    }


    // Méthode pour parseur et créer les projets
    public void createProjects() {

        try {
            JSONArray records = parseur(ressourceIdTravaux);
            System.out.println(records.length());
            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);

                // Extraction des données
                String typeTravaux = record.getString("reason_category");
                String quartier = record.getString("boroughid");
                String rue =  "Non spécifié";
                String intervenant = record.optString("organizationname", "Non spécifié");

                // Titre amélioré
                String titre = typeTravaux + " à " + quartier ;

                // Description améliorée
                String description = "Type de travaux : " + typeTravaux + ", Quartier : " + quartier +", Rues affectées : " + rue + ", Intervenant : " + intervenant + ", Statut actuel : " + record.optString("currentstatus", "Non spécifié") + ", Catégorie : " + record.optString("submittercategory", "Non spécifié");

                if (!TypesTravaux.contains(typeTravaux)) {
                    TypesTravaux.add(typeTravaux);
                }

                String idRequest = record.getString("id");
                ArrayList<String> quartiersAffectes = new ArrayList<>();
                quartiersAffectes.add(quartier);

                ArrayList<String> ruesAffectees = new ArrayList<>();
                ruesAffectees.add(rue);

                String dateDebut = record.optString("duration_start_date", "Non spécifié").substring(0,10);
                String dateFin = record.optString("duration_end_date", "Non spécifié").substring(0,10);
                String horaireTravaux = "Non spécifié";

                // Création de l'objet ProjetTravaux
                ProjetTravaux projet = new ProjetTravaux(
                        idRequest,
                        titre,
                        description,
                        typeTravaux,
                        quartiersAffectes,
                        ruesAffectees,
                        dateDebut,
                        dateFin,
                        horaireTravaux
                );

                // Ajout à la liste
                projets.add(projet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher tous les projets
    public void afficherTousLesProjets() {
        if (projets.isEmpty()) {
            System.out.println("Aucun projet disponible.");
        } else {
            int compteur = 1;
            for (ProjetTravaux projet : projets) {
                System.out.println("--- Projet " + compteur + " ---");
                System.out.println("Titre : " + projet.getTitre());
                System.out.println("Description : " + projet.getDescription());
                System.out.println("Quartiers affectés : " + String.join(", ", projet.getQuartiersAffectes()));
                System.out.println("Dates : " + projet.getDateDebut() + " au " + projet.getDateFin());
                System.out.println("Type de travaux : " + projet.getTypeTravaux());
                System.out.println(); // Ligne vide pour séparer les projets
                compteur++;
            }
        }
    }


    public void associerEntravesProjets() {
        try {
            JSONArray entravesArray = parseur(ressourceIdEntraves);

            for (int i = 0; i < entravesArray.length(); i++) {
                JSONObject entraveJson = entravesArray.getJSONObject(i);

                // Extraction des données de l'entrave
                String idRequest = entraveJson.getString("id_request");
                String streetId = entraveJson.getString("streetid");
                String shortName = entraveJson.getString("shortname");
                String streetImpactType = entraveJson.getString("streetimpacttype");

                // Création de l'objet Entrave
                Entrave entrave = new Entrave(idRequest, streetId, shortName, streetImpactType);
                entraves.add(entrave);

                // Recherche du projet correspondant
                for (ProjetTravaux projet : projets) {
                    if (projet.getId().equals(idRequest)) {
                        // Ajout de l'entrave au projet
                        projet.ajouterEntrave(entrave);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public  void addProjet(ProjetTravaux Projet){
        projets.add(Projet);
    };

    public void removeProjet(ProjetTravaux Projet){
        if(projets.contains(Projet)){
            projets.remove(Projet);}
    }

    public  String GenerateId() {
        // Générer un UUID aléatoire
        String uuid = UUID.randomUUID().toString();

        // Vérifier l'unicité de l'UUID par rapport aux projets existants
        for (ProjetTravaux projet : projets) {
            if (projet.getId().equals(uuid)) {
                // Si le UUID existe déjà, générer un autre
                return GenerateId();
            }
        }
        return uuid;
    }

    public ArrayList<ProjetTravaux> getProjets(){
        return projets;
    };

    public static ArrayList<String> getQuartiers() {
        return quartiers;
    }

    public ArrayList<String> getTypesTravaux() {
        return TypesTravaux;
    }

    public ArrayList<Entrave> getEntraves(){ return entraves;}

    public String getRessourceIdEntraves() {
        return ressourceIdEntraves;
    }

    public void setTypesTravaux(ArrayList<String> typesTravaux) {
        TypesTravaux = typesTravaux;
    }

    public void setProjets(ArrayList<ProjetTravaux> projets) {
        this.projets = projets;
    }

}
