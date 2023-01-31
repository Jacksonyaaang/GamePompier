# Ensimag 2A POO - TP 2018/19
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testInvader testLecture testSimulateur testEvenement testEvenementExemple0 testEvenementExemple1 testTestStrategieElementaire testTestStrategieElementaire2 testTestStrategieAdvanced

strategie : testTestStrategieElementaire testTestStrategieElementaire2 testTestStrategieAdvanced

testInvader:	
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestInvader.java

testLecture:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestLecteurDonnees.java

testSimulateur:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestSimulateur.java

testEvenement:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestEvenement.java

testEvenementExemple0:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestEvenementExemple0.java

testEvenementExemple1:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestEvenementExemple1.java

testTestStrategieElementaire:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestStrategieElementaire1.java

testTestStrategieElementaire2:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestStrategieElementaire2.java

testTestStrategieAdvanced:
	javac -d bin -classpath lib/gui.jar -sourcepath src src/tests/TestStrategieAdvanced.java



# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:lib/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader
exeInvader: 
	java -classpath bin:lib/gui.jar tests/TestInvader
exeLecture: 
	java -classpath bin:lib/gui.jar tests/TestLecteurDonnees cartes/carteSujet.map
exeSimulateur: 
	java -classpath bin:lib/gui.jar tests/TestSimulateur cartes/spiralOfMadness-50x50.map
exeEvenement: 
	java -classpath bin:lib/gui.jar tests/TestEvenement cartes/carteSujet.map
exeEvenementExemple0: 
	java -classpath bin:lib/gui.jar tests/TestEvenementExemple0 cartes/carteSujet.map
exeEvenementExemple1: 
	java -classpath bin:lib/gui.jar tests/TestEvenementExemple1 cartes/carteSujet.map
 
exeTestStrategieElementaire1Map1: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire1 cartes/carteSujet.map

exeTestStrategieElementaire1Map2: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire1 cartes/desertOfDeath-20x20.map

exeTestStrategieElementaire1Map3: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire1 cartes/mushroomOfHell-20x20.map

exeTestStrategieElementaire1Map4: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire1 cartes/spiralOfMadness-50x50.map


exeTestStrategieElementaire2Map1: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire2 cartes/carteSujet.map

exeTestStrategieElementaire2Map2: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire2 cartes/desertOfDeath-20x20.map

exeTestStrategieElementaire2Map3: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire2 cartes/mushroomOfHell-20x20.map

exeTestStrategieElementaire2Map4: 
	java -classpath bin:lib/gui.jar tests/TestStrategieElementaire2 cartes/spiralOfMadness-50x50.map


exeTestStrategieAdvancedMap1: 
	java -classpath bin:lib/gui.jar tests/TestStrategieAdvanced cartes/carteSujet.map

exeTestStrategieAdvancedMap2: 
	java -classpath bin:lib/gui.jar tests/TestStrategieAdvanced cartes/desertOfDeath-20x20.map

exeTestStrategieAdvancedMap3: 
	java -classpath bin:lib/gui.jar tests/TestStrategieAdvanced cartes/mushroomOfHell-20x20.map

exeTestStrategieAdvancedMap4: 
	java -classpath bin:lib/gui.jar tests/TestStrategieAdvanced cartes/spiralOfMadness-50x50.map


clean:
	rm -rf bin/*/*.class
