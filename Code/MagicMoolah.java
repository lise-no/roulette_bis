import java.util.Scanner;

public class MagicMoolah implements Machine{
    private String[][] rouleaux = new String[3][4];
    private int mise;
    private int gain;
    private int gainMultiple = 0;
    private int miseMultiple = 0;
    private int gainTotal = 0;
    private int miseTotale = 0;
    private String nom;
    private String description;
    private int volatilite;
    private String[] symboles = {"🍒", "🍀", "💎", "💰", "🎲"};
    private double[] probabilites = {0.35, 0.3, 0.01, 0.19, 0.15};
    private int[] multiplicateur = {5, 10, 700, 50, 200};
    private int compteurDiamant;
    private boolean event;
    private int difference;

    public MagicMoolah() {
        this.nom = "Magic Moolah";
        this.description = "Une machine à sous en 3x4 dans laquelle il faut aligner 4 symboles";
        this.mise = 5;
        this.gain = 0;
    }

    @Override
    public void lancer() {
        reset();
        // Remplir le tableau avec des symboles aléatoires
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                rouleaux[i][j] = tierSymbole(symboles, probabilites);
            }
        }
        afficherResultat();
        verifierResultat();
    }
    

    // Tire des symboles par apport à la probabilité associée à chaque symbole 
    public String tierSymbole(String[] tabS, double[] tabP){
        double nbAleatoire = Math.random();
        double somme = 0.0;
        for(int i = 0; i < tabS.length; i++){
            somme += tabP[i];
            if(nbAleatoire < somme){
                return tabS[i];
            }
        }
        return tabS[0];
    }

    // Remet à 0 les compteurs et flags
    private void reset() {
        gain = 0;
        compteurDiamant = 0;
        event = false;
    }

    @Override
    public void afficherResultat() {
        // Afficher le tableau avec les résultats
        System.out.println("= Affichage =");
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 4; j++) {
                System.out.print(rouleaux[i][j] +"|");
            }
            System.out.println();  // Saut de ligne après chaque ligne du tableau
        }
        System.out.println(); // Espace entre les lancers
    }

    // Calculer les gains et les afficher
    @Override
    public void verifierResultat() {
        compteurDiamant = 0;  // Réinitialiser compteurDiamant ici pour chaque vérification
        for (int i = 0; i  < 3;i++) {
            event = false; // On met event à false pour verifier si les evenements suivants sont réalisés
            eventAligne(i); // On verifie si l'evenement est réalisé, si oui alors on calcul les gains lié à cet evenement
            if(event){
                System.out.println("Aligmement sur la ligne " + (i+1) + " avec " + rouleaux[i][0] + '\n'); // Si oui pour l'alignement alors on previens le joueur
            }
            // Un compteur de diamants
            for (int j = 0; j < 4; j++) {
                if (rouleaux[i][j].equals("💎")) {
                    compteurDiamant++;
                }
            }
        }
        event = false; // On remet à false au cas où il y a un alignement
        eventDiamant(); // On verifie si l'evenement est réalisé, si oui alors on calcul les gains lié à cet evenement
        if(event){
        System.out.println("Vous avez trouvé " + compteurDiamant + " 💎 ! \n"); // Si oui pour les diamants alors on previens le joueur
        }
    }
    

    // Verifie si les symboles sont alignés
    public void eventAligne(int i){
        if (rouleaux[i][0].equals(rouleaux[i][1]) && rouleaux[i][1].equals(rouleaux[i][2]) && rouleaux[i][2].equals(rouleaux[i][3])) {
            event = true;
            // Calcul les gains
            for(int j = 0; j < symboles.length; j++){
                if(rouleaux[i][0].equals(symboles[j])){
                    gain += multiplicateur[j] * mise;
                }
            }
        }
    }

    // Méthode pour gérer les gains si 3 diamants ou plus sont présents
    public void eventDiamant() {
        // Si 3 diamants ou plus sont présents, afficher les diamants et calculer les gains
        if (compteurDiamant >= 3) {
            event = true;
            int multiplicateur = compteurDiamant - 2; // La multiplication commence à partir de 3 diamants
            // Calcul des gains en fonction des symboles présents dans la ligne
            for(int i = 0; i < 3; i++){
                for (int j = 0; j < 4; j++) {
                    String symbole = rouleaux[i][j];
                    // Calculer le gain pour chaque symbole
                    switch (symbole) {
                        case "🍒":
                            gain += mise * 2 * multiplicateur;
                            break;
                        case "🍀":
                            gain += mise * 4 * multiplicateur;
                            break;
                        case "💎":
                            gain += mise * 100 * multiplicateur;
                            break;
                        case "💰":
                            gain += mise * 15 * multiplicateur;
                            break;
                        case "🎲":
                            gain += mise * 10 * multiplicateur;
                            break;
                }
            }
        }
    }
}


    // Lance la machine 20 fois
    public void lancerMultiple(){
        for(int i = 0; i < 20; i++){
            System.out.println("tirage numéro " + (i+1) + " :");
            lancer();
            gainMultiple += gain;
            miseMultiple += mise;
            try {
            Thread.sleep(500);
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        bilanMultiple();
    }

    public void lancerSimple(){
        lancer();
        bilanSimple();
    }

    public void bilanMultiple(){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gainMultiple;
        // On augmente la mise depuis le lancement du programme
        miseTotale += miseMultiple;
        difference = gainMultiple-miseMultiple;

        // Afficher le bilan du tirage multiple
        System.out.println("🎰 Résumé des tirages multiples 🎰");
        System.out.println("--------------------------------");
        System.out.println("Vous avez effectué 20 tirages.");
        System.out.println("Vous avez misé " + miseMultiple + " jetons.");
        System.out.println("Vous avez gagné un total de " + gainMultiple + " jetons.");
        System.out.println("Le bilan est donc : " + difference + " jetons.");
        System.out.println("==========================================");
        if(difference <= 0){
            System.out.println("Dommage vous n'avez pas gagné cette fois. \nLa victoire n'est qu'a un pas!");
        }
        else{
            System.out.println("🏆 Félicitations! 🏆");
        }
    }

    public void bilanSimple(){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gain;
        // On augmente la mise depuis le lancement du programme
        miseTotale += mise;

        // Afficher le bilan du tirage simple
        System.out.println("🎰 Résumé du tirage simple 🎰");
        System.out.println("----------------------------");
        System.out.println("Vous avez misé " + mise + " jetons.");
        System.out.println("Vous avez gagné " + gain + " jetons.");
        System.out.println("Le bilan est donc : " + (gain-mise) + " jetons.");
        System.out.println("==========================================");
        System.out.println("Bonne chance pour le prochain tirage!");
    }


    public void modifMise(int nouvelleMise) {
        this.mise = nouvelleMise;
    }

    // Getter pour la mise
    public int getMise() {
        return mise;
    }

    // Getter pour la mise totale des lancerMultiple
    public int getmiseMultiple() {
        return miseMultiple;
    }

    // Getter pour le gain
    public int getGain() {
        return gain;
    }
}