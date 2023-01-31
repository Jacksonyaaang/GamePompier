package evenements;
import robots.*;
import cartes.*;
import io.DonneesSimulation;
import simulation.*;

/**
* <b>La classe IntervenirRobot représente un evenement d'intervention pour un robot spécifique</b>
*/
public class IntervenirRobot extends EvenementRobot {
		
	public IntervenirRobot(long date, int robotId, Simulateur simulateur) throws Exception{
		super(date, robotId, simulateur);
	}

	@Override
	public void execute() throws Exception {
		try {
			Robot robot = super.getDonneesSim().getRobots()[super.getRobotId()-1];
			Case caseIncendie = robot.getPosition();
	        Incendie[] incendies = super.getDonneesSim().getIncendies();
	        Incendie incendie = null;

			//Si le robot n'est pas occupée, on execute l'évenement
			if (!robot.isrobotOccupee()) {
				//Le temps de l'evenement est égal au temps d'intervention unitaire 
				robot.setTempsEvenement(robot.getIntUnitDuree());
				occupyRobot(robot);
			}

    		//Si le robot est occupée, on décale les événements du robot jusqu'à la fin d'exécution d'événement actuel		
	        if (robot.isrobotOccupee() && robot.getEvenementExecution() != this) {
				incrementeEvenementRobot(robot, this);
				return;
			}
			
	        if (robot.getTempsEvenementCase() == 0){
				//On verifie si on se trouve dans une incedie
		        for (Incendie incendieIter: incendies) {
		        	if (caseIncendie == incendieIter.getPosition()) {
		        		incendie = incendieIter;
		        		robot.setIncendieIntervention(incendie);
		        	}
		        }	       
		        if (incendie == null) {
					//On n'est pas dans un incedie
		        	releaseRobot(robot);
					return;
		        }
		        
		        if (robot.getReservoir() == 0) {
					//Le reservoir du robot est vide
		        	releaseRobot(robot);
					return;
		        }
				super.addTimeAndCallNewEvent(robot, super.getSimulateur(), this);
		        return;
	        }
	        else {
				//On reprend l'incendie
	        	incendie = robot.getIncendieIntervention();
	        }
			
				//Quand le temps qui correspand à getIntUnitDuree déroule, on deverse de l'eau
				if (robot.getTempsEvenementCase() % robot.getIntUnitDuree() == 0){
		        int volumeMinimal = Math.min(robot.getReservoir(), robot.getIntUnitVol());
				robot.deverserEau(volumeMinimal);
				if (incendie.getVolEauExtinction() == 0){
					releaseRobot(robot);
					return;
				}
				incendie.eteindre(volumeMinimal);
				robot.setTempsEvenementCase(0);
                //Si l'incendie a été éteint on l'enlève
				if (incendie.getVolEauExtinction() == 0){
					super.getSimulateur().setDrawOnThisDate(true);
					Incendie[] incendiesN = new Incendie[incendies.length-1];
					int iter = 0;
					for (Incendie incendieIter : incendies){
						if (incendieIter != incendie){
							incendiesN[iter] = incendieIter;
							iter++;
						}
					}
					super.getDonneesSim().setIncendies(incendiesN);
					//On libére le robot
					releaseRobot(robot);
					return;
				}
				
			    if (robot.getReservoir() == 0) {
					// Si le reservoir du robot est vide, on libere le robot
		        	releaseRobot(robot);
					return;
		        }
			}
			super.addTimeAndCallNewEvent(robot, super.getSimulateur(), this);
		}
		catch (Exception e) {
			e.printStackTrace();
        	throw new Exception(e.getMessage());
        }
	}
	
	@Override
	public String toString(){
		    return "[Intervention] Evenement Intervention and date = " + super.getDate()  ;
		  }
	
}