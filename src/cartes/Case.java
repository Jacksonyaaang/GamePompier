package cartes;

/**
 * <b>La classe Case represente une case de la carte de simulation</b>
 * <p>
 * Une case est caracterise :
 * <ul>
 * <li>ligne: sa ligne dans la carte</li>
 * <li>colonne: sa colonne dans la carte</li>
 * <li>nature: sa nature de terrain</li>
 * </ul>
 * </p>
 */
public class Case {
	
	private int ligne;
	private int colonne;
	private NatureTerrain nature;

	/**
	 * Constructeur de Case
	 * @param lig (ligne de la case)
	 * @param col (colonne de la case)
	 * @param nat (nature de la case)
	 */
	public Case(int lig, int col, NatureTerrain nat) throws Exception {
		if (lig < 0 || col < 0) {
			throw new Exception("Les nombres de lignes et colonnes doivent etre positifs");
		}
		this.ligne = lig;
		this.colonne = col;
		this.nature = nat;
	}
	
	public int getLigne() {
		return this.ligne;
	}

	public int getColonne() {
		return this.colonne;
	}

	public NatureTerrain getNature() {
		return this.nature;
	}
	
	/**
	 * Retourne la direction par rapport a elle meme dans laquelle se trouve la case donnee
	 * @param arrivee (case a localiser)
	 * @return dir (direction de la case)
	 */
	public Direction getDirection(Case arrivee) throws Exception {
		Direction dir = null;
		if (arrivee.getLigne() > this.ligne) {
			dir = Direction.SUD;
		} else if (arrivee.getLigne() < this.ligne) {
			dir = Direction.NORD;
		} else if (arrivee.getColonne() < this.colonne) {
			dir = Direction.OUEST;
		} else if (arrivee.getColonne() > this.colonne) {
			dir = Direction.EST;

		} else {
			throw new Exception("Direction non trouvee");
		}
		return dir;
	}

		
	@Override
	public String toString(){
		    return "[Case][X = " + this.getLigne() + " Y = " + this.getColonne() + " ]"  ;
		  }

}
