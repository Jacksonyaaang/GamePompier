package evenements;

import io.DonneesSimulation;
import pompier.ChefPompier;
import robots.Robot;
import simulation.Simulateur;

public class LancerStrategie extends Evenement {

	private Simulateur simulateur;
	private ChefPompier chefPompier;
	public LancerStrategie(long date, ChefPompier chefPompier, Simulateur simulateur) throws Exception {
		super(date);
		if (chefPompier == null || simulateur == null) {
			throw new Exception("[LancerStrategie] un des parametre est invalide ");
		}
		this.chefPompier = chefPompier;
		this.simulateur = simulateur;
	}
	@Override
	public void execute() throws Exception {
		chefPompier.executeStrategie(super.getDate(), simulateur);
		if (simulateur.getDonneesSim().getIncendies().length != 0){
			simulateur.ajouteEvenement(new LancerStrategie(super.getDate() + 1, chefPompier,  simulateur));	 
		}
		return;	
	}

		
	@Override
	public String toString(){
		    return "[LancerStrategie] Evenement Strategie and date = " + super.getDate()  ;
		  }

	
}



