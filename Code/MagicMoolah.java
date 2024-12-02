import java.util.Scanner;

public class MagicMoolah implements Machine{
    private String[][] rouleaux = new String[3][4];
    private int mise;
    private int gain;
    private int gainMultiple = 0; // Le gain d'un tirage multiple
    private int miseMultiple = 0;  // La mise d'un tirage multiple
    private int gainTotal = 0; // Permet d'avoir un suivi général des gains sur l'ensemble des parties
    private int miseTotale = 0; // Permet d'avoir un suivi général des mises sur l'ensemble des parties
    private String nom;
    private String description;
    private int volatilite;
    private String[] symboles = {"🍒", "🍀", "💎", "💰", "🎲"};
    private double[] probabilites = {0.35, 0.3, 0.01, 0.19, 0.15}; // Permet de gérer la probabilité d'apparition de chaque symbole
    private int[] multiplicateur = {5, 10, 700, 50, 200}; // Permet de gérer le multiplicateur de chaque symbole pour le calcul des gains
    private int compteurDiamant; // Un compteur qui permet de vérifier l'évenement des diamants
    private boolean event; // Un flag pour vérifier la présence d'un évenement

    // Constructeur
    public MagicMoolah(){
        this.nom = "Magic Moolah";
        this.description = "Une machine à sous en 3x4 dans laquelle il faut aligner 4 symboles";
        this.mise = 5;
        this.gain = 0;
    }

