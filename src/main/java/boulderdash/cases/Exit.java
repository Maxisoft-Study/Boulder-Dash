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
public final class Exit extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Exit instance = new Exit();

	private Exit() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.cases.ActionOnPlayer#OnPlayer(boulderdash.Joueur)
	 */
	@Override
	public void OnPlayer(final Joueur joueur) {
		if (joueur.getScore() >= 3 * Constantes.NBR_POINT_GAGNE_DIAMANT) {
			joueur.finDuParcours();
		}
	}

	@Override
	protected char char_repr() {
		return 'F';
	}

	@Override
	public final void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		// dessin
		g2.setColor(Color.MAGENTA);
		g2.fillRoundRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
				Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS / 2,
				Constantes.CASE_EN_PIXELS / 2);

	}

}
