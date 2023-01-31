package strategie;

import java.util.Collections;
import java.util.LinkedList;

import cartes.Case;
import robots.*;

/**
 * <b>La classe Chemin represente les cases a parcourir pour aller d'une case depart a une case arrivee</b>
 * <p>
 * Un chemin est caracterise :
 * <ul>
 * <li>robot : une instance de robot (seul son type est utile pour connaitre les temps de parcours)</li>
 * <li>parcours: liste des cases du chemin</li>
 * <li>tempsPassage: temps necessaire pour passer d'une case a l'autre</li>
 * <li>depart: case de depart du chemin</li>
 * <li>arrivee: case d'arrivee du chemin</li>
 * <li>tempsTotal: temps de parcours total du chemin</li>
 * </ul>
 * </p>
 * <p>
 * </p>
 */
public class Chemin {
	
	private Robot robot; 	// pour connaitre le type de robot
	private LinkedList<Case> parcours;
	private LinkedList<Integer> tempsPassage;
	private Case depart;
	private Case arrivee;
	private int tempsTotal = 0;
	
	/**
	 * Constructeur de Chemin. On le cree avec une seul case et on va ensuite les ajouter une par une.
	 * @param robot (instance de robot dont seul le type interesse pour les temps de parcours)
	 * @param depart (case de depart)
	 */
	public Chemin(Robot rob, Case depart) {
		this.robot = rob;
		this.parcours = new LinkedList<Case>();
		this.parcours.add(depart);
		this.tempsPassage = new LinkedList<Integer>();
		this.tempsPassage.add(0);
		this.depart = depart;
		this.arrivee = depart;
	}

	/**
	 * Ajoute une case a parcourir au chemin et met a jour l'arrive
	 * @param caseSuivante (nouvelle case)
	 * @throws Exception
	 */
	public void addCase(Case caseSuivante) throws Exception {
		try {			
			int tempsDepl =  robot.tempsParcAdj(parcours.getLast(), caseSuivante);
			this.tempsPassage.add(tempsDepl);
			this.tempsTotal += tempsDepl;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		this.parcours.add(caseSuivante);
		this.arrivee = caseSuivante;
	}
		
	public LinkedList<Case> getParcours() {
		return parcours;
	}

	public LinkedList<Integer> getTempsPassage() {
		return tempsPassage;
	}

	public int getTempsTotal() {
		return this.tempsTotal;
	}

	public Case getDepart() {
		return depart;
	}
	
	public Case getArrivee() {
		return arrivee;
	}
	
	/**
	 * Inverse le chemin entre l'arrivee et le depart
	 */
	public void inverseChemin() {
		Collections.reverse(this.parcours);
		Collections.reverse(this.tempsPassage);
		Case temp = this.depart;
		this.depart = this.arrivee;
		this.arrivee = temp;
	}

	
	public void printChemin(){
		System.out.println("---------Chemin-------------");
		for (int i  = 0; i<parcours.size(); i++){
			System.out.println(parcours.get(i) + " temps  " +  tempsPassage.get(i));
		}
		System.out.println("---------FinChemin-------------");
	return ;
	}
	
	@Override
	public String toString() {
		String str ="---------Chemin-------------";
		for (int i  = 0; i<parcours.size(); i++){
			str += parcours.get(i) + " temps  " +  tempsPassage.get(i);
		}
		str +="---------FinChemin-------------";
		return str;
	}

}
