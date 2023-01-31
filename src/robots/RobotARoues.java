package robots;


import cartes.Case;
import cartes.NatureTerrain;

/**
 * <b>La classe RobotARoues est un type de robot. La classe herite de la classe robot.</b>
 * <p>
 * Un robot a roues est caracterise par l'ensemble des attributs caracterisant un robot et qui sont les memes pour tous les robots de type robot a roues.
 * A ces attributs quelques attributs sont propres a chaque instance de robot a pattes :
 * <ul>
 * <li>sa position</li>
 * <li>sa vitesse</li>
 * </ul>
 * </p>
 */
public class RobotARoues extends Robot {
	
	private static final NatureTerrain [] natTerrInaccessible = { NatureTerrain.EAU, NatureTerrain.FORET, NatureTerrain.ROCHE };
	private static final String iconPath = "./img/robot_roues.png";
	private static final int reservoirMax = 5000;
	private static final int dureeRemplissage = 600;
	private static final int intUnitVol = 10;								
	private static final int intUnitDuree = 1;
	private static final int vitesseParDefaut = 80;
	private  static final boolean seRemplitDansEau = false;
	
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
	 * Constructeur du robot a roues
	 * @param pos (position du robot)
	 * On utilise la vitesse par defaut
	 */
	public RobotARoues(Case pos) throws Exception {
		super(pos, vitesseParDefaut);
		setup();
	}
	
	/**
	 * Constructeur du robot a roues
	 * @param pos (position du robot)
	 * @param vitesse (vitesse du robot)
	 */
	public RobotARoues(Case pos, int vit) throws Exception {
		super(pos, vit);
		setup();
	}
	
	@Override
	public String getIconPath() {
		return RobotARoues.iconPath;
	}

	@Override
	public int getVitesse(NatureTerrain nature) {
		return RobotARoues.vitesseParDefaut;
	}

}
