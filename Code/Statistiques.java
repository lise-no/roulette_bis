package casino;

import java.util.HashMap;
import java.util.Map;

public class Statistiques {
    private int gainsTotaux;
    private Map<String, Integer> gainsParMachine;  // Relation machine - gains
    private int nombreDeParties;
    private int miseTotale;
    private int nombreDeMises;

    public Statistiques() {
    }

    public void ajouterGainMachine(String nomMachine, int gain) {
        // Ajouter les gains d'une partie sur une machine spécifique
    }

    public void ajouterPartie() {
        // Augmenter le nombre de parties jouées
    }

    public void afficherStatistiques() {
        // Afficher les statistiques générales
    }

    public void ajouterMise(int mise) {
        // Ajouter la mise au total et augmenter le nombre de mises
    }

    public double getMiseMoyenne() {
    }

    public double getGainsMoyen() {
    }
}
