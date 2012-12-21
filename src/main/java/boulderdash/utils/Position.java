package boulderdash.utils;

import java.io.Serializable;

/**
 * Définition d'une Position 2D.<br/>
 * Utilisé ici dans le but de se repérer dans une matrice.
 * 
 * @version 3
 * 
 */
public final class Position implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	/**
	 * Composante Horizontale.
	 */
	private int x;
	/**
	 * Composante Verticale.
	 */
	private int y;

	/**
	 * @return le x
	 */
	public final int getX() {
		return this.x;
	}

	/**
	 * @return le y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * @param x
	 *            le x à définir
	 */
	public final void setX(final int x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            le y à définir
	 */
	public final void setY(final int y) {
		this.y = y;
	}

	@Override
	public final String toString() {
		return "Pos(" + this.x + "," + this.y + ")";
	}

	// _______________________________________________________________________________
	// ----------------------- Methode Operateur (+, -, *)
	// ------------------------

	/** Ajoute */
	public final void add(final Position pos) {
		this.x += pos.x;
		this.y += pos.y;
	}

	/** Soustrait */
	public final void sub(final Position pos) {
		this.x -= pos.x;
		this.y -= pos.y;
	}

	/** Multiplication */
	public final void mul(final int coeff) {
		this.x *= coeff;
		this.y *= coeff;
	}

	/** Multiplication */
	public final void div(final int coeff) {
		this.x /= coeff;
		this.y /= coeff;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Position other = (Position) obj;
		return this.x == other.x && this.y == other.y;
	}

	// _____________________________________________________________
	// ----------------------- Constructeurs -----------------------
	@Override
	public final Position clone() {
		return new Position(this.x, this.y);
	}

	/** Defaut : Initialize x et y a 0 */
	public Position() {
		this(0, 0);
	}

	/**
	 * Initialise Avec les parametres
	 * 
	 * @param x
	 * @param y
	 */
	public Position(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/** Initialise avec une array */
	public Position(final int[] tab) {
		this(tab[0], tab[1]);
	}

}
