package casino;

public class MagicMoolah implements Machine {
    private int res1; // Le nombre renvoyé
    private String res2; // La couleur renvoyée
    private int pariNb; // Le pari du joueur sur les nombres
    private String pariCouleur; // Le pari du joueur sur les couleurs
    private List<String> listeCouleur;
    private Liste<int> listeNombre;
    private int niveauExpAcces;
    private int mise;
    private int gain;
    private String nom;
    private String description;
    private int volatilite;

    public void lancer() {
        // Remplir le tableau avec des chiffres et symboles aléatoires
    }

    public void afficherResultat() {
        // Afficher le tableau avec les résultats
    }

    public void verfiierResultat() {
        // Calculer les gains et les afficher
    }

    public void modifMise(int nouvelleMise) {
        // Modifier la mise actuelle
    }

    public boolean peutJouer(){
        // Renvoie si la machine peut etre lancée
    }

    public void effectuerPari(){
        // Permet au joueur de poser son/ses paris
    }
}
