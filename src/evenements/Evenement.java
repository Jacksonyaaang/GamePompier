package evenements;

/**
* <b>La classe Evenement représente la classe parent de tous les événements</b>
* <li>date : date de l'événement </li>
*/
public abstract class Evenement implements Comparable<Evenement> {
	
	private long date;
	
	Evenement(long date) throws Exception {
		if (date < 0) {
			throw new Exception("La date donnee est negatif");
		}
		this.date = date;
	}

	public long setDate(long date) {
		return this.date = date;
	}
	
	public long getDate() {
		return date;
	}
	/**
	 * La fonction execute est la fonction pricipale de chaque evenement.
	 * C'est elle qui execute la fonction associe a l'evenement
	 * @throws Exception
	 */
	public abstract void execute()throws Exception ;
	
	/*
	 * Cette fonction est pris de l'interface Comparable
	 * elle nous permet de comparer les evenements avec leur date.
	 */
    @Override
    public int compareTo(Evenement evenement) {
        if(this.date == evenement.date){
            return 0;
        }else if(this.date < evenement.date){
            return -1;
        }else{
            return 1;
        }
    }
}