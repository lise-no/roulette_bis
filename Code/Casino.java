import java.util.List;
import java.util.Scanner; // Pour gerer les entrées utilisateur
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

public class Casino {
     // Créer les objets machine
    MagicMoolah machine1 = new MagicMoolah();
    AllIn machine2 = new AllIn();
    Roulette machine3 = new Roulette(5);

    boolean continuer = true;  // Contrôle du menu

    // Menu du casino
    public void menu(Joueur joueur, Scanner sc){
        // Tant que le joueur veut jouer
        while (continuer) {
            // Affichage du menu principal
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire apparaitre le terminal vide)
            System.out.println("--+" + Affichage.violet + '-' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
            System.out.println(Affichage.bleu + "=================Menu du Casino Magnifico=================" + Affichage.resetCouleur);
            System.out.println("\nQue voulez-vous faire ?");
            System.out.println("Jouer à une machine :");
            System.out.println(Affichage.violet + "     1. Magic Moolah" + Affichage.resetCouleur);
            System.out.println(Affichage.jaune + "      2. AllIn" + Affichage.resetCouleur);
            System.out.println(Affichage.cyan + "       3. Roulette" + Affichage.resetCouleur);
            System.out.println("Autre action :");
            System.out.println(Affichage.vert + "       4. Afficher vos informations" + Affichage.resetCouleur);
            System.out.println(Affichage.rouge + "      5. Quitter" +Affichage.resetCouleur);
            
            int choix = 0; // Initialiser la variable de choix
    
            // Demander le choix du joueur tant que l'entrée est invalide
            while (true) {
                System.out.print("Votre choix : ");
                try {
                    choix = sc.nextInt(); // Essayer de lire un entier
                    sc.nextLine();  // Consommer la ligne restante
                    break; // Sortir de la boucle si la saisie est valide
                } catch (InputMismatchException e) {
                    System.out.println(Affichage.rouge + "\nErreur : Veuillez entrer un nombre entier valide." + Affichage.resetCouleur);
                    sc.nextLine(); // Consommer l'entrée invalide
                }
            }
        
            switch (choix) {
                // Machine MagicMoolah
                case 1:
                    machine1.jouer(joueur, sc);  // Lancer une partie de MagicMoolah
                    break;
                // Machine AllIn
                case 2:
                    machine2.jouer(joueur, sc);
                    break;
                // Machine Roulette
                case 3:
                    machine3.Jouer(joueur, sc);
                    break; 
                case 4:
                    joueur.afficherInfo(sc, joueur);  // Afficher les informations du joueur
                    break;
                case 5:
                    continuer = false;  // Quitter le jeu
                    System.out.println("\nMerci d'avoir joué !");
                    break;
                default:
                    System.out.println(Affichage.rouge + "\nChoix invalide." + Affichage.resetCouleur);
                    System.out.println("\nAppuyez sur entrée pour mettre un autre choix.");
                    sc.nextLine();  // Consommer la ligne restante
            }
        }
    }
    

}
