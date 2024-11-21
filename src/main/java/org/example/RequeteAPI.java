package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequeteAPI {
    private static final String FILE_PATH = "resources/requetes.json";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7001);

        app.get("/requetes", RequeteAPI::getRequetes);
        app.post("/requetes", RequeteAPI::addRequete);
    }

    private static void getRequetes(Context ctx) {
        try {
            ArrayList<RequeteTravail> requetes = readRequetesFromFile();
            ctx.json(requetes);
        } catch (IOException e) {
            ctx.status(500).result("Erreur lors de la lecture des requêtes.");
        }
    }

    private static void addRequete(Context ctx) {
        try {
            RequeteTravail requete = ctx.bodyAsClass(RequeteTravail.class);
            ArrayList<RequeteTravail> requetes = readRequetesFromFile();
            requetes.add(requete);
            writeRequetesToFile(requetes);
            ctx.status(201).json(requete);
        } catch (IOException e) {
            ctx.status(500).result("Erreur lors de l'ajout de la requête.");
        }
    }

    private static ArrayList<RequeteTravail> readRequetesFromFile() throws IOException {
        return objectMapper.readValue(new File(FILE_PATH), objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, RequeteTravail.class));
    }

    private static void writeRequetesToFile(ArrayList<RequeteTravail> requetes) throws IOException {
        objectMapper.writeValue(new File(FILE_PATH), requetes);
    }
}
