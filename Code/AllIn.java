import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 * Représente une machine à sous nommée "AllIn", dans laquelle les joueurs peuvent miser
 * des jetons et tenter de gagner ou de perdre en fonction des résultats d'un 
 * tableau de nombres et de symboles, selon une difficulté choisie.
 */
public class AllIn implements Machine {
    private int[] tab = new int[3];
    private int isSmaller; // stocke index du chiffre le plus petit dans le tableau
    private int difficulte; // La difficulté choisie par le joueur
    private int mise;
    private int gain;
    public int nivJoueurMin;


    /**
     * Constructeur de la machine AllIn. Initialise le tableau, la difficulté, la mise, 
     * le gain, la description des règles du jeu et le niveau minimum du joueur.
     */
    public AllIn() {
        this.tab = new int[3];
        this.difficulte = 0;
        this.mise = 0;
        this.gain = 0;
        this.nivJoueurMin = 8;
    }

    /**
     * Affiche les règles de la machine All In.
     * 
     * @param joueur Le joueur qui veut jouer.
     */
    public void affichageDescription(Joueur joueur){
        System.out.println(Affichage.jaune + "\n- Règles de la machine All In -\n\n" + Affichage.vert + "Pour le niveau de difficulté 1, la mise minimale est de " + joueur.getSolde() * 25 / 100 + " jetons !\n" + Affichage.bleu + "Pour le niveau de difficulté 2, la mise minimale est de " +joueur.getSolde() * 50 / 100 + " jetons !\n" + Affichage.violet + "Pour le niveau de difficulté 3, la mise minimale est de " + joueur.getSolde() + " jetons !\n" + Affichage.resetCouleur + "---------------------------------------------------------------------------");
    } 

    /**
     * Vérifie si le joueur a un niveau suffisant pour jouer à la machine.
     * 
     * @param joueur Le joueur qui veut jouer.
     * @return true si le joueur peut jouer, false sinon.
     */
    public boolean verifNivJoueurMin(Joueur joueur) {
        boolean peutJouer = true;
        // teste si le niveau du joueur est assez haut pour pouvoir utiliser la machine
        if (joueur.getNiveau() < this.nivJoueurMin) {
            System.out.println(Affichage.rouge + "Votre niveau est trop bas pour jouer au All In !" + Affichage.resetCouleur);
            try {
                    Thread.sleep(2000); // pour afficher le message avant de revenir sur le menu de Casino
                } catch (Exception e) {
                    System.out.println(e);
                }
                // break;
            return !peutJouer;
        }
        return peutJouer;
    }


    /**
     * Lance la machine, génère les chiffres dans le tableau et le symbole 
     * correspondant à la difficulté choisie. Le joueur gagne de l'expérience 
     * basée sur la mise.
     * 
     * @param joueur Le joueur qui joue.
     */
    public void lancer(Joueur joueur) {
        // crée tableau avec les chiffres et le symbole

        // à chaque fois qu'un tableau est créé, le joueur gagne la mise actuelle * 2 d'xp
        joueur.gagnerExperience(mise * 2);
        // nombres dans tableau
        this.tab[0] = nbAleatoire();
        this.tab[1] = nbAleatoire();
        this.isSmaller = (this.tab[0] < this.tab[1]) ? 0 : 1; // condition ternaire

        // symbole en fonction de la difficulté
        tab[2] = trouverSymbole();
    }


