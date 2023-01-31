package io;


import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

import cartes.*;
import robots.*;



/**
 * Lecteur de cartes au format spectifie dans le sujet.
 * Les donnees sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affiches.
 * A noter: pas de verification semantique sur les valeurs numeriques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des methodes, inspirees de celles presentes
 * (ou non), qui CREENT les objets au moment adequat pour construire une
 * instance de la classe DonneesSimulation a partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une methode qui cree et retourne un objet
 * contenant toutes les donnees lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des methode creeCase(), creeRobot(), ... qui lisent les donnees,
 * creent les objets adequats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {


    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est methode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     * @return donneesSim donnees de simulation
     */
    public static DonneesSimulation lire(String fichierDonnees) throws FileNotFoundException, DataFormatException 
    {
        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        DonneesSimulation donneesSim = new DonneesSimulation();
        donneesSim.setCarte(lecteur.lireCarte());
        donneesSim.setIncendies(lecteur.lireIncendies(donneesSim));
        donneesSim.setRobots(lecteur.lireRobots(donneesSim));
        donneesSim.setFichierDonnees(fichierDonnees);
        scanner.close();
        System.out.println("\n == Lecture terminee");
        return donneesSim;
    }




    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte lireCarte() throws DataFormatException {
        ignorerCommentaires();
        Carte carteSim = null;
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();	// en m
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);
            
            carteSim = new Carte(nbLignes,nbColonnes,tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    carteSim.setCase(lig, col, lireCase(lig,col));
                }
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        } catch (Exception e) {
        	throw new DataFormatException(e.getMessage());
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
        return carteSim;
    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        Case caseLue = null;
        System.out.print("Case (" + lig + "," + col + "): ");
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);

            verifieLigneTerminee();

            System.out.print("nature = " + chaineNature);
            caseLue = new Case(lig,col,NatureTerrain.stringToNature(chaineNature));

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        } catch (Exception e) {
        	throw new DataFormatException("format de nature de terrain invalide");
        }

        System.out.println();
        return caseLue;
    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private Incendie[] lireIncendies(DonneesSimulation donneesSim) throws DataFormatException {
        ignorerCommentaires();
        Incendie[] incendies = null;
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            incendies = new Incendie[nbIncendies];
            for (int i = 0; i < nbIncendies; i++) {
                incendies[i] = lireIncendie(i,donneesSim);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
        return incendies;
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private Incendie lireIncendie(int i, DonneesSimulation donneesSim) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");
        Incendie incendieLu = null;

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);
            
            incendieLu = new Incendie(donneesSim.getCarte().getCase(lig, col), intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        } catch (Exception e) {
        	throw new DataFormatException(e.getMessage());
        }
        return incendieLu;
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private Robot[] lireRobots(DonneesSimulation donneesSim) throws DataFormatException {
        ignorerCommentaires();
        Robot[] robots = null;
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("Nb de robots = " + nbRobots);
            robots = new Robot[nbRobots];
            for (int i = 0; i < nbRobots; i++) {
                robots[i] = lireRobot(i,donneesSim);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
        return robots;
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private Robot lireRobot(int i, DonneesSimulation donneesSim) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");
        Robot robotLu = null;
        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            System.out.print("\t type = " + type);


            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");
            
            switch(type) {
            case "DRONE":
                if (s == null) {
                    System.out.print("valeur par defaut");
                    robotLu = new Drone(donneesSim.getCarte().getCase(lig, col));
                } else {
                    int vitesse = Integer.parseInt(s);
                    System.out.print(vitesse);
                    robotLu = new Drone(donneesSim.getCarte().getCase(lig, col), vitesse);
                }
            	break;
            case "ROUES":
                if (s == null) {
                    System.out.print("valeur par defaut");
                    robotLu = new RobotARoues(donneesSim.getCarte().getCase(lig, col));
                } else {
                    int vitesse = Integer.parseInt(s);
                    System.out.print(vitesse);
                    robotLu = new RobotARoues(donneesSim.getCarte().getCase(lig, col), vitesse);
                }
            	break;
            case "PATTES":
                System.out.print("valeur par defaut");
                robotLu = new RobotAPattes(donneesSim.getCarte().getCase(lig, col));
            	break;
            case "CHENILLES":
                if (s == null) {
                    System.out.print("valeur par defaut");
                    robotLu = new RobotAChenilles(donneesSim.getCarte().getCase(lig, col));
                } else {
                    int vitesse = Integer.parseInt(s);
                    System.out.print(vitesse);
                    robotLu = new RobotAChenilles(donneesSim.getCarte().getCase(lig, col), vitesse);
                }
            	break;
            default:	
            }
            verifieLigneTerminee();

            System.out.println();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        } catch (Exception e) {
        	throw new DataFormatException(e.getMessage());
        }
        return robotLu;
    }




    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
