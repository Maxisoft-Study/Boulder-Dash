package boulderdash;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.annotation.Nullable;

import org.apache.commons.lang.NotImplementedException;

import boulderdash.cases.Case;
import boulderdash.cases.Pierre;
import boulderdash.cases.Vide;
import boulderdash.enemy.Personnage;
import boulderdash.image.Texture;
import boulderdash.utils.Direction;
import boulderdash.utils.Generator;
import boulderdash.utils.Position;

/**
 * Représente le joueur qui est sur le Damier. Les méthodes permettent entre
 * autre une interface entre le joueur Humain et le joueur virtuel.
 */
public final class Joueur extends Personnage {

	/** True si mort. */
	private boolean mort = false;
	/** Le score du joueur */
	private int score;
	/** Le nom du joueur */
	private final String nom;

	/** Le joueur est arriver sur la case de fin. */
	public final void finDuParcours() {
		this.gaint_points(this.getDamier().getTimeDiff() * 2);
		this.getDamier().setFin();
		// System.out.println("Fin !");
	}

	/**
	 * Le joueur gagne des points .
	 * 
	 * @param nbr_point
	 *            nombre de point gagné
	 */
	public final void gaint_points(final int nbr_point) {

		this.setScore(this.getScore() + nbr_point);
		if (this.getScore() < 0) {
			this.setScore(0);
		}
	}

	// GETTER - SETTER

	/**
	 * @return le score du Joueur
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * @param score
	 *            le score à définir
	 */
	void setScore(final int score) {
		this.score = score;
	}

	/**
	 * @return le nom du Joueur
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * @return le mort
	 */
	public final boolean isMort() {
		return this.mort;
	}

	/**
	 * Affiche le Tableau d'info personnage.
	 * 
	 * @return Tableau d'info personnage
	 */
	public final String genInfoTab() {
		return "Score : " + this.getScore();

	}

	@Override
	protected char char_repr() {
		return '&';
	}

	@Override
	public void affichage(Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		final int Pas = 20;// en % => depent du Thread Sleep !
		if (this.getPos() == null) {// Tempory state
			return;
		}

		if (this.isLastDeplacementStatus()) {
			g2 = this.createAnimatedGraphic2d(g2);
		} else {
			g2 = (Graphics2D) g2.create();
		}
		g2.setColor(Color.RED);
		if (!this.isLastDeplacementStatus()) {
			if (this.getDir() == Direction.GAUCHE) {

				g2.setPaint(Texture.toad_bad_1);
			} else {
				g2.setPaint(Texture.toad_bad_0);
			}
		} else {
			if (this.dir != null) {
				switch (this.dir) {
				case HAUT:
					if (this.getAnimProgress() > 0 && this.getAnimProgress() <= 25) {
						g2.setPaint(Texture.toad_up_1);
					} else if (this.getAnimProgress() > 50 && this.getAnimProgress() <= 80) {
						g2.setPaint(Texture.toad_up_1);
					} else {
						g2.setPaint(Texture.toad_up_0);
					}
					break;
				case BAS:
					if (this.getAnimProgress() > 0 && this.getAnimProgress() <= 25) {
						g2.setPaint(Texture.toad_facing_1);
					} else if (this.getAnimProgress() > 50 && this.getAnimProgress() <= 80) {
						g2.setPaint(Texture.toad_facing_2);
					} else {
						g2.setPaint(Texture.toad_facing_0);
					}

					break;
				case DROITE:
					if (this.getAnimProgress() > 0 && this.getAnimProgress() <= 50) {
						g2.setPaint(Texture.toad_walk_rigth_1);
					} else {
						g2.setPaint(Texture.toad_idle_rigth_0);
					}
					break;
				case GAUCHE:
					if (this.getAnimProgress() > 0 && this.getAnimProgress() <= 50) {
						g2.setPaint(Texture.toad_walk_left_1);
					} else {
						g2.setPaint(Texture.toad_idle_left_0);
					}
					break;
				}
			} else {
				g2.setPaint(Texture.toad_idle_rigth_0);
			}
		}

		// update l'anim
		if (this.animProgress <= 100 - Pas && this.dir != null) {
			this.animProgress += Pas;

			final int x = this.previousPos.getX() * Constantes.CASE_EN_PIXELS;
			final int y = this.previousPos.getY() * Constantes.CASE_EN_PIXELS;

			g2.fillRect(x, y, Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);
		} else {
			g2.fillRect(pos.getX() * Constantes.CASE_EN_PIXELS, pos.getY() * Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS, Constantes.CASE_EN_PIXELS);
		}
		g2.dispose();

	}

	@Override
	protected boolean deplacement_Pierre_Possible(final Direction dir, final Position pos_pierre) {
		if (Constantes.CONFIG.isMapEditorMode()) {
			return false;
		}
		if (dir == Direction.HAUT || dir == Direction.BAS) {
			// deplacer
			// verticalement.
			return false;
		}

		if (this.damier.getCaseAt(pos_pierre).getClass() != Pierre.class) {
			// la
			// case
			// n'est
			// pas
			// une
			// pierre.
			return false;
		}

		final Position pos_derriere_pierre = Generator.genNextPos(dir, pos_pierre);

		final Case caseAt = this.damier.getCaseAt(pos_pierre);
		final int animProgress2 = caseAt.getAnimProgress();
		return !(animProgress2 > 0 && animProgress2 < 98)
				&& this.damier.getCaseAt(pos_derriere_pierre).getClass() == Vide.class;
	}

	@Override
	public void kill() {
		this.getDamier().setFin();
		this.mort = true;
		System.out.println("mort !");

	}

	@Override
	protected void setPos(final Position pos) {
		this.pos = pos;
	}

	@Override
	public void OnPlayer(final Joueur joueur) {
		throw new NotImplementedException("Pas possible !"); // stop l'appel
	}

	@Override
	public void IA() {
		throw new NotImplementedException("Pas possible !"); // stop l'appel
	}

	@Override
	public Personnage clone() {
		throw new NotImplementedException("Pas possible !"); // stop l'appel

	}

	public Joueur(@Nullable final Position init_Pos, final String nom) {
		this.nom = nom;
		this.setPos(init_Pos != null ? init_Pos : new Position());
		this.previousPos = init_Pos;
	}

}
