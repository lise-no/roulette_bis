import java.util.Scanner;
import java.util.InputMismatchException; // pour pouvoir utliser l'exception

/**
 * La classe MagicMoolah représente une machine à sous classique.
 * Elle permet à un joueur de parier des jetons, de lancer un tirage avec des rouleaux et de calculer les gains
 * en fonction des symboles alignés ou des événements spéciaux.
 * Cette machine offre la possibilité de lancer un tirage simple ou 20 d'un coup.
 */
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
    private String[] symboles = {"🍒", "🍀", "💎", "💰", "🎲"};
    private double[] probabilites = {0.40, 0.23, 0.05, 0.19, 0.13}; // Permet de gérer la probabilité d'apparition de chaque symbole
    private int[] multiplicateur = {5, 10, 700, 50, 200}; // Permet de gérer le multiplicateur de chaque symbole pour le calcul des gains
    private int compteurDiamant; // Un compteur qui permet de vérifier l'évenement des diamants
    private boolean event; // Un flag pour vérifier la présence d'un évenement

    /**
     * Constructeur par défaut de la machine à sous Magic Moolah.
     * Initialise les attributs de la machine avec des valeurs par défaut.
     */
    public MagicMoolah(){
        this.nom = "Magic Moolah";
        this.description = "MagicMoolah est la machine à sous classique:\n"
                        + "Elle propose des tirages simples ou multiples (jusqu'à 20 tirages consécutifs), vous permet de gagner \n" 
                        + "des jetons en fonction symboles alignés et d'un événement spécial (3+ diamants).\n";
        this.mise = 5;
        this.gain = 0;
    }

    /**
     * Lance un tirage de la machine à sous et vérifie les gains du joueur.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
    @Override
    public void lancer(Joueur joueur) {
        // Si la mise est valide, procéder au tirage
        // On vide les compteurs
        reset();
        
        // On parcours le tableau afin de le remplir
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                rouleaux[i][j] = tirerSymbole(symboles, probabilites);
            }
        }
        
        // On affiche le rsultat
        afficherResultat();

        // On vérifie la présence d'événements pour calculer les gains
        verifierResultat(joueur);
    }
    
    /**
     * Tire un symbole aléatoire en fonction de la probabilité associée à chaque symbole.
     *
     * @param tabS Le tableau des symboles.
     * @param tabP Le tableau des probabilités associées aux symboles.
     * @return Le symbole tiré.
     */
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

    /**
     * Remet à zéro les compteurs et le flag des événements.
     */
    private void reset(){
        gain = 0;
        compteurDiamant = 0;
        event = false;
    }

    /**
     * Affiche les rouleaux remplis.
     */
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
    
    /**
     * Vérifie les résultats du tirage et calcule les gains du joueur.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
    @Override
    public void verifierResultat(Joueur joueur){
        compteurDiamant = 0;  // Réinitialiser compteurDiamant ici pour chaque vérification
        for(int i = 0; i  < 3; i++){
            event = false; // On met event à false pour verifier si les evenements suivants sont réalisés
            eventAligne(i); // On verifie si l'evenement de l'alignement est réalisé, si oui alors on calcul les gains lié à cet evenement
            if(event){
                System.out.println(Affichage.vert + "Aligmement sur la ligne " + (i+1) + " avec " + rouleaux[i][0] + Affichage.resetCouleur + '\n'); // Si oui pour l'alignement alors on previens le joueur
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
            System.out.println(Affichage.cyan + "Vous avez trouvé " + compteurDiamant + Affichage.resetCouleur + " 💎 ! \n"); // Si oui pour les diamants alors on previens le joueur
        }
    }

    /**
     * Vérifie si une ligne de rouleaux est composée du même symbole (alignement).
     *
     * @param i L'indice de la ligne à vérifier.
     */
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

    /**
     * Vérifie si l'événement des diamants (3 diamants ou plus) est réalisé et calcule les gains associés.
     */
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

    /**
     * Effectue 20 tirages consécutifs et calcule les gains et mises totales.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
    public void lancerMultiple(Joueur joueur) {
        miseMultiple = 0;
        gainMultiple = 0;
    
        // Vérifier si le joueur a assez de jetons pour effectuer 20 tirages
        int totalMise = mise * 20; // La mise totale pour les 20 tirages
    
        if (joueur.getSolde() < totalMise) {
            System.out.println(Affichage.rouge + "Erreur : Vous n'avez pas assez de jetons pour effectuer 20 tirages. Votre solde est insuffisant." + Affichage.resetCouleur);
            return; // Arrêter l'exécution de la méthode si le solde est insuffisant
        }
    
        // Si le joueur a assez de jetons, procéder aux 20 tirages
        for (int i = 0; i < 20; i++) {
            // On affiche le suivi des tirages et on affiche un message sur le tirage multiple rencontre une erreur
            try {
                System.out.println("Tirage numéro " + (i + 1) + " :");
                lancer(joueur);  // Tentative de lancer un tirage
            } catch (Exception e) {
                System.out.println("Erreur lors du tirage : " + e.getMessage());
            }
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
    

    /**
     * Effectue un tirage simple et calcule les gains du joueur.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
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

    /**
     * Affiche le bilan après 20 tirages.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
    public void bilanMultiple(Joueur joueur){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gainMultiple;
        // On augmente la mise depuis le lancement du programme
        miseTotale += miseMultiple;

        // Afficher le bilan du tirage multiple
        System.out.println(Affichage.bleu + "🎰 Résumé des tirages multiples 🎰" + Affichage.resetCouleur);
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

    /**
     * Affiche le bilan après un tirage simple.
     *
     * @param joueur Le joueur qui joue à la machine à sous.
     */
    public void bilanSimple(Joueur joueur){
        // On augmente le gain depuis le lancement du programme
        gainTotal += gain;
        // On augmente la mise depuis le lancement du programme
        miseTotale += mise;

        // Afficher le bilan du tirage simple
        System.out.println(Affichage.bleu + "🎰 Résumé du tirage simple 🎰" + Affichage.resetCouleur);
        System.out.println("----------------------------");
        System.out.println("Vous avez misé " + mise + " jetons.");
        System.out.println("Vous avez gagné " + gain + " jetons.");
        System.out.println("Le bilan est donc : " + (gain-mise) + " jetons.");
        System.out.println("==========================================");
        System.out.println("Bonne chance pour le prochain tirage!");
    }
      
    /**
     * Modifie la mise du joueur avec validation.
     *
     * @param joueur Le joueur qui modifie sa mise.
     * @param sc     Le scanner pour lire l'entrée de l'utilisateur.
     */
    public void modifMise(Joueur joueur, Scanner sc) {
        // On met à false par défaut pour vérifier la mise
        boolean validMise = false;
        
        // Une boucle pour répéter les vérifications si c'est faux
        while (!validMise) {
            try {
                // Demande à l'utilisateur de saisir une nouvelle mise
                System.out.print("Entrez votre mise: ");
                int nouvelleMise = 0;
                
                // Vérifie si l'entrée est bien un entier
                if (sc.hasNextInt()) {
                    nouvelleMise = sc.nextInt();
                    
                    // Vérifie si la mise est positive
                    if (nouvelleMise <= 0) {
                        throw new IllegalArgumentException("La mise doit être un nombre positif.");
                    }
    
                    // Vérifie si la mise dépasse le solde du joueur
                    if (nouvelleMise > joueur.getSolde()) {
                        throw new IllegalArgumentException(Affichage.resetCouleur + "Vous n'avez pas assez de jetons pour cette mise." + Affichage.resetCouleur);
                    }
    
                    // Si la mise est valide, on l'applique
                    this.mise = nouvelleMise;
                    validMise = true; // La mise est valide, on sort de la boucle
                } else {
                    // Si l'utilisateur entre quelque chose qui n'est pas un entier, on demande de saisir un nombre
                    System.out.println(Affichage.rouge + "Erreur : Veuillez entrer un nombre entier." + Affichage.resetCouleur);
                    sc.nextLine(); // Consomme l'entrée invalide pour éviter une boucle infinie
                }
            } catch (IllegalArgumentException e) {
                // Affiche le message d'erreur et redemande une mise
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }
    
    

    /**
     * Demande au joueur s'il souhaite continuer à jouer.
     *
     * @param sc     Le scanner pour lire l'entrée de l'utilisateur.
     * @param joueur Le joueur qui joue à la machine à sous.
     * @return true si le joueur souhaite continuer à jouer, sinon false.
     */
    public boolean demanderContinuer(Scanner sc, Joueur joueur) {
        System.out.print("Voulez-vous continuer à jouer ? (Oui/Non) : ");
        // On prend l'entrée de l'utilisateur
        String reponse = sc.nextLine().trim().toLowerCase();
        
        try {
            // Si la réponse est positive (oui, o, yes, y)
            if (reponse.equals("oui") || reponse.equals("o") || reponse.equals("yes") || reponse.equals("y")) {
                return true;
            }
            // Si la réponse est négative (non, n, no)
            else if (reponse.equals("non") || reponse.equals("n") || reponse.equals("no")) {
                return false;
            } else {
                // Si la réponse est invalide, on lance une exception
                throw new IllegalArgumentException(Affichage.rouge + "Réponse invalide. Veuillez répondre par Oui/Non." + Affichage.resetCouleur);
            }
        } catch (IllegalArgumentException e) {
            // Gestion de l'exception, on affiche le message d'erreur et on redemande la réponse
            System.out.println(e.getMessage());
            return demanderContinuer(sc, joueur);  // On retourne la valeur de l'appel récursif
        }
    }
    
    

    /**
     * Méthode qui permet de démarrer une session de jeu. Elle gère le processus de modification de la mise et
     * lance le menu de la machine à sous Magic Moolah.
     * 
     * @param joueur L'objet Joueur qui participe à la partie.
     * @param sc L'objet Scanner utilisé pour lire les entrées de l'utilisateur.
     */
    public void jouer(Joueur joueur, Scanner sc) {
        try {    
            // Changer la mise
            modifMise(joueur, sc);
        } catch (IllegalArgumentException e) {
            // Afficher un message d'erreur si la mise n'est pas valide ou si le joueur n'a pas assez de jetons
            System.out.println("Erreur : " + e.getMessage());
        }

        // On lance le menu de la machine
        menuMagicMoolah(joueur, sc);
    }

    /**
     * Méthode qui affiche un menu interactif permettant au joueur de choisir l'option qu'il souhaite dans la machine. 
     * Cette méthode permet au joueur de lancer un tirage simple ou multiple, d'afficher les gains
     * et les mises totales, et de continuer ou arrêter la session de jeu.
     * 
     * @param joueur L'objet Joueur qui participe à la partie.
     * @param sc L'objet Scanner utilisé pour lire les entrées de l'utilisateur.
     */
    public void menuMagicMoolah(Joueur joueur, Scanner sc) {
        // On met par défaut continuer à vrai afin de demander au joueur s'il voudra rejouer
        boolean continuer = true;
        while (continuer) {
            System.out.print("\033[H\033[2J"); // Clear le terminal (On saute suffisamment de lignes pour faire apparaître le terminal vide)
            System.out.println("---" + Affichage.violet + '+' + Affichage.jaune + '-' + Affichage.cyan + '-'); // Permet de savoir où on se trouve
            System.out.println(Affichage.violet + "=================MagicMoolah=================\n" + Affichage.resetCouleur);
            System.out.println("Descritpion : " + description + '\n');
            System.out.println("Choisissez l'option :");
            System.out.println(Affichage.jaune + "1. Lancer un tirage simple (Un seul tirage)");
            System.out.println(Affichage.jaune + "2. Lancer un tirage multiple (20 tirages)");
            System.out.println(Affichage.vert + "3. Afficher le total de jetons misé et gagné" + Affichage.resetCouleur);
            System.out.print("Votre choix : ");
    
            int choix = -1; // Initialisation par une valeur non valide
            boolean choixValide = false; // Flag pour vérifier que l'utilisateur fait un choix valide
    
            // Boucle pour garantir que l'entrée est correcte
            while (!choixValide) {
                try {
                    choix = sc.nextInt(); // Lecture du choix de l'utilisateur
                    // Vérifier que le choix est dans la plage valide (1, 2, 3)
                    if (choix >= 1 && choix <= 3) {
                        choixValide = true; // Le choix est valide, on sort de la boucle
                    } else {
                        System.out.println(Affichage.rouge + "Choix invalide. Veuillez choisir un chiffre entre 1 et 3." + Affichage.resetCouleur);
                        sc.nextLine(); // Consommer la ligne pour éviter les erreurs futures
                        System.out.print("Votre choix : "); // Redemander l'entrée
                    }
                } catch (InputMismatchException e) {
                    // Si l'utilisateur entre quelque chose de non numérique
                    System.out.println(Affichage.rouge + "Erreur : Veuillez entrer un chiffre entre 1 et 3." + Affichage.resetCouleur);
                    sc.nextLine(); // Consommer la ligne incorrecte
                    System.out.print("Votre choix : "); // Redemander l'entrée
                }
            }
    
            // Traiter le choix de l'utilisateur
            switch (choix) {
                case 1:
                    lancerSimple(joueur);
                    break;
                case 2:
                    lancerMultiple(joueur);
                    break;
                case 3:
                    System.out.println(Affichage.rouge + "Total des jetons misés : " + miseTotale); // Affichage des jetons perdus
                    System.out.println(Affichage.vert + "Total des jetons gagnés : " + gainTotal + Affichage.resetCouleur); // Affichage des jetons gagnés
                    break;
                default:
                    // Ce cas ne sera jamais atteint grâce à la validation dans le while
                    break;
            }
    
            // On vide le scanner
            sc.nextLine();
            // Demander si l'utilisateur souhaite continuer
            continuer = demanderContinuer(sc, joueur);
        }
    }    
}
