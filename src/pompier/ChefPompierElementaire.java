package pompier;


import cartes.*;
import robots.*;
import simulation.*;
import strategie.*;

public class ChefPompierElementaire extends ChefPompier{

   /**
    * Dans cette strategie, on appelle une fonction qui rend les reservoir des robots infini
    * et on cherche uniquement des les incendies.
    * @param simulateur
    */
	public ChefPompierElementaire(Simulateur simulateur){
		super(simulateur);
		rendreReservoirInfiniRobot();
	}

	
	@Override
	public void executeStrategie(long date, Simulateur simulateur) throws Exception {
		/*
		 * Dans cette strategie, on considere une incendie et on cherche le plus proche et on lui donne l'ordre 
		 * d'intervention en utilisant le plus court chemin.
		 */
		clearDictionnary(simulateur);		
		if (simulateur.getDonneesSim().getRobots().length == ListRobotTacheDonnee.size() ){
			return;
		}
		Incendie[] incendies = simulateur.getDonneesSim().getIncendies();
		Robot[] robots = simulateur.getDonneesSim().getRobots();
		for (Incendie incendie : incendies) {
			for (Robot robot : robots){
				if (!robot.isrobotOccupee() && robot.getReservoir()!=0 && !ListRobotTacheDonnee.containsKey(robot)){
					Chemin chemin = robot.calcChemin(robot.getPosition(), incendie.getPosition());
					if (chemin == null){
						continue;
					}
					long dateEventfinal = creeEvenementItervention(chemin, robot);
					ListRobotTacheDonnee.put(robot, dateEventfinal);
					break;
				}
			}
		}
	}

}
