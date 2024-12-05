import java.util.List;
import java.util.Scanner;

public class Casino {
     // Créer les objets machine
    MagicMoolah machine1 = new MagicMoolah();
    //Allin machine2
    //Roulette machine3// Liste des machines disponibles

    boolean continuer = true;  // Contrôle du menu

    // Menu du casino
    public void menu(Joueur joueur, Scanner sc){
        // Tant que le joueur veux jouer
        while (continuer) {
            // Affichage du menu principal
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
            System.out.println(joueur.bleu + "=================Menu du Casino Magnifico=================" + joueur.resetCouleur);
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println(joueur.violet + "1. Jouer à Magic Moolah" + joueur.resetCouleur); // à remplacer par un menu de selection de machine
            System.out.println(joueur.vert + "2. Afficher vos informations" + joueur.resetCouleur);
            System.out.println(joueur.rouge + "3. Quitter" +joueur.resetCouleur);
            System.out.print("Votre choix : ");
            int choix = sc.nextInt();
    
            switch (choix) {
                // Machine MagicMoolah
                case 1:
                    machine1.jouer(joueur, sc);  // Lancer une partie de MagicMoolah
                    break;
                    /*
                // Machine AllIn
                case 2:
                    machine2.jouer(joueur, sc);
                    break;
                // Machine Roulette
                case 3:
                    machine3.jouer(joueur, sc);
                    break; 
                     */
                case 2:
                    joueur.afficherInfo(sc);  // Afficher les informations du joueur
                    break;
                case 3:
                    continuer = false;  // Quitter le jeu
                    System.out.println("\nMerci d'avoir joué !");
                    break;
                default:
                    System.out.println("\nChoix invalide.");
            }
        }

    }

}
