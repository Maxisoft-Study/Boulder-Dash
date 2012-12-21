/**
 * 
 */
package boulderdash.cases;

import java.awt.Color;
import java.awt.Graphics2D;

import boulderdash.Constantes;
import boulderdash.Joueur;
import boulderdash.annotations.Bloquant;
import boulderdash.annotations.Fixe;
import boulderdash.annotations.UniqueInstance;
import boulderdash.image.Texture;
import boulderdash.utils.Position;

/**
 * 
 */
@UniqueInstance
@Bloquant
@Fixe
public final class Mur extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Mur instance = new Mur();

	private Mur() {
		super();
	}

	@Override
	protected char char_repr() {
		return '#';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		throw new IllegalStateException("Le joueur ne peut pas etre dans un/une " + this.getClass().getName());
	}

	// @Override
	// public Case clone() {
	// return instance;
	// }

	@Override
	public void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		g2.setColor(Color.LIGHT_GRAY);
		g2.setPaint(Texture.wall_0);

		g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
				Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

	}

}
