/**
 * 
 */
package boulderdash.utils;

/**
 * Les 4 Directions.
 */
public enum Direction {
	DROITE, GAUCHE, HAUT, BAS;

	private Direction() {

	}

	/**
	 * Convertir en Position.<br/>
	 * Pratique pour <b>Additionner</b> ou <b>Soustraire</b> a l'aide d'une
	 * autre Position<br/>
	 * Note : Developper pour correspondre a une matrice . (ie HAUT et BAS
	 * inverser)
	 * 
	 * @return Position
	 */
	public final Position asDeltaPosition() {
		switch (this) {
		case DROITE:
			return new Position(1, 0);
		case GAUCHE:
			return new Position(-1, 0);
		case HAUT:
			return new Position(0, -1);
		case BAS:
			return new Position(0, 1);
		}
		// Else
		throw new IllegalStateException();
	}

}
