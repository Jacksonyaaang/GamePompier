package pompier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cartes.*;
import robots.*;
import simulation.*;
import strategie.*;

public class ChefPompierAdvanced extends ChefPompier{
    
	protected Map<Robot, Chemin> ListRobotCheminIntervention= new HashMap<Robot, Chemin>();
	protected Map<Case, Chemin> ListCaseCheminRemplisage= new HashMap<Case, Chemin>();
    /**
    * Dans cette strategie, le reservoir des robots n'est pas infini, on instancie le chef pompier
    * et on appelle la fonction qui calcule tous les cases qui contient de l'eau et tout les cases pres de l'eau
    * @param simulateur
     * @throws Exception
    */
    public ChefPompierAdvanced(Simulateur simulateur) throws Exception{
		super(simulateur);
		super.setupCaseEau(simulateur);
	}

	@Override
	public void executeStrategie(long date, Simulateur simulateur) throws Exception {
		clearDictionnary(simulateur);		
 
       /**
        * La strategie est la suivante :
        * On choisit un incendie quelconque, et on cherche le robot le plus proche et lui donne l'ordre d'intervenir
        * si son reservoir n'est pas vide et il n'est pas occupee
        * Si le robot n'est pas occupee et reservoir est vide, on lui l'ordre pour qui se remplit
        */
		if (simulateur.getDonneesSim().getRobots().length == ListRobotTacheDonnee.size() ){
			return;
		}
		Incendie[] incendies = simulateur.getDonneesSim().getIncendies();
		Robot[] robots = simulateur.getDonneesSim().getRobots();
		for (Incendie incendie : incendies) {
			for (Robot robot : robots){
				if (!robot.isrobotOccupee() && robot.getReservoir()!=0 && !ListRobotTacheDonnee.containsKey(robot)){
					calculeCheminEntreDeuxCaseEtStockeCheminIntervention(robot ,robot.getPosition(), incendie.getPosition());
				}
				else if (robot.getReservoir() == 0 && !robot.isrobotOccupee() && !ListRobotTacheDonnee.containsKey(robot)){
					chercheEauEtRemplisage(robot, simulateur);
				}
			}
		Robot robotOptimale = ChercheRobotOptimale();
		if (robotOptimale != null){
			long dateEventfinal = creeEvenementItervention(ListRobotCheminIntervention.get(robotOptimale), robotOptimale);
			ListRobotTacheDonnee.put(robotOptimale, dateEventfinal+2);
		}
		ListRobotCheminIntervention = new HashMap<Robot, Chemin>();
		}
	}
	
   /**
    * Cette methode appelle la fonction qui calcule le plus court chemin entre le robot et la case destination
    * et elle stocke le calcul realisee dans un dictionnaire qui relie le robot avec le chemin calculee
    * @param robot
    * @param caseRobot
    * @param CaseDestination
 * @throws Exception
    */
	public void calculeCheminEntreDeuxCaseEtStockeCheminIntervention(Robot robot ,Case caseRobot, Case CaseDestination) throws Exception{
		Chemin chemin = robot.calcChemin(caseRobot, CaseDestination);
		if (chemin == null){
			return;
		}
		ListRobotCheminIntervention.put(robot, chemin);
	}

   /**
    * Parcours la liste des chemin entre une incendie et tous les robots
    * et retourne le robot le plus proche de l'incendie
    * @return le robot le plus proche de l'incendie
    */
	public Robot ChercheRobotOptimale(){
		int tempsMinimal = Integer.MAX_VALUE;
		Robot robotOptimale = null;
		for (Map.Entry<Robot, Chemin> entry : ListRobotCheminIntervention.entrySet()) {
			Robot key = entry.getKey();
			int value = entry.getValue().getTempsTotal();
			if (value <= tempsMinimal){
				robotOptimale = key;
				tempsMinimal = value;
			}
		}
		return robotOptimale;
	}

   /**
    * Cette fonction est utilisee pour chercher une zone d'eau qui est accessible et la plus proche
    * @param robot
    * @param simulateur
    * @throws Exception
    */
	public void chercheEauEtRemplisage(Robot robot, Simulateur simulateur) throws Exception{

		ArrayList <Case> listeToIterInto ;
		if (robot.isSeRemplitDansEau()){
			listeToIterInto = listeCaseEau;
		}
		else{
			listeToIterInto = listeCaseNextEau;
		}
		if (listeToIterInto.size() ==0){
			return;
		}
		for (Case caseIter : listeToIterInto){
			calculeCheminEntreDeuxCaseEtStockeCheminRemplisage(robot , robot.getPosition(), caseIter);
		}
		Case caseOptimal = ChercheCaseOptimale();
		if (caseOptimal != null){
			long dateEventfinal = creeEvenementRemplisage(ListCaseCheminRemplisage.get(caseOptimal), robot);
			ListRobotTacheDonnee.put(robot, dateEventfinal + 2);
		}
		ListCaseCheminRemplisage= new HashMap<Case, Chemin>();
	}
  	
	/**
    * Cette methode appelle la fonction qui calcule le plus court chemin entre le robot et une case d'eau
    * et elle stocke le calcule realisee dans un dictionnaire qui relie la case d'eau avec le chemin calculee.
    * @param robot
    * @param caseRobot
    * @param CaseDestination
	 * @throws Exception
    */
	public void calculeCheminEntreDeuxCaseEtStockeCheminRemplisage(Robot robot ,Case caseRobot, Case CaseDestination) throws Exception{
		Chemin chemin = robot.calcChemin(caseRobot, CaseDestination);
		if (chemin == null){
			return;
		}
		ListCaseCheminRemplisage.put(CaseDestination, chemin);
	}

	/**
	 * @return 	la case d'eau la plus proche et accesible pour un robot specifique
	 */
	public Case ChercheCaseOptimale(){
		int tempsMinimal = Integer.MAX_VALUE;
		Case caseOptimal = null;
		for (Map.Entry<Case, Chemin> entry : ListCaseCheminRemplisage.entrySet()) {
			Case key = entry.getKey();
			int value = entry.getValue().getTempsTotal();
			if (value <= tempsMinimal){
				caseOptimal = key;
				tempsMinimal = value;
			}
		}
		return caseOptimal;
	}




}

    
