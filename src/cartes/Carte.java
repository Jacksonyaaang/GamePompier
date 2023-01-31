package cartes;

/**
 * <b>La classe Carte represente une carte de simulation</b>
 * <p>
 * Une carte est caracterise :
 * <ul>
 * <li>tailleCase : la taille de ses cases</li>
 * <li>nbLig: don nombre de lignes</li>
 * <li>nbCol: son nombre de colonnes</li>
 * <li>grille: sa grille de Cases</li>
 * </ul>
 * </p>
 * <p>
 * On peut, a partir d'une case, voir si une case adjacente dans une direction existe
 * et obtenir cette case.
 * </p>
 */
public class Carte {
	
	private int tailleCase;
	private int nbLig;					// nombre de lignes
	private int nbCol; 					// nombre de colonnes
	private Case[][] grille;

	/**
	 * Constructeur de Carte
	 * @param nbLignes (nombre de lignes)
	 * @param nbColonnes (nombre de colonnes)
	 * @param taille (taille des cases)
	 */
	public Carte(int nbLignes, int nbColonnes, int taille) throws Exception {
		if (nbLignes < 0 || nbColonnes < 0) {
			throw new Exception("Les nombres de lignes et colonnes doivent etre positifs");
		}
		this.tailleCase = taille;
		this.nbLig = nbLignes;
		this.nbCol = nbColonnes;
		this.grille = new Case[nbLig][nbCol];
	}
	
	public int getNbLig() {
		return this.nbLig;
	}
	
	public int getNbCol() {
		return this.nbCol;
	}
	
	public Case[][] getGrille() {
		return grille;
	}

	public void setGrille(Case[][] grille) {
		this.grille = grille;
	}

	public int getTailleCases() {
		return this.tailleCase;
	}
	
	public void setCase(int lig, int col, Case c) throws Exception {
		if (lig < 0 || lig >= this.nbLig || col >= this.nbCol || col < 0) {
			throw new Exception("Position invalide");
		}
		this.grille[lig][col] = c;
	}
	
	public Case getCase(int lig, int col) {
		return this.grille[lig][col];
	}
	
	/**
	 * Retourne vrai si la case adjacente dans la direction existe depuis la case src
	 * @param src (case d'origine)
	 * @param dir (direction evalue)
	 * @return res (boolean de l'existance du voisin)
	 */
	public boolean voisinExiste(Case src, Direction dir) {
		boolean res = true;
		switch(dir) {
			case NORD:
				res = src.getLigne() > 0;
				break;
			case SUD:
				res = src.getLigne() < this.nbLig-1;
				break;
			case OUEST:
				res = src.getColonne() > 0;
				break;
			case EST:
				res = src.getColonne() < this.nbCol-1;
				break;
			default:
				res = false;
		}
		return res;
	}
	
	/**
	 * Renvoie la case adjacente dans la direction donnee
	 * @param src (case d'origine)
	 * @param dir (direction du voisin)
	 * @return Case (case du voisin)
	 * @throws Exception (si la case demandee n'existe pas)
	 */
	public Case getVoisin(Case src, Direction dir) throws Exception {
		if (!this.voisinExiste(src,dir)) {
			throw new Exception("Le voisin n'existe pas");
		}
		int ligne = src.getLigne();
		int colonne = src.getColonne();
		
		switch(dir) {
			case NORD:
				ligne--;
				break;
			case SUD:
				ligne++;
				break;
			case OUEST:
				colonne--;
				break;
			case EST:
				colonne++;
				break;
			default:
		}
		return this.grille[ligne][colonne];
	}

}
