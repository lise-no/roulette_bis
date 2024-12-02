import java.util.ArrayList;// pour les listes
import java.util.Random;// pour générer des nombres aléatoires
import java.util.Scanner;//pour lire les entrées clavier

public class Roulette implements Machine{
private boolean etat; // Indique si la machine est allumée
private final int niveauExperienceMin; // Niveau minimum pour jouer
private final ArrayList<Integer> rouges; // Liste des numéros rouges
private final ArrayList<Integer> noirs;  // Liste des numéros noirs

public  Roulette(int niveauExperienceMin) {
    this.etat = false;
    this.niveauExperienceMin = niveauExperienceMin;
    this.rouges = initialiserRouges();
    this.noirs = initialiserNoirs();
}

@Override
public void lancer() {
    this.etat = true;
    System.out.println("Roulette démarrée.");
}

@Override
public void jouer(Joueur joueur) {
    if (!etat) {
        System.out.println("La machine est éteinte.");
        return;
    }

    if (!peutJouer(joueur)) {
        System.out.println("Votre niveau d'expérience est insuffisant pour jouer à cette machine.");
        return;
    }

    effectuerPari(joueur);
}


boolean peutJouer(Joueur joueur) // Vérifie si le joueur peut utiliser la machine
{
    return joueur.getExperience() >= niveauExperienceMin;
}
private void effectuerPari(Joueur joueur) {
        Scanner scanner = new Scanner(System.in); // Scanner pour lire les saisies
        System.out.println("Choisissez votre type de pari :");
        System.out.println("1. Parier sur un numéro (1 à 36)");
        System.out.println("2. Parier sur une couleur (rouge ou noir)");
        System.out.println("3. Parier sur le numéro zéro (couleur verte)");
        System.out.println("4. Parier sur un numéro et une couleur");

        int choix = -1;

        // Lire le choix du joueur
        while (choix < 1 || choix > 4) {
            System.out.print("Entrez votre choix (1-4) : ");
            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
            } else {
                System.out.println("Veuillez entrer un nombre valide.");
                scanner.next(); // Ignorer l'entrée invalide
            }
        }

        // Traiter le choix
        if (choix == 1) {
            System.out.print("Choisissez un numéro (1 à 36) : ");
            int numeroPari = scanner.nextInt();
            while (numeroPari < 1 || numeroPari > 36) {
                System.out.print("Veuillez entrer un numéro valide entre 1 et 36 : ");
                numeroPari = scanner.nextInt();
            }
            System.out.println("Vous avez parié sur le numéro " + numeroPari + ".");
            int resultat = new Random().nextInt(37); // Résultat aléatoire de 0 à 36
            System.out.println("La roulette a tourné... le numéro est : " + resultat);

            if (numeroPari == resultat) {
                System.out.println("Félicitations, vous avez gagné !");
                joueur.ajouterJetons(35); // Gain : 35 fois la mise
            } else {
                System.out.println("Dommage, vous avez perdu.");
            }
        } else if (choix == 2) {
            System.out.print("Choisissez une couleur (rouge ou noir) : ");
            String couleurPariee = scanner.next().toLowerCase();
            while (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
                System.out.print("Veuillez entrer 'rouge' ou 'noir' : ");
                couleurPariee = scanner.next().toLowerCase();
            }
            System.out.println("Vous avez parié sur la couleur " + couleurPariee + ".");
            int resultat = new Random().nextInt(37);

            String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");

            System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

            if (couleurPariee.equals(couleurResultat)) {
                System.out.println("Félicitations, vous avez gagné !");
                joueur.ajouterJetons(2); // Gain : 2 fois la mise
            } else {
                System.out.println("Dommage, vous avez perdu.");
            }
        } else if (choix == 3) {
            System.out.println("Vous avez parié sur le numéro zéro (couleur verte).");
            int resultat = new Random().nextInt(37);

            System.out.println("La roulette a tourné... le numéro est : " + resultat);

            if (resultat == 0) {
                System.out.println("Félicitations, vous avez gagné !");
                joueur.ajouterJetons(17); // Gain : 17 fois la mise
            } else {
                System.out.println("Dommage, vous avez perdu.");
            }
        } else if (choix == 4) {
            System.out.print("Choisissez un numéro (1 à 36) : ");
            int numeroPari = scanner.nextInt();
            while (numeroPari < 1 || numeroPari > 36) {
                System.out.print("Veuillez entrer un numéro valide entre 1 et 36 : ");
                numeroPari = scanner.nextInt();
            }
            System.out.print("Choisissez une couleur (rouge ou noir) : ");
            String couleurPariee = scanner.next().toLowerCase();
            while (!couleurPariee.equals("rouge") && !couleurPariee.equals("noir")) {
                System.out.print("Veuillez entrer 'rouge' ou 'noir' : ");
                couleurPariee = scanner.next().toLowerCase();
            }
            System.out.println("Vous avez parié sur le numéro " + numeroPari + " et la couleur " + couleurPariee + ".");
            int resultat = new Random().nextInt(37);

            String couleurResultat = resultat == 0 ? "vert" : (rouges.contains(resultat) ? "rouge" : "noir");

            System.out.println("La roulette a tourné... le numéro est : " + resultat + " (" + couleurResultat + ").");

            if (numeroPari == resultat && couleurPariee.equals(couleurResultat)) {
                System.out.println("Félicitations, vous avez gagné !");
                joueur.ajouterJetons(50); // Gain : 50 fois la mise
            } else {
                System.out.println("Dommage, vous avez perdu.");
            }
        }
    }

    private ArrayList<Integer> initialiserRouges() {
        ArrayList<Integer> rouges = new ArrayList<>();
        int[] rougesArr = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        for (int num : rougesArr) {
            rouges.add(num);
        }
        return rouges;
    }

    private ArrayList<Integer> initialiserNoirs() {
        ArrayList<Integer> noirs = new ArrayList<>();
        int[] noirsArr = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        for (int num : noirsArr) {
            noirs.add(num);
        }
        return noirs;
    }
    public class Main {
            Joueur joueur = new Joueur("Alice", 50, 100);
            MachineRoulette machine = new MachineRoulette(30);
            machine.lancer();
            System.out.println("Bienvenue à la roulette, " + joueur.getNom() + " !");
            machine.jouer(joueur);
            System.out.println("Vous avez maintenant " + joueur.getJetons() + " jetons.");
        }
    }