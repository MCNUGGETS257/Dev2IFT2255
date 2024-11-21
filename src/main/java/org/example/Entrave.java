package org.example;
public class Entrave {
    private String idRequest; // Identifiant du travail
    private String streetId; // Identifiant de la rue
    private String shortName; // Nom court de la rue
    private String streetImpactType; // Impact sur la rue

    // Constructeur
    public Entrave(String idRequest, String streetId, String shortName, String streetImpactType) {
        this.idRequest = idRequest;
        this.streetId = streetId;
        this.shortName = shortName;
        this.streetImpactType = streetImpactType;
    }

    // Getters et Setters
    public String getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStreetImpactType() {
        return streetImpactType;
    }

    public void setStreetImpactType(String streetImpactType) {
        this.streetImpactType = streetImpactType;
    }

    @Override
    public String toString() {
        return "Entrave{" +
                "idRequest='" + idRequest + '\'' +
                ", streetId='" + streetId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", streetImpactType='" + streetImpactType + '\'' +
                '}';
    }
}
