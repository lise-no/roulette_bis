/**
 * Interface représentant une machine de jeu. 
 * Toute machine implémentant cette interface doit fournir des mécanismes pour lancer le jeu, 
 * afficher les résultats et vérifier les résultats après le jeu.
 */
public interface Machine {

    /**
     * Lance la machine, effectue les opérations de jeu et met à jour l'état en fonction des résultats.
     * 
     * @param joueur Le joueur qui lance la machine.
     */
    void lancer(Joueur joueur);

    /**
     * Affiche le résultat du jeu (par exemple, les valeurs générées par la machine).
     */
    void afficherResultat();

    /**
     * Vérifie les résultats du jeu et met à jour l'état du joueur (par exemple, son solde de jetons) 
     * en fonction des gains ou pertes.
     * 
     * @param joueur Le joueur qui a joué et dont le résultat doit être vérifié.
     */
    void verifierResultat(Joueur joueur);
}
