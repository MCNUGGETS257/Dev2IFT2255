package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MaVilleTest {

    private Resident resident;
    private Intervenant intervenant;


    @Test
    void testValiderCourriel_Valide() {
        assertTrue(MaVille.validerCourriel("valid.email@example.com"));
    }

    @Test
    void testValiderCourriel_Invalide() {
        assertFalse(MaVille.validerCourriel("invalid.email"));
    }

    @Test
    void testValiderMotDePasse_Valide() {
        assertTrue(MaVille.validerMotDePasse("Password1"));
    }

    @Test
    void testValiderMotDePasse_Invalide() {
        assertFalse(MaVille.validerMotDePasse("Pass1"));
    }

    @Test
    void testValiderMotDePasse_Invalide_SansMajuscule() {
        assertFalse(MaVille.validerMotDePasse("password1"));
    }

    @Test
    void testValiderMotDePasse_Invalide_SansChiffre() {
        assertFalse(MaVille.validerMotDePasse("Password"));
    }

    @Test
    void testChargerResidentsDepuisApi() throws IOException {
        ArrayList<Resident> residents = MaVille.chargerResidentsDepuisApi();
        assertNotNull(residents);
        assertFalse(residents.isEmpty());
        assertEquals("alice.dupont@example.com", residents.get(0).getCourriel());
    }

    /*@Test
    void testChargerIntervenantsDepuisApi() throws IOException {
        ArrayList<Intervenant> intervenants = MaVille.chargerIntervenantsDepuisApi();
        assertNotNull(intervenants);
        assertFalse(intervenants.isEmpty());
        assertEquals("12345678", intervenants.get(0).getIdentifiantVille());
    }*/


}
