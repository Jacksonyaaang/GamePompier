package cartes;

/**
 * <b>La classe Incendie represente un incendie de la carte de simulation</b>
 * <p>
 * Un incendie est caracterise :
 * <ul>
 * <li>position: la case ou il se trouve</li>
 * <li>volEauExtinction: le volume necessaire pour l'eteindre</li>
 * <li>iconPath: le path de l'image a afficher</li>
 * </ul>
 * </p>
 */
public class Incendie {
	
	private Case position;
	private int volEauExtinction;
	public static final String iconPath = "./img/feu.png";

	/**
	 * Constructeur d'incendie
	 * @param pos (position de l'incendie)
	 * @param vol (volume d'eau necessaire pour l'eteindre)
	 */
	public Incendie(Case pos, int vol) throws Exception {
		if (vol < 0) {
			throw new Exception("Le volume d'eau doit etre positif");
		}
		this.position = pos;
		this.volEauExtinction = vol;
	}
	
	public int getVolEauExtinction() {
		return volEauExtinction;
	}

	public Case getPosition() {
		return this.position;
	}
	
	/**
	 * Diminue le volume d'eau necessaire a son extinction du volume indique
	 * @param vol (vol)
	 */
	public void eteindre(int vol) throws Exception {
		if (vol < 0) {
			throw new Exception("Le volume d'eau doit etre positif");
		}
		this.volEauExtinction = Math.max(0, volEauExtinction-vol);
	}

}