    /**
     * Gère l'interaction avec le joueur pendant le jeu, permettant de choisir la 
     * difficulté, de miser, de jouer et de redemander à rejouer.
     * 
     * @param joueur Le joueur qui joue.
     * @param sc Scanner pour l'entrée du joueur.
     */
    public void jouer(Joueur joueur, Scanner sc) {
        // permet de commencer à jouer
        boolean rejouer = true;

        System.out.print("\033[H\033[2J"); // Clear le terminal
        affichageDescription(joueur);
        if (!verifNivJoueurMin(joueur)){
            rejouer = false ;
        }
        // permet de rejouer immédiatement
        while (rejouer) {
            // choix de la mise, vérification de la mise et choix de la difficulté
            nivDifficulte(sc);
            miser(joueur, sc);
            rejouer = verifEntreeMise(joueur, sc);

            // permet au joueur d'arrêter de jouer avant d'avoir créé un tableau
            if (rejouer) {
                // remplit tableau de chiffres et d'un symbole
                lancer(joueur);

                // affichage tableau
                afficherResultat();
                verifierResultat(joueur);

                // si le joueur veut rejouer
                rejouer = demandeRejouer(joueur, sc);
            }
        }
    }

    /**
     * Demande au joueur s'il veut continuer à jouer ou non.
     * 
     * @param joueur Le joueur qui joue.
     * @param sc Scanner pour l'entrée du joueur.
     * @return true si le joueur veut rejouer, false sinon.
     */
    public boolean demandeRejouer(Joueur joueur, Scanner sc) {
        // demande si l'utilisateur veut rejouer
        boolean rejouer = false, valide = false;
        String reponseUtilisateur;

        System.out.println("Voulez-vous continuer à jouer ? (oui/non)");
        sc.nextLine(); // consomme le \n
        // permet de redemmander un input plusieurs fois si besoin
        while (!valide) {
            reponseUtilisateur = sc.nextLine().trim().toLowerCase();
            // si le joueur veut rejouer
            if (reponseUtilisateur.equals("oui") || reponseUtilisateur.equals("o") || reponseUtilisateur.equals("yes") || reponseUtilisateur.equals("y")) {
                rejouer = true;
                valide = true;
            // si le joueur ne veut pas rejouer
            } else if (reponseUtilisateur.equals("non") || reponseUtilisateur.equals("n") || reponseUtilisateur.equals("no")) {
                rejouer = false;
                valide = true;
            // si l'input n'est pas dans (oui, o, yes, y, non, no, n)
            } else {
                System.out.println(Affichage.rouge + "Une erreur s'est produite, veuillez entrer oui ou non." + Affichage.resetCouleur);
            }
        }
        // joueur récupère toute mise restante dans mise avant de quitter la machine
        joueur.modifSolde(this.mise);
        this.mise = 0;
        return rejouer;
    }

    /**
     * Affiche le tableau remplis.
     */
    public void afficherResultat() {
        // Afficher le tableau avec les résultats
        char[] operateurs = { '-', '/', '+', '*' };
        System.out.println("[ " + this.tab[0] + " | " + this.tab[1] + " | " + operateurs[this.tab[2] + 1] + " ] \n");
    }

    /**
     * Vérifie le résultat du jeu et met à jour le solde du joueur en fonction des gains ou pertes.
     * 
     * @param joueur Le joueur qui joue.
     */
    public void verifierResultat(Joueur joueur) {
        // Calculer les gains et les afficher
        // variable
        int nb;
        
        // calcul du gain pour pour symbole est + ou -
        if (this.tab[2] == 1 || this.tab[2] == -1) {

            // calcul de la somme des nombres dans le tableau x la mise / par 1000 pour perdre ou gagner plus
            nb = this.tab[0] * this.tab[1] * this.mise / 1000;
            this.gain = this.gain + tab[2] * nb;
            this.mise = this.mise + this.gain;
            // différent affichage en fonction du symbole (+ ou -)
            if (tab[2] * nb >= 0) {
                System.out.println(
                "Bravo ! Vous gagnez " + this.gain + " jetons.");
            } else {
                System.out.println(
                "Dommage... Vous perdez " + this.gain * -1 + " jetons.");
            }

        } else if (this.tab[2] == 2) { // calcul du gain pour symbole est *
            // on utilise le plus petit chiffre des deux obtenus dans le tableau
            nb = tab[this.isSmaller];
            this.gain = this.mise * nb;
            this.mise = this.mise + this.mise * nb;
            // affichage
            System.out.println(
                "Bravo ! Vous gagnez " + this.gain + " jetons.");

        } else if (this.tab[2] == 0) { // calcul du gain pour symbole est /
            // on utilise le plus grand chiffre des deux obtenus dans le tableau
            nb = tab[(isSmaller == 0) ? 1 : 0];
            this.gain = this.mise - (this.mise / nb);
            this.mise = this.mise / nb;
            // affichage
            System.out.println(
                "Dommage... Vous perdez " + this.gain + " jetons.");
        }

        // modifie dans joueur le nombre de jetons qu'il obtient après avoir joué
        joueur.modifSolde(this.mise);
        this.mise = 0;
        this.gain = 0;
    }

