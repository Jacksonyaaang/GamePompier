# GamePompier
- src: contient les package et leur classés, dans leurs dossier respectifs et ca contient aussi les fichier tests   
  -> tests/TestStrategieElementaire1.java      : Fichier test pour la stratégie elementaire 1
  -> test/TestStrategieElementaire2.java     : Fichier test pour la stratégie elementaire 2
  -> tests/TestStrategieAdvanced.java   : Fichier test pour la stratégie avancée

- cartes: quelques exemples de fichiers de donnees

- img : un dossier qui contient les images utilisee dans le projet

- lib/gui.jar: archive Java contenant les classes de l'interface graphique.

- Makefile: Contient les instruction necessaire pour compiler et tester le code

Exécution du code:
Pour exécuter le code, il faut décompresser le zip file dans un dossier. 
Ensuite il faut faire la commande make all dans le root.
Et pour effectuer les tests, vous pouvez consulter le makefile et choisir un test et ensuite faire un `make <nom_test>`  
Par exemple pour tester la stratégie 1 sur la carte 1, il suffit de faire après le make all:
`make exeTestStrategieElementaire1Map1`   
// Pour les autres stratégie il suffit de faire : 
`make exeTestStrategie<Stratname>Map<Numcarte>` 
// Numcarte est entre 1 et 4  et Stratname est {Elementaire1, Elementaire2, Advanced}

Pour créer la documntation executer cette commande:
javadoc -d doc  -classpath lib/gui.jar -sourcepath src cartes  evenements  io  pompier  robots  simulation  strategie  tests
