package evenements;

import cartes.Case;
import robots.Robot;
import java.util.ArrayList;
import cartes.*;
import simulation.Simulateur;
/**
* <b>La classe RemplirEau représente un evenement de remplissage pour un robot spécifique</b>
*/

public class RemplirEau extends EvenementRobot {
		
	public RemplirEau(long date, int robotId, Simulateur simulateur) throws Exception{
		super(date, robotId, simulateur);
	}

	@Override
	public void execute() throws Exception {
		try {
			Robot robot = super.getDonneesSim().getRobots()[super.getRobotId()-1];
			Case caseRemplisage = robot.getPosition();

			//Si le robot n'est pas occupée, on execute l'évenement
			if (!robot.isrobotOccupee()) {
				//Le temps total de l'évenement est égal au temps total de remplissage
				robot.setTempsEvenement(robot.getDureeRemplissage());
				occupyRobot(robot);
			}
    		//Si le robot est occupée, on décale les événements du robot jusqu'à la fin d'exécution d'événement actuel
			if (robot.isrobotOccupee() && robot.getEvenementExecution() != this) {
				incrementeEvenementRobot(robot, this);
				return;
			}
	        if (robot.getTempsEvenementCase() == 0){

				//Un robot peut se remplit de deux facons suivant sa nature : a cote d'une case qui 
				//contient de l'eau (par exemple : un robot a roue)
				//ou une case qui contient de l'eau (par exemple une drone) 
	        	if (!robot.isSeRemplitDansEau())
	        	{
	        		//Dans ce cas la on doit verifier que le robot est a cote de l'eau
					ArrayList<Case>  listCaseVoisin= new ArrayList<Case>();
					Direction directions[] = Direction.values();
					for (Direction direction : directions){
						if (super.getDonneesSim().getCarte().voisinExiste(caseRemplisage, direction)){
							listCaseVoisin.add(super.getDonneesSim().getCarte().getVoisin(caseRemplisage, direction));
						}
					} 
					boolean eauExiste = false;
					for (Case caseIter: listCaseVoisin){
						if (caseIter.getNature() == NatureTerrain.EAU){
							eauExiste = true;
						}
					}
					if (!eauExiste){
						releaseRobot(robot);
						// Il n'y pas d'eau dans cette region
						return;
					}
				}
				else
				{
					if (caseRemplisage.getNature() != NatureTerrain.EAU){
						releaseRobot(robot);
						//Il n'y pas d'eau dans cette case
						return;
					}
				}
		        if (robot.getReservoir() == robot.getReservoirMax()) {
					releaseRobot(robot);
		        	// Le reservoir du robot est plein
		        	return;
		        } 
				super.addTimeAndCallNewEvent(robot, super.getSimulateur(), this);
		        return;
	        }	 
			//Si le temps passé dans la case est supérieur ou égal à la duree de remplissage on remplit le robot
	        if (robot.getTempsEvenementCase() >= robot.getDureeRemplissage()){
				// Le reservoir du robot a ete rempli"
				releaseRobot(robot);
	        	robot.remplirReservoir();
			}
	        else{
				super.addTimeAndCallNewEvent(robot, super.getSimulateur(), this);
	        	return;
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
        	throw new Exception(e.getMessage());
        }
	}
	

	@Override
	public String toString(){
		    return "[Remplisage] Evenement Remplisage and date = " + super.getDate()  ;
		  }
}
