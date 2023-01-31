package robots;

import cartes.NatureTerrain;

import java.util.ListIterator;

import cartes.*;
import evenements.*;
import simulation.Simulateur;
import strategie.Chemin;
import strategie.PlusCourtChemin;

/**
 * <b>La classe Robot represente les robots de la simulation</b>
 * <p>
 * Un robot est caracterise :
 * <ul>
 * <li>id: l'identifiant du robot</li>
 * <li>nextIdToAttribute: attribut de classe pour le prochain id a attribuer afin d'avoir des id uniques</li>
 * <li>position: la case sur laquelle se trouve le robot dans la simulation</li>
 * <li>vitessseParDEfaut: la vitesse par defaut du robot</li>
 * <li>reservoir: le volume en eau du reservoir du robot</li>
 * <li>reservoirMax: la capacite maximale du reservoir</li>
 * <li>dureeRemplissage: le temps necessaire pour remplir de facon totale et immediate le reservoir</li>
 * <li>intUnitVol: le volume d'eau deverse par intervention du robot sur un incendie</li>
 * <li>intUnitDuree: le temps d'une intervention du robot sur un incendie</li>
 * <li>incendieIntervention: l'incendie sur lequel intervient le robot</li>
 * <li>natTerrInaccessible: la liste des natures de terrain inaccessibles pour le robot</li>
 * <li>iconPath: le path vers l'image a afficher pour representer le robot</li>
 * <li>robotOccupe: un booleen pour indiquer l'etat occupe du robot</li>
 * <li>tempsEvenementCase: temps passe par le robot sur l'execution de l'evenement en cours</li>
 * <li>tempsEvenement: la duree totale de  l'evenement en cours</li>
 * <li>directionDeplacement: la direction suivant laquelle se deplace a l'instant le robot</li>
 * <li>seRemplitDansEau: un booleen indiquant si le robot se remplit directement sur l'eau ou sur les berges</li>
 * <li>simulateur: la simulation a laquelle appartient le robot</li>
 * </ul>
 * </p>
 * <p>
 * Robot est une classe abstraite heritee par tous les types de robot 
 * </p>
 */
public abstract class Robot {

	protected PlusCourtChemin strategie;
	
	public static int nextIdToAttribute = 1;
	private int id;
	protected Case position;
	protected int vitesseParDefaut; // km/h
	protected int reservoir = 0; // litres
	protected int reservoirMax; // litres
	protected int dureeRemplissage; // min
	protected int intUnitVol; // litres volume unitaire d'intervention
	protected int intUnitDuree; /// seconde duree unitaire d'intervention

	private Incendie incendieIntervention;
	protected NatureTerrain [] natTerrInaccessible;
	protected String iconPath;
	protected boolean robotOccupee;
	protected int tempsEvenementCase = 0;
	protected int tempsEvenement = 0;
	protected Evenement EvenementExecution;
	protected Direction directionDeplacement;
	protected boolean seRemplitDansEau;
	protected Simulateur simulateur;

	/**
	 * Constructeur du robot
	 * @param pos (la position du robot)
	 * @param vit (la vitesse du robot)
	 */
	public Robot(Case pos, int vit) throws Exception {
		if (vit < 0) {
			throw new Exception("Vitesse negative");
		}
		this.position = pos;
		this.vitesseParDefaut = vit;
		this.id = attributeId();
	}
	
	/**
     * attribue un id au robot
	 */
	private int attributeId() {
		int id = Robot.nextIdToAttribute;
		Robot.nextIdToAttribute++;
		return id;
	}

	public Case getPosition() {
		return this.position;
	}

	public void setPosition(Case pos) {
		this.position = pos;
	}

	/**
	 * deplace le robot sur une nouvelle case
	 * @param nouvPos (la nouvelle position du robot)
	 */
	public void deplacer(Case nouvPosition) throws Exception {
		if (this.caseAccessible(nouvPosition)) {
			this.position = nouvPosition;
		} else {
			throw new Exception("Case inaccessible");
		}
	}

	/**
	 * verifie si la case est accessible pour le robot suivant la nature du terrain
	 * @param c (la case d'interet)
	 * @return l'accessibilite
	 */
	public boolean caseAccessible(Case c) {
		NatureTerrain natCase = c.getNature();
		if (natTerrInaccessible == null){
			return true;
		}
		for (NatureTerrain nat : natTerrInaccessible) {
			if (nat == natCase)
			{
				return false;
			}		
		}
		return true;
	}

