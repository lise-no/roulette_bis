import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Créer un objet Joueur
        Joueur joueur = new Joueur();

        // Créer un casino
        Casino casino = new Casino();
        
        // Initialisation du joueur
        joueur.initialisationJoueur(sc); 
        
        // Appel le menu
        casino.menu(joueur, sc);

        sc.close();
    }
}
