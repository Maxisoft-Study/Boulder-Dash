/**
 * 
 */
package boulderdash.cases;

import java.awt.Graphics2D;

import boulderdash.Joueur;
import boulderdash.annotations.Fixe;
import boulderdash.annotations.UniqueInstance;
import boulderdash.utils.Position;

/**
 * 
 */
@UniqueInstance
@Fixe
public final class Vide extends Case {
	/**
	 * Instance initiale.
	 */
	public static final Vide instance = new Vide();

	private Vide() {
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
		return ' ';
	}

	@Override
	public void affichage(final Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		// afficher rien :)

	}

}
