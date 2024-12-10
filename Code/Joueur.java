import java.util.Scanner;
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

public class Joueur {
    private String nom;
    private int niveau;
    private int prochainNiveau;
    private int barreExperience;
    private int solde;

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
        System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire apparaître le terminal vide)
        System.out.println("-+-" + Affichage.violet + '-' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
        System.out.println(Affichage.bleu + "======Choix difficulté======\n" + Affichage.resetCouleur);
        System.out.println("Bonjour " + nom + " choisissez votre difficulté !\n");
        System.out.println(Affichage.vert + "1. Départ avec 100k jetons." + Affichage.resetCouleur);
        System.out.println(Affichage.jaune + "2. Départ avec 10k jetons." + Affichage.resetCouleur);
        System.out.println(Affichage.rouge + "3. Départ avec 1k jetons." + Affichage.resetCouleur);
        
        // Boucle pour s'assurer que l'utilisateur entre un entier valide
        while (true) {
            try {
                System.out.println("Votre choix : ");
                choix = sc.nextInt();
                if (choix < 1 || choix > 3) {
                    throw new IllegalArgumentException(Affichage.rouge + "Choix invalide, veuillez entrer 1, 2 ou 3." + Affichage.resetCouleur);
                }
                break; // Si l'entrée est correcte, on sort de la boucle
            } catch (InputMismatchException e) {
                // Si l'entrée n'est pas un entier
                System.out.println(Affichage.rouge + "Erreur: Veuillez entrer un nombre entier." + Affichage.resetCouleur);
                sc.nextLine(); // Consommer la ligne incorrecte
            } catch (IllegalArgumentException e) {
                // Si l'utilisateur choisit un nombre en dehors des options disponibles
                System.out.println(e.getMessage());
            }
        }
    
        // Attribution du solde en fonction du choix
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
        }
    }
    

    // Méthode pour modifier le solde
    public void modifSolde(int montant) {
        if (solde - montant >= 0) {
            this.solde += montant;  // On ajoute le montant au solde
        } else {
            System.out.println(Affichage.rouge + "Solde insuffisant." + Affichage.resetCouleur);
        }
    }
    
    // Médothe pour modifier le solde
    /*public void modifSolde(int montant) {
        // Montant est en entrée négatif quand il est retiré au solde
        if (solde + montant >= 0) {
            this.solde += montant;  // On ajoute le montant au solde
        } else {
            System.out.println(Affichage.rouge + "Solde insuffisant." + Affichage.resetCouleur);
        }
    }*/

    /*// Méthode pour modifier le solde
    public void modifSolde(int montant) {
        if (montant < 0 && solde + montant < 0) { // Vérification pour les pertes
            System.out.println("Solde insuffisant. Impossible de miser ou de retirer " + Math.abs(montant) + " jetons.");
        } else {
            this.solde += montant;
        }
    }*/

    // Méthode pour gagner de l'expérience
    public void gagnerExperience(int experienceGagnee) {
        this.barreExperience += experienceGagnee;  // On ajoute l'expérience gagnée
        // Vérifier si l'expérience du joueur atteint ou dépasse le seuil
        while (this.barreExperience >= this.prochainNiveau) {
            // Le joueur passe au niveau supérieur
            this.niveau++;
            this.barreExperience -= this.prochainNiveau;  // Réinitialise l'expérience en excédent
            this.prochainNiveau = (int) (this.prochainNiveau * 1.5);  // Augmenter le seuil pour le prochain niveau
            System.out.println(Affichage.cyan + "Félicitations, vous êtes passé au niveau " + this.niveau + "!\n" + Affichage.resetCouleur); // préviens de l'augmentation de niveau en bleu
        }
    }

    // Méthode pour afficher les informations du joueur
    public void afficherInfo(Scanner sc, Joueur joueur) {
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
            System.out.println("--+" + Affichage.violet + '-' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
            System.out.println(Affichage.bleu + "======Infos " + this.nom + "===========" + Affichage.resetCouleur);
            System.out.println(Affichage.vert + "\nNom du joueur : " + nom);
            System.out.println(Affichage.cyan +"Niveau : " + niveau);
            System.out.println("Expérience : " + barreExperience + "/" + prochainNiveau);
            System.out.println(Affichage.jaune +"Solde : " + solde + " jetons" + Affichage.resetCouleur);
            System.out.println(Affichage.bleu + "\n==============================\n" + Affichage.resetCouleur);
            System.out.println("Appuyez sur entrée pour revenir au menu.");
            // Permet de vider le scanner
            sc.nextLine();
    }

    // Méthode pour demander le nom du joueur
    public void initialisationJoueur(Scanner sc, Joueur joueur) {
        System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
        System.out.println("+--" + Affichage.violet + '-' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
        System.out.println(Affichage.bleu + "=================Création du joueur=================" + Affichage.resetCouleur);
        System.out.print("Entrez votre nom : ");
        this.nom = sc.nextLine();  // Lit l'entrée et l'assigne au nom du joueur
        difficulte(sc);  // Choisit la difficulté et initialise le solde
    }
    //pour accéder au nom de joueur dans les autres class
    public String getNom(){
        return this.nom;

    }
    //pour accéder au solde du joueur 
    public int getSolde(){
        return solde;
    }
    //pour accéder a l'expérience du joueur 
    public int getBarreExp(){
        return this.barreExperience;
    }
    //pour accéder au niveau du joueur 
    public int getNiveau(){
        return this.niveau;
    }
    //pour accéder au niveau
    public int ajouterNiveau(){
       return this.niveau++;
    }
}
