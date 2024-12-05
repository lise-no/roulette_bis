import java.util.Scanner;
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

public class MagicMoolah implements Machine{
    private String[][] rouleaux = new String[3][4];
    private int mise;
    private int gain;
    private int gainMultiple = 0; // Le gain d'un tirage multiple
    private int miseMultiple = 0;  // La mise d'un tirage multiple
    public int gainTotal = 0; // Permet d'avoir un suivi général des gains sur l'ensemble des parties
    public int miseTotale = 0; // Permet d'avoir un suivi général des mises sur l'ensemble des parties
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
    // Lancer un tirage après avoir vérifié le solde du joueur
    public void lancer(Joueur joueur) throws IllegalArgumentException {
        // Vérifier que le joueur a assez de jetons pour la mise
        if (joueur.getSolde() < mise) {
            throw new IllegalArgumentException("Vous n'avez pas assez de jetons pour lancer la machine. Votre solde est insuffisant.");
        }

        // Si la mise est valide, procéder au tirage
        // On vide les compteurs
        reset();
        // On parcours le tableau afin de le remplir
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                // Pour chaque case de notre tableau, on va faire appelle à la méthode tirerSymbole
                rouleaux[i][j] = tirerSymbole(symboles, probabilites);
            }
        }
        // On affiche les rouleaux remplis
        afficherResultat();
        // On vérifie la présence d'événements pour calculer les gains
        verifierResultat(joueur);
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
    public void verifierResultat(Joueur joueur){
        compteurDiamant = 0;  // Réinitialiser compteurDiamant ici pour chaque vérification
        for(int i = 0; i  < 3; i++){
            event = false; // On met event à false pour verifier si les evenements suivants sont réalisés
            eventAligne(i); // On verifie si l'evenement de l'alignement est réalisé, si oui alors on calcul les gains lié à cet evenement
            if(event){
                System.out.println(joueur.vert + "Aligmement sur la ligne " + (i+1) + " avec " + rouleaux[i][0] + joueur.resetCouleur + '\n'); // Si oui pour l'alignement alors on previens le joueur
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
            System.out.println(joueur.cyan + "Vous avez trouvé " + compteurDiamant + joueur.resetCouleur + " 💎 ! \n"); // Si oui pour les diamants alors on previens le joueur
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
    public void lancerMultiple(Joueur joueur) {
        miseMultiple = 0;
        gainMultiple = 0;
    
        // Vérifier si le joueur a assez de jetons pour effectuer 20 tirages
        int totalMise = mise * 20; // La mise totale pour les 20 tirages
    
        if (joueur.getSolde() < totalMise) {
            System.out.println("Erreur : Vous n'avez pas assez de jetons pour effectuer 20 tirages. Votre solde est insuffisant.");
            return; // Arrêter l'exécution de la méthode si le solde est insuffisant
        }
    
        // Si le joueur a assez de jetons, procéder aux 20 tirages
        for (int i = 0; i < 20; i++) {
            // On affiche le suivi des tirages
            System.out.println("Tirage numéro " + (i + 1) + " :");
            lancer(joueur); // Lancer un seul tirage
            // Incrémenter les gains et la mise
            gainMultiple += gain;
            miseMultiple += mise;
            // Retirer la mise du solde du joueur et ajouter le gain
            joueur.modifSolde(-mise);
            joueur.modifSolde(gain);
            // Ajouter de l'expérience
            joueur.gagnerExperience(mise * 2);
            // Pause entre chaque tirage
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // Afficher le bilan après les 20 tirages
        bilanMultiple(joueur);
    }
    

    // Lance la machine une fois
    public void lancerSimple(Joueur joueur){
        lancer(joueur);
        // On retire au solde du joueur la mise et on ajoute le gain
        joueur.modifSolde(-mise);
        joueur.modifSolde(gain);
        // On ajoute de l'experience
        joueur.gagnerExperience(mise*2);
        // On affiche le bilan comme pour la méthode ci-dessus
        bilanSimple(joueur);
    }

    public void bilanMultiple(Joueur joueur){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gainMultiple;
        // On augmente la mise depuis le lancement du programme
        miseTotale += miseMultiple;

        // Afficher le bilan du tirage multiple
        System.out.println(joueur.bleu + "🎰 Résumé des tirages multiples 🎰" + joueur.resetCouleur);
        System.out.println("--------------------------------");
        System.out.println("Vous avez effectué 20 tirages.");
        System.out.println("Vous avez misé " + miseMultiple + " jetons.");
        System.out.println("Vous avez gagné un total de " + gainMultiple + " jetons.");
        System.out.println("Le bilan est donc : " + (gainMultiple-miseMultiple) + " jetons.");
        System.out.println("==========================================");
        // Si le joueur est perdant
        if((gainMultiple-miseMultiple) <= 0){
            System.out.println("\nDommage vous n'avez pas gagné cette fois. \nLa victoire n'est qu'a un pas!\n");
        }
        // Si le joueur est gagnant
        else{
            System.out.println("\n🏆 Félicitations! 🏆\n");
        }
    }

    public void bilanSimple(Joueur joueur){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gain;
        // On augmente la mise depuis le lancement du programme
        miseTotale += mise;

        // Afficher le bilan du tirage simple
        System.out.println(joueur.bleu + "🎰 Résumé du tirage simple 🎰" + joueur.resetCouleur);
        System.out.println("----------------------------");
        System.out.println("Vous avez misé " + mise + " jetons.");
        System.out.println("Vous avez gagné " + gain + " jetons.");
        System.out.println("Le bilan est donc : " + (gain-mise) + " jetons.");
        System.out.println("==========================================");
        System.out.println("Bonne chance pour le prochain tirage!");
    }

    // Modifie la mise avec validation
    public void modifMise(int nouvelleMise, Joueur joueur) throws IllegalArgumentException {
        // Vérifie si la mise est positive
        if (nouvelleMise <= 0) {
            throw new IllegalArgumentException("La mise doit être un nombre positif.");
        }
    
        // Vérifie si la mise dépasse le solde du joueur
        if (nouvelleMise > joueur.getSolde()) {
            throw new IllegalArgumentException("Vous n'avez pas assez de jetons pour cette mise.");
        }
    
    // Si aucune exception n'est lancée, on applique la mise
    this.mise = nouvelleMise;
}

    // Demander à l'utilisateur s'il veut continuer à jouer
    public boolean demanderContinuer(Scanner sc){
        System.out.print("Voulez-vous continuer à jouer ? (Oui/Non) : ");
        // On prends l'entrée de l'utilisateur
        String reponse = sc.nextLine().toLowerCase();
        // On prends sa réponse sous forme de booléen vrai si "oui", faux sinon
        return reponse.equals("oui");
    }

    // Méthode main pour relancer sans utiliser la classe main
    public void jouer(Joueur joueur, Scanner sc) {
        // On met par défaut continuer à vrai afin de demander au joueur s'il voudra rejouer
        boolean continuer = true;
    
        try {
            // Demander une nouvelle mise
            System.out.print("Entrez votre mise : ");
            int nouvelleMise = 0;
    
            // S'assurer que l'utilisateur entre bien une valeur numérique
            while (true) {
                try {
                    nouvelleMise = sc.nextInt();
                    sc.nextLine();  // pour consommer la ligne restante
                    // Si la mise est valide, sortir de la boucle
                    break;
                } catch (InputMismatchException e) {
                    // Si l'utilisateur entre une valeur non numérique
                    System.out.println("Erreur : La mise doit être un nombre entier. Veuillez réessayer.\nEntrez votre mise : ");
                    sc.nextLine(); // Consommer l'entrée invalide
                }
            }
    
            // Appliquer la mise
            modifMise(nouvelleMise, joueur);
    
            // Vérification avant de lancer
            lancer(joueur); // Le lancement va maintenant vérifier si le joueur a suffisamment de jetons
        } catch (IllegalArgumentException e) {
            // Afficher un message d'erreur si la mise n'est pas valide ou si le joueur n'a pas assez de jetons
            System.out.println("Erreur : " + e.getMessage());
        }
    
        // Tant que le joueur veut rejouer
        while (continuer) {
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisamment de lignes pour faire apparaître le terminal vide)
            System.out.println(joueur.violet + "=================MagicMoolah=================\n" + joueur.resetCouleur);
            System.out.println("Choisissez l'option :");
            System.out.println(joueur.jaune + "1. Lancer un tirage simple");
            System.out.println(joueur.jaune + "2. Lancer un tirage multiple");
            System.out.println(joueur.vert + "3. Afficher le total de jetons misé et gagné" + joueur.resetCouleur);
            System.out.print("Votre choix : ");
            int choix = sc.nextInt();
    
            // On reset le scanner pour pouvoir l'utiliser dans la méthode demanderContinuer sinon il reste avec la valeur précédente et ne redemande pas une entrée au prochain appel
            sc.nextLine();
    
            // Traiter le choix de l'utilisateur
            switch (choix) {
                case 1:
                    lancerSimple(joueur);
                    break;
                case 2:
                    lancerMultiple(joueur);
                    break;
                case 3:
                    System.out.println(joueur.rouge + "Total des jetons misés : " + miseTotale); // Affichage des jetons perdus
                    System.out.println(joueur.vert + "Total des jetons gagnés : " + gainTotal + joueur.resetCouleur); // Affichage des jetons gagnés
                    break;
                default:
                    System.out.println(joueur.rouge + "Choix invalide" + joueur.resetCouleur);
            }
    
            // Demander si l'utilisateur souhaite continuer
            continuer = demanderContinuer(sc);
        }
    }
       
}
