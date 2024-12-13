import java.util.Scanner;

/**
 * Classe principale du programme qui lance le jeu.
 * Cette classe crée un joueur, un casino et gère le déroulement du jeu à travers le menu.
 */
public class Main {

    /**
     * Point d'entrée principal du programme.
     * Cette méthode initialise le joueur et le casino, puis appelle le menu du casino pour démarrer le jeu.
     * 
     * @param args Arguments de ligne de commande (non utilisés dans cette application).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Créer un objet Joueur
        Joueur joueur = new Joueur();

        // Créer un casino
        Casino casino = new Casino();
        
        // Initialisation du joueur
        joueur.initialisationJoueur(sc, joueur); 
        
        // Appel le menu
        casino.menu(joueur, sc);

        sc.close();
    }
}
