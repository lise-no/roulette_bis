import java.util.ArrayList;  // pour les listes
import java.util.Random;     // pour générer des nombres aléatoires
import java.util.Scanner;    // pour lire les entrées clavier 
import java.lang.IllegalArgumentException;//utilisé pour capturer les exceptions liées à des arguments invalides passés à une méthode.

/**
 * Classe représentant une machine de type roulette.
 */
public class Roulette implements Machine {
    private int niveauMin;//Niveau minimum requis pour jouer à la roulette.
    private final ArrayList<Integer> rouges;//Liste des numéros rouges sur la roulette.
    private final ArrayList<Integer> noirs;//Liste des numéros noirs sur la roulette
    private int totalMise;//Total des mises effectuées au cours de la session de jeu
    private int nombreParties;//Nombre de parties jouées pendant la session.
    private ArrayList<String> resultats;//Liste des résultats de chaque partie jouée.

    /**
     * Constructeur par défaut de la classe Roulette.
     * Initialise les paramètres et les listes de numéros.
     */
    public Roulette() {
        this.niveauMin = 5;
        this.rouges = initialiserRouges();
        this.noirs = initialiserNoirs();
        this.totalMise = 0;
        this.nombreParties = 0;
        this.resultats = new ArrayList<>();
    }

    /**
     * Initialise et retourne la liste des numéros rouges.
     * 
     * @return une liste contenant les numéros rouges.
     */
    private ArrayList<Integer> initialiserRouges() {
        ArrayList<Integer> rouges = new ArrayList<>();
        int[] rougesArr = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int num : rougesArr) {
            rouges.add(num);
        }
        return rouges;
    }

    /**
     * Initialise et retourne la liste des numéros noirs.
     * 
     * @return une liste contenant les numéros noirs.
     */
    private ArrayList<Integer> initialiserNoirs() {
        ArrayList<Integer> noirs = new ArrayList<>();
        int[] noirsArr = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        for (int num : noirsArr) {
            noirs.add(num);
        }
        return noirs;
    }

    /**
     * Lancer la roulette et afficher les informations utiles pour l'utilisateur.
     * 
     * @param joueur le joueur utilisant la machine.
     */
    @Override
    public void lancer(Joueur joueur) {
        System.out.println(Affichage.bleu + "Bienvenue dans la Machine Roulette, " + joueur.getNom() + " !" + Affichage.resetCouleur);
        System.out.println(Affichage.vert + "Informations sur la roulette:" + Affichage.resetCouleur);
        System.out.println("- Cette machine est déblocable avec un niveau minimum.");
        System.out.println("- Votre niveau augmente de 1 après chaque session de jeu.");
        System.out.println("- Elle fonctionne avec différents paris, et en fonction du pari, vous obtiendrez un gain en fonction de votre mise.");
        System.out.println("- Votre expérience augmente en fonction de votre mise et si vous avez gagné.");
    }

    /**
     * Simule le tournage de la roulette et retourne le numéro final obtenu.
     * 
     * @return le numéro obtenu après le tournage de la roulette.
     */
    public int tournerRoulette() {
        System.out.println("\nLa roulette tourne...");
        Random random = new Random();
        int dernierResultat = random.nextInt(37); // Générer le résultat final à l'avance

        for (int i = 0; i < 20; i++) {
            int numeroTemp = random.nextInt(37);
            if (i == 19) {
                numeroTemp = dernierResultat;
            }
            System.out.print("\r" + Affichage.rouge + numeroTemp + Affichage.resetCouleur);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return dernierResultat;
    }

    /**
     * Gère une session de jeu à la roulette.
     * 
     * @param joueur le joueur jouant à la roulette.
     * @param scanner un objet Scanner pour lire les entrées utilisateur.
     */
    public void jouer(Joueur joueur, Scanner scanner) {
        lancer(joueur);
        boolean rejouer = true;

        while (rejouer && nombreParties < 10) {
            if (!peutJouer(joueur)) {
                System.out.print("\033[H\033[2J"); // Clear le terminal
                System.out.println(Affichage.rouge + "Votre niveau est insuffisant pour jouer à cette machine." + Affichage.resetCouleur);
            // Pause de deux secondes pour qu'on voit le message
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
                return;
            }
            System.out.println(Affichage.blanc + "Vous commencez avec " + joueur.getSolde() + " jetons." + Affichage.resetCouleur);

            try {
                effectuerPari(scanner, joueur);
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                scanner.nextLine();
            }

            if (joueur.getSolde() <= 0) {
                System.out.println(Affichage.rouge + "Votre solde est insuffisant pour jouer." + Affichage.resetCouleur);
                return;
            }

            System.out.println("Souhaitez-vous rejouer ? (oui/non) : ");
            String reponse = scanner.next().toLowerCase();
            if (!reponse.equals("oui")) {
                rejouer = false;
                break;
            }
        }

        if (joueur.getBarreExp() >= 100) {
            niveauMin += 2;
            System.out.println(Affichage.rouge + "La difficulté a augmenté ! Le niveau minimal requis est maintenant de " + niveauMin + Affichage.resetCouleur + ".");
        }

        joueur.ajouterNiveau();
        verifierResultat(joueur);
        afficherResultat();
    }

    /**
     * Vérifie si le joueur peut jouer en fonction de son niveau.
     * 
     * @param joueur le joueur concerné.
     * @return true si le joueur peut jouer, false sinon.
     */
    boolean peutJouer(Joueur joueur) {
        return joueur.getNiveau() >= niveauMin;
    }

    /**
     * Effectue un pari basé sur les choix du joueur.
     * 
     * @param scanner un objet Scanner pour lire les entrées utilisateur.
     * @param joueur le joueur effectuant le pari.
     */
    private void effectuerPari(Scanner scanner, Joueur joueur) {
        int mise = demanderMise(scanner, joueur);
        int choix = demanderChoixPari(scanner);

        totalMise += mise;
        nombreParties++;

        System.out.println(Affichage.cyan + "Vous avez choisi le pari : " + choix + "." + Affichage.resetCouleur);

        switch (choix) {
            case 1 :
                parierNumero(joueur, mise, scanner);
            case 2 :
                parierCouleur(joueur, mise, scanner);
            case 3 : 
                parierZero(joueur, mise);
            case 4 : 
                parierNumeroEtCouleur(joueur, mise, scanner);
            default :
                throw new IllegalArgumentException("Option de pari invalide.");
        }
    }

    /**
     * Demande au joueur de saisir une mise et la valide.
     * 
     * @param scanner un objet Scanner pour lire les entrées utilisateur.
     * @param joueur le joueur effectuant la mise.
     * @return le montant de la mise validée.
     */
    //retourne la mise pour simplifier la méthode effectuerPari
    private int demanderMise(Scanner scanner, Joueur joueur) {
        int mise = -1;
        System.out.println(Affichage.violet + "Combien de jetons souhaitez-vous parier ? (vous avez " 
                           + joueur.getSolde() + " jetons) : " + Affichage.resetCouleur);
    
        while (mise < 1 || mise > joueur.getSolde()) {
            try {
                mise = Integer.parseInt(scanner.next());
                if (mise < 1 || mise > joueur.getSolde()) {
                    System.out.println(Affichage.rouge + "Mise invalide. Vous devez parier entre 1 et " 
                                       + joueur.getSolde() + " jetons." + Affichage.resetCouleur);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un nombre entier." 
                                   + Affichage.resetCouleur);
            }
        }
        return mise;
    }

    /**
     * Demande et retourne le choix de pari pour rendre la méthode effectuerPari plus lisible.
     * 
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return Le choix du pari (évalué entre 1 et 4).
     */
    private int demanderChoixPari(Scanner scanner) {
        int choix = -1;
        System.out.println("Choisissez votre type de pari :");
        System.out.println("1. Parier sur un numéro (1 à 36)");
        System.out.println("2. Parier sur une couleur (rouge ou noir)");
        System.out.println("3. Parier sur le numéro zéro (couleur verte)");
        System.out.println("4. Parier sur un numéro et une couleur");
        System.out.println("Entrez votre choix (1-4) : ");
    
        while (choix < 1 || choix > 4) {
            try {
                choix = Integer.parseInt(scanner.next());
                if (choix < 1 || choix > 4) {
                    System.out.println(Affichage.rouge + "Choix invalide. Vous devez entrer un nombre entre 1 et 4." 
                                       + Affichage.resetCouleur);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un nombre entier entre 1 et 4." 
                                   + Affichage.resetCouleur);
            }
        }
        return choix;
    }
     

     /**
     * Vérifie et retourne un numéro valide choisi par l'utilisateur (entre 1 et 36).
    * 
    * @param scanner Scanner pour lire l'entrée utilisateur.
    * @return Un numéro valide choisi par l'utilisateur.
    */
    private int numeroValide(Scanner scanner) {
        int numeroPari = -1;
        while (numeroPari < 1 || numeroPari > 36) {
            try {
                System.out.print("Choisissez un numéro (1 à 36) : ");
                numeroPari = Integer.parseInt(scanner.next());
                if (numeroPari < 1 || numeroPari > 36){
                    System.out.println(Affichage.rouge +"Entrée invalide. Vous devez entrer un numéro entre 1 et 36" + Affichage.resetCouleur);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un nombre entier." + Affichage.resetCouleur);
            }
        }
        return numeroPari;
    }
     /**
     * Vérifie que la couleur choisie par l'utilisateur est valide (« rouge » ou « noir ») et la retourne.
     * @param scanner Scanner pour lire l'entrée utilisateur.
     * @return La couleur choisie (« rouge » ou « noir »).
     */
    private String couleurValide(Scanner scanner) {
        String couleurPariee = "";
        while (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
            try {
                System.out.print("Choisissez une couleur (rouge ou noir) : ");
                couleurPariee = scanner.next().toLowerCase();
                if (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")){
                    System.out.println(Affichage.rouge + "Couleur invalide. Veuillez entrer 'rouge' ou 'noir'." + Affichage.resetCouleur);
                  }
            } catch (IllegalArgumentException e) {
                System.out.println(Affichage.rouge + "Couleur invalide. Veuillez entrer 'rouge' ou 'noir'." + Affichage.resetCouleur);
            }
        }
        return couleurPariee;
    }
     
    /**
      * Traite le résultat d'un pari en fonction de la condition de victoire. 
      * @param conditionVictoire Condition à vérifier pour déterminer si le joueur a gagné.
      * @param joueur           Le joueur effectuant le pari.
      * @param mise             La mise du joueur.
      * @param multiplicateur   Multiplicateur appliqué à la mise en cas de victoire.
      * @param messageGagne     Message ajouté aux résultats en cas de victoire.
      * @param messagePerdu     Message ajouté aux résultats en cas de défaite.
      */
    private void traiterResultat(boolean conditionVictoire, Joueur joueur, int mise, int multiplicateur, String messageGagne, String messagePerdu) {
        if (conditionVictoire) {
            System.out.println(Affichage.vert + "Félicitations, vous avez gagné !" + Affichage.resetCouleur);
            joueur.modifSolde(mise * multiplicateur);
            resultats.add(messageGagne);
        } else {
            System.out.println(Affichage.jaune + "Dommage, vous avez perdu." + Affichage.resetCouleur);
            joueur.modifSolde(-mise);
            resultats.add(messagePerdu);
        }
    }
    

    /**
     * Permet au joueur de parier sur un numéro. 
     * @param joueur  Le joueur effectuant le pari.
     * @param mise    La mise du joueur.
     * @param scanner Scanner pour lire l'entrée utilisateur.
     */
    private void parierNumero(Joueur joueur, int mise, Scanner scanner) {
    int numeroPari= numeroValide(scanner);

        System.out.println("Vous avez parié sur le numéro " + numeroPari + ".");
        int resultat = tournerRoulette();
        System.out.println("La roulette a tourné... le numéro est :" + resultat + ".");
        boolean conditionVictoire= (numeroPari== resultat);
        String messageGagne = "Gagné : Numéro parié " + numeroPari;
        String messagePerdu = "Perdu : Numéro parié" + numeroPari;
       traiterResultat(conditionVictoire, joueur, mise, 30, messageGagne, messagePerdu); 
    }
     
    /**
     * Permet au joueur de parier sur une couleur (« rouge » ou « noir »). 
     * @param joueur  Le joueur effectuant le pari.
     * @param mise    La mise du joueur.
     * @param scanner Scanner pour lire l'entrée utilisateur.
     */
    private void parierCouleur(Joueur joueur, int mise, Scanner scanner) {
        String couleurPariee = couleurValide(scanner);

        System.out.println("Vous avez parié sur la couleur " + couleurPariee + ".");
        int resultat = tournerRoulette();
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : noirs.contains(resultat)? "noir": "vert");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");
        Boolean conditionVictoire = couleurPariee.equals(couleurResultat);
        String messageGagne = "Gagné : Couleur pariée  "+ couleurPariee;
        String messagePerdu = "Perdu : Couleur pariée " + couleurPariee;

        traiterResultat(conditionVictoire, joueur, mise, 20, messageGagne, messagePerdu);   
    }

    /**
     * Permet au joueur de parier sur le numéro zéro.
     * 
    * @param joueur Le joueur effectuant le pari.
     * @param mise   La mise du joueur.
      */
    private void parierZero(Joueur joueur, int mise) {
        int resultat = tournerRoulette();
        System.out.println("La roulette a tourné... le numéro est : " + resultat);
        boolean conditionVictoire = (resultat==0);
        String messageGagne = "Gagné : Numéro 0";
        String messagePerdu =  "Perdu : Numéro parié 0";
        traiterResultat(conditionVictoire, joueur, mise, 40, messageGagne, messagePerdu);
    }

    /**
     * Permet au joueur de parier sur un numéro et une couleur simultanément.
      * 
      * @param joueur  Le joueur effectuant le pari.
      * @param mise    La mise du joueur.
     * @param scanner Scanner pour lire l'entrée utilisateur.
    */
    private void parierNumeroEtCouleur(Joueur joueur, int mise, Scanner scanner) {
        int numeroPari = numeroValide(scanner);
        String couleurPariee = couleurValide(scanner);
        
        int resultat = tournerRoulette();
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

        boolean conditionVictoire =(numeroPari == resultat && couleurPariee.equals(couleurResultat));
        String messageGagne = "Gagné : Numéro parié " + numeroPari + " et Couleur pariée " + couleurPariee;
        String messagePerdu = "Perdu : Numéro parié " + numeroPari + " et Couleur pariée " + couleurPariee;
        
        traiterResultat(conditionVictoire, joueur, mise, 50, messageGagne, messagePerdu);
    }
     
     /**
     * Affiche un récapitulatif des résultats de toutes les parties jouées.
    */
    @Override
    public void afficherResultat() {
        System.out.println(Affichage.cyan +"\nRésultats des parties :"+ Affichage.resetCouleur);
        for (int i = 0; i < resultats.size(); i++) {
            System.out.println(Affichage.violet +"Partie " + (i+1) + ": " + resultats.get(i) + Affichage.resetCouleur);
        }

        System.out.println(Affichage.cyan + "\nRésumé de la session de jeu :" + Affichage.resetCouleur);
        System.out.println(Affichage.cyan +"Total des mises : " + totalMise + Affichage.resetCouleur);
        System.out.println(Affichage.cyan +"Nombre de parties jouées : " + nombreParties + Affichage.resetCouleur);
        
    }
    
    /**
    * Vérifie les résultats de la session pour ajuster l'expérience du joueur en fonction de ses performances.
     * 
     * @param joueur Le joueur pour lequel les résultats doivent être vérifiés.
     */
    @Override
    public void verifierResultat(Joueur joueur) {
    boolean aGagne = false;
        for (String resultat : resultats) {
            if (resultat.contains("Gagné")) {
                aGagne = true;
            break; // Pas besoin de continuer une fois qu'on sait qu'il a gagné
            }
        }
         // Augmenter l'expérience en fonction des gains
        if (aGagne) {
            System.out.println(Affichage.vert + "Félicitations, vous avez remporté au moins une partie ! Vous gagnez de l'expérience supplémentaire." + Affichage.resetCouleur);
            joueur.gagnerExperience(10); // Expérience pour avoir gagné au moins une partie
        }
        else {
           System.out.println(Affichage.rouge + "Vous n'avez remporté aucune partie cette fois. Essayez encore !" + Affichage.resetCouleur);
        }

        // Bonus expérience pour avoir misé gros
        if (totalMise >= 100) {
            System.out.println(Affichage.vert + "Vous avez misé un total élevé de " + totalMise + " jetons ! Vous gagnez un bonus d'expérience." + Affichage.resetCouleur);
            joueur.gagnerExperience(10);
        }
    }
}