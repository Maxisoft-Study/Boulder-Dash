/**
 * 
 */
package boulderdash.cases;

import java.awt.Color;
import java.awt.Graphics2D;

import boulderdash.Constantes;
import boulderdash.Joueur;
import boulderdash.annotations.Fixe;
import boulderdash.annotations.UniqueInstance;
import boulderdash.utils.Position;

/**
 * 
 */
@UniqueInstance
@Fixe
public final class Spawn extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Spawn instance = new Spawn();

	private Spawn() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.cases.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		// Ne Rien faire
	}

	@Override
	protected char char_repr() {
		return 'D';
	}

	@Override
	public void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		g2.setColor(Color.BLUE);

		g2.fillRoundRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
				Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS / 2,
				Constantes.CASE_EN_PIXELS / 2);

	}

}
