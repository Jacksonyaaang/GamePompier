package evenements;

import robots.*;
import simulation.Simulateur;

import javax.swing.text.Position;

import cartes.*;
import cartes.Direction;

/**
* <b>La classe DeplacerRobot représente un evenement de deplacement dans une direction spécifique</b>
* <li>direction : la direction de deplacement </li>
*/
public class DeplacerRobot extends EvenementRobot {

	protected Direction direction;

	public DeplacerRobot(long date, int robotId, Direction direction, Simulateur simulateur) throws Exception{
		super(date, robotId, simulateur);
		this.direction = direction;
	}

	@Override
	public void execute() throws Exception {
		try {
			Robot robot = super.getDonneesSim().getRobots()[super.getRobotId()-1];
			Case nouvelleCase= null;	
			nouvelleCase = super.getDonneesSim().getCarte().getVoisin(robot.getPosition(), direction);
			//Si le robot n'est pas occupée, on execute l'évenement
			if (!robot.isrobotOccupee()) {
				//On calcule le temps necessaire pour faire le deplacement et on le stocke dans TempsEvenement
				//qui represente le temps necessaire pour ce evenement 
				robot.setTempsEvenement(robot.tempsParcAdj(robot.getPosition(), nouvelleCase));
				occupyRobot(robot);
				//TempsEvenementCase represente le temps passé dans la case
				robot.setDirectionDeplacement(direction);
			}
			
    			//Si le robot est occupée, on décale les événements du robot jusqu'à la fin d'exécution d'événement actuel		
				if (robot.isrobotOccupee() && robot.getEvenementExecution() != this) {
				incrementeEvenementRobot(robot, this);
			    return;
			}

			//Si on passe dans la case plus de temps que le temps de deplacement total, on se deplace et on quite l'evenement
			if (robot.getTempsEvenementCase() >= robot.getTempsEvenement()) {
				super.getSimulateur().setDrawOnThisDate(true);
				//On libére le robot
				releaseRobot(robot);
				robot.deplacer(nouvelleCase);
				return;
			}
			else {
				super.addTimeAndCallNewEvent(robot, super.getSimulateur(), this);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
        	throw new Exception(e.getMessage());
        }
	}
	
	@Override
	public String toString(){
		Case position = null;
			for (Robot robot : super.getSimulateur().getDonneesSim().getRobots()){
				if (robot.getId() == super.getRobotId()){
					position = robot.getPosition();
				}
			}
		    return "[Deplacement] Evenement deplacement and date = " + super.getDate() + " Robot id = " + super.getRobotId()+ "Direction = " + direction + " Postion = " + position;
		  }
}
