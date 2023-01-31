package strategie;

import cartes.Case;

/**
 * <b>L'interface PlusCourtChemin declare la methode a implementer pour trouver un chemin.</b>
 * <p>
 * Un algorithme implementant PlusCourtChemin doit retourner un chemin liant une case de depart a une case d'arrivee.
 * L'objectif est de respecter le patron de conception strategie pour que les robots puissent dynamiquement changer de strategie.
 * </p>
 */
public interface PlusCourtChemin {
	
	/**
	 * Renvoie un chemin liant une case de depart a une case d'arrivee
	 * @param depart (case de depart)
	 * @param arrivee (case d'arrivee)
	 */
	public Chemin calcPlusCourtChemin(Case depart, Case arrivee) throws Exception;

}