	public abstract int getVitesse(NatureTerrain nature);
	
	public int getVitesse() {
		return this.getVitesse(this.position.getNature());
	}

	public int getReservoir() {
		return this.reservoir;
	}

	/**
	 * diminue le volume d'eau dans le reservoir du robot
	 * @param volume (vooume a deverser)
	 */
	public void deverserEau(int volume) throws Exception {
		if (volume < 0) {
			throw new Exception("Volume a deverser negatif");
		}
		if (volume > this.reservoir) {
			throw new Exception("Volume a deverser superieur au reservoir courant");
		}
		int nbInterventions = (int) (Math.ceil((double) (volume) / intUnitVol));
		this.reservoir -= volume * nbInterventions;
	}

	/**
	 * calcul le temps necessaire pour le robot pour passer d'une case a l'autre adjacente entre elles
	 * @param depart (la case de depart)
	 * @param arrivee (la case d'arrivee)
	 */
	public int tempsParcAdj(Case depart, Case arrivee) throws Exception {
		int ligDep = depart.getLigne();
		int colDep = depart.getColonne();
		int ligArr = arrivee.getLigne();
		int colArr = arrivee.getColonne();
		int temps;
		
		if (this.natTerrInaccessible != null) {
			for (NatureTerrain nat : this.natTerrInaccessible) {
				if (arrivee.getNature() == nat || depart.getNature() == nat) {
					return Integer.MAX_VALUE;
				}
			}			
		}

		if (((ligDep==ligArr) && (Math.abs(colDep-colArr)==1)) 
				|| ((colDep==colArr) && (Math.abs(ligDep-ligArr)==1))) {
			
			int TailleCase = simulateur.getDonneesSim().getCarte().getTailleCases();
			temps = (int)( Math.ceil(((double)TailleCase/2)/((double)getVitesse(depart.getNature()))+
							((double)TailleCase/2)/((double)getVitesse(arrivee.getNature()))) * 3600/1000);
					} else {
			throw new Exception("Cases non-adjacentes");
		}
		return temps;		
	}
	
	/**
	 * calcul le temps necessaire pour le robot pour passer de sa position courrante a une case adjacente
	 * @param arrivee (la case d'arrivee)
	 */
	public int tempsParcAdj(Case arrivee) throws Exception {
	 	return tempsParcAdj(this.position, arrivee);
	 }

	public void setSim(Simulateur sim) {
		this.simulateur = sim;
	}
	
	/**
	 * transforme un chemin en une serie d'evenement pour le robot
	 * @param c (chemin a parcourir)
	 */
	public void CheminToEvent(Chemin c) throws Exception {
		if (this.position != c.getDepart()) {
			throw new Exception("Chemin incoherent avec la position du robot");
		}
		else {
			if (!verifiyListEventNull(this)){
				return;
			}
			Case precedente = null;
			ListIterator itParc = c.getParcours().listIterator();
			ListIterator itTempsPass = c.getTempsPassage().listIterator();
			// start from the second element of the list
			int tempsCumule = 0;
			if (itParc.hasNext() && itTempsPass.hasNext()) {
				precedente = (Case)itParc.next();
				tempsCumule += (Integer)itTempsPass.next();
			}
			long dateDebutEvent = this.simulateur.getDateSimulation();
			while (itParc.hasNext() && itTempsPass.hasNext()) {
				Case suivante = (Case)itParc.next();
				Direction dir = precedente.getDirection(suivante);
				this.simulateur.ajouteEvenement(new DeplacerRobot(dateDebutEvent,
						this.id, dir, this.simulateur));
				tempsCumule += (Integer)itTempsPass.next();
				dateDebutEvent = this.simulateur.getDateSimulation() + tempsCumule;
				precedente = suivante;
			}
		}
	}
	
