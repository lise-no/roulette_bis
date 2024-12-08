import java.util.ArrayList;  // pour les listes
import java.util.Random;     // pour générer des nombres aléatoires
import java.util.Scanner;    // pour lire les entrées clavier 

public class Roulette{// implements Machine {
        private int experienceMin; // expérience minimum pour jouer
        private final ArrayList<Integer> rouges; // Liste des numéros rouges
        private final ArrayList<Integer> noirs;  // Liste des numéros noirs
        private int totalMise; // Total des mises pour chaque session de jeu
        private int nombreParties; // Nombre de parties jouées
        private ArrayList<String> resultats; // Liste pour stocker les résultats de chaque partie
    
        public Roulette(int experienceMin) {
            this.experienceMin = experienceMin;
            this.rouges = initialiserRouges();
            this.noirs = initialiserNoirs();
            this.totalMise = 0;
            this.nombreParties = 0;
            this.resultats = new ArrayList<>(); // Initialisation de la liste des résultats
        }
    
        //@Override
        public void lancer(Joueur joueur) {
            Scanner scanner = new Scanner(System.in);
            boolean rejouer = true;
            // Simuler la création d'un joueur (exemple basique, à adapter selon votre code)
    
        System.out.println("Bienvenue dans la Machine Roulette !" + joueur.getNom());
        System.out.println("Cette machine évalue les chances de victoire des joueurs.");
            
     // Boucle de jeu
        while (rejouer && nombreParties < 5) {
             // Vérifier si le joueur peut jouer? s'il a l'expérience nécéssaire pour jouer
             if (!Exp(joueur)) {
                System.out.println("Votre niveau d'expérience est insuffisant pour jouer à cette machine.");
            break;
           }
            try {
                effectuerPari(scanner,joueur);
            } catch (Roulette.MiseInvalideException e) {
                System.out.println("Erreur de mise : " + e.getMessage());
            } catch (Roulette.ChoixInvalideException e) {
                System.out.println("Erreur de choix : " + e.getMessage());
            } catch (Roulette.NumeroInvalideException e) {
                System.out.println("Numéro invalide : " + e.getMessage());
            } catch (Roulette.CouleurInvalideException e) {
                System.out.println("Couleur invalide : " + e.getMessage());
            }
           // Vérifier si le joueur a encore des jetons pour continuer
            if (joueur.getSolde() <= 0) {
               System.out.println("Vous n'avez plus de jetons pour continuer. Fin de la partie.");
             break;
            }
            // Demander à l'utilisateur s'il veut rejouer
            System.out.println("Souhaitez-vous rejouer ? (oui/non) : ");
            String reponse = scanner.next().toLowerCase();
            if (!reponse.equals("oui")) {
                rejouer = false;
            break;
             }
        }
        //l'experience du joueur augmente de 5 après chaque séries de 5 session
        if (joueur.getBarreExp() % 5 == 0) { 
            experienceMin += 5;
            System.out.println("La difficulté a augmenté ! L'expérience minimale requise est maintenant de " + experienceMin + ".");
        }
        
        //le niveau du joueur augmente aprés chaque session
        joueur.ajouterNiveau();
        // Afficher les résultats finaux de la session de jeu
        afficherResultat(joueur);
    }

    // Vérifie si le joueur a l'expérience nécessaire pour jouer
    boolean Exp(Joueur joueur) {
        return joueur.getBarreExp() >= experienceMin;
    }

    // Classe interne pour les exceptions liées à une mise invalide
    public static class MiseInvalideException extends Exception {
        public MiseInvalideException(String message) {
            super(message);
        }
    }

    // Classe interne pour les exceptions liées à un numéro invalide
    public static class NumeroInvalideException extends Exception {
        public NumeroInvalideException(String message) {
            super(message);
        }
    }

