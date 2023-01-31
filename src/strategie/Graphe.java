package strategie;

import cartes.Carte;
import cartes.Case;
import robots.*;

/**
 * <b>La classe graphe represente les cases de la carte par des sommets lies entre eux par des liens ponderes par le temps de parcours
 *  pour aller d'une case depart a une autre</b>
 * <p>
 * Un graphe est caracterise :
 * <ul>
 * <li>robot : une instance de robot (seul son type est utile pour connaitre les temps de parcours)</li>
 * <li>carte: la carte represente par le graphe</li>
 * <li>tempsPassage: temps necessaire pour passer d'une case a l'autre</li>
 * <li>grille: une grille de sommet pour retrouver le somet a une position</li>
 * <li>arrivee: le sommet d'arrivee a partir duquel on construit les chemins</li>
 * </ul>
 * </p>
 * <p>
 * Le graphe permet de lier des sommets entre eux ce qui permet de les parcourir
 * acceder a leur attribut notamment pour trouver des chemins 
 * </p>
 */
public class Graphe {
	
	private Robot robot;	// pour connaitre le type de robot pour le calcul des temps
	private Carte carte;
	private Sommet[][] grille;
	private Sommet arrivee;

	/**
	 * Constructeur de Graphe
	 * @param robot (pour connaitre le type de robot pour la ponderation)
	 * @param col (carte representee par le graphe)
	 */
	public Graphe(Robot robot, Carte carte) throws Exception {
		this.robot = robot;
		this.carte = carte;
		int nbLig = this.carte.getNbLig();
		int nbCol = this.carte.getNbCol();
		Case[][] grilleCase = this.carte.getGrille();
		this.grille = new Sommet[nbLig][nbCol];
		
		for (int i=0; i<nbLig; i++) {
			for (int j=0; j<nbCol; j++) {
				this.grille[i][j] = new Sommet(this.robot, grilleCase[i][j]);
			}
		}
		
		if (nbLig < 3 || nbCol <3) {
			throw new Exception("La carte est trop petite !");
		}
		
		for (int i=0; i<nbLig; i++) {
			for (int j=0; j<nbCol; j++) {
				if (i>0) {
					this.grille[i][j].addVoisin(this.grille[i-1][j]);					
				}
				if (i<nbLig-1) {
					this.grille[i][j].addVoisin(this.grille[i+1][j]);					
				}
				if (j>0) {
					this.grille[i][j].addVoisin(this.grille[i][j-1]);					
				}
				if (j<nbCol-1) {
					this.grille[i][j].addVoisin(this.grille[i][j+1]);					
				}
			}
		}
	}
	
	/**
	 * Constructeur de recopie du Graphe.
	 * On cree de nouveaux sommets qu'on lie de la meme maniere mais on ne copie pas la ponderation.
	 * Le type de robot et l'arrivee sont les memes.
	 * @param Graphe (graphe copie)
	 */
	public Graphe(Graphe copie) throws Exception {
		this.robot = copie.robot;
		this.carte = copie.carte;
		this.grille = new Sommet[copie.grille.length][copie.grille[0].length];
		
		int nbLig = this.carte.getNbLig();
		int nbCol = this.carte.getNbCol();
		for (int i=0; i<this.grille.length; i++) {
			for (int j=0; j<this.grille[0].length; j++) {
				this.grille[i][j] = new Sommet(copie.grille[i][j]);
			}
		}
		for (int i=0; i<nbLig; i++) {
			for (int j=0; j<nbCol; j++) {
				if (i>0) {
					this.grille[i][j].addVoisin(this.grille[i-1][j]);					
				}
				if (i<nbLig-1) {
					this.grille[i][j].addVoisin(this.grille[i+1][j]);					
				}
				if (j>0) {
					this.grille[i][j].addVoisin(this.grille[i][j-1]);					
				}
				if (j<nbCol-1) {
					this.grille[i][j].addVoisin(this.grille[i][j+1]);					
				}
			}
		}
		if (copie.arrivee != null) {
			this.arrivee = this.grille[copie.arrivee.getCasePosition().getLigne()][copie.arrivee.getCasePosition().getColonne()];			
		}
		else{
			this.arrivee = copie.arrivee;
		}
	}
	
	public Sommet[][] getGrille() {
		return grille;
	}

	public Sommet getArrivee() {
		return this.arrivee;
	}

	public void setArrivee(Sommet arrivee) {
		this.arrivee = arrivee;
	}
	
	public Robot getRobot() {
		return this.robot;
	}

}
