import java.util.Scanner;
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

/**
 * Classe représentant un joueur dans un jeu.
 * Cette classe gère les informations du joueur telles que son nom, son niveau, son expérience, son solde et les interactions avec ces informations.
 */
public class Joueur {
    private String nom;
    private int niveau;
    private int prochainNiveau;
    private int barreExperience;
    private int solde;

    /**
     * Constructeur de la classe Joueur.
     * Initialise les attributs du joueur avec des valeurs par défaut avec le nom, le niveau, l'experience nexcessaire pour passer au prochain niveau,
     * la barre d'experience et le solde.
     */
    public Joueur() {
        this.nom = "";
        this.niveau = 1;  // Niveau de départ
        this.prochainNiveau = 100;  // Expérience nécessaire pour passer au niveau suivant
        this.barreExperience = 0;  // Initialisation de l'expérience
        this.solde = 0;  // Solde initial
    }

    /**
     * Permet au joueur de choisir la difficulté du jeu et d'initialiser son solde en fonction du choix.
     * Affiche les options de difficulté et gère les erreurs d'entrée.
     *
     * @param sc Le scanner utilisé pour lire l'entrée de l'utilisateur.
     */
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
    

    /**
     * Modifie le solde du joueur.
     * Si le montant est positif, il est ajouté au solde. Si le montant est négatif, il est retiré du solde,
     * mais seulement si le solde est suffisant pour couvrir le retrait.
     *
     * @param montant Le montant à ajouter ou retirer au solde du joueur.
     */
    public void modifSolde(int montant) {
        if (montant < 0 && solde + montant < 0) { // Vérification pour les pertes
            System.out.println("Solde insuffisant. Impossible de miser ou de retirer " + Math.abs(montant) + " jetons.");
        } else {
            this.solde += montant;
        }
    }


    /**
     * Permet au joueur de gagner de l'expérience.
     * Si l'expérience du joueur dépasse le seuil nécessaire pour passer au niveau supérieur, il passe au niveau suivant.
     * Le seuil d'expérience pour le prochain niveau est augmenté de 50% à chaque niveau supérieur.
     *
     * @param experienceGagnee L'expérience gagnée par le joueur.
     */
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

    /**
     * Affiche les informations du joueur, y compris son nom, son niveau, son expérience et son solde.
     *
     * @param sc Le scanner utilisé pour lire l'entrée de l'utilisateur.
     * @param joueur L'instance du joueur dont les informations doivent être affichées.
     */
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

    /**
     * Demande au joueur de saisir son nom et initialise sa difficulté ainsi que son solde.
     * Cette méthode est appelée lors de la création du joueur.
     *
     * @param sc Le scanner utilisé pour lire l'entrée de l'utilisateur.
     * @param joueur L'instance du joueur à initialiser.
     */
    public void initialisationJoueur(Scanner sc, Joueur joueur) {
        System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisament de lignes pour faire appaitre le terminal vide)
        System.out.println("+--" + Affichage.violet + '-' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
        System.out.println(Affichage.bleu + "=================Création du joueur=================" + Affichage.resetCouleur);
        System.out.print("Entrez votre nom : ");
        this.nom = sc.nextLine();  // Lit l'entrée et l'assigne au nom du joueur
        difficulte(sc);  // Choisit la difficulté et initialise le solde
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return Le nom du joueur.
     */
    public String getNom(){
        return this.nom;

    }

    /**
     * Retourne le solde du joueur.
     *
     * @return Le solde actuel du joueur.
     */
    public int getSolde(){
        return solde;
    }

    /**
     * Retourne l'expérience actuelle du joueur.
     *
     * @return L'expérience actuelle du joueur.
     */
    public int getBarreExp(){
        return this.barreExperience;
    }

    /**
     * Retourne le niveau actuel du joueur.
     *
     * @return Le niveau actuel du joueur.
     */
    public int getNiveau(){
        return this.niveau;
    }

    /**
     * Incrémente le niveau du joueur de 1 et retourne le nouveau niveau.
     *
     * @return Le niveau du joueur après l'incrémentation.
     */
    public int ajouterNiveau(){
       return this.niveau++;
    }
}
