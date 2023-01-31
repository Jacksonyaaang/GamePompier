package tests;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

import gui.GUISimulator;
import io.*;
import simulation.Simulateur;
import evenements.*;
import cartes.*;

public class TestEvenementExemple0 {
	
	public static void main(String[] args) throws Exception {
		
		DonneesSimulation donneesSim = null;

        if (args.length < 1) {
            System.out.println("Syntaxe: java TestLecteurDonnees <nomDeFichier>");
            System.exit(1);
        }

        try {
            donneesSim = LecteurDonnees.lire(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("fichier " + args[0] + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + args[0] + " invalide: " + e.getMessage());
        }
        
        int tailleGrille = 800;
        // cree la fenetre graphique dans laquelle dessiner
        GUISimulator gui = new GUISimulator(tailleGrille, tailleGrille, Color.BLACK);
        // cree la grille en l'associant a la fenetre graphique precedente
        Simulateur simulateur = new Simulateur(gui,donneesSim);
        Direction dirNord = Direction.NORD;
        Direction dirOuest = Direction.OUEST;
        Direction dirEst = Direction.EST;
        Direction dirSud = Direction.SUD;
        int date = 1;
        simulateur.ajouteEvenement(new DeplacerRobot(date + 1, 1, dirNord, simulateur));
        simulateur.ajouteEvenement(new DeplacerRobot(date + 2, 1, dirNord, simulateur));
        simulateur.ajouteEvenement(new DeplacerRobot(date + 3, 1, dirNord, simulateur));
        simulateur.ajouteEvenement(new DeplacerRobot(date + 4, 1, dirNord, simulateur));	    

	}

}
