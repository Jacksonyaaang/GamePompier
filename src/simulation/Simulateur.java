package simulation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.DataFormatException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import gui.GUISimulator;
import gui.GraphicalElement;
import gui.ImageElement;
import gui.Rectangle;
import gui.Simulable;
import io.DonneesSimulation;
import io.LecteurDonnees;
import pompier.ChefPompier;
import cartes.*;
import evenements.*;
import robots.*;
import strategie.*;


public class Simulateur implements Simulable {	
    /** L'interface graphique associee */
    private GUISimulator gui;
	private DonneesSimulation donneesSim;
	private int tailleCase;
	private int nbLignes;
	private int NbColonne;
	private boolean drawOnThisDate;
    private HashMap<Class, Graphe> graphes = new HashMap<Class, Graphe>();
    private HashMap<Class, PlusCourtChemin> strategies = new HashMap<Class, PlusCourtChemin>();;
	private ArrayList<Long> dateEvenementInitiaux = new ArrayList<Long>();
	private long dateSimulation = 0;
	private PriorityQueue<Evenement> listeEvenement = new PriorityQueue<Evenement>(); 
	private PriorityQueue<Evenement> listeEvenementInitial = null; 
	
	/**
	 * @param gui
	 * @param donnees
	 * @throws Exception
	 */
    public Simulateur(GUISimulator gui, DonneesSimulation donnees) throws Exception {
        this.gui = gui;
        this.donneesSim = donnees;
        this.nbLignes = donnees.getCarte().getNbLig();
		this.NbColonne = donnees.getCarte().getNbCol();
        this.tailleCase = gui.getWidth()/this.nbLignes;
        setupRobotStrategie(); 
        gui.setSimulable(this);				// association a la gui!
        draw();
    }
    
	public DonneesSimulation getDonneesSim() {
		return this.donneesSim;
	}

	/**
	 * setupRobotStrategie associe a chaque type de robot un graphe pondere avec des poids
	 *  qui depend de la vitesse de deplacement du robot.
	 * @throws Exception
	 */
	public void setupRobotStrategie() throws Exception{
		for (Robot robot : donneesSim.getRobots()) {
			robot.setSim(this);
			Class typeRobot = robot.getClass();
			if (this.graphes.get(typeRobot) == null) {
				graphes.put(typeRobot, new Graphe(robot,this.donneesSim.getCarte()));
				strategies.put(typeRobot, new Dijkstra(graphes.get(typeRobot)));
			}
			robot.setStrategie(strategies.get(typeRobot));
		}
	}

	/**
	* next est une fonction herite de l'interface Simulable, elle appelle la fonction incremente date
    * qui appelle la fonction qui execute tous les evenements qui ont une date inferieur a la date courant
    */
	@Override
	public void next(){
		/*Quand la date est egal a 0, on stocke les evenements initiaux
       	pour qu'on puissent les remettre quand on apple a la fonction restart */
		if (dateSimulation == 0 && dateEvenementInitiaux.size() ==0){
			saveState();
		}
		if (!simulationTerminee()){
			incrementeDate();
		}
		return ;
	}

