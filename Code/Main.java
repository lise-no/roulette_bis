public class Main {
    public static void main(String[] args) {
        // Création d'une instance de MagicMoolah
        MagicMoolah machine = new MagicMoolah("Magic Moolah", "Une machine à sous en 3x4 dans laquelle il faut aligner 4 symboles");

        // Lancer la machine et afficher les résultats
        machine.lancer();

        // Vérification des résultats
        machine.verifierResultat();
    }
}