    // Classe interne pour les exceptions liées à une couleur invalide
    public static class CouleurInvalideException extends Exception {
        public CouleurInvalideException(String message) {
            super(message);
        }
    }
    //class interne pour les exception liées aux choix invalides
    public static class ChoixInvalideException extends Exception {
        public ChoixInvalideException(String message) {
            super(message);
        }
    }

    
    private void effectuerPari(Scanner scanner, Joueur joueur) throws MiseInvalideException, ChoixInvalideException, NumeroInvalideException, CouleurInvalideException {
        // Demander au joueur sa mise
        int mise = -1;
    System.out.println("Combien de jetons souhaitez-vous parier ? (vous avez " + joueur.getSolde() + " jetons) : ");
     if (!scanner.hasNextInt()) {//si l'entrée est  un entier
        throw new MiseInvalideException("Entrée invalide. Vous devez entrer un nombre entier.");
     }
     mise = scanner.nextInt();

    if (mise < 1 || mise > joueur.getSolde()) {
                throw new MiseInvalideException("Mise invalide. Vous devez parier entre 1 et " + joueur.getSolde() + " jetons.");
            }
        // Ajouter la mise au total
        totalMise += mise;
        nombreParties++;

        System.out.println("Vous avez parié " + mise + " jetons.");
        System.out.println("Choisissez votre type de pari :");
        System.out.println("1. Parier sur un numéro (1 à 36)");
        System.out.println("2. Parier sur une couleur (rouge ou noir)");
        System.out.println("3. Parier sur le numéro zéro (couleur verte)");
        System.out.println("4. Parier sur un numéro et une couleur");
        System.out.println("Entrez votre choix (1-4) : ");
        int choix = -1;
        if (!scanner.hasNextInt()) {
                throw new ChoixInvalideException("Entrée invalide. Veuillez entrer un nombre entier entre 1 et 4.");
            } 
       
        choix = scanner.nextInt();
        if(choix < 1 || choix > 4) {
            throw new ChoixInvalideException("Choix invalide. Vous devez sélectionner une option entre 1 et 4.");
        }

        System.out.println("vous avez choisis le parie:" + choix + ".");

        // Traiter le choix
        if (choix == 1) {
            parierNumero(joueur, mise, scanner);
        } else if (choix == 2) {
            parierCouleur(joueur, mise, scanner);
        } else if (choix == 3) {
            parierZero(joueur, mise);
        } else if (choix == 4) {
            parierNumeroEtCouleur(joueur, mise,scanner);
        }
        
    }

   
    private void parierNumero(Joueur joueur, int mise, Scanner scanner ) throws NumeroInvalideException {
        System.out.print("Choisissez un numéro (1 à 36) : ");
        if (!scanner.hasNextInt()) {
            throw new NumeroInvalideException("Entrée invalide. Veuillez entrer un numéro entre 1 et 36.");
        }
        int numeroPari = scanner.nextInt();
        if (numeroPari < 1 || numeroPari > 36) {
            throw new NumeroInvalideException("Numéro invalide. Veuillez entrer un numéro entre 1 et 36.");
        }
        System.out.println("Vous avez parié sur le numéro " + numeroPari + ".");
        int resultat = new Random().nextInt(37);
        System.out.println("La roulette a tourné... le numéro est : " + resultat);

        if (numeroPari == resultat) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.modifSolde(mise * 35); // Gain : 35 fois la mise
            joueur.gagnerExperience(5);
            resultats.add("Gagné : Numéro parié " + numeroPari);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.modifSolde(-mise); // Perte : mise retirée
            resultats.add("Perdu : Numéro Parié" + numeroPari);
        }
        
    }

    private void parierCouleur(Joueur joueur, int mise, Scanner scanner) throws CouleurInvalideException {
        System.out.print("Choisissez une couleur (rouge ou noir) : ");
        String couleurPariee = scanner.next().toLowerCase();
        if (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
            throw new CouleurInvalideException("Couleur invalide. Veuillez entrer 'rouge' ou 'noir'.");
        }
        System.out.println("Vous avez parié sur la couleur " + couleurPariee + ".");
        int resultat = new Random().nextInt(37);
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : noirs.contains(resultat)? "noir": "vert");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

        if (couleurPariee.equals(couleurResultat)) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.modifSolde(mise * 35); // Gain : 2 fois la mise
            joueur.gagnerExperience(5);
            resultats.add("Gagné : Couleur Parié " + couleurPariee);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.modifSolde(-mise); // Perte : mise retirée
            resultats.add("Perdu : Couleur Parié " + couleurPariee);
        }
       
    }

    private void parierZero(Joueur joueur, int mise) {
        int resultat = new Random().nextInt(37);
        System.out.println("La roulette a tourné... le numéro est : " + resultat);
        if (resultat == 0) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.modifSolde(mise * 40); // Gain : 17 fois la mise
            joueur.gagnerExperience(6);
            resultats.add("Gagné : Numéro 0");
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.modifSolde(-mise); // Perte : mise retirée
            resultats.add("Perdu : Numéro Parié 0");
        }
    }

    
    private void parierNumeroEtCouleur(Joueur joueur, int mise, Scanner scanner)throws NumeroInvalideException,CouleurInvalideException {
        System.out.println("Choisissez un numéro (1 à 36) : ");
        int numeroPari = scanner.nextInt();
        if (numeroPari < 1 || numeroPari > 36) {
            throw new NumeroInvalideException("Numéro invalide. Veuillez entrer un numéro entre 1 et 36.");
        }
        System.out.println("Choisissez une couleur (rouge ou noir) : ");
        String couleurPariee = scanner.next().toLowerCase();
        if(!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
              throw new CouleurInvalideException("Couleur invalide. Veuillez entrer 'rouge' ou 'noir'.");
            
        }

        int resultat = new Random().nextInt(37);
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

        if (numeroPari == resultat && couleurPariee.equals(couleurResultat)) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.modifSolde(mise * 50); // Gain : 50 fois la mise
            joueur.gagnerExperience(10);
            resultats.add("Gagné : Numéro Parié; " + numeroPari + " et Couleur Parié " + couleurPariee);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.modifSolde(-mise); // Perte : mise retirée
            resultats.add("Perdu : Numéro Parié; " + numeroPari + " et Couleur Parié " + couleurPariee);
        }
        
    }

    // Méthode pour afficher les résultats finaux
    // @Override Mise en commentaire du override pour pouvoir lancer le main
    public void afficherResultat(Joueur joueur) {
        System.out.println("\nRésultats des parties :");
        for (int i = 0; i < resultats.size(); i++) {
            System.out.println("Partie " + (i+1) + ": " + resultats.get(i));
        }

        System.out.println("\nRésumé de la session de jeu :");
        System.out.println("Total des mises : " + totalMise);
        System.out.println("Nombre de parties jouées : " + nombreParties);
        System.out.println("Jetons restants : " + joueur.getSolde());
        System.out.println("Expérience actuelle : " + joueur.getBarreExp()); // Afficher l'expérience du joueur
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
}