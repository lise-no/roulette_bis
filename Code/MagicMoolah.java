public class MagicMoolah implements Machine{
    private String[][] rouleaux = new String[3][4];
    private int mise;
    private int gain;
    private String nom;
    private String description;
    private int volatilite;

    public MagicMoolah(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.mise = 0;
        this.gain = 0;
    }

    @Override
    public void lancer() {
        String[] symboles = {"🍒", "🍀", "💎", "💰", "🎲"};
        
        // Remplir le tableau avec des symboles aléatoires
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                rouleaux[i][j] = symboles[(int) (Math.random() * symboles.length)];
            }
        }

        afficherResultat();
    }

    @Override
    public void afficherResultat() {
        // Afficher le tableau avec les résultats
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(rouleaux[i][j] + " ");
            }
            System.out.println();  // Saut de ligne après chaque ligne du tableau
        }
    }

    @Override
    public void verifierResultat() {
        // Calculer les gains et les afficher
        for (int i = 0; i < 3; i++) {
            if (rouleaux[i][0].equals(rouleaux[i][1]) && rouleaux[i][1].equals(rouleaux[i][2]) && rouleaux[i][2].equals(rouleaux[i][3])) {
                System.out.println("Gagné sur la ligne " + (i+1) + " avec " + rouleaux[i][0]);
            }
        }
    }

    public void modifMise(int nouvelleMise) {
        this.mise = nouvelleMise;
    }

    // Getter pour la mise
    public int getMise() {
        return mise;
    }

    // Getter pour le gain
    public int getGain() {
        return gain;
    }
}