package cartes;

import java.awt.Color;

/**
 * <b>L'enumeration NatureTerrain represente les natures de terrain possible d'une case</b>
 * <p>
 * Une nature est caracterise :
 * <ul>
 * <li>couleur: sa couleur d'affichage</li>
 * <li>iconPath: le path de l'image d'affichage</li>
 * </ul>
 * </p>
 */
public enum NatureTerrain {
	EAU(Color.CYAN,null),
	FORET(new Color(51, 204, 51),"./img/arbre.png"),
	ROCHE(new Color(51, 204, 51),"./img/roche.png"),
	TERRAIN_LIBRE(new Color(51, 204, 51),null), 
	HABITAT(new Color(51, 204, 51),"./img/maison.png");
	/* (new Color(51, 204, 51) = light green*/
	
	public final Color couleur;
	public final String iconPath;
	
    private NatureTerrain(Color c, String path) {
        this.couleur = c;
        this.iconPath = path;
    }
	
	/**
	 * Retourne la nature correspondant a la chaine de caracteres
	 * @param str (chaine de caractere)
	 * @return nat (nature correspondante)
	 */
	public static NatureTerrain stringToNature(String str) throws Exception {
		NatureTerrain[] natures = values();
		for(NatureTerrain nat : natures) {
			if(nat.toString().equals(str)){
				return nat;
			}
		}
		throw new Exception("Nature du terrain invalide");
	}
	
}
