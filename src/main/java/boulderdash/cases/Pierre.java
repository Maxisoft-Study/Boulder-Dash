/**
 * 
 */
package boulderdash.cases;

import java.awt.Color;
import java.awt.Graphics2D;

import boulderdash.Constantes;
import boulderdash.Joueur;
import boulderdash.annotations.Bloquant;
import boulderdash.image.Texture;
import boulderdash.utils.Direction;
import boulderdash.utils.Position;

/**
 * 
 */
@Bloquant
public final class Pierre extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Pierre instance = new Pierre();

	private Pierre() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		if (!this.falling) {
			throw new IllegalStateException("Le joueur ne peut pas etre dans un/une " + this.getClass().getName());
		}
		if (this.getAnimProgress() > 55 || this.getDir() == Direction.BAS) {
			joueur.kill();
		}
	}

	@Override
	protected char char_repr() {
		return '@';
	}

	@Override
	public void affichage(Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		g2 = (Graphics2D) g2.create();
		g2.setColor(Color.darkGray);
		g2.setPaint(Texture.pierre_0);
		final int Pas = 11;// en % => depent du Thread Sleep ! + Permet de
							// control la vitesse

		// update l'anim
		if (this.animProgress <= 100 - Pas && this.dir != null) {
			this.animProgress += Pas;
			final int x = this.previousPos.getX() * Constantes.CASE_EN_PIXELS;
			final int y = this.previousPos.getY() * Constantes.CASE_EN_PIXELS;

			g2 = this.createAnimatedGraphic2d(g2); // crée la translation

			g2.setPaint(Texture.pierre_0);
			g2.fillRect(x, y, Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

		} else {
			g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

		}
		g2.dispose();
	}
}
