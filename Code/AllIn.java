import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class AllIn implements Machine {
    private int[] tab = new int[3];
    private int isSmaller; // stocke index du chiffre le plus petit dans le tableau
    // private String nom; // pas besoin ??
    private int difficulte; // La difficulté choisie par le joueur
    private int mise;
    private int gain;
    private String description;
    private int volatilite;

    // constructeur
    public AllIn() {
        this.tab = new int[3];
        this.difficulte = 0;
        this.mise = 0;
        this.gain = 0;
        this.description = "Règles de la machine All In : pour le niveau de difficulté 1, la mise minimale est de 100 jetons, pour le niveau de difficulté 2, la mise minimale est de 150, pour le niveau de difficulté 3, la mise minimale est de 250.";
        this.volatilite = 0;
    }

    public void lancer() {
        // lance une partie en appelant les autres méthodes de la classe
        boolean test = false, rejouer = true;
        Scanner console = new Scanner(System.in);

        System.out.println(this.description);

        // permet de rejouer immédiatement
        while (rejouer) {
            // fixation de la mise, vérification de la mise et choix de la difficulté
            modifMise();
            nivDifficulte();
            while (test == false) {
                test = verifMise();
            }

            // nombres dans tableau
            this.tab[0] = nbAleatoire();
            this.tab[1] = nbAleatoire();
            this.isSmaller = (this.tab[0] < this.tab[1]) ? 0 : 1; // condition ternaire

            // symbole en fonction de la difficulté
            tab[2] = trouverSymbole();

            // affichage tableau
            afficherResultat();
            verifierResultat();

            System.out.println("Voulez-vous rejouer ? (true = oui, false = non)");
            rejouer = console.nextBoolean();
        }
    }

    public void afficherResultat() {
        // Afficher le tableau avec les résultats
        char[] operateurs = { '-', '/', '+', '*' };
        System.out.println("[ " + this.tab[0] + " | " + this.tab[1] + " | " + operateurs[this.tab[2] + 1] + " ] ");
    }

    public void verifierResultat() {
        // Calculer les gains et les afficher

        // variables
        int nb;

        // calcul du gain pour pour symbole est + ou -
        if (this.tab[2] == 1 || this.tab[2] == -1) {

            // calcul de la somme des nombres dans le tableau
            nb = this.tab[0] * this.tab[1];
            this.gain = this.gain + tab[2] * nb;
            this.mise = this.mise + this.gain;

        } else if (this.tab[2] == 2) { // calcul du gain pour symbole est *
            // on utilise le plus petit chiffre des deux obtenus dans le tableau
            nb = tab[this.isSmaller];
            this.gain = this.mise * nb;
            this.mise = this.mise + this.mise * nb;

        } else if (this.tab[2] == 0) { // calcul du gain pour symbole est /
            // on utilise le plus grand chiffre des deux obtenus dans le tableau
            nb = tab[(isSmaller == 0) ? 1 : 0];
            this.gain = this.mise - (this.mise / nb);
            this.mise = this.mise / nb;
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

    public void nivDifficulte() {
        // Retourne le choix de difficulté de l'utilisateur entre 1 et 3

        // variables
        Scanner console = new Scanner(System.in);

        System.out.println("Entrez la difficulté voulue (facile : 1, moyenne : 2, difficile : 3) : ");
        this.difficulte = console.nextInt();
        System.out.println("Vous avez choisi la difficulté " + difficulte + ".");
    }

    private int nbAleatoire() {
        // Retourne un nombre aléatoire entre 1 et 9 en utilisant la fonction random
        Random rand = new Random();
        return rand.nextInt(10);
    }

    public void modifMise() {
        // Modifie la mise actuelle
        Scanner console = new Scanner(System.in);

        System.out.println(
                "Votre mise actuelle est de : " + this.mise + ". Veuillez entrer le montant que vous voulez ajouter.");
        this.mise = this.mise + console.nextInt();
    }

    public boolean verifMise() {

        Scanner console = new Scanner(System.in);
        int exit;
        int[] tab = { 100, 150, 250 };
        // vérifie que la mise minimale est respectée pour chaque niveau de difficulté
        if (this.mise < tab[this.difficulte - 1]) {
            System.out.println(
                    "La mise minimale pour la difficulté 1 est de 100 jetons. Voulez-vous changer votre mise (1) ou sortir de cette difficulté (2) ?");
            exit = console.nextInt();
            if (exit == 1) {
                modifMise();
            } else {
                nivDifficulte();
            }
            return false;
        } else {
            return true;
        }
    }

}