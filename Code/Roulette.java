import java.util.ArrayList;  // pour les listes
import java.util.Random;     // pour générer des nombres aléatoires
import java.util.Scanner;    // pour lire les entrées clavier

public class MachineRoulette implements Machine {
    private final int niveauExperienceMin; // Niveau minimum pour jouer
    private final ArrayList<Integer> rouges; // Liste des numéros rouges
    private final ArrayList<Integer> noirs;  // Liste des numéros noirs
    private int totalMise; // Total des mises pour chaque session de jeu
    private int nombreParties; // Nombre de parties jouées
    private ArrayList<String> resultats; // Liste pour stocker les résultats de chaque partie

    public MachineRoulette(int niveauExperienceMin) {
        this.niveauExperienceMin = niveauExperienceMin;
        this.rouges = initialiserRouges();
        this.noirs = initialiserNoirs();
        this.totalMise = 0;
        this.nombreParties = 0;
        this.resultats = new ArrayList<>(); // Initialisation de la liste des résultats
    }

    @Override
    public void lancer() {
        Scanner scanner = new Scanner(System.in);
        boolean rejouer = true;
        // Simuler la création d'un joueur (exemple basique, à adapter selon votre code)
        Joueur joueur = new Joueur("Joueur1", 100, 50); // Exemple de joueur avec 100 jetons et 50 d'expérience

    System.out.println("Bienvenue dans la Machine Roulette !" + joueur.getNom() + "." + "   " +"Cette machine evalue les chances de victoire des joueurs.");
        
 // Boucle de jeu
    while (rejouer && nombreParties < 10) {
    // Vérifier si le joueur peut jouer? s'il a l'expérience nécéssaire pour jouer
         if (!Exp(joueur)) {
             System.out.println("Votre niveau d'expérience est insuffisant pour jouer à cette machine.");
         break;
        }
        try {
            effectuerPari(joueur);
        } catch (MachineRoulette.MiseInvalideException e) {
            System.out.println("Erreur de mise : " + e.getMessage());
        } catch (MachineRoulette.ChoixInvalideException e) {
            System.out.println("Erreur de choix : " + e.getMessage());
        } catch (MachineRoulette.NumeroInvalideException e) {
            System.out.println("Numéro invalide : " + e.getMessage());
        } catch (MachineRoulette.CouleurInvalideException e) {
            System.out.println("Couleur invalide : " + e.getMessage());
        }
       // Vérifier si le joueur a encore des jetons pour continuer
        if (joueur.getJetons() <= 0) {
           System.out.println("Vous n'avez plus de jetons pour continuer. Fin de la partie.");
         break;
        }
// Demander à l'utilisateur s'il veut rejouer
     System.out.print("Souhaitez-vous rejouer ? (oui/non) : ");
 String reponse = scanner.next().toLowerCase();
 if (!reponse.equals("oui")) {
    rejouer = false;
 break;
}
    }
// Afficher les résultats finaux de la session de jeu
    afficherResultat(joueur);
         }


    // Vérifie si le joueur a l'expérience nécessaire pour jouer
    boolean Exp(Joueur joueur) {
        return joueur.getExperience() >= niveauExperienceMin;
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

    private void effectuerPari(Joueur joueur) throws MiseInvalideException, ChoixInvalideException, NumeroInvalideException, CouleurInvalideException {
        Scanner scanner = new Scanner(System.in);

        // Demander au joueur sa mise
        int mise = -1;
    System.out.print("Combien de jetons souhaitez-vous parier ? (vous avez " + joueur.getJetons() + " jetons) : ");
     if (!scanner.hasNextInt()) {//si l'entrée est  un entier
        throw new MiseInvalideException("Entrée invalide. Vous devez entrer un nombre entier.");
     }
     mise = scanner.nextInt();

    if (mise < 1 || mise > joueur.getJetons()) {
                throw new MiseInvalideException("Mise invalide. Vous devez parier entre 1 et " + joueur.getJetons() + " jetons.");
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
            parierNumero(joueur, mise);
        } else if (choix == 2) {
            parierCouleur(joueur, mise);
        } else if (choix == 3) {
            parierZero(joueur, mise);
        } else if (choix == 4) {
            parierNumeroEtCouleur(joueur, mise);
        }
    }

    private void parierNumero(Joueur joueur, int mise) throws NumeroInvalideException {
        Scanner scanner = new Scanner(System.in);
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
            joueur.ajouterJetons(mise * 35); // Gain : 35 fois la mise
            resultats.add("Gagné : Numéro " + numeroPari);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.retirerJetons(mise); // Perte : mise retirée
            resultats.add("Perdu : NuméroParier" + numeroPari);
        }
    }

