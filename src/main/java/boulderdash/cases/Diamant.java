package boulderdash.cases;

import java.awt.Color;
import java.awt.Graphics2D;

import boulderdash.Constantes;
import boulderdash.Joueur;
import boulderdash.annotations.Cassable;
import boulderdash.image.Texture;
import boulderdash.utils.Direction;
import boulderdash.utils.Position;

@Cassable
public final class Diamant extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Diamant instance = new Diamant();

	private Diamant() {
	}

	@Override
	public void OnPlayer(final Joueur joueur) {
		joueur.detruireCase();
		if (this.falling && !(this.getAnimProgress() > 80 && this.getDir() != Direction.BAS)) { // tue
																								// le
																								// joueur
																								// meme
																								// si
																								// il
																								// vient
																								// de
																								// dessus.
			joueur.kill();
		} else {
			joueur.gaint_points(Constantes.NBR_POINT_GAGNE_DIAMANT);
		}

	}

	@Override
	protected char char_repr() {
		return '*';
	}

	@Override
	public final void affichage(Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		// dessin
		g2 = (Graphics2D) g2.create();
		g2.setColor(Color.GREEN);
		g2.setPaint(Texture.diamant_0);

		final int Pas = 9;// en % => depent du Thread Sleep ! + Permet de
							// control la vitesse

		// update l'anim
		if (this.animProgress <= 100 - Pas && this.dir != null) {
			this.animProgress += Pas;

			final int x = this.previousPos.getX() * Constantes.CASE_EN_PIXELS;
			final int y = this.previousPos.getY() * Constantes.CASE_EN_PIXELS;

			g2 = this.createAnimatedGraphic2d(g2); // Mise a jour du graphique.

			g2.fillRect(x, y, Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

		} else {
			g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);

		}
		g2.dispose();

	}
}
