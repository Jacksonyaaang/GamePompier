package strategie;

import java.util.HashMap;
import java.util.Map;

import cartes.*;
import robots.*;

/**
 * <b>La classe Sommet represente une case au sein d'un graphe</b>
 * <p>
 * Un sommet est caracterise :
 * <ul>
 * <li>robot : une instance de robot (seul son type est utile pour connaitre les temps de parcours)</li>
 * <li>casePosition: la case qu'il represente</li>
 * <li>sommetsVoisins: une Map des voisins avec le temps d'acces</li>
 * <li>tempsToArrivee: le temps necessaire pour aller jusque l'arrivee</li>
 * <li>nextHop: le sommet par lequel passer pour atteindre l'arrivee</li>
 * </ul>
 * </p>
 * <p>
 * Les attributs tempsToArrivee et nextHop sont utilisees lors de l'exploration des graphes
 * et de la recherche du plus court chemin.
 * Le type de robot correspond � celui du graphe auquel il appartient.
 * </p>
 */
class Sommet {
	
	private Robot robot;	// pour connaitre le type de robot pour le calcul des temps
	private Case casePosition;
	private Map <Sommet,Integer> sommetsVoisins = new HashMap<Sommet,Integer>(); // l'integer correspond au temps d'acces au voisin
	private int tempsToArrivee = Integer.MAX_VALUE;
	private Sommet nextHop;

	/**
	 * Constructeur de sommet.
	 * On cree de nouveaux sommets qu'on lie de la meme maniere mais on ne copie pas la ponderation.
	 * Le type de robot et l'arrivee sont les memes.
	 * @param casePosition (la case qu'il represente)
	 * @param robot (le type de robot du graphe auquel il appartient)
	 */
	public Sommet(Robot robot, Case casePosition) {
		this.robot = robot;
		this.casePosition = casePosition;
	}
	
	/**
	 * Constructeur de recopie du sommet.
	 * On copie la case qu'il represente ainsi que le type de robot
	 * @param copie (sommet copie)
	 * @param robot (le type de robot du graphe auquel il appartient)
	 */
	public Sommet(Sommet copie) {
		this.robot = copie.robot;
		this.casePosition = copie.casePosition;
	}
	
	/**
	 * Met a jour la valeur du temps pour atteindre l'arrivee depuis lui-meme
	 * si en passant par le sommet passe en parametre (son voisin) le temps est inferieur
	 * @param courrant: sommet voisin (en cours de traitement par l'algorithme de plus court chemin)
	 * @param tempsAuSommetCourrant: temps pour atteindre le voisin
	 * @throws Exception
	 */
	public void updateTempsToArrivee(Sommet courrant, Integer tempsAuSommetCourrant) throws Exception {
		if (!courrant.sommetsVoisins.containsKey(this)) {
			throw new Exception("Les 2 sommets ne sont pas voisins");
		}

		int tempsToArriveeCourrant = courrant.getTempsToArrivee();
		if (tempsToArriveeCourrant + tempsAuSommetCourrant > 0 && tempsToArriveeCourrant + tempsAuSommetCourrant <= this.getTempsToArrivee()) {
			this.setTempsToArrivee(tempsToArriveeCourrant + tempsAuSommetCourrant);
			this.setNextHop(courrant);
		}
	}
	
	public Map<Sommet,Integer> getSommetsVoisins() {
		return this.sommetsVoisins;
	}

	public void addVoisin(Sommet s) throws Exception {
		this.sommetsVoisins.put(s,this.robot.tempsParcAdj(this.casePosition, s.getCasePosition()));
	}
	
	public Case getCasePosition() {
		return this.casePosition;
	}

	public int getTempsToArrivee() {
		return this.tempsToArrivee;
	}

	public void setTempsToArrivee(int tempsToArrivee) {
		this.tempsToArrivee = tempsToArrivee;
	}
	
	public Sommet getNextHop() {
		return this.nextHop;
	}

	public void setNextHop(Sommet nextHop) {
		this.nextHop = nextHop;
	}
	
	public void setCasePosition(Case casePosition) {
		this.casePosition = casePosition;
	}

}
