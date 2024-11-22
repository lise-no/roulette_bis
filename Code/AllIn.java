package casino;

public class AllIn implements Machine {
    private String[] tableau = new String[3];
    private String nom;
    private int difficulté; // La difficulté choisie par le joueur
    private int mise;
    private int gain;
    private String description;
    private int volatilite;

    public void lancer(){
        // Remplir le tableau avec des chiffres et symboles aléatoires
    }

    public void afficherResultat(){
        // Afficher le tableau avec les résultats
    }

    public void verifierResultat(){
        // Calculer les gains et les afficher
    }

    private void trouverSymbole1(){
        // Lance la case du tableau des symbole en difficulté 1
    }

    private void trouverSymbole2(){
        // Lance la case du tableau des symbole en difficulté 2
    }

    private void trouverSymbole3(){
        // Lance la case du tableau des symbole en difficulté 3
    }

    public int nivDificulte(){
        // Retourne le choix de difficulté de l'utilisateur entre 1 et 3
    }

    private int nbAleatoire(){
        // Retourne un nombre aléatoire entre 1 et 9
    }

    public void modifMise(int nouvelleMise){
        // Modifier la mise actuelle
    }

}
