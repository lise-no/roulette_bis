import java.util.ArrayList;  // pour les listes
import java.util.InputMismatchException;// pour etre capable d'utiliser les exceptions
import java.util.Random;     // pour générer des nombres aléatoires
import java.util.Scanner;    // pour lire les entrées clavier 


public class Roulette implements Machine {
        private int niveauMin; // niveau minimum pour jouer
        private final ArrayList<Integer> rouges; // Liste des numéros rouges
        private final ArrayList<Integer> noirs;  // Liste des numéros noirs
        private int totalMise; // Total des mises pour chaque session de jeu
        private int nombreParties; // Nombre de parties jouées
        private ArrayList<String> resultats; // Liste pour stocker les résultats de chaque partie
        //private int dernierResultat;
        public Roulette() {
            this.niveauMin = 5;
            this.rouges = initialiserRouges();
            this.noirs = initialiserNoirs();
            this.totalMise = 0;
            this.nombreParties = 0;
            this.resultats = new ArrayList<>(); // Initialisation de la liste des résultats
        }

        // Initialiser les numéros rouges
    private ArrayList<Integer> initialiserRouges() {
        ArrayList<Integer> rouges = new ArrayList<>();
        int[] rougesArr = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int num : rougesArr) {
            rouges.add(num);
        }
        return rouges;
    }

    // Initialiser les numéros noirs
    private ArrayList<Integer> initialiserNoirs() {
        ArrayList<Integer> noirs = new ArrayList<>();
        int[] noirsArr = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        for (int num : noirsArr) {
            noirs.add(num);
        }
        return noirs;
    }

    /**  Lancer la roulette, afficher les informations
     * a savoir sur la roulette, pour l'utilisateur
    **/
    @Override
    public void lancer(Joueur joueur) {
        //affichage des informations sur la roulette
    System.out.println(Affichage.bleu + "Bienvenue dans la Machine Roulette, " + joueur.getNom() + " !" + Affichage.resetCouleur);
    System.out.println(Affichage.vert + "Informations sur la roulette:" + Affichage.resetCouleur);
    System.out.println("- Cette machine est déblocable avec un niveau minimum.");
    System.out.println("votre niveau augmente de 1 après chaque session de jeu");
    System.out.println("- Elle fonctionne avec différents paris, et en fonction du pari, vous obtiendrez un gain en fonction de votre mise.");
    System.out.println("- Votre expérience augmente en fonction de votre mise et si vous avez gagné.");
    }

    //méthode pour simuler une roulette avec des chifrres
    public int tournerRoulette() {
        // Simulation de la roulette avec des numéros défilants
        System.out.println("\nLa roulette tourne...");
        Random random = new Random();
        int dernierResultat = random.nextInt(37); // Générer le résultat final à l'avance
        
        // Animation de la roulette
        for (int i = 0; i < 20; i++) {
            int numeroTemp = random.nextInt(37); // Numéro aléatoire entre 0 et 36
            if (i == 19) { 
                // Pour la dernière itération, afficher le résultat final
                numeroTemp = dernierResultat;
            }
            System.out.print("\r" + Affichage.rouge + numeroTemp + Affichage.resetCouleur);
            try {
                Thread.sleep(100); // Pause de 100ms pour l'animation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Réinitialiser l'état d'interruption
            }
        }
        return dernierResultat;
    }
    
    /**
     *Méthode principale pour jouer à la roulette 
     *@param scanner pour lire l'entréé utilisateur
     *@param joueur 
     */ 
    public void jouer(Joueur joueur, Scanner scanner){
        //on affiche d'abord les informations de la roulette avant tout
        lancer(joueur);
        boolean rejouer = true;
        // Boucle de jeu
        while (rejouer && nombreParties < 10) {
        // Vérifier si le joueur peut jouer s'il a le niveau nécéssaire pour jouer
             if (!peutJouer(joueur)) {
                System.out.println(Affichage.rouge +"Votre niveau est insuffisant pour jouer à cette machine."+ Affichage.resetCouleur);
                return; //sortie immédiate de la boucle et de jouer.
           }
           System.out.println(Affichage.blanc + "Vous commencez avec " + joueur.getSolde() + " jetons." + Affichage.resetCouleur);

            try {
                effectuerPari(scanner,joueur);
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
                scanner.nextLine(); // Vider le buffer
            }
           // Vérifier si le joueur a encore des jetons pour continuer
            if (joueur.getSolde() <= 0) {
                System.out.println(Affichage.rouge + "Votre solde est insuffisant pour jouer." + Affichage.resetCouleur);
             return;
            }
            // Demander à l'utilisateur s'il veut rejouer
            System.out.println("Souhaitez-vous rejouer ? (oui/non) : ");
            String reponse = scanner.next().toLowerCase();
            if (!reponse.equals("oui")) {
                rejouer = false;
            break;
             }
        }
        //le niveau minimum nécéssaire pour jouer augmente de 2 lorsque la barre d'expérience est supérieur a 10
        if (joueur.getBarreExp()>=100) { 
            niveauMin += 2;
            System.out.println(Affichage.rouge +"La difficulté a augmenté ! Le niveau minimale requise est maintenant de " + niveauMin + Affichage.resetCouleur +".");
        }
        
        //le niveau du joueur augmente aprés chaque session
        joueur.ajouterNiveau();
        // Vérifier les résultats et augmenter l'expérience si nécessaire
        verifierResultat(joueur);
        // Afficher les résultats finaux de la session de jeu
        afficherResultat();
    }

    // Vérifie si le joueur a l'expérience nécessaire pour jouer
    boolean peutJouer(Joueur joueur) {
        return joueur.getNiveau() >= niveauMin;
    }
    /*éffectuer les différents paris selon son choix du joueur
    avec un scanner pour lire l'entrée du joueur
    diviser en sous méthode pour faciliter le lisibilité 
      */
    private void effectuerPari(Scanner scanner, Joueur joueur) {
        int mise = demanderMise(scanner, joueur); // Méthode pour demander et valider la mise
        int choix = demanderChoixPari(scanner);  // Méthode pour demander et valider le choix de pari
    
        totalMise += mise; // Ajouter la mise au total
        nombreParties++; // Incrémenter le nombre de parties
    
        System.out.println(Affichage.cyan + "Vous avez choisi le pari : " + choix + "." + Affichage.resetCouleur);
    
        // Traiter le choix
        switch (choix) {
            case 1 -> parierNumero(joueur, mise, scanner);
            case 2 -> parierCouleur(joueur, mise, scanner);
            case 3 -> parierZero(joueur, mise);
            case 4 -> parierNumeroEtCouleur(joueur, mise, scanner);
            default -> throw new IllegalArgumentException("Option de pari invalide."); // Sécurité supplémentaire
        }
    }
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
            } catch (NumberFormatException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un nombre entier." 
                                   + Affichage.resetCouleur);
            }
        }
        return mise;
    }
    //demander et retourne le choix pour rendre effectuerPari plus lisible
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
            } catch (NumberFormatException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un nombre entier entre 1 et 4." 
                                   + Affichage.resetCouleur);
            }
        }
        return choix;
    }
     

    /*vérifie et retourne l'entré utilisateur
     * verifie si l'entrée est bien un entier 
     * limite la repétivité
    */
    private int numeroValide(Scanner scanner) {
        int numeroPari = -1;
        while (numeroPari < 1 || numeroPari > 36) {
            try {
                System.out.print("Choisissez un numéro (1 à 36) : ");
                numeroPari = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println(Affichage.rouge + "Entrée invalide. Vous devez entrer un numéro entre 1 et 36." + Affichage.resetCouleur);
            }
        }
        return numeroPari;
    }
    /*vérifie que l'entréé utilisateur est valide
    *limite la repétivité
    * vérifie si la couleur entréé est rouge ou noir
    */
    private String couleurValide(Scanner scanner) {
        String couleurPariee = "";
        while (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
            try {
                System.out.print("Choisissez une couleur (rouge ou noir) : ");
                couleurPariee = scanner.next().toLowerCase();
            } catch (Exception e) {
                System.out.println(Affichage.rouge + "Couleur invalide. Veuillez entrer 'rouge' ou 'noir'." + Affichage.resetCouleur);
            }
        }
        return couleurPariee;
    }
     
    /**méthode qui traite le résultat pour éviter la redondance et la repétivité
     * compare les différents résultat en fonction du pari
     * @param conditionVictoire; condition a vérifier pour gagner. 
       variable selon le type de pari
       @param multiplicateur; le nombre auquel la mise est multiplié pour donner le gain
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
    

    // Méthode pour parier sur un numéro
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
     
    //parié uniquement sur une couleur
    private void parierCouleur(Joueur joueur, int mise, Scanner scanner) throws InputMismatchException {
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

    //parier sur le numéro zéro
    private void parierZero(Joueur joueur, int mise) {
        int resultat = tournerRoulette();
        System.out.println("La roulette a tourné... le numéro est : " + resultat);
        boolean conditionVictoire = (resultat==0);
        String messageGagne = "Gagné : Numéro 0";
        String messagePerdu =  "Perdu : Numéro parié 0";
        traiterResultat(conditionVictoire, joueur, mise, 40, messageGagne, messagePerdu);
    }

    // parier sur un numéro et une couleur en même temps
    private void parierNumeroEtCouleur(Joueur joueur, int mise, Scanner scanner)throws InputMismatchException {
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
     
    /**Méthode pour afficher les résultats finaux
     * affiche un récap de la session de jeux 
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
    
    /** Méthode pour vérifier les résultats 
     * augmente l'expérience du joueur s'il a gagné aumoins une fois 
     * et s'il a misé aumoins 1000jetons
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
        if (totalMise >= 1000) {
            System.out.println(Affichage.vert + "Vous avez misé un total élevé de " + totalMise + " jetons ! Vous gagnez un bonus d'expérience." + Affichage.resetCouleur);
            joueur.gagnerExperience(10);
        }
    }
}