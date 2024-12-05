import java.util.Scanner;

public class Joueur {
    private String nom;
    private int niveau;
    private int prochainNiveau;
    private int barreExperience;
    private int solde;
    private MagicMoolah machine;
    // Permet de changer les couleurs (https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println)
    public static final String resetCouleur = "\u001B[0m";
    public static final String rouge = "\u001B[31m";
    public static final String bleu = "\u001B[34m";
    public static final String vert = "\u001B[32m";
    public static final String jaune = "\u001B[33m";
    public static final String violet = "\u001B[35m";
    public static final String cyan = "\u001B[36m";
    public static final String blanc = "\u001B[37m";

    // Constructeur
    public Joueur() {
        this.nom = "";
        this.niveau = 1;  // Niveau de départ
        this.prochainNiveau = 100;  // Expérience nécessaire pour passer au niveau suivant
        this.barreExperience = 0;  // Initialisation de l'expérience
        this.solde = 0;  // Solde initial
    }

    // Choisir la difficulté et initialiser le solde
    public void difficulte(Scanner sc) {
        int choix;
        System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
        System.out.println(bleu + "======Choix difficulté======\n" + resetCouleur);
        System.out.println("Bonjour " + nom + " choisissez votre difficulté !\n");
        System.out.println(vert + "1. Départ avec 100 Mille jetons." + resetCouleur);
        System.out.println(jaune + "2. Départ avec 10 mille jetons." + resetCouleur);
        System.out.println(rouge + "3. Départ avec mille jetons." + resetCouleur);
        choix = sc.nextInt();
        switch (choix) {
            case 1:
                this.solde = 100000;
                break;
            case 2:
                this.solde = 10000;
                break;
            case 3:
                this.solde = 1000;
                break;
            default:
                this.difficulte(sc);
        }
    }

    // Méthode pour modifier le solde
    public void modifSolde(int montant) {
        if (solde - montant >= 0) {
            this.solde += montant;  // On ajoute le montant au solde
        } else {
            System.out.println(rouge + "Solde insuffisant." + resetCouleur);
        }
    }

    // Méthode pour gagner de l'expérience
    public void gagnerExperience(int experienceGagnee) {
        this.barreExperience += experienceGagnee;  // On ajoute l'expérience gagnée
        // Vérifier si l'expérience du joueur atteint ou dépasse le seuil
        while (this.barreExperience >= this.prochainNiveau) {
            // Le joueur passe au niveau supérieur
            this.niveau++;
            this.barreExperience -= this.prochainNiveau;  // Réinitialise l'expérience en excédent
            this.prochainNiveau = (int) (this.prochainNiveau * 1.5);  // Augmenter le seuil pour le prochain niveau
            System.out.println(cyan + "Félicitations, vous êtes passé au niveau " + this.niveau + "!\n" + resetCouleur); // préviens de l'augmentation de niveau en bleu
        }
    }

    // Méthode pour afficher les informations du joueur
    public void afficherInfo(Scanner sc) {
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
            System.out.println(bleu + "======Infos " + this.nom + "===========" + resetCouleur);
            System.out.println(vert + "\nNom du joueur : " + nom);
            System.out.println(cyan +"Niveau : " + niveau);
            System.out.println("Expérience : " + barreExperience + "/" + prochainNiveau);
            System.out.println(jaune +"Solde : " + solde + " jetons" + resetCouleur);
            System.out.println(bleu + "\n==============================\n" + resetCouleur);
            System.out.println("Appuyez sur entrée pour revenir au menu.");
            // Permet de vider le scanner
            sc.nextLine();
            sc.nextLine();
    }

    // Méthode pour demander le nom du joueur
    public void initialisationJoueur(Scanner sc) {
        System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
        System.out.println(bleu + "=================Création du joueur=================" + resetCouleur);
        System.out.print("Entrez votre nom : ");
        this.nom = sc.nextLine();  // Lit l'entrée et l'assigne au nom du joueur
        difficulte(sc);  // Choisit la difficulté et initialise le solde
    }

    public int getSolde(){
        return solde;
    }
}