    private void parierCouleur(Joueur joueur, int mise) throws CouleurInvalideException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez une couleur (rouge ou noir) : ");
        String couleurPariee = scanner.next().toLowerCase();
        if (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
            throw new CouleurInvalideException("Couleur invalide. Veuillez entrer 'rouge' ou 'noir'.");
        }
        System.out.println("Vous avez parié sur la couleur " + couleurPariee + ".");
        int resultat = new Random().nextInt(37);
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

        if (couleurPariee.equals(couleurResultat)) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.ajouterJetons(mise * 2); // Gain : 2 fois la mise
            resultats.add("Gagné : CouleurParier " + couleurPariee);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.retirerJetons(mise); // Perte : mise retirée
            resultats.add("Perdu : CouleurParier " + couleurPariee);
        }
    }

    private void parierZero(Joueur joueur, int mise) {
        int resultat = new Random().nextInt(37);
        System.out.println("La roulette a tourné... le numéro est : " + resultat);
        if (resultat == 0) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.ajouterJetons(mise * 17); // Gain : 17 fois la mise
            resultats.add("Gagné : Numéro 0");
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.retirerJetons(mise); // Perte : mise retirée
            resultats.add("Perdu : NuméroParier 0");
        }
    }

    private void parierNumeroEtCouleur(Joueur joueur, int mise)throws NumeroInvalideException,CouleurInvalideException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez un numéro (1 à 36) : ");
        int numeroPari = scanner.nextInt();
        if (numeroPari < 1 || numeroPari > 36) {
            throw new NumeroInvalideException("Numéro invalide. Veuillez entrer un numéro entre 1 et 36.");
        }
        System.out.print("Choisissez une couleur (rouge ou noir) : ");
        String couleurPariee = scanner.next().toLowerCase();
        if(!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
              throw new CouleurInvalideException("Couleur invalide. Veuillez entrer 'rouge' ou 'noir'.");
            
        }

        int resultat = new Random().nextInt(37);
        String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");
        System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

        if (numeroPari == resultat && couleurPariee.equals(couleurResultat)) {
            System.out.println("Félicitations, vous avez gagné !");
            joueur.ajouterJetons(mise * 50); // Gain : 50 fois la mise
            resultats.add("Gagné : NuméroParier; " + numeroPari + " et CouleurParier " + couleurPariee);
        } else {
            System.out.println("Dommage, vous avez perdu.");
            joueur.retirerJetons(mise); // Perte : mise retirée
            resultats.add("Perdu : NuméroParier; " + numeroPari + " et CouleurParier " + couleurPariee);
        }
    }

    // Méthode pour afficher les résultats finaux
    private void afficherResultat(Joueur joueur) {
        System.out.println("\nRésultats des parties :");
        for (int i = 0; i < resultats.size(); i++) {
            System.out.println("Partie " + (i + 1) + ": " + resultats.get(i));
        }

        System.out.println("\nRésumé de la session de jeu :");
        System.out.println("Total des mises : " + totalMise);
        System.out.println("Nombre de parties jouées : " + nombreParties);
        System.out.println("Jetons restants : " + joueur.getJetons());
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