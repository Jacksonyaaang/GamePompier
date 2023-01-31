package io;

import cartes.*;
import robots.*;

/**
 * <b>La DonneesSimulation regroupe toute les informations d'etat relatives a une simulation</b>
 * <p>
 * Un objet DonneesSimulation est caracterise :
 * <ul>
 * <li>carteSim : la carte de la simulation<li>
 * <li>robotsSim: la liste des robots de la simulation</li>
 * <li>incendiesSim: la liste des incendies de la simulation</li>
 * <li>fichiersDonnees: le path du fichier d'ou ont ete lues les donnees initiales de la simulation</li>
 * </ul>
 * </p>
 */
public class DonneesSimulation {

	private Carte carteSim;
	private Robot[] robotsSim;
	private Incendie[] incendiesSim;
	private String fichierDonnees;


	public DonneesSimulation() {}

	public Carte getCarte() {
		return carteSim;
	}

	public void setCarte(Carte carteSim) {
		this.carteSim = carteSim;
	}

	public Robot[] getRobots() {
		return robotsSim;
	}

	public void setRobots(Robot[] robotsSim) {
		this.robotsSim = robotsSim;
	}

	public Incendie[] getIncendies() {
		return incendiesSim;
	}

	public void setIncendies(Incendie[] incendiesSim) {
		this.incendiesSim = incendiesSim;
	}
	
	public String getFichierDonnees() {
		return fichierDonnees;
	}

	public void setFichierDonnees(String fichierDonnees) {
		this.fichierDonnees = fichierDonnees;
	}
}
