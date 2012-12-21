/**
 * 
 */
package boulderdash;

import java.awt.Graphics2D;

import boulderdash.annotations.Bloquant;
import boulderdash.annotations.Cassable;
import boulderdash.annotations.Fixe;
import boulderdash.utils.Direction;
import boulderdash.utils.Position;

/**
 * Classe qui sera héritée par tous les objets qui composent le damier ansi que
 * par les Personnages.
 */
public abstract class GameObject implements Constantes {
	/**
	 * Animation en cours. (100 = animation finie. 0 = commence)
	 */
	protected int animProgress = 100;
	/**
	 * Sert pour l'animation.
	 */
	protected Position previousPos = null;
	/**
	 * Sert pour l'animation.
	 */
	protected Direction dir = null;

	/**
	 * Crée un nouveau pointeur graphique.<br/>
	 * Celui ci sera decalé automatiquement selon la valeur de l'animation.<br/>
	 * Mode de translation lineaire.
	 * 
	 * @param g2
	 *            Graphique
	 * @return nouveau pointeur graphique
	 */
	protected final Graphics2D createAnimatedGraphic2d(Graphics2D g2) {
		g2 = (Graphics2D) g2.create();
		if (this.previousPos == null || this.dir == null || this.animProgress >= 100) {
			return g2;
		}
		switch (this.dir) {
		case HAUT:
			g2.translate(0, -Constantes.CASE_EN_PIXELS * this.animProgress / 100);
			// y -= CASE_EN_PIXELS * animProgress / 100;
			return g2;
		case BAS:
			g2.translate(0, Constantes.CASE_EN_PIXELS * this.animProgress / 100);
			// y += CASE_EN_PIXELS * animProgress / 100;
			return g2;
		case DROITE:
			g2.translate(Constantes.CASE_EN_PIXELS * this.animProgress / 100, 0);
			// x += CASE_EN_PIXELS * animProgress / 100;
			return g2;
		case GAUCHE:
			g2.translate(-Constantes.CASE_EN_PIXELS * this.animProgress / 100, 0);
			// x -= CASE_EN_PIXELS * animProgress / 100;
			return g2;
		}
		return g2;

	}

	/**
	 * Chaque Classe herite doit redefinir cette methode pour renvoye sa
	 * representation. Utiliser en mode Console.
	 * 
	 * @return Le caractere qui represante la class en cours (hardcoded)
	 */
	protected abstract char char_repr();

	/**
	 * Permet de dessiner (a l'aide du Graphics) le GameObject en cours.
	 * 
	 * @param g2
	 * @param pos
	 * @param largeur
	 * @param hauteur
	 */
	public abstract void affichage(Graphics2D g2, Position pos, int largeur, int hauteur);

	/**
	 * @return le previousPos
	 */
	public final Position getPreviousPos() {
		return this.previousPos;
	}

	/**
	 * @param previousPos
	 *            le previousPos à définir
	 */
	public final void setPreviousPos(final Position previousPos) {
		this.previousPos = previousPos;
	}

	/**
	 * @return le dir
	 */
	protected final Direction getDir() {
		return this.dir;
	}

	/**
	 * @param dir
	 *            le dir à définir
	 */
	public final void setDir(final Direction dir) {
		this.dir = dir;
	}

	/**
	 * @param animProgress
	 *            le animProgress à définir
	 */
	public final void setAnimProgress(final int animProgress) {
		this.animProgress = animProgress;
	}

	/** @return true si le GameObject peut en bloquer un Joueur. */
	public final boolean est_bloquant() {
		return this.getClass().getAnnotation(Bloquant.class) != null;
	}

	/** @return true si le GameObject ne bouge pas. */
	final public boolean est_fixe() {
		return this.getClass().getAnnotation(Fixe.class) != null;
	}

	/** @return true si le GameObject peut etre detruit. */
	public final boolean est_cassable() {
		return this.getClass().getAnnotation(Cassable.class) != null;
	}

	/**
	 * @return le animProgress
	 */
	public final int getAnimProgress() {
		return this.animProgress;
	}

	@Override
	/**
	 * 
	 */
	public final String toString() {
		return String.valueOf(this.char_repr());
	}

	protected GameObject() {
		super();
	}

}
