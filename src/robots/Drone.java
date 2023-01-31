package robots;

import cartes.Case;
import cartes.NatureTerrain;

/**
 * <b>La classe Drone est un type de robot. La classe herite de la classe robot.</b>
 * <p>
 * Un drone est caracterise par l'ensemble des attributs caracterisant un robot et qui sont les memes pour tous les robots de type drone.
 * A ces attributs quelques attributs sont propres a chaque instance de drone :
 * <ul>
 * <li>sa position</li>
 * <li>sa vitesse</li>
 * </ul>
 * </p>
 */
public class Drone extends Robot {
	
	private static final NatureTerrain [] natTerrInaccessible = null;
	private static final String iconPath = "./img/drone.png";
	private static final int reservoirMax = 10000;
	private static final int dureeRemplissage = 1800;
	private static final int intUnitVol = 10000;								// litres volume unitaire d'intervention
	private static final int intUnitDuree = 30;								// seconde duree unitaire d'intervention
	private static final int vitesseParDefaut = 100;
	private static final int vitesseMaximale = 150;
	private static final boolean seRemplitDansEau = true;

	/**
	 * fixe les caracteristique du drone qu'on retrouve pour tous les robots avec les attributs identiques (static) pour tous les drones
	 */
	public void setup() {
		super.setReservoirMax(reservoirMax);
		super.setReservoir(reservoirMax);
		super.setDureeRemplissage(dureeRemplissage);
		super.setIntUnitVol(intUnitVol);								
		super.setIntUnitDuree(intUnitDuree);	
		super.setNatTerrInaccessible(natTerrInaccessible);
		super.setSeRemplitDansEau(seRemplitDansEau);
	}

	/**
	 * Constructeur du Drone
	 * @param pos (position du drone)
	 * On utilise la valeur par defaut de la vitesse
	 */
	public Drone(Case pos) throws Exception {
		super(pos, vitesseParDefaut);
		setup();
	}
	
	/**
	 * Constructeur du Drone
	 * @param pos (position du drone)
	 * @param vitesse (vitesse du drone)
	 */
	public Drone(Case pos, int vit) throws Exception {
		super(pos, vit);
		if (vit > vitesseMaximale) {
			throw new Exception("La vitesse doit etre inferieure a 150 km/h");
		}
		setup();
	}
	
	@Override
	public String getIconPath() {
		return Drone.iconPath;
	}

	@Override
	public int getVitesse(NatureTerrain nature) {
		return Drone.vitesseParDefaut;
	}


}
