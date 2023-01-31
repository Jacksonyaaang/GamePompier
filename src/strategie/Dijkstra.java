package strategie;

import robots.*;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


import cartes.Case;

/**
 * <b>La classe Dijkstra represente une strategie d'obtention du plus court chemin</b>
 * <p>
 * Un algorithme de Dijkstra est caracterise :
 * <ul>
 * <li>robot : une instance de robot (seul son type est important)</li>
 * <li>grapheInitial: un graphe pondere par les temps de parcours du type de robot en attribut</li>
 * <li>memoGraphe: une collection de graphe associes a une case d'arrivee</li>
 * </ul>
 * </p>
 * <p>
 * La classe permet pour un certain type de robot de retourner le plus court chemin entre 2 cases.
 * L'algorithme explore le graphe initial en partant de la case d'arrivee puis construit le plus court chemin a partir du graphe explore.
 * Chaque exploration se fait sur une copie du graphe initial.
 * On memorise les graphes explore dans memoGraphe pour ne pas refaire les explorations. 
 * </p>
 */
public class Dijkstra implements PlusCourtChemin {
	
	private final Robot robot;	// pour connaitre le type de robot
	private Graphe grapheInitial;
	private HashMap<Case, Graphe> memoGraphes = new HashMap<Case,Graphe>(); // memorisation des calculs precedents
	
	public Dijkstra(Graphe graphe) {
		this.grapheInitial = graphe;
		this.robot = graphe.getRobot();
	}
	
	/**
	 * Retourne le plus court chemin pour le type de robot de l'instance de Dijkstra.
	 * Utilise la memorisation des graphes explores.
	 * Implemente PlusCourtChemin
	 * Si l'arrivee est inaccessible alors le chemin aura un temps de parcours de la valeur maximale d'un int.
	 * @param depart (case du robot)
	 * @param arrivee (case objectif)
	 * @return chemin (plus court chemin entre le depart et l'arrivee)
	 * @throws Exception
	 */	@Override
	public Chemin calcPlusCourtChemin(Case depart, Case arrivee) throws Exception {
		if (this.memoGraphes.containsKey(arrivee)) {
			Sommet sommetArriveeDejaCalcule = this.memoGraphes.get(arrivee).getGrille()[depart.getLigne()][depart.getColonne()];
			return construireChemin(this.memoGraphes.get(arrivee),sommetArriveeDejaCalcule);
		}
		else if (this.memoGraphes.containsKey(depart)) {
			Chemin chemin = calcPlusCourtChemin(arrivee,depart);
			chemin.inverseChemin();
			return chemin;	
		}
		// copie du graphe
		Graphe grapheDeCalcul = null;
		try {
			grapheDeCalcul = new Graphe(this.grapheInitial);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("[Dijkstra]Erreur dans la copie de graphe  ");
		}
		Sommet sommetArriveePasCalcule = grapheDeCalcul.getGrille()[arrivee.getLigne()][arrivee.getColonne()];
		sommetArriveePasCalcule.setTempsToArrivee(0);
		grapheDeCalcul.setArrivee(sommetArriveePasCalcule);
		
		Set<Sommet> sommetsAExplorer = new HashSet<Sommet>();
	    Set<Sommet> sommetsExplores = new HashSet<Sommet>();
	    
	    sommetsAExplorer.add(sommetArriveePasCalcule);
		
		while (!sommetsAExplorer.isEmpty()) {
			Sommet sommetCourrant = getSommetPlusProche(sommetsAExplorer);
			sommetsAExplorer.remove(sommetCourrant);
			sommetsExplores.add(sommetCourrant);
			for (Map.Entry<Sommet, Integer> voisinEntry : sommetCourrant.getSommetsVoisins().entrySet()) {
				Sommet voisin = voisinEntry.getKey();
				Integer tempsVoisin = voisinEntry.getValue();
				if (!sommetsExplores.contains(voisin)) {
					voisin.updateTempsToArrivee(sommetCourrant,tempsVoisin);
					sommetsAExplorer.add(voisin);
				}
			}
		}
		this.memoGraphes.put(arrivee,grapheDeCalcul);
		Chemin chemin = construireChemin(grapheDeCalcul,
				grapheDeCalcul.getGrille()[depart.getLigne()][depart.getColonne()]);
		return chemin;
	}
	
	/**
	 * Retourne le sommet le plus proche de l'arrivee parmi la collection.
	 * Leur distance a l'arrivee a deja ete calculee
	 * @param sommetsAExplorer (Set des sommets a explo)
	 * @return sommetPlusProche (Sommet le plus proche de l'arrivee)
	 */
	private static Sommet getSommetPlusProche(Set <Sommet> sommetsAExplorer) {
	    Sommet sommetPlusProche = null;
	    int minTemps = Integer.MAX_VALUE;
	    for (Sommet s : sommetsAExplorer) {
	    	int tempsSommet = s.getTempsToArrivee();
	    	if (tempsSommet <= minTemps) {
	    		minTemps = tempsSommet;
	    		sommetPlusProche = s;
	    	}
	    }
	    return sommetPlusProche;
	}
	
	/**
	 * renvoi le chemin a partir d'un sommet depart et d'un graphe explore qui possede une arrivee
	 * @param depart (Sommet de depart)
	 * @return g (Graphe explore)
	 * @throws Exception
	 */
	private Chemin construireChemin(Graphe g, Sommet depart) throws Exception {
		Chemin chemin = new Chemin(this.robot,depart.getCasePosition());
		Sommet sommetCourrant = depart;
		while (sommetCourrant.getTempsToArrivee() != 0) {
			sommetCourrant = sommetCourrant.getNextHop();
			if (sommetCourrant == null){
				return null;
			}
			chemin.addCase(sommetCourrant.getCasePosition());
			
		}
		return chemin;
	}
	
}

