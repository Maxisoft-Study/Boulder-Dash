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
import boulderdash.image.Texture;
import boulderdash.utils.Position;

@UniqueInstance
@Fixe
public class RainBow extends Case {
	/**
	 * Instance initiale.
	 */
	public static final RainBow instance = new RainBow();

	/**
	 * 
	 */
	private RainBow() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.cases.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		joueur.gaint_points(-1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.GameObject#char_repr()
	 */
	@Override
	protected char char_repr() {
		return 'l';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.GameObject#affichage(java.awt.Graphics2D,
	 * boulderdash.utils.Position, int, int)
	 */
	@Override
	public void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		g2.setColor(Color.RED);

		if (pos.getY() % 2 == 0) {
			g2.setPaint(Texture.rainbow_1);
		} else {
			g2.setPaint(Texture.rainbow_0);
		}

		g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
				Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

	}

}
