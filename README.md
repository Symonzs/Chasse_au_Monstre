## Lancer le jeu

Depuis le dossier du projet.
Vous pouvez lancer le jeu avec les commandes suivantes :

```bash
java --module-path "Chemin absolu de votre javafx" --add-modules javafx.controls,javafx.base,javafx.graphics -jar J1_SAE3A.jar
```

Exemple :

```bash
java --module-path "C:\\Program Files\\Java\\javafx-sdk-21.0.1\\lib\\" --add-modules javafx.controls,javafx.base,javafx.graphics -jar J1_SAE3A.jar
```

## Lancer les tests

Penser à changer le chemin des fichiers dans les tests.
Sous la forme : "C:\\Users\\...\\J1_SAE3A\\src\\main\\resources\\", "fichier.txt"