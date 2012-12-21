/**
 * 
 */
package boulderdash.cases;

import java.awt.Color;
import java.awt.Graphics2D;

import boulderdash.Constantes;
import boulderdash.Joueur;
import boulderdash.annotations.Cassable;
import boulderdash.annotations.Fixe;
import boulderdash.utils.Position;

/**
 * 
 */
@Fixe
@Cassable
public final class Terre extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Terre instance = new Terre();

	private Terre() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		joueur.detruireCase();
	}

	@Override
	protected char char_repr() {
		return ':';
	}

	@Override
	public void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		g2.setColor(Color.ORANGE);

		g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
				Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

	}

}
