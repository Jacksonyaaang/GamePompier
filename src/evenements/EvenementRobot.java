package evenements;

import gui.Simulable;
import io.DonneesSimulation;
import robots.*;
import simulation.Simulateur;

/**
* <b>La classe EvenementRobot représente la classe parent de tous les événements associée au robot</b>
* <li>robotid : id du robot associé à l'événement </li>
* <li>simulateur : le simulateur dans lequel l'événement sera exécutée </li>
*/
public abstract class EvenementRobot extends Evenement {

	private int robotId;
	private Simulateur simulateur;
  	
	/**
    * Chaque evenement robot utilise ses parametres.
    * @param date : date d'execution de l'evenement
    * @param robotId : correspond a l'id sur robot auquel on va executer l'evenement
    * @param simulateur
    * @throws Exception
    */
	public EvenementRobot(long date, int robotId, Simulateur simulateur) throws Exception{
		super(date);
		if (robotId >=1 && robotId <= simulateur.getDonneesSim().getRobots().length) {
			this.robotId = robotId;
		}
		else {
			throw new Exception("Robot id est invalide = "+ robotId);
		}
		this.simulateur = simulateur;

	}


	public abstract void execute() throws Exception;

 
   /**
    * Cette fonction libere un robot i.e elle rend le boolean RobotOccpee egal a false
    * et reset des parametres stockees qui sont liee a l'evenement
    * @param robot
    */
	static public void releaseRobot(Robot robot) {
		robot.setTempsEvenement(0);
		robot.setTempsEvenementCase(0);
		robot.setRobotOccupee(false);
		robot.setEvenementExecution(null);
	}
 
   /**
    * Cette fonction rend le robot occupee au point de vue du simulateur
    * et stocke l'evenement en cours d'execution
    * @param robot
    */
	protected void occupyRobot(Robot robot) {
		robot.setTempsEvenementCase(0);
		robot.setRobotOccupee(true);
		robot.setEvenementExecution(this);
	}

   /**
    * Cette fonction est appelee quand un evenement veut ordonner un robot or qu'il est occupee.
    * Dans ce cas, on decale tous les evenements associee a ce robot.
    * @param robot
    * @param event
    */
	protected void incrementeEvenementRobot(Robot robot, EvenementRobot event){
		int valeurIncrementation =  robot.getTempsEvenement()-robot.getTempsEvenementCase();
		if (valeurIncrementation == 0){valeurIncrementation =1;}
		simulateur.incrementAllOther(super.getDate(), robot.getEvenementExecution(), robotId, valeurIncrementation);
		event.setDate(event.getDate()+valeurIncrementation);
		simulateur.ajouteEvenement(event);
	}
	
    /**
    * Cette fonction quand l'evenement n'a pas encore terminee a executee et il faut le rappeler dans la date suivante
    * Elle incremente TempsEvenementCase qui correspond au temps passe dans la case.
    * Elle incremente la date d'execution de l'evenement et le remet dans la priorityqueue comme il sera enlevee par la fonction poll
    * @param robot
    * @param simulateur
    * @param event
    */
	protected void addTimeAndCallNewEvent(Robot robot, Simulateur simulateur, EvenementRobot event) {
		robot.setTempsEvenementCase(robot.getTempsEvenementCase()+1);
		event.setDate(event.getDate()+1);
		simulateur.ajouteEvenement(event);
		robot.setEvenementExecution(event); 
	}

	public Simulateur getSimulateur() {
		return simulateur;
	}
		
	public DonneesSimulation getDonneesSim() {
		return simulateur.getDonneesSim();
	}

	public int getRobotId() {
		return robotId;
	}
}
