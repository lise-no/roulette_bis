import java.util.List;
import java.util.Scanner; // Pour gerer les entrées utilisateur
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

public class Casino {
     // Créer les objets machine
    MagicMoolah machine1 = new MagicMoolah();
    //Allin machine2
    //Roulette machine3// Liste des machines disponibles

    boolean continuer = true;  // Contrôle du menu

    // Menu du casino
    public void menu(Joueur joueur, Scanner sc){
        // Tant que le joueur veut jouer
        while (continuer) {
            // Affichage du menu principal
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire apparaitre le terminal vide)
            System.out.println("--+" + joueur.violet + '-' + joueur.jaune + '-' + joueur.cyan + '-'); // Permet de savoir où on se trouve
            System.out.println(joueur.bleu + "=================Menu du Casino Magnifico=================" + joueur.resetCouleur);
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println(joueur.violet + "1. Jouer à Magic Moolah" + joueur.resetCouleur); // à remplacer par un menu de selection de machine
            System.out.println(joueur.vert + "2. Afficher vos informations" + joueur.resetCouleur);
            System.out.println(joueur.rouge + "3. Quitter" +joueur.resetCouleur);
            
            int choix = 0; // Initialiser la variable de choix
    
            // Demander le choix du joueur tant que l'entrée est invalide
            while (true) {
                System.out.print("Votre choix : ");
                try {
                    choix = sc.nextInt(); // Essayer de lire un entier
                    sc.nextLine();  // Consommer la ligne restante
                    break; // Sortir de la boucle si la saisie est valide
                } catch (InputMismatchException e) {
                    System.out.println(joueur.rouge + "\nErreur : Veuillez entrer un nombre entier valide." + joueur.resetCouleur);
                    sc.nextLine(); // Consommer l'entrée invalide
                }
            }
        
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
                    joueur.afficherInfo(sc, joueur);  // Afficher les informations du joueur
                    break;
                case 3:
                    continuer = false;  // Quitter le jeu
                    System.out.println("\nMerci d'avoir joué !");
                    break;
                default:
                    System.out.println(joueur.rouge + "\nChoix invalide." + joueur.resetCouleur);
                    System.out.println("\nAppuyez sur entrée pour mettre un autre choix.");
                    sc.nextLine();  // Consommer la ligne restante
            }
        }
    }
    

}
