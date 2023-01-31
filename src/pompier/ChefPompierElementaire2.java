package pompier;

import java.util.HashMap;
import java.util.Map;

import cartes.*;
import robots.*;
import simulation.*;
import strategie.*;

public class ChefPompierElementaire2 extends ChefPompier{
    
	protected Map<Incendie, Chemin> ListIncendieChemin= new HashMap<Incendie, Chemin>();

	/**
	 * Le reservoir est infini dans cette strategie.
	 * @param simulateur
	 */
    public ChefPompierElementaire2(Simulateur simulateur){
		super(simulateur);
		rendreReservoirInfiniRobot();
	}

	@Override
	public void executeStrategie(long date, Simulateur simulateur) throws Exception {
		clearDictionnary(simulateur);
		/**
		* Dans cette strategie, on prend un robot quelconque et si il n'est pas occupee on lui
		* donne l'ordre d'intervenir dans l'incendie la plus proche en prenant le plus court chemin
		*/
		if (simulateur.getDonneesSim().getRobots().length == ListRobotTacheDonnee.size() ){
			return;
		}
		Incendie[] incendies = simulateur.getDonneesSim().getIncendies();
		Robot[] robots = simulateur.getDonneesSim().getRobots();
		for (Robot robot : robots){
			if (!robot.isrobotOccupee() && robot.getReservoir()!=0 && !ListRobotTacheDonnee.containsKey(robot)){
				for (Incendie incendie : incendies) {
					Chemin chemin = robot.calcChemin(robot.getPosition(), incendie.getPosition());
					if (chemin == null){
						continue;
					}
					ListIncendieChemin.put(incendie, chemin);
				}
			int tempsMinimal = Integer.MAX_VALUE;
			Incendie IncendieOptimale = null;
			for (Map.Entry<Incendie, Chemin> entry : ListIncendieChemin.entrySet()) {
				Incendie key = entry.getKey();
				int value = entry.getValue().getTempsTotal();
				if (value <= tempsMinimal){
					IncendieOptimale = key;
					tempsMinimal = value;
				}
			}
			if (IncendieOptimale != null){
				long dateEventfinal = creeEvenementItervention(ListIncendieChemin.get(IncendieOptimale), robot);
				ListRobotTacheDonnee.put(robot, dateEventfinal);
			}
			ListIncendieChemin = new HashMap<Incendie, Chemin>();
			}
		}
	}

}

    
