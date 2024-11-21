package org.example;



import java.time.LocalDateTime;

public class Notification {
    private LocalDateTime date_envoi;
    private String message;


    public Notification(String message) {
        this.message = message;
        this.date_envoi = LocalDateTime.now();  // La date et l'heure actuelles
    }


    public String getMessage() {
        return message;
    }



    public LocalDateTime getDateEnvoi() {
        return date_envoi;
    }

    // Méthode pour afficher les détails de la notification
    @Override
    public String toString() {
        return "Notification envoyée :\n" +
                " message: "+message+
                "\nsDate d'envoi : " + date_envoi;
    }
}

