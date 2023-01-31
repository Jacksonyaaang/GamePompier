package pompier;

import simulation.Simulateur;
import strategie.Chemin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cartes.Carte;
import cartes.Case;
import cartes.Direction;
import cartes.NatureTerrain;
import evenements.IntervenirRobot;
import evenements.RemplirEau;
import robots.*;

public abstract class ChefPompier {
	
	static boolean eauInfini = false;
	static protected Simulateur simulateur; 
	static protected Map<Robot, Long> ListRobotTacheDonnee= new HashMap<Robot, Long>();
	static protected ArrayList <Case> listeCaseEau = new ArrayList<Case>();
	static protected ArrayList <Case> listeCaseNextEau = new ArrayList<Case>();

	public ChefPompier(Simulateur simulateur){
		ChefPompier.simulateur = simulateur;
	}

	/**
	 * executeStrategie : est une fonction qui execute une strategie pour eteindre les feux
	 * en donnant des ordres au robots
	 * @param date : date d'execution
	 * @param simulateur : correspand au simulateur actuelle
	 * @throws Exception
	 */
	abstract public void executeStrategie(long date, Simulateur simulateur) throws Exception;


 
   /**
   * creeEvenementItervention : cree la liste des evenements qui correspand a un deplacement et ensuite
   * une intervention du robot
    * @param chemin : le chemin pris par le robot pour atteindre le feu
    * @param robot : le robot auquel on donne des ordres
    * @return
    * @throws Exception
    */
	protected long creeEvenementItervention(Chemin chemin,Robot robot) throws Exception{
		robot.CheminToEvent(chemin);
		long dateIntervention = (long) (simulateur.getDateSimulation()+ (long)chemin.getTempsTotal());
		simulateur.ajouteEvenement(new IntervenirRobot(dateIntervention, robot.getId(), simulateur));	
		return dateIntervention + 10;
	}

	/** 
	* creeEvenementRemplisage : cree la liste des evenements qui correspand a un deplacement et ensuite
	* le remplissage du robot
	 * @param chemin : le chemin pris par le robot pour atteindre l'eau
	 * @param robot : le robot auquel on donne des ordres
	 * @return
	 * @throws Exception
	 */
	protected long creeEvenementRemplisage(Chemin chemin,Robot robot) throws Exception{
		robot.CheminToEvent(chemin);
		long dateRemplisage = (long) (simulateur.getDateSimulation()+ (long)chemin.getTempsTotal());
		simulateur.ajouteEvenement(new RemplirEau(dateRemplisage, robot.getId(), simulateur));	
		return dateRemplisage + 10;
	}
	
	/**
     * Cette fonction est utilisee pour eliminer tout les robots dans ListRobotTacheDonnee
     * quand les ordres qui correspond a la strategie ont ete executees
	 * @param simulateur
	 * @throws Exception
	 */
	protected void clearDictionnary(Simulateur simulateur) throws Exception{
		for (Map.Entry<Robot, Long> entry : ListRobotTacheDonnee.entrySet()) {
			Robot key = entry.getKey();
			Long value = entry.getValue();
			if (value + 1 < simulateur.getDateSimulation()){
				ListRobotTacheDonnee.remove(key);
				break;
			}
		}
	}

	/**
	 * Cette fonction touve tout les cases qui contient de l'eau dans la carte
	 * et les stocke dans listeCaseEau
	 * @param simulateur
	 * @throws Exception
	 */
	static protected void setupCaseEau(Simulateur simulateur) throws Exception{
		Case[][] grilleCarte = simulateur.getDonneesSim().getCarte().getGrille();
		for (Case[] caseVector : grilleCarte){
			for (Case caseIter : caseVector){
				if (caseIter.getNature() == NatureTerrain.EAU)
				{
					listeCaseEau.add(caseIter);
				}
			}
		}
		try {
			setupCaseNextEau(simulateur);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * Cette fonction touve tout les cases qui ne contient de l'eau dans la carte
	 * mais ceux qui sont pres de l'eau
	 * @param simulateur
	 * @throws Exception
	 */
	static protected void setupCaseNextEau(Simulateur simulateur) throws Exception{
		Carte carte = simulateur.getDonneesSim().getCarte();
		for (Case caseIter : listeCaseEau){
				for (Direction dir : Direction.values()) {
					if (carte.voisinExiste(caseIter, dir) && carte.getVoisin(caseIter, dir).getNature() != NatureTerrain.EAU){
						listeCaseNextEau.add(carte.getVoisin(caseIter, dir));
					}
				}
		}
	}

   /**
    * Cette fonction rend les reservoirs de l'eau des robots infini
    */
	static public void rendreReservoirInfiniRobot(){
		ChefPompier.eauInfini = true;
		Robot[] robots = simulateur.getDonneesSim().getRobots();
		for (Robot robot : robots){
			robot.setReservoir(Integer.MAX_VALUE);
		}
	}
 
   /**
    * Cette fonction est appele par la fonction restart pour remettre des parametres
    * de classe a leur valeur par defauts
 * @throws Exception
    */
	static public void resetClass() throws Exception{
		if (simulateur != null){
			ListRobotTacheDonnee = new HashMap<Robot, Long>();
			setupCaseEau(simulateur);
			if (ChefPompier.eauInfini){
				rendreReservoirInfiniRobot();
			}
		}
	}

	protected Simulateur getSimulateur() {
		return simulateur;
	}

}

