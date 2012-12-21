package boulderdash.enemy;

import boulderdash.Constantes;
import boulderdash.Damier;
import boulderdash.GameObject;
import boulderdash.cases.ActionOnPlayer;
import boulderdash.cases.Case;
import boulderdash.utils.Direction;
import boulderdash.utils.Generator;
import boulderdash.utils.Position;

/**
 * Represente un Personnage.
 * 
 */
public abstract class Personnage extends GameObject implements ActionOnPlayer, Cloneable {
	/**
	 * Bloque le joueur.
	 */
	private boolean freez = false;
	/** La position du Joueur sur le Damier */
	protected Position pos;
	/** Pointe vers le damier */
	protected Damier damier;
	/**
	 * Vrai si le dernier deplacement est ok.
	 */
	private boolean lastDeplacementStatus = true;

	/**
	 * @return le lastDeplacementStatus
	 */
	public final boolean isLastDeplacementStatus() {
		return this.lastDeplacementStatus;
	}

	/**
	 * Permet de Deplacer le Joueur d'une Direction donnee.
	 * 
	 * @param dir
	 * @return true si le joueur s'est deplace lors de l'opperation.
	 */
	public synchronized final boolean seDeplace(final Direction dir) {

		this.damier.setChangement(true);

		if (this.getAnimProgress() < 74 && Constantes.CONFIG.isUseThread()) {// si
																				// l'animation
																				// n'est
																				// pas
																				// terminer
																				// a
			// plus de 74%
			if (Constantes.CONFIG.isLogDebug()) {
				Constantes.LOGGER.debug("" + this.getClass().getSimpleName()
						+ " try to Move but is Waiting anim ( Anim Percent : " + this.getAnimProgress() + " %)");
			}
			return false; // Attendre :)
		}
		// this.dir = null;

		this.previousPos = this.getPos();// set previous Pos

		final Position pos_cible = Generator.genNextPos(dir, this.getPos());
		if (Constantes.CONFIG.isLogDebug()) {
			Constantes.LOGGER.info("" + this.getClass().getSimpleName() + " player tries to move from " + this.getPos()
					+ " to " + pos_cible + " (dir " + dir + ")");
		}

		if (this.deplacement_Pierre_Possible(dir, pos_cible) && !Constantes.CONFIG.isMapEditorMode()) {
			// Si on Clone la pos_cible => le joueur se deplace en meme temp que
			// la pierre.
			// Sinon Seul la pierre se Deplace.
			// Note : on peut le faire en return :)

			this.damier.pousserLaPierreAndUpdate(pos_cible.clone(), dir);
		}
		this.lastDeplacementStatus = this.deplacementPossible(pos_cible);

		if (this.lastDeplacementStatus) {
			this.dir = dir;
			this.setPos(pos_cible);
			this.animProgress = 0;// reset anim

			this.damier.update();
			// L'appel a l'update permet d'eviter un bug : Si le joueur se
			// deplace exactement au
			// même instant que le calcul des chutes de pierres/diamants =>
			// le joueur Passe a travers l'objet :)

			return true;

		}

		return false;

	}

	/**
	 * Le personnage fait ce qu'il doit faire .
	 */
	public abstract void IA();

	/**
	 * Verification si le Joueur peut se rendre sur la Position donnee.<br/>
	 * Peut être redefini .
	 * 
	 * @param pos_cible
	 * @return true si le deplacement est possible.
	 */
	boolean deplacementPossible(final Position pos_cible) {
		if (this.isFreez()) {
			return false;
		}
		// verfif pas en dehors
		final int x = pos_cible.getX();
		final int y = pos_cible.getY();
		if (x < 0 || x >= this.damier.getLargeur() || y < 0 || y >= this.damier.getHauteur()) {
			if (Constantes.CONFIG.isLogDebug()) {
				Constantes.LOGGER.info("" + this.getClass().getSimpleName() + " tries to Move Outside the matrix ("
						+ pos_cible + ")");
			}
			return false;
		}

		if (Constantes.CONFIG.isMapEditorMode()) {
			return true;
		}

		final Case case_cible = this.damier.getCaseAt(pos_cible);
		if (Constantes.CONFIG.isLogDebug()) {
			Constantes.LOGGER.info("Case @ " + pos_cible + " : " + case_cible.getClass().getSimpleName());
		}
		final boolean b = !case_cible.est_bloquant();
		if (Constantes.CONFIG.isLogDebug()) {
			Constantes.LOGGER.debug(""
					+ this.getClass().getSimpleName()
					+ (b ? " can move to " + pos_cible : " " + case_cible.getClass().getSimpleName() + " @ "
							+ pos_cible + " is blocking"));
		}
		return b;
	}

	/**
	 * Verification si la Pierre peut se rendre sur la Position donnee (suivant
	 * la Direction donnee).
	 * 
	 * @param dir
	 * @param pos_pierre
	 * @return true si le deplacement est possible.
	 */
	protected abstract boolean deplacement_Pierre_Possible(final Direction dir, final Position pos_pierre);

	/** Tue le Joueur. */
	public abstract void kill();

	/**
	 * Le Joueur detruit la Case ou il se trouve.
	 */
	public final void detruireCase() {
		final Case case_cible = this.damier.getCaseAt(this.getPos());
		if (Constantes.CONFIG.isUSEASSERT()) {
			assert case_cible.est_cassable();
		}
		this.damier.removeAllRef(case_cible, this.getPos());
	}

	/**
	 * @return la position du Joueur
	 */
	public final Position getPos() {
		return this.pos;
	}

	/**
	 * Note : Peut être redefinit.
	 * 
	 * @param pos
	 *            le pos à définir
	 */
	protected void setPos(final Position pos) {
		this.pos = pos;
	}

	/**
	 * @return le damier
	 */
	public final Damier getDamier() {
		return this.damier;
	}

	public final void setDamier(final Damier damier) {
		this.damier = damier;
	}

	/**
	 * @return le freez
	 */
	synchronized final boolean isFreez() {
		return this.freez;
	}

	/**
	 * @param freez
	 *            le freez à définir
	 */
	public synchronized final void setFreez(final boolean freez) {
		this.freez = freez;
	}

	@Override
	public abstract Personnage clone() throws CloneNotSupportedException;

}