    @Override
    public void lancer(){
        // On vide les compteurs
        reset();
        // On parcours le tableau afin de le remplir
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                // Pour chaque case de notre tableau, on va faire appelle à la méthode tirerSymbole
                rouleaux[i][j] = tirerSymbole(symboles, probabilites);
            }
        }
        // On affiche les rouleaux remplis
        afficherResultat();
        // On verifie la presence d'évenements pour compter le gains par apport à la mise
        verifierResultat();
    }

    // Tire des symboles par apport à la probabilité associée à chaque symbole 
    public String tirerSymbole(String[] tabS, double[] tabP){
        double nbAleatoire = Math.random();
        double somme = 0.0;
        // On parcours le tableau des probabilités
        for(int i = 0; i < tabS.length; i++){
            // On fait une addition de toutes les probabilités
            somme += tabP[i];
            // On va vérifier si le nombre aléatoire choisit en 0 et 1 est dans une intervalle de probabilité
            // Puis on assigne le symbole lié à l'intervalle de probabilité dans laquelle le nbAléatoire est
            if(nbAleatoire < somme){
                return tabS[i];
            }
        }
        // Si il y a un probleme dans la boucle alors, on assigne par défaut le premier symbole
        return tabS[0];
    }

    // Remet à 0 les compteurs et flag
    private void reset(){
        gain = 0;
        compteurDiamant = 0;
        event = false;
    }

    @Override
    public void afficherResultat(){
        // Afficher le tableau avec les résultats
        System.out.println("= Affichage =");
        // On parcours le tableau
        for(int i = 0; i < 3; i++){
            System.out.print("|");
            for(int j = 0; j < 4; j++){
                // On affiche les symboles de la case du parcours
                System.out.print(rouleaux[i][j] +"|");
            }
            System.out.println();  // Saut de ligne après chaque ligne du tableau
        }
        System.out.println(); // Espace entre les lancers
    }

    // Calculer les gains et les afficher
    @Override
    public void verifierResultat(){
        compteurDiamant = 0;  // Réinitialiser compteurDiamant ici pour chaque vérification
        for(int i = 0; i  < 3; i++){
            event = false; // On met event à false pour verifier si les evenements suivants sont réalisés
            eventAligne(i); // On verifie si l'evenement de l'alignement est réalisé, si oui alors on calcul les gains lié à cet evenement
            if(event){
                System.out.println("Aligmement sur la ligne " + (i+1) + " avec " + rouleaux[i][0] + '\n'); // Si oui pour l'alignement alors on previens le joueur
            }
            // On parcours le tableau et on compte les diamants
            for(int j = 0; j < 4; j++){
                if(rouleaux[i][j].equals("💎")){
                    compteurDiamant++;
                }
            }
        }
        event = false; // On remet à false au cas où il y a un alignement
        eventDiamant(); // On verifie si l'evenement diamant est réalisé, si oui alors on calcul les gains lié à cet evenement
        if(event){
            System.out.println("Vous avez trouvé " + compteurDiamant + " 💎 ! \n"); // Si oui pour les diamants alors on previens le joueur
        }
    }

    // Verifie si les symboles sont alignés
    public void eventAligne(int i){
        // On vérifie que une ligne complete est composée des meme syboles
        if(rouleaux[i][0].equals(rouleaux[i][1]) && rouleaux[i][1].equals(rouleaux[i][2]) && rouleaux[i][2].equals(rouleaux[i][3])){
            // On indique donc qu'un évenement est réalisé
            event = true;
            // On parcours le tableau des symboles
            for(int j = 0; j < symboles.length; j++){
                // On récupère le symbole aligné
                if(rouleaux[i][0].equals(symboles[j])){
                    // On calcul les gains associés à ce symbole
                    gain += multiplicateur[j] * mise;
                }
            }
        }
    }

    // Méthode pour gérer les gains si 3 diamants ou plus sont présents
    public void eventDiamant(){
        // Si 3 diamants ou plus sont présents, afficher les diamants et calculer les gains
        if(compteurDiamant >= 3){
            // On indique qu'un évenement est réalisé
            event = true;
            int multiplicateur = compteurDiamant - 2; // La multiplication commence à partir de 3 diamants
            // Calcul des gains en fonction des symboles présents dans les rouleaux
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 4; j++){
                    String symbole = rouleaux[i][j];
                    // Calculer le gain pour chaque symbole
                    switch(symbole){
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
        // On fait appel à la méthode lancer 2à fois
        for(int i = 0; i < 20; i++){
            // On affiche le suivit des tirages
            System.out.println("tirage numéro " + (i+1) + " :");
            lancer();
            // On incrémente les gains et mise afin qu'ils ne soient pas reset comme dans un tirage simple
            gainMultiple += gain;
            miseMultiple += mise;
            // On fait une pause entre chaque tirage afin que le joueur puisse les suivre
            try{
                Thread.sleep(500);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        // On affiche le bilan pour que le joueur comprenne bien si il a gagné ou pas
        bilanMultiple();
    }

    // Lance la machine une fois
    public void lancerSimple(){
        lancer();
        // On affiche le bilan comme pour la méthode ci-dessus
        bilanSimple();
    }

    public void bilanMultiple(){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gainMultiple;
        // On augmente la mise depuis le lancement du programme
        miseTotale += miseMultiple;

        // Afficher le bilan du tirage multiple
        System.out.println("🎰 Résumé des tirages multiples 🎰");
        System.out.println("--------------------------------");
        System.out.println("Vous avez effectué 20 tirages.");
        System.out.println("Vous avez misé " + miseMultiple + " jetons.");
        System.out.println("Vous avez gagné un total de " + gainMultiple + " jetons.");
        System.out.println("Le bilan est donc : " + (gainMultiple-miseMultiple) + " jetons.");
        System.out.println("==========================================");
        // Si le joueur est perdant
        if((gainMultiple-miseMultiple) <= 0){
            System.out.println("Dommage vous n'avez pas gagné cette fois. \nLa victoire n'est qu'a un pas!");
        }
        // Si le joueur est gagnant
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

    // Modifie la mise
    public void modifMise(int nouvelleMise){
        this.mise = nouvelleMise;
    }

    // Demander à l'utilisateur s'il veut continuer à jouer
    public boolean demanderContinuer(Scanner sc){
        System.out.print("Voulez-vous continuer à jouer ? (oui/non) : ");
        // On prends l'entrée de l'utilisateur
        String reponse = sc.nextLine();
        // On prends sa réponse sous forme de booléen vrai si "oui", faux sinon
        return reponse.equals("oui");
    }

    // Méthode main pour relancer sans utiliser la classe main
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        // On crée l'objet machine afin de l'utiliser
        MagicMoolah machine = new MagicMoolah();
        // On met par defaut continuer à vrai afin de demander au joueur si il voudra rejouer
        boolean continuer = true;
        // On met une mise par defaut
        machine.modifMise(50);

        // tant que le joueur veut rejouer
        while(continuer){
            System.out.println("Choisissez l'option :");
            System.out.println("1. Lancer un tirage simple");
            System.out.println("2. Lancer un tirage multiple");
            System.out.println("3. Afficher le total de jetons misé et gagné");
            System.out.print("Votre choix : ");
            int choix = sc.nextInt();

            // On reset le scanner pour pouvoir l'utiliser dans la méthode demanderContinuer sinon il reste avec la valeur précédente et ne redemande pas une entrée au prochain appel
            sc.nextLine();

            // Traiter le choix de l'utilisateur
            switch(choix){
                case 1:
                    machine.lancerSimple();
                    break;
                case 2:
                    machine.lancerMultiple();
                    break;
                case 3:
                    System.out.println("Total des jetons misés : " + machine.miseTotale);
                    System.out.println("Total des jetons gagnés : " + machine.gainTotal);
                    break;
                default:
                    System.out.println("Choix invalide");
            }

            // Demander si l'utilisateur souhaite continuer
            continuer = machine.demanderContinuer(sc);
        }
    }
}
