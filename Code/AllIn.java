import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class AllIn implements Machine {
    private int[] tab = new int[3];
    private int isSmaller; // stocke index du chiffre le plus petit dans le tableau
    private int difficulte; // La difficulté choisie par le joueur
    private int mise;
    private int gain;
    private int gainsPositifs; // total des gains
    private int gainsNegatifs; // total des pertes
    private int miseTotale; // mise totale pariée
    private String description;
    private int volatilite;

    // constructeur de la classe AllIn
    public AllIn() {
        this.tab = new int[3];
        this.difficulte = 0;
        this.mise = 0;
        this.gain = 0;
        this.gainsPositifs = 0;
        this.gainsNegatifs = 0;
        this.miseTotale = 0;
        this.description = "Règles de la machine All In : pour le niveau de difficulté 1, la mise minimale est de 100 jetons, pour le niveau de difficulté 2, la mise minimale est de 150, pour le niveau de difficulté 3, la mise minimale est de 250.";
        this.volatilite = 0;
    }

    public void lancer(Joueur joueur) {
        // crée tableau avec les chiffres et le symbole

        // à chaque fois qu'un tableau est créé, le joueur gagne la mise actuelle * 2
        // d'xp
        joueur.gagnerExperience(mise * 2);
        // nombres dans tableau
        this.tab[0] = nbAleatoire();
        this.tab[1] = nbAleatoire();
        this.isSmaller = (this.tab[0] < this.tab[1]) ? 0 : 1; // condition ternaire

        // symbole en fonction de la difficulté
        tab[2] = trouverSymbole();
    }

    public void jouer(Joueur joueur, Scanner sc) {
        // permet de commencer à jouer
        boolean rejouer = true;

        System.out.println(this.description);

        // permet de rejouer immédiatement
        while (rejouer) {
            // fixation de la mise, vérification de la mise et choix de la difficulté
            modifMise(joueur, sc);
            nivDifficulte(sc);
            rejouer = verifEntreeMise(joueur, sc);

            // permet au joueur d'arrêter de jouer avant d'avoir créé un tableau
            if (rejouer) {
                // remplit tableau de chiffres et symbole
                lancer(joueur);

                // affichage tableau
                afficherResultat();
                verifierResultat(joueur);

                rejouer = demandeRejouer(joueur, sc);
            }
        }
        // modifie dans joueur le nombre de jetons qu'il obtient après avoir joué
        joueur.modifSolde(mise);
    }

    public boolean demandeRejouer(Joueur joueur, Scanner sc) {
        // demande si l'utilisateur veut rejouer
        boolean rejouer = false, valide = false;
        int reponseUtilisateur;

        System.out.println("Voulez-vous rejouer ? (0 : non, 1 : oui)");
        // permet de répéter plusieurs fois le catch/try si besoin
        while (!valide) {
            try {
                reponseUtilisateur = sc.nextInt();
                // si le chiffre entré n'est pas un 1 ou un 0
                if (reponseUtilisateur != 1 && reponseUtilisateur != 0) {
                    System.out.println(
                            "Veuillez entrer 1 si vous voulez rejouer ou 0 si vous ne voulez pas rejouer.");
                    reponseUtilisateur = sc.nextInt();
                }
                // si le joueur veut rejouer
                if (reponseUtilisateur == 1) {
                    rejouer = true;
                    // si le joueur veut récupérer des jetons après un tour
                    modifJetons(joueur, sc);
                }
                valide = true;
                // si l'input n'est pas un chiffre, le catch est déclenché
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Une erreur s'est produite, veuillez 1 pour rejouer ou 0 pour ne pas rejouer.");
            }
        }
        return rejouer;
    }

    public void modifJetons(Joueur joueur, Scanner sc) {
        // permet au joueur de récupérer des jetons dans son solde depuis la mise
        // actuelle
        int reponse, jetonsRecuperes;
        boolean verifJetons = false, verifReponse = false;

        System.out.println("Voulez-vous récupérer des jetons ? (oui : 1, non : 0)");
        // permet de répéter plusieurs fois le catch/try si besoin
        while (!verifReponse) {
            try {
                reponse = sc.nextInt();
                // si le chiffre entré n'est pas un 1 ou un 2
                if (reponse != 1 && reponse != 2) {
                    System.out.println(
                            "Veuillez entrer 1 pour récupérer des jetons ou 0 si vous ne voulez pas en récupérer");
                    reponse = sc.nextInt();
                }
                // si le joueur veut récupérer des jetons
                if (reponse == 1) {
                    System.out.println("Combien de jetons voulez-vous récupérer ?");
                    // permet de répéter plusieurs fois le catch/try si besoin
                    while (!verifJetons) {
                        try {
                            // combien de jetons le joueur veut récupérer
                            jetonsRecuperes = sc.nextInt();
                            // si le nombre de jetons est négatif
                            if (jetonsRecuperes < 0) {
                                System.out.println("Veuillez entrer un nombre de jetons positifs.");
                                jetonsRecuperes = sc.nextInt();
                            }
                            // si le nombre de jetons est plus grand que la mise du joueur actuelle
                            if (jetonsRecuperes > this.mise) {
                                System.out.println(
                                        "Vous ne pouvez pas récupérer plus de jetons que votre mise actuelle !");
                                jetonsRecuperes = sc.nextInt();
                            }
                            // ajout au solde du joueur les jetons récupérés
                            joueur.modifSolde(jetonsRecuperes);
                            // on enlève à mise les jetons récupérés par le joueur
                            this.mise -= jetonsRecuperes;
                            verifJetons = true;
                            // si l'input n'est pas un chiffre, le catch est déclenché
                        } catch (InputMismatchException e) {
                            sc.nextLine();
                            System.out.println("Une erreur s'est produite, veuillez entrer un nombre de jetons.");
                        }
                    }
                }
                verifReponse = true;
                // si l'input n'est pas un chiffre, le catch est déclenché
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Une erreur s'est produite, veuillez entrer 1 pour oui ou 0 pour non.");
            }

        }
    }

    public void afficherResultat() {
        // Afficher le tableau avec les résultats
        char[] operateurs = { '-', '/', '+', '*' };
        System.out.println("[ " + this.tab[0] + " | " + this.tab[1] + " | " + operateurs[this.tab[2] + 1] + " ] ");
    }

    public void verifierResultat(Joueur joueur) {
        // Calculer les gains et les afficher

        // variables
        int nb;
        this.gain = 0;

        // calcul du gain pour pour symbole est + ou -
        if (this.tab[2] == 1 || this.tab[2] == -1) {

            // calcul de la somme des nombres dans le tableau
            nb = this.tab[0] * this.tab[1];
            this.gain = this.gain + tab[2] * nb;
            this.mise = this.mise + this.gain;
            if (tab[2] * nb >= 0) {
                this.gainsPositifs += this.gain;
            } else {
                this.gainsNegatifs += this.gain;
            }

        } else if (this.tab[2] == 2) { // calcul du gain pour symbole est *
            // on utilise le plus petit chiffre des deux obtenus dans le tableau
            nb = tab[this.isSmaller];
            this.gain = this.mise * nb;
            this.mise = this.mise + this.mise * nb;
            this.gainsPositifs += this.gain;

        } else if (this.tab[2] == 0) { // calcul du gain pour symbole est /
            // on utilise le plus grand chiffre des deux obtenus dans le tableau
            nb = tab[(isSmaller == 0) ? 1 : 0];
            this.gain = this.mise - (this.mise / nb);
            this.mise = this.mise / nb;
            this.gainsNegatifs += this.gain;
        }

        // affichage
        System.out.println(
                "Vous obtenez " + this.gain + " jetons. Votre mise est maintenant à " + this.mise + " jetons !");
    }

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

    public void nivDifficulte(Scanner sc) {
        // Retourne le choix de difficulté de l'utilisateur entre 1 et 3

        // variables
        boolean valide = false;
        System.out.println("Entrez la difficulté voulue (facile : 1, moyenne : 2, difficile : 3) : ");
        while (!valide) {
            try {
                this.difficulte = sc.nextInt();
                // si la valeur entrée dans difficulté n'est pas comprise entre 1 et 3
                while (this.difficulte != 1 && this.difficulte != 2 && this.difficulte != 3) {
                    System.out.println("Veuillez entrer un nombre entre 1 et 3 !");
                    this.difficulte = sc.nextInt();
                }
                valide = true;
                // si la valeur entrée dans difficulté n'est pas un chiffre
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Une erreur s'est produite, veuillez entrer un chiffre.");
            }
        }
        // s'affiche si le chiffre entré est valide
        System.out.println("Vous avez choisi la difficulté " + difficulte + ".");
    }

    private int nbAleatoire() {
        // Retourne un nombre aléatoire entre 1 et 9 en utilisant la fonction random
        Random rand = new Random();
        return rand.nextInt(10);
    }

    public void modifMise(Joueur joueur, Scanner sc) {
        // Modifie la mise actuelle
        int nouvMise;
        boolean valide = false;

        System.out.println(
                "Votre mise actuelle est de : " + this.mise + ". Veuillez entrer le montant que vous voulez ajouter.");
        // permet au catch/try de se déclencher autant de fois nécessaires
        while (!valide) {
            try {
                nouvMise = sc.nextInt();
                // si la mise ajoutée est négative
                if (nouvMise < 0) {
                    System.out.println("Veuillez entrer une mise positive.");
                    nouvMise = sc.nextInt();
                }
                // vérification que la mise ajoutée n'est pas supérieure au nombre de jetons du
                // joueur
                if (nouvMise <= joueur.getSolde()) {
                    // on enlève au solde du joueur les jetons qui sont misés
                    joueur.modifSolde(-nouvMise);
                    // on ajoute à la mise les jetons ajoutés
                    this.mise += nouvMise;
                    this.miseTotale += nouvMise;
                    valide = true;
                } else {
                    // si la mise entrée est supérieure au solde du joueur
                    System.out.println("La mise entrée dépasse le nombre de jetons restants ! Il vous reste "
                            + joueur.getSolde() + " jetons. Veuillez entrer un nouveau montant.");
                }
                // si l'input n'est pas un chiffre, le catch est déclenché
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Une erreur s'est produite, veuillez entrer un nombre.");
            }
        }
    }

    public boolean verifEntreeMise(Joueur joueur, Scanner sc) {
        // permet au joueur de choisir entre changer sa mise, changer de difficulté ou
        // arrêter de jouer quand la mise n'est pas suffisante pour la difficulté
        int exit = 0;
        int[] misePourChaqueDifficulte = { 100, 150, 250 };
        boolean valide = false, rejouer = true;

        // si la mise est trop petite pour la difficulté choisie
        if (this.mise < misePourChaqueDifficulte[this.difficulte - 1]) {
            System.out.println(
                    "La mise minimale pour la difficulté 1 est de 100 jetons. Voulez-vous changer votre mise (1), changer de difficulté (2) ou arrêter de jouer (3)?");
            // permet au catch/try de se déclencher autant de fois que besoin
            while (!valide) {
                try {
                    exit = sc.nextInt();
                    // si l'entrée n'est pas entre 1 et 3
                    if (exit != 1 && exit != 2 && exit != 3) {
                        System.out.println("Veuillez choisir un nombre entre 1 et 3 !");
                        exit = sc.nextInt();
                    }
                    valide = true;
                    // si l'input n'est pas un chiffre, l'exception se déclenche
                } catch (InputMismatchException e) {
                    sc.nextLine();
                    System.out.println("Une erreur s'est produite, veuillez entrer un nombre.");
                }
            }
        }
        // si le joueur veut changer sa mise
        if (exit == 1) {
            modifMise(joueur, sc);
            verifEntreeMise(joueur, sc);
        }
        // si le joueur veut changer sa difficulté
        if (exit == 2) {
            nivDifficulte(sc);
            verifEntreeMise(joueur, sc);
        }
        // si le joueur veut arrêter de jouer
        if (exit == 3) {
            rejouer = demandeRejouer(joueur, sc);
        }
        return rejouer;
    }

}