	/**
	 * Calcul pour aller du depart a l'arrivee avec sa strategie
	 * @param depart (case de depart)
	 * @param arrivee (case de arrivee)
	 * @throws Exception
	 * On peut recuperer le temps total de parcours avec chemin.getTempsTotal()
	 */
	public Chemin calcChemin(Case depart, Case arrivee) throws Exception {
		if (this.natTerrInaccessible != null) {
			for (NatureTerrain nat : this.natTerrInaccessible) {
				if (arrivee.getNature() == nat) {
					return null;
				}
			}			
		}
		return this.strategie.calcPlusCourtChemin(depart, arrivee);
	}
	
	/**
	 * Calcul pour aller de la position courrante du robot a l'arrivee avec sa strategie
	 * @param arrivee (case de arrivee)
	 * @throws Exception
	 * On peut recuperer le temps total de parcours avec chemin.getTempsTotal()
	 */
	public Chemin calcChemin(Case arrivee) throws Exception {
		return calcChemin(this.position, arrivee);
	}

	/**
	 * verifiyListEventNull verifie que le robot n'a pas ete deja ordonne pour s'assuer 
	 * que l'appel ne se fait pas deux fois
	 */
	public boolean verifiyListEventNull(Robot robot){
		for (Evenement event : simulateur.getListeEvenement()){
			if (event instanceof EvenementRobot && ((EvenementRobot) event).getRobotId() == this.id){
				return false;
			}
		}
		return true;
	}

	/**
	 * remet le compteur d'id a sa valeur initiale pour donner a nouveau des id aux robots
	 * en commencant par 1 pour le reset de la simulation
	 */
	public static void resetId() {
		Robot.nextIdToAttribute = 1;
	}

	public void setStrategie(PlusCourtChemin strategie) {
		this.strategie = strategie;
	}

	public void remplirReservoir() {
		this.reservoir = reservoirMax;
	}

	public abstract String getIconPath();
	
	public int getIntUnitVol() {
		return intUnitVol;
	}

	public void setIntUnitVol(int intUnitVol) {
		this.intUnitVol = intUnitVol;
	}

	public int getIntUnitDuree() {
		return intUnitDuree;
	}

	public void setIntUnitDuree(int intUnitDuree) {
		this.intUnitDuree = intUnitDuree;
	}

	public int getReservoirMax() {
		return reservoirMax;
	}

	public void setReservoirMax(int reservoirMax) {
		this.reservoirMax = reservoirMax;
	}

	public void setReservoir(int reservoir) {
		this.reservoir = reservoir;
	}

	public int getDureeRemplissage() {
		return dureeRemplissage;
	}

	public void setDureeRemplissage(int dureeRemplissage) {
		this.dureeRemplissage = dureeRemplissage;
	}

	public Incendie getIncendieIntervention() {
		return incendieIntervention;
	}

	public void setIncendieIntervention(Incendie incendieIntervention) {
		this.incendieIntervention = incendieIntervention;
	}
	
	public boolean isrobotOccupee() {
		return robotOccupee;
	}

	public void setRobotOccupee(boolean robotOccupee) {
		this.robotOccupee = robotOccupee;
	}

	public Direction getDirectionDeplacement() {
		return directionDeplacement;
	}

	public void setDirectionDeplacement(Direction directionDeplacement) {
		this.directionDeplacement = directionDeplacement;
	}

	public Evenement getEvenementExecution() {
		return EvenementExecution;
	}

	public void setEvenementExecution(Evenement EvenementExecution) {
		this.EvenementExecution = EvenementExecution;
	}

	public NatureTerrain[] getNatTerrInaccessible() {
		return natTerrInaccessible;
	}

	public void setNatTerrInaccessible(NatureTerrain[] natTerrInaccessible) {
		this.natTerrInaccessible = natTerrInaccessible;
	}

	public int getTempsEvenementCase() {
		return tempsEvenementCase;
	}

	public void setTempsEvenementCase(int tempsEvenementCase) {
		this.tempsEvenementCase = tempsEvenementCase;
	}
	
	public boolean isSeRemplitDansEau() {
		return seRemplitDansEau;
	}

	public void setSeRemplitDansEau(boolean seRemplitDansEau) {
		this.seRemplitDansEau = seRemplitDansEau;
	}

	public int getTempsEvenement() {
		return tempsEvenement;
	}

	public void setTempsEvenement(int tempsEvenement) {
		this.tempsEvenement = tempsEvenement;
	}

	public int getId() {
		return id;
	}
	

}
