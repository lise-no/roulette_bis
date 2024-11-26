import java.util.Scanner;

public class Joueur {
    private String nom;
    private int niveau;
    private int prochaineNiveau;
    private int barreExperience;
    private int solde;

    public void nommer(String nom) {
        Scanner scanner = new Scanner(System.in);  // Création d'un objet Scanner pour lire l'entrée
        System.out.print("Entrez votre nom : ");
        this.nom = scanner.nextLine(); // Lit l'entrée et l'assigne au nom du joueur
    }

    public int getSolde() {
        return solde;  // Retourner la valeur de solde
    }

    public void ajouterSolde(int montant) {
    }

    public void retirerSolde(int montant) {
    }

    public void ajouterExp(int experience) {
    }

    public void afficherInfo() {
    }
}
