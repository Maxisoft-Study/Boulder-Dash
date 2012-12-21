/**
 * 
 */
package boulderdash.enemy;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import boulderdash.Constantes;
import boulderdash.Damier;
import boulderdash.Joueur;
import boulderdash.cases.Case;
import boulderdash.cases.Diamant;
import boulderdash.cases.Exit;
import boulderdash.cases.RainBow;
import boulderdash.image.Texture;
import boulderdash.utils.Direction;
import boulderdash.utils.Position;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * L'ennemies nyan cat.<br/>
 * C'est un chat qui mange les diamants et laisse des arc en ciel derriere lui.
 */
public class NyanCat extends Personnage {

	// public static final HashMap<NyanCat, Position> TARGET_POS = new
	// HashMap<NyanCat, Position>();
	/**
	 * Position des Diamants.
	 */
	private ArrayList<Position> DiamentPos = null;
	/**
	 * Utiliser comme compteur pour attendre. (depent de la vitesse)
	 */
	private int tick = 0;
	/**
	 * La vitesse .
	 */
	private int speed = 1;
	/**
	 * Position Cible.
	 */
	private Position targetPos;

	/**
	 * @return le speed
	 */
	public final int getSpeed() {
		return this.speed;
	}

	/**
	 * 
	 */
	public NyanCat(final Position pos) {
		this.pos = pos;
	}

	/**
	 * Ne deplace pas les Pierres !
	 */
	@Override
	protected boolean deplacement_Pierre_Possible(final Direction dir, final Position pos_pierre) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.enemy.Personnage#kill()
	 */
	@Override
	public void kill() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.GameObject#char_repr()
	 */
	@Override
	protected char char_repr() {
		return 'C';
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see boulderdash.GameObject#affichage(java.awt.Graphics,
	 * boulderdash.utils.Position, int, int)
	 */
	@Override
	public final void affichage(Graphics2D g2, final Position pos, final int largeur, final int hauteur) {
		final Graphics2D old_g2 = g2;
		if (this.isLastDeplacementStatus()) {
			g2 = this.createAnimatedGraphic2d(g2);
		} else {
			g2 = (Graphics2D) g2.create();
		}
		g2.setColor(Color.RED);

		if (this.dir != null) {
			switch (this.dir) {
			case HAUT:
				g2.setPaint(Texture.nyan_rigth_0);
				break;
			case BAS:
				g2.setPaint(Texture.nyan_left_0);
				break;
			case DROITE:
				g2.setPaint(Texture.nyan_rigth_0);
				break;
			case GAUCHE:
				g2.setPaint(Texture.nyan_left_0);
				break;
			}
		} else {
			g2.setPaint(Texture.nyan_rigth_0);
		}

		final int Pas = 25;// en % => depent du Thread Sleep !

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

		// repaint Diamant
		if (this.targetPos == null) {
			return;
		}
		final Case caseAt = this.getDamier().getCaseAt(this.targetPos);

		if (caseAt.getClass() == Diamant.class) {
			final TexturePaint tmp = Texture.diamant_0;
			Texture.diamant_0 = Texture.diamant_1;
			caseAt.affichage(old_g2, this.targetPos, 0, 0);
			Texture.diamant_0 = tmp;
		}
	}

	@Override
	public void OnPlayer(final Joueur joueur) {
		joueur.kill();

	}

	@Override
	protected final boolean deplacementPossible(final Position pos_cible) {
		final int x = pos_cible.getX();
		final int y = pos_cible.getY();
		return !(x < 0 || x >= this.damier.getLargeur() || y < 0 || y >= this.damier.getHauteur());
	}

	@Override
	public void IA() {
		this.tick += this.speed * 10;

		if (this.tick < 100) {
			return;
		}
		// else
		this.tick = 10; // reset

		final Damier damier2 = this.getDamier();

		if (this.DiamentPos == null) {// then build
			Collection<Position> tmp;
			tmp = new ArrayList<Position>(damier2.getFallingObjPositionsSet());
			tmp = Collections2.filter(tmp, new DiamantPredicate());
			this.DiamentPos = new ArrayList<Position>(tmp);// on recrée car tmp
															// n'est plus
															// focement une
															// arraylist
			Collections.reverse(this.DiamentPos);
		}

		if (!this.DiamentPos.isEmpty()) {
			this.targetPos = this.DiamentPos.get(this.DiamentPos.size() - 1);
			if (this.targetPos == null) {
				this.DiamentPos = null;
				return;
			}
		} else {
			this.targetPos = this.damier.getJoueur().getPos();
		}

		// TARGET_POS.put(this, targetPos);

		if (this.targetPos.equals(this.getPos())) {// ennemie sur la position
													// voulu
			this.DiamentPos = null;// recalc
			return;
		}
		final Position diffPos = this.targetPos.clone();
		diffPos.sub(this.getPos());

		// Conversion Position en dir.
		Direction toMove;
		final int x = diffPos.getX();
		final int y = diffPos.getY();

		if (Math.abs(x) >= Math.abs(y)) {
			toMove = x > 0 ? Direction.DROITE : Direction.GAUCHE;

		} else {
			toMove = y > 0 ? Direction.BAS : Direction.HAUT;
		}

		this.seDeplace(toMove);

		final Case caseAt = damier2.getCaseAt(this.getPos());

		if (this.speed < Constantes.MAX_ENNEMY_SPEED && caseAt.getClass() == Diamant.class) {
			this.speed += 1;
		}

		if (caseAt.getClass() == Exit.class) {
			return;
		}

		damier2.removeAllRef(caseAt, this.getPos());
		damier2.setCase(RainBow.instance.clone(), this.getPos());

	}

	@Override
	public Personnage clone() {
		return new NyanCat(this.getPos());

	}

	private class DiamantPredicate implements Predicate<Position> {

		@Override
		public boolean apply(@Nullable final Position arg0) {
			return NyanCat.this.damier.getCaseAt(arg0).getClass() == Diamant.class;
		}

	}

}