   /**
    * La fonction restart reset tout les modifications faite et remet la date d'execution a 0
    */
	@Override
	public void restart() {
		System.out.println("[Simulation] Restarting ....");
		dateSimulation = 0;
		try {
			Robot.resetId();
            //On relit les informations de la carte a partir du fichier d'execution
		    this.donneesSim =  LecteurDonnees.lire(donneesSim.getFichierDonnees());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
		//Avec cette fonction on remet les evenements a leurs valeurs initiaux
		try {
			returnInitialState();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(0);
		}
		draw();
	}
	
	public void ajouteEvenement(Evenement e){
		listeEvenement.add(e);
	}
	
   /**
    * incrementeDate :
    * -incremente la dateSimulation de simulation
    * -Appelle la fonction qui execute tous les evenements qui ont une date inferieur a la date courant
    * -Appelle la fonction quand il y a un changement dans la carte
    */
	private void incrementeDate() {
		dateSimulation++;
		this.drawOnThisDate = false;
		runEvents();
		if(drawOnThisDate){
			draw();
		}
		return;
	}

	/**
	 * runEvents : parcours la liste des donnees qui stocke les evenements 
	 * et execute touts les evenements qui ont une date inferieur a la date courante
	 */
	private void runEvents() {
		while (true){
			if (simulationTerminee()){
				System.out.println("[Simulation] Tous les evenements ont ete realisees");
			}
			Evenement evenementCourant = listeEvenement.peek();
			if (evenementCourant == null){
			break;
			}
			if (evenementCourant.getDate() <= dateSimulation) {
				try {
					evenementCourant.execute();
					listeEvenement.poll();
				}
				catch (Exception e){
					e.printStackTrace();
		        	System.out.println(e.getMessage());
		        	System.exit(0);
				}
			}
			else {
				break;
			}
		}
		return ;	
	}

    /**
    * incrementAllOther : est appelle quand deux evenements robots veulent s'executer en même temps
    * pour le même robot. Dans ce cas on decale tous les evenements sauf l'evenement en cours d'execution pour ce robot specifique
    * @param date : date d'execution actuel
    * @param EvenementExecution : l'evenement en cours d'execution
    * @param robotId : l'id du robot dont les deux evenements veulent ordonnee
    * @param incrementValue : Le temps restant pour l'execution du robot
    */
	public void incrementAllOther(long date, Evenement EvenementExecution, int robotId, int incrementValue) {
		ArrayList<Evenement> listEvenementAremettre = new ArrayList<Evenement>();
		for(Evenement event : listeEvenement  ) {
	    	if (event.getDate() > date && event != EvenementExecution && event instanceof EvenementRobot &&	((EvenementRobot) event).getRobotId() == robotId) {
	    		event.setDate(event.getDate()+incrementValue);
				Evenement event_to_replace;
				event_to_replace = event;
				listEvenementAremettre.add(event_to_replace);
	    	}
		}	
		for (Evenement event : listEvenementAremettre)
		{
			listeEvenement.remove(event);
			listeEvenement.add(event);
		}

    	if (EvenementExecution == null) {
			return;
		}
    	listeEvenement.remove(EvenementExecution);
		listeEvenement.add(EvenementExecution);
		return;
	}

	/**
	 * simulationTerminee indique si la simulation a terminee ou non
	 * @return un boolean qui est true quand la simulation a terminee
	 */
	private boolean simulationTerminee(){
		return listeEvenement.size() == 0;
	}
	
	public void setDrawOnThisDate(boolean bool){
		this.drawOnThisDate = bool;
	}
	
	/**
	 * saveState : Cette fonction est appelle pour sauver l'etat initial d'execution 
	 * pour qu'on puisse faire du restart avec les evenements initiaux  
	 */
	public void saveState(){
		listeEvenementInitial = new PriorityQueue<Evenement>(listeEvenement); 
		for (Evenement event : listeEvenementInitial){
			dateEvenementInitiaux.add(event.getDate());
		}
	}

 
   /**
    * La fonction returnInitialState est appelee par la fonction restart
    * pour remettre les evenements a leur etat initiaux et elle faite aussi le reset
    * de quelque parametre de classe
 * @throws Exception
    */	
	public void returnInitialState() throws Exception{
		int iterArrayDateInitial = 0;
		for (Evenement event : listeEvenementInitial){
			event.setDate(dateEvenementInitiaux.get(iterArrayDateInitial));
			iterArrayDateInitial++;
		}
		listeEvenement = new PriorityQueue<Evenement>(listeEvenementInitial); 
		ChefPompier.resetClass();
		graphes = new HashMap<Class, Graphe>();
		strategies = new HashMap<Class, PlusCourtChemin>();
		try {
			setupRobotStrategie();
		} catch (Exception e) {	
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}	
	}

	/**
     * Dessine la carte du jeu.
     */
    private  void draw() {
        gui.reset();	// clear the window   
        
        for (int i=0; i<nbLignes; i++) {
    	for (int j=0; j<NbColonne; j++) {
        		NatureTerrain natCase = this.donneesSim.getCarte().getCase(i, j).getNature();
        		
        		if (natCase == NatureTerrain.EAU) {
        			gui.addGraphicalElement(			
	        			new Rectangle(
	        					(int)(((double)j+1.0/2)*this.tailleCase),
	        					(int)(((double)i+1.0/2)*this.tailleCase),
	        					natCase.couleur,
	        					natCase.couleur,
	        					this.tailleCase));
        		} else {
        			gui.addGraphicalElement(			
    	        			new Rectangle(
    	        					(int)(((double)j+1.0/2)*this.tailleCase),
    	        					(int)(((double)i+1.0/2)*this.tailleCase),
    	        					new Color(0, 153, 51),
    	        					natCase.couleur,
    	        					this.tailleCase));
        		}
        		
        		ImageElement imgNat = new ImageElement(j*this.tailleCase,i*this.tailleCase,
        				natCase.iconPath,
        				this.tailleCase,this.tailleCase,null);
                gui.addGraphicalElement(imgNat);	
        	}
        }
        
        Robot[] robots = donneesSim.getRobots();
        for (int i=0; i<robots.length; i++) {
    		Case position = robots[i].getPosition();
    		int lig = position.getLigne();
    		int col = position.getColonne();
    		
    		ImageElement imgRob = new ImageElement(col*this.tailleCase,lig*this.tailleCase,
    				robots[i].getIconPath(),this.tailleCase,this.tailleCase,null);
            gui.addGraphicalElement(imgRob);
        }
        
        Incendie[] incendies = donneesSim.getIncendies();
        for (int i=0; i<incendies.length; i++) {
    		Case position = incendies[i].getPosition();
    		int lig = position.getLigne();
    		int col = position.getColonne();
    		
    		ImageElement imgFeu = new ImageElement(col*this.tailleCase,lig*this.tailleCase,
    				Incendie.iconPath,this.tailleCase,this.tailleCase,null);
            gui.addGraphicalElement(imgFeu);
        }
    }

	/**
	 * Cette fonction affiche tout les evenements stockees
	 */
	private void printEvents() {
    	System.out.println("------------ListeEvenemnt----------------");
		for(Evenement event : listeEvenement  ) {
	    	System.out.println(event);
		}
    	System.out.println("------------FinListeEvenemnt----------------");
	}

	public long getDateSimulation() {
		return this.dateSimulation;
	}

	public PriorityQueue<Evenement> getListeEvenement() {
		return this.listeEvenement;
	}

}
