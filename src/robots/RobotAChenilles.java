package robots;

import cartes.Case;
import cartes.NatureTerrain;

/**
 * <b>La classe RobotAChenilles est un type de robot. La classe herite de la classe robot.</b>
 * <p>
 * Un robot a chenilles est caracterise par l'ensemble des attributs caracterisant un robot et qui sont les memes pour tous les robots de type robot a chenilles.
 * A ces attributs quelques attributs sont propres a chaque instance de robot a chenilles :
 * <ul>
 * <li>sa position</li>
 * <li>sa vitesse</li>
 * </ul>
 * </p>
 */
public class RobotAChenilles extends Robot {
	
	private static final NatureTerrain [] natTerrInaccessible = { NatureTerrain.EAU, NatureTerrain.ROCHE };
	private  static final String iconPath = "./img/robot_chenilles.png";
	private  static final int reservoirMax = 2000;
	private  static final int dureeRemplissage = 300;
	private  static final int intUnitVol = 100;								
	private  static final int intUnitDuree = 8;
	private  static final int vitesseParDefaut = 60;
	private  static final int vitesseMaximale = 80;
	private  static final boolean seRemplitDansEau = false;

	/**
	 * fixe les caracteristique du robot a chenilles qu'on retrouve pour tous les robots avec les attributs identiques (static) pour tous les drones
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
	 * Constructeur du RobotAChenilles
	 * @param pos (position du robot)
	 * On utilise la valeur par defaut de la vitesse
	 */
	public RobotAChenilles(Case pos) throws Exception {
		super(pos, vitesseParDefaut);
		setup();
	}
	
	/**
	 * Constructeur du RobotAChenilles
	 * @param pos (position du robot)
	 * @param vitesse (vitesse du robot)
	 */
	public RobotAChenilles(Case pos, int vit) throws Exception {
		super(pos, vit);
		if (vit > vitesseMaximale) {
			throw new Exception("La vitesse doit etre inferieure a 80 km/h");
		}
	}

	@Override
	public int getVitesse(NatureTerrain nature) {
		if (nature == NatureTerrain.FORET) {
			return RobotAChenilles.vitesseParDefaut/2;
		}
		return RobotAChenilles.vitesseParDefaut;
	}
	
	@Override
	public String getIconPath() {
		return RobotAChenilles.iconPath;
	}

}
