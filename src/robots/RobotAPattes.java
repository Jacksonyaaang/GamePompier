package robots;

import cartes.Case;
import cartes.NatureTerrain;

/**
 * <b>La classe RobotAPattes est un type de robot. La classe herite de la classe robot.</b>
 * <p>
 * Un robot a pattes est caracterise par l'ensemble des attributs caracterisant un robot et qui sont les memes pour tous les robots de type robot a pattes.
 * A ces attributs quelques attributs sont propres a chaque instance de robot a pattes :
 * <ul>
 * <li>sa position</li>
 * </ul>
 * </p>
 * <p>
 * Tous les robots a pattes ont la meme vitesse, celle par defaut
 * </p>
 */
public class RobotAPattes extends Robot {
	
	private static final NatureTerrain [] natTerrInaccessible = { NatureTerrain.EAU };
	private static final String iconPath = "./img/robot_pattes.png";
	private static final int reservoirMax = Integer.MAX_VALUE;
	private static final int reservoir = Integer.MAX_VALUE;
	private static final int dureeRemplissage = 0;
	private static final int intUnitVol = 10 ;								
	private static final int intUnitDuree = 1 ;	
	private static final int vitesseParDefaut = 30;
	private static final int ralentissementRoche= 10;
	
	public void setup() {
		super.setReservoirMax(reservoirMax);
		super.setDureeRemplissage(dureeRemplissage);
		super.setIntUnitVol(intUnitVol);								
		super.setIntUnitDuree(intUnitDuree);
		super.setReservoir(reservoir);	
		super.setNatTerrInaccessible(natTerrInaccessible);
	}

	/**
	 * Constructeur du RobotAPattes
	 * @param pos (position du robot)
	 * On utilise la valeur par defaut de la vitesse
	 */
	public RobotAPattes(Case pos) throws Exception {
		super(pos, vitesseParDefaut);
		setup();
	}

	@Override
	public int getVitesse(NatureTerrain nature) {
		if (nature == NatureTerrain.ROCHE) {
			return RobotAPattes.vitesseParDefaut-ralentissementRoche;
		}
		return RobotAPattes.vitesseParDefaut;
	}
	
	@Override
	public String getIconPath() {
		return RobotAPattes.iconPath;
	}

}
