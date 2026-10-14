# Casino Magnifico — projet POO (Java)

Une petite application console en Java qui simule un casino avec plusieurs machines (MagicMoolah, AllIn, Roulette). Le jeu est orienté "POO" : chaque machine implémente une interface commune, un joueur possède un niveau, de l'expérience et un solde de jetons. Le jeu se lance depuis la classe `Main`.

## Fonctionnalités
- Création d'un joueur (nom + choix de difficulté qui détermine le solde de départ).
- Trois machines de jeu :
  - MagicMoolah : machine à sous avec symboles, probabilités et tirages simples ou 20 tirages consécutifs.
  - AllIn : mini-jeu basé sur des opérations et une difficulté modifiant le calcul des gains.
  - Roulette : roulette avec paris sur numéro, couleur, zéro, ou combinaison numéro+couleur.
- Système d'expérience et de niveaux : gagner de l'expérience en jouant, montée en niveau automatique.
- Interface console colorée (séparateurs/affichages via la classe `Affichage`).

## Stack
- Langage : Java (code en package par défaut — fichiers `.java` dans `Code/`)
- Cible d'exécution : Java SE (Java 8+ recommandé, Java 11+ conseillé)

## Arborescence importante
```text
Code/                Java source et classes compilées
  Main.java          Point d'entrée (initialise joueur et lance le menu du casino)
  Casino.java        Menu global, orchestre les machines
  Joueur.java        Modélise le joueur (nom, niveau, expérience, solde)
  Affichage.java     Constantes pour couleur / formatage console
  MagicMoolah.java   Machine à sous (tirage, gains, événements)
  AllIn.java         Machine "AllIn" (mini-jeu avec difficulté)
  Roulette.java      Machine Roulette (paris, tours, résultats)
  *.class            Classes compilées (générées par javac)
DiapoPoo.pdf         Présentation / diaporama (ressource)
RapportConception/   (dossier de rapport de conception)
Trame/               (trame / documents)
diagrameuml.txt      Diagramme UML (texte)
LienDiapo.md         Lien / note vers le diaporama
```

## Comment compiler et lancer (rapide)
Prérequis : JDK installé (Java 11 recommandé). Ouvrez un terminal à la racine du projet.

1. Compiler :
   ```bash
   javac Code/*.java
   ```
   Les fichiers `.class` seront créés dans `Code/`.

2. Lancer :
   ```bash
   java -cp Code Main
   ```
   Remarque : les classes sont dans le package par défaut, donc le classpath pointe sur `Code/`.

Si vous préférez compiler dans un répertoire séparé :
```bash
javac -d out Code/*.java
java -cp out Main
```

## Exemples d'utilisation
- Au lancement, le programme demande le nom du joueur puis la difficulté (solde de départ : 100000 / 10000 / 1000).
- Ensuite, le menu du casino propose : MagicMoolah, Roulette, AllIn, afficher infos, quitter.
- Suivez les prompts (nombre pour choix, saisies de mise, oui/non pour rejouer). Les entrées invalides sont gérées et redemandées.

## Points d'attention & suggestions d'amélioration
- Le code utilise le package par défaut (sans déclaration `package`). Pour un projet plus grand, ajouter des packages (`fr.example.casino`) aiderait la gestion et les builds.
- Ajouter un gestionnaire de dépendances / build (Maven ou Gradle) faciliterait la compilation et l'exécution, et permettrait d'ajouter des tests.
- Tests unitaires : ajouter JUnit pour tester les calculs de gains / logique métier.
- Internationalisation : actuellement tout est en français ; prévoir des ressources pour d'autres langues si nécessaire.
- Séparation UI / logique : la logique métier mélange affichage console et calculs ; extraire la logique pure faciliterait le test unitaire.
- création d'interface graphique

## Développement local
- Recommandé : installer un IDE (IntelliJ IDEA, Eclipse, VS Code + extension Java).
- Pour adopter Maven :
  - Créez un `pom.xml`, placez les sources sous `src/main/java`, tests sous `src/test/java`.
  - Ajouter JUnit comme dépendance pour écrire des tests automatisés.