    /**
     * Détermine le symbole à afficher en fonction de la difficulté choisie.
     * 
     * @return Le symbole (1 pour +, -1 pour -, 2 pour *, 0 pour /).
     */
    private int trouverSymbole() {
        // trouve symbole en fonction de la difficulté
        int[] dif1 = { 1, 1, 1, 1, 1, -1, -1, -1, -1, -1 };
        int[] dif2 = { 2, 1, 1, 1, -1, -1, -1, -1, -1, -1 };
        int[] dif3 = { 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2 };
        // -1 est -, 1 est +, 2 est *, 0 est /
        ArrayList<int[]> tab = new ArrayList<int[]>();
        tab.add(dif1);
        tab.add(dif2);
        tab.add(dif3);
        return tab.get(this.difficulte - 1)[nbAleatoire()];
    }

    /**
     * Demande au joueur de choisir la difficulté du jeu entre 1 et 3.
     * 
     * @param sc Scanner pour l'entrée du joueur.
     */
    public void nivDifficulte(Scanner sc) {
        // Retourne le choix de difficulté de l'utilisateur entre 1 et 3

        // variables
        boolean valide = false;
        System.out.println("Quelle difficulté allez-vous tenter ? (" + Affichage.vert + "facile : 1" + Affichage.resetCouleur + ", " + Affichage.bleu + "moyenne : 2" + Affichage.resetCouleur + ", " + Affichage.violet + "difficile : 3" + Affichage.resetCouleur + ")");
        while (!valide) {
            try {
                this.difficulte = sc.nextInt();
                // si la valeur entrée dans difficulté n'est pas comprise entre 1 et 3
                while (this.difficulte != 1 && this.difficulte != 2 && this.difficulte != 3) {
                    System.out.println(Affichage.rouge + "Veuillez entrer un nombre entre 1 et 3 !" + Affichage.resetCouleur);
                    this.difficulte = sc.nextInt();
                }
                valide = true;
                // si la valeur entrée dans difficulté n'est pas un chiffre
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println(Affichage.rouge + "Une erreur s'est produite... Veuillez entrer un chiffre entre 1 et 3 !" + Affichage.resetCouleur);
            }
        }
        // s'affiche si le chiffre entré est valide
        System.out.println("Vous avez choisi la difficulté " + difficulte + ", bonne chance !\n");
    }

    /**
     * Retourne un nombre aléatoire entre 1 et 9.
     * 
     * @return Un nombre entier aléatoire entre 1 et 9.
     */
    private int nbAleatoire() {
        // Retourne un nombre aléatoire entre 1 et 9 en utilisant la fonction random
        Random rand = new Random();
        return rand.nextInt(10);
    }

    /**
     * Demande au joueur combien de jetons il veut miser et met à jour sa mise.
     * 
     * @param joueur Le joueur qui joue.
     * @param sc Scanner pour l'entrée du joueur.
     */
    public void miser(Joueur joueur, Scanner sc) {
        // demander au joueur combien il veut miser
        // variables
        int nouvMise;
        boolean valide = false;

        System.out.println(
                "Vous possédez " + joueur.getSolde() + " jetons. Combien de jetons voulez-vous miser ?");
        // permet au catch/try de se déclencher autant de fois que nécessaire
        while (!valide) {
            try {
                nouvMise = sc.nextInt();
                // si la mise ajoutée est négative
                if (nouvMise < 0) {
                    System.out.println(Affichage.rouge + "Une erreur s'est produite, veuillez entrer une mise positive." + Affichage.resetCouleur);
                    nouvMise = sc.nextInt();
                }
                // vérification que la mise ajoutée n'est pas supérieure au nombre de jetons du joueur
                if (nouvMise <= joueur.getSolde()) {
                    // on enlève au solde du joueur les jetons qui sont misés
                    joueur.modifSolde(-nouvMise);
                    // on ajoute à la mise les jetons ajoutés
                    this.mise += nouvMise;
                    valide = true;
                } else {
                    // si la mise entrée est supérieure au solde du joueur
                    System.out.println(Affichage.rouge + "Solde insuffisant, veuillez entrer un nouveau montant." + Affichage.resetCouleur);
                }
            // si l'input n'est pas un chiffre, le catch est déclenché
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println(Affichage.rouge + "Une erreur s'est produite, veuillez entrer un nombre." + Affichage.resetCouleur);
            }
        }
    }

    /**
     * Retourne un booléan pour savoir si le joueur rejoue ou non, vérifie si la mise est suffisante pour la difficulté choisie, et permet 
     * au joueur de changer sa mise, sa difficulté, ou d'arrêter de jouer.
     * 
     * @param joueur Joueur qui joue.
     * @param sc Scanner pour l'entrée du joueur.
     * @return playAgain boolean pour savoir si le joueur rejoue ou non.
     */
    public boolean verifEntreeMise(Joueur joueur, Scanner sc) {
        // permet au joueur de choisir entre changer sa mise, changer de difficulté ou
        // arrêter de jouer quand la mise n'est pas suffisante pour la difficulté
        int exit = 0;
        int[] misePourChaqueDifficulte = { (joueur.getSolde() + this.mise) * 25 / 100, (joueur.getSolde() + this.mise) * 50 / 100, (joueur.getSolde() + this.mise)  };
        boolean valide = false, playAgain = true;

        // si la mise est trop petite pour la difficulté choisie
        if (this.mise < misePourChaqueDifficulte[this.difficulte - 1]) {
            joueur.modifSolde(this.mise);
            System.out.println(Affichage.rouge + "! Attention !" + Affichage.resetCouleur + " La mise minimale pour la difficulté " + this.difficulte + " est de " + misePourChaqueDifficulte[this.difficulte - 1] + " jetons. Voulez-vous : " + Affichage.jaune +" \n   1. Changer votre mise " + Affichage.vert + "\n   2. Changer de difficulté " + Affichage.violet +"\n   3. Arrêter de jouer" + Affichage.resetCouleur);
            // permet au catch/try de se déclencher autant de fois que besoin
            while (!valide) {
                try {
                    exit = sc.nextInt();
                    // si l'entrée n'est pas entre 1 et 3
                    if (exit != 1 && exit != 2 && exit != 3) {
                        System.out.println(Affichage.rouge + "Veuillez choisir un nombre entre 1 et 3 !" + Affichage.resetCouleur);
                        exit = sc.nextInt();
                    }
                    valide = true;
                    // si l'input n'est pas un chiffre, l'exception se déclenche
                } catch (InputMismatchException e) {
                    sc.nextLine();
                    System.out.println(Affichage.rouge + "Une erreur s'est produite, veuillez entrer un nombre." + Affichage.resetCouleur);
                }
            }
        }
        // si le joueur veut changer sa mise
        if (exit == 1) {
            this.mise = 0;
            miser(joueur, sc);
            playAgain = verifEntreeMise(joueur, sc);
        }
        // si le joueur veut changer sa difficulté
        else if (exit == 2) {
            this.mise = 0;
            nivDifficulte(sc);
            miser(joueur, sc);
            playAgain = verifEntreeMise(joueur, sc);
        }
        // si le joueur veut arrêter de jouer
        else if (exit == 3) {
            this.mise = 0;
            playAgain = false;
        }
        return playAgain;
    }

}