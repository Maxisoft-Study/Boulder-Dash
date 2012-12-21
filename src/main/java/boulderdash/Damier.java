/**
 * 
 */
package boulderdash;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import boulderdash.cases.Case;
import boulderdash.cases.Diamant;
import boulderdash.cases.Mur;
import boulderdash.cases.Pierre;
import boulderdash.cases.Spawn;
import boulderdash.cases.Vide;
import boulderdash.enemy.Personnage;
import boulderdash.image.Texture;
import boulderdash.utils.Direction;
import boulderdash.utils.Generator;
import boulderdash.utils.Position;

/**
 * Classe importante qui est composée d'une matrice de Case (Tableau de Tableaux
 * de Cases).<br/>
 * Cela permet de savoir la position spatiale de chaque Case. Ses méthodes
 * permettent le bon fonctionnement du jeu (i.e. la plupart des algorithmes
 * primordiaux sont codés dans ces méthodes).
 */
public final class Damier implements Serializable, Constantes {
	/**
	 * Contient les Case en mouvement (ie l'anim pas finit).
	 */
	private final ArrayList<Case> caseEnMouvement = new ArrayList<Case>(10);

	/**
	 * Si !=0 alors on est a la fin.
	 */
	private int animfin = 0;

	/**
	 * Liste de toutes les positions des Diamants/Pierres.
	 */
	private final SortedSet<Position> fallingObjPositionsSet = Collections.synchronizedSortedSet(new TreeSet<Position>(
			new MyReversePositionComparator()));

	/**
	 * True si il y a du changement depuis le dernier update.
	 */
	private boolean changement = true;

	/**
	 * Tous les ennemies.
	 */
	private Personnage[] all_Enemies = null;

	private static final long serialVersionUID = 5L;

	/**
	 * Compteur de temps
	 */
	private long time = System.currentTimeMillis();
	/**
	 * Compteur de temps interne. (pour mettre a jour le boolean changement)
	 */
	private int __lasttime = this.getTimeDiff();

	/**
	 * Compteur de temps interne. (pour compte le nombre de seconde en pause)
	 */
	private long pause_time = 0;

	//
	// * Note :
	// * MATRICE Par rapport a la Class Position:
	// * x
	// * .--------------->
	// * |
	// * |
	// * y|
	// * |
	// * |
	//
	/** Matrice de GameObject */
	private final Case[][] content;

	/** Pointe vers le joueur */
	private Joueur joueur;

	/** Permet de Mettre a jour le damier .<br/> */
	public final synchronized void update() {
		if (Constantes.CONFIG.isMapEditorMode()) {
			this.time = System.currentTimeMillis();
			return;
		}
		if (this.getAnimfin() != 0) {
			this.pauseGame();
			this.joueur.setFreez(true);
			return;
		}

		if (this.getTimeDiff() <= 0 && this.pause_time == 0) {
			this.getJoueur().kill();
		}

		// if (CONFIG.isLogDebug()) {
		// LOGGER.debug("Update Called .");
		// }

		final Case player_case = this.getCaseAt(this.joueur.getPos());
		player_case.OnPlayer(this.joueur);

		// call all onPlayer ennemies
		if (this.all_Enemies != null) {
			for (final Personnage enemy : this.all_Enemies) {
				if (this.getJoueur().getPos().equals(enemy.getPos())) {
					enemy.OnPlayer(this.getJoueur());
				}

				// update changement
				final int animProgress = enemy.getAnimProgress();
				if (animProgress < 98 && animProgress != 0) {
					this.setChangement(true);
				}

			}
		}
		int animProgress;
		// Update caseEnMouvement :
		for (final Iterator<Case> it = this.caseEnMouvement.iterator(); it.hasNext();) {
			final Case curr_case = it.next();
			animProgress = curr_case.getAnimProgress();
			if (animProgress > 98) {
				it.remove();
			}

		}

		animProgress = this.joueur.getAnimProgress();

		final boolean b = this.__lasttime - this.getTimeDiff() > 0.5; // Ie :
																		// force
																		// au
																		// moins
																		// 1
																		// changement
																		// par
																		// sec

		this.changement = this.changement || !this.caseEnMouvement.isEmpty() || animProgress < 99 && animProgress > 0
				|| b;
		if (b) {
			this.__lasttime = this.getTimeDiff();
		}

		if (Constantes.CONFIG.isUseThread()) {
			this.notifyAll(); // Les pierres peuvent de nouveau se deplacer
		}

	}

	/**
	 * Gestion local du Clavier.
	 * 
	 * @param event
	 */
	public final synchronized void gestionDuClavier(final KeyEvent event) {
		if (this.getAnimfin() != 0) {
			return;
		}
		final int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			this.joueur.seDeplace(Direction.DROITE);
		} else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_A) {
			this.joueur.seDeplace(Direction.GAUCHE);
		} else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_Z || keyCode == KeyEvent.VK_W) {
			this.joueur.seDeplace(Direction.HAUT);
		} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			this.joueur.seDeplace(Direction.BAS);
		} else if (keyCode == KeyEvent.VK_DELETE) {
			this.getJoueur().kill();
		}
	}

	/**
	 * Gestion local des click.
	 * 
	 * @param clicked_pos
	 *            la position du click.
	 */
	public final synchronized void gestionClick(final Position clicked_pos) {
		if (this.getAnimfin() != 0) {
			return;
		}
		// Clone afin de ne pas alterer.
		final Position diffPos = clicked_pos.clone();
		final Position joueurPos = this.joueur.getPos().clone();
		// Si map editor alors on teleporte
		if (Constantes.CONFIG.isMapEditorMode()) {
			diffPos.div(Constantes.CASE_EN_PIXELS);
			this.joueur.setPos(diffPos);
			return;
		}

		// Convert Joueur Pos to PIxel Pos
		joueurPos.mul(Constantes.CASE_EN_PIXELS);

		// Centre
		joueurPos.add(new Position(Constantes.CASE_EN_PIXELS / 2, Constantes.CASE_EN_PIXELS / 2));

		diffPos.sub(joueurPos);

		// Conversion Position en dir.
		Direction toMove;
		final int x = diffPos.getX();
		final int y = diffPos.getY();
		boolean deplacementFait = false;
		boolean result_deplacement;
		if (x == y) {
			return;
		}
		if (Math.abs(x) >= Math.abs(y)) {
			toMove = x > 0 ? Direction.DROITE : Direction.GAUCHE;
			result_deplacement = this.joueur.seDeplace(toMove);
			deplacementFait = true;

			// check deplacement ok
			if (Math.abs(y) > Constantes.CASE_EN_PIXELS * 0.98 && !result_deplacement) {
				toMove = y > 0 ? Direction.BAS : Direction.HAUT;
				deplacementFait = false;
			}
		} else {
			toMove = y > 0 ? Direction.BAS : Direction.HAUT;
			result_deplacement = this.joueur.seDeplace(toMove);
			deplacementFait = true;

			// check deplacement ok
			if (Math.abs(x) > Constantes.CASE_EN_PIXELS * 0.98 && !result_deplacement) {
				toMove = x > 0 ? Direction.DROITE : Direction.GAUCHE;
				deplacementFait = false;
			}
		}
		if (!deplacementFait) {
			this.joueur.seDeplace(toMove);
		}
	}

	// le dessin graphique du jeu
	/**
	 * Lance l'appel de la methode affichage de chaque case.
	 * 
	 * @param g
	 */
	public final synchronized void affichage(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		if (Constantes.CONFIG.isUseAA()) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		if (this.getAnimfin() == 0) {

			Case current_case;
			Position current_pos;
			for (int i = 0; i < this.content.length; i++) {
				for (int j = 0; j < this.content[i].length; j++) {
					current_pos = new Position(j, i);
					current_case = this.getCaseAt(current_pos);
					current_case.affichage(g2, current_pos, 20, 20);
				}
			}
			// Paint Player
			this.joueur.affichage(g2, this.joueur.getPos(), 15, 20);

			// Paint Enemies
			if (this.all_Enemies != null) {
				for (final Personnage enemy : this.all_Enemies) {
					enemy.affichage(g2, enemy.getPos(), 0, 0);
				}
			}
			if (!Constantes.CONFIG.isMapEditorMode()) {
				// paint Score - time conteneur
				Graphics2D g_score = (Graphics2D) g2.create();
				g_score.setPaint(Texture.showDiam);
				g_score.translate(this.getLargeur() * Constantes.CASE_EN_PIXELS - Texture.showDiam_len_x, 0);
				g_score.fillRect(0, 0, Texture.showDiam_len_x, Texture.showDiam_len_y);
				g_score.dispose();

				// Paint Score
				g_score = (Graphics2D) g2.create();// reset
				String str = String.valueOf(this.getJoueur().getScore());
				g_score.setColor(Color.WHITE);
				g_score.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g_score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
				final FontMetrics fm = g.getFontMetrics();
				int x = this.getLargeur() * Constantes.CASE_EN_PIXELS - Texture.showDiam_len_x - fm.stringWidth(str)
						/ 2 + 65;
				int y = fm.getHeight() + 12;
				g_score.drawString(str, x, y);
				g_score.dispose();

				// paint time
				final Graphics2D g_time = (Graphics2D) g2.create();// reset
				str = String.valueOf(this.getTimeDiff());
				g_time.setColor(Color.WHITE);
				g_time.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g_time.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
				x = this.getLargeur() * Constantes.CASE_EN_PIXELS - Texture.showDiam_len_x - fm.stringWidth(str) / 2
						+ 150;
				y = fm.getHeight() + 12;
				g_time.drawString(str, x, y);
				g_time.dispose();
			}
		}
	}

	/**
	 * Algo de Chute de pierre.
	 */
	synchronized final void detectAndMoveFallingObject() {
		if (Constantes.CONFIG.isMapEditorMode()) {
			return;
		}
		// locales
		final ArrayList<Position> toadd = new ArrayList<Position>(this.getLargeur() * this.getHauteur() / 3);// Les
																												// positions
																												// modifiées
		@SuppressWarnings("rawtypes")
		final Class[] diagonal_objClasses = new Class[] { Pierre.class, Diamant.class, Mur.class };// Les
																									// class
																									// des
																									// objets
																									// qui
																									// permettent
																									// a
																									// la
																									// pierre
																									// de
																									// continuer
																									// a
																									// tomber
																									// a
																									// droite/gauche
		final Direction[] randdir_choice1 = new Direction[] { Direction.DROITE, Direction.GAUCHE };
		final Direction[] randdir_choice2 = new Direction[] { Direction.GAUCHE, Direction.DROITE };
		Direction[] randdir;
		Case case_dessous;
		Case current_case;
		Position pos_case_dessous;
		Position curr_position;
		Position tmp_randPos;
		Position pos_case_diag;
		Case case_diag;
		final Position pos = this.joueur.getPos();
		final Position pos_joueur = pos;
		Case tmp_randCase;
		boolean last_falling_state;

		for (final Iterator<Position> iterator = this.fallingObjPositionsSet.iterator(); iterator.hasNext();) {
			curr_position = iterator.next();
			if (curr_position == null) { // Pas possible normalement.
				iterator.remove();
				continue;
			}
			current_case = this.getCaseAt(curr_position);

			if (current_case.est_fixe()) {
				iterator.remove();
				continue;
			}

			if (Constantes.CONFIG.isUseThread() && current_case.getAnimProgress() < 85) {
				continue;// attendre la fin de l'anim
			}

			last_falling_state = current_case.isFalling();
			current_case.setFalling(false); // Reset du booleen faling

			// On regarde la case en dessous.
			pos_case_dessous = curr_position.clone();
			pos_case_dessous.add(Direction.BAS.asDeltaPosition());
			case_dessous = this.getCaseAt(pos_case_dessous);

			if (!case_dessous.bloque_Chute_Pierre() && !(!last_falling_state && pos_joueur.equals(pos_case_dessous))) {// Le
																														// rocher
																														// tombe
																														// si
																														// le
																														// joueur
																														// ne
																														// bloque
																														// pas
																														// la
																														// chute
																														// &
																														// que
																														// la
																														// case
																														// en
																														// dessous
																														// ne
																														// bloque
																														// pas.
				this.changement = true;
				pos_case_dessous = this.pousserLaPierre(curr_position.clone(), Direction.BAS);
				if (Constantes.CONFIG.isUSEASSERT()) {
					assert this.getCaseAt(curr_position).getClass() == Vide.class;
					assert !this.getCaseAt(pos_case_dessous).est_fixe();
				}

				iterator.remove();

				if (pos_case_dessous.equals(this.getJoueur().getPos())) {// Le
																			// joueur
					// est mort
					// !
					this.update();
				}

				toadd.add(pos_case_dessous);

				continue;
			}
			// Sinon.

			// on prend un set de pos aleatoire.
			randdir = Math.random() > 0.5d ? randdir_choice1 : randdir_choice2;
			// tout d'habord on regarde si le joueur ne bloque pas puis on
			// cherche la case a droite/gauche de la case de la
			// pierre
			for (final Direction direction : randdir) {
				tmp_randPos = Generator.genNextPos(direction, curr_position);
				if (pos.equals(tmp_randPos)) {
					continue;// le joueur bloque
				}
				tmp_randCase = this.getCaseAt(tmp_randPos);
				// Puis On regarde la position de la case en diagonale
				pos_case_diag = Generator.genNextPos(Direction.BAS, tmp_randPos);
				case_diag = this.getCaseAt(pos_case_diag);
				if (!tmp_randCase.bloque_Chute_Pierre() && !case_diag.bloque_Chute_Pierre()
						&& ArrayUtils.contains(diagonal_objClasses, case_dessous.getClass())) { // Alors
																								// on
																								// pousse
																								// la
																								// pierre
																								// a
																								// droite/gauche
					this.changement = true;

					// this._fallingObjPositionsSet.add(tmp_randPos);
					tmp_randPos = this.pousserLaPierre(curr_position, direction);
					iterator.remove();
					if (tmp_randPos.equals(this.joueur.getPos())) {
						this.update();
					}

					toadd.add(tmp_randPos);

					break;
				}
			}

		}
		this.fallingObjPositionsSet.addAll(toadd);

		if (Constantes.CONFIG.isUseThread()) {
			try {
				this.wait(); // On attent la prochaine update()
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Initialise fallingObjPositionsSet .
	 * 
	 */
	final void init_checkedPosition() {

		for (int i = 0; i < this.content.length; i++) {
			for (int j = 0; j < this.content[i].length; j++) {
				if (!this.content[i][j].est_fixe()) {
					this.fallingObjPositionsSet.add(new Position(j, i));
				}
			}
		}
	}

	/**
	 * Permet de Pousser la Pierre.<br/>
	 * Note : Attention Les parametres doivent etre valide !
	 */
	private synchronized Position pousserLaPierre(final Position pos_pierre, final Direction dir) {
		final Case case_pierre = this.getCaseAt(pos_pierre);

		this.caseEnMouvement.add(case_pierre);

		case_pierre.setFalling(true);
		if (Constantes.CONFIG.isUSEASSERT()) {
			assert !case_pierre.est_fixe();
		}
		case_pierre.setAnimProgress(0);
		case_pierre.setPreviousPos(pos_pierre.clone());
		case_pierre.setDir(dir);

		this.setCaseVide(case_pierre, pos_pierre);
		pos_pierre.add(dir.asDeltaPosition()); // UPDATE de la Position
		this.setCase(case_pierre, pos_pierre);
		this.setChangement(true);
		return pos_pierre;

	}

	/**
	 * Permet de Pousser la Pierre et met a jour le damier.<br/>
	 * Note : Attention Les parametres doivent etre valide !
	 */
	public final synchronized void pousserLaPierreAndUpdate(final Position pos_pierre, final Direction dir) {
		final Position old_pos = pos_pierre.clone();
		this.pousserLaPierre(pos_pierre, dir); // apres la pos_pierre est
												// changer

		// Update manually Pierre

		this.fallingObjPositionsSet.remove(old_pos);
		this.fallingObjPositionsSet.add(pos_pierre);

	}

	/**
	 * Detruit la case (ie remplace la case par la Case vide).<br/>
	 * Enleve toute ref a la case cible existante dans la classe en cours.
	 * 
	 * @param case_cible
	 * @param pos
	 */
	public final synchronized void removeAllRef(final Case case_cible, final Position pos) {

		this.fallingObjPositionsSet.remove(pos);

		this.caseEnMouvement.remove(case_cible);

		this.setCaseVide(case_cible, pos);
	}

	/**
	 * Met le jeu en pause.<br/>
	 * ie plus de compteur de temp.
	 */
	public final void pauseGame() {
		if (this.pause_time == 0) {
			this.pause_time = System.currentTimeMillis();
		} else {
			this.time += System.currentTimeMillis() - this.pause_time;
			this.pause_time = 0;
		}
	}

	/**
	 * Met le joueur sur la case Depart.
	 */
	final void placerJoueurSpawn() {

		// local
		Position current_pos;
		Case current_case;

		// on cherche le spanw.

		for (int i = 0; i < this.content.length; i++) {
			for (int j = 0; j < this.content[i].length; j++) {
				current_pos = new Position(j, i);
				current_case = this.getCaseAt(current_pos);
				if (current_case.getClass() == Spawn.class) {
					this.joueur.setPos(current_pos);
					return;
				}
			}
		}
		// if not found
		throw new IllegalStateException("Il n'y a pas de spawn ...");

	}

	/**
	 * Calcul du temps restant pour le niveau.
	 * 
	 * @return temps restant
	 */
	public final int getTimeDiff() {
		return 300 - (int) ((System.currentTimeMillis() - this.time) / 1000);
	}

	/**
	 * @return le animfin
	 */
	public final int getAnimfin() {
		return this.animfin;
	}

	public final void setCase(final Case case_, final Position pos) {
		this.content[pos.getY()][pos.getX()] = case_;
	}

	/**
	 * Detruit la case (ie remplace la case par la Case vide).
	 * 
	 * @param case_cible
	 * @param pos
	 */
	private void setCaseVide(final Case case_cible, final Position pos) {

		if (Constantes.CONFIG.isUSEASSERT()) {
			assert this.content[pos.getY()][pos.getX()] == case_cible;
		}

		this.content[pos.getY()][pos.getX()] = Case.VIDE_CASE;
	}

	public final Case getCaseAt(final Position pos) {
		return this.content[pos.getY()][pos.getX()];
	}

	/**
	 * @return le joueur
	 */
	public final Joueur getJoueur() {
		return this.joueur;
	}

	/**
	 * @param joueur
	 *            le joueur à définir
	 */
	public final void setJoueur(final Joueur joueur) {
		this.joueur = joueur;
	}

	/**
	 * @return le fallingObjPositionsSet
	 */
	public final SortedSet<Position> getFallingObjPositionsSet() {
		return this.fallingObjPositionsSet;
	}

	/**
	 * @return content
	 */
	public final synchronized Case[][] getContent() {
		return this.content;
	}

	/**
	 * @return le changement
	 */
	public synchronized final boolean isChangement() {
		return this.changement;
	}

	/**
	 * @param changement
	 *            le changement à définir
	 */
	public synchronized final void setChangement(final boolean changement) {
		this.changement = changement;
	}

	/**
	 * @return le all_Enemies
	 */
	public final Personnage[] getAll_Enemies() {
		return this.all_Enemies;
	}

	public final int getLargeur() {
		return this.content[0].length;
	}

	public final int getHauteur() {
		return this.content.length;
	}

	public final void setFin() {
		this.animfin++;
	}

	/**
	 * Permet de construire une Array de String represantant le Damier.<br/>
	 * Chaque String de l'array correspond a une ligne du damier.
	 * 
	 * @return Array de String represantant le Damier.
	 */
	private String[] toStringArray() {
		String[] ret;
		final char sep = '|'; // Caract�re s�parateur.
		final char space = ' ';
		final String caseString = "% " + sep + space; // '%' sera remplacer;

		// Pour savoir si le dernier char est un espace.
		byte nbrdeCaseaenlever = 0;
		if (caseString.charAt(caseString.length() - 1) == space) {
			nbrdeCaseaenlever = (byte) (caseString.length() - caseString.indexOf(sep) - 1);
		}

		ret = new String[this.content.length * 2 + 2];

		// 1er Colonne
		for (int i = 2; i < ret.length; i += 2) {
			ret[i] = "T" + caseString.replace('%', '>');
		}

		// Appelle la m�thode toChar de chaque case (g�n�ration lignes pair)

		final int posduJoueur_x = this.joueur.getPos().getX();
		final int posduJoueur_y = this.joueur.getPos().getY();
		for (int i = 0; i < this.content.length; i++) {
			for (int j = 0; j < this.content[i].length; j++) {
				if (posduJoueur_x == j && posduJoueur_y == i) {
					ret[i * 2 + 2] += caseString.replace('%', this.joueur.toString().charAt(0));
					continue;
				}

				ret[i * 2 + 2] += caseString.replace('%', this.content[i][j].toString().charAt(0));
			}
		}

		// Ajoute les "--+" aux ligne impair
		for (int i = 1; i < ret.length; i += 2) {
			ret[i] = ""; // initialisation
			int compteur = caseString.length();
			for (int j = 0; j < ret[2].length() - nbrdeCaseaenlever; j++) {
				compteur++;
				if (compteur % caseString.length() == 0) {
					// Si vrai, alors on est au dessus du s�parateur '|'
					ret[i] += '+';
				} else {
					ret[i] += '-';
				}
			}
		}

		// G�n�ration ligne 1
		ret[0] = ""; // initialisation

		// Permet de mettre les espaces n�cessaire avant le premier chiffre.
		for (int i = 0; i <= ret[2].indexOf(sep); i++) {
			ret[0] += " ";
		}

		// En 2 For; permet de mettre des 0 devant les chiffres ie( 1->"01" )
		for (int i = 1; i < 10 && i < this.content[0].length + 1; i++) {
			ret[0] += StringUtils.center(String.valueOf(0) + String.valueOf(i), caseString.length());
		}
		for (int i = 10; i < this.content[0].length + 1; i++) {
			ret[0] += StringUtils.center(String.valueOf(i), caseString.length());
		}

		// Fin g�n�ration premiere ligne

		return ret;
	}

	@Override
	public final String toString() {
		String ret = "";
		final String[] tmp = this.toStringArray();
		for (final String aTmp : tmp) {
			ret += aTmp + "\n";
		}
		return ret;
	}

	public Damier(final Case[][] content, final Joueur joueur, @Nullable final Personnage[] all_Enemies) {
		this.content = content;
		this.joueur = joueur;
		this.joueur.setDamier(this);
		this.init_checkedPosition();
		this.all_Enemies = all_Enemies;
		// update Ennemies
		if (all_Enemies != null) {
			for (final Personnage enemy : all_Enemies) {
				enemy.setDamier(this);
			}
		}

		// Set Joueur Pos
		if (Constantes.CONFIG.isMapEditorMode()) {
			try {
				this.placerJoueurSpawn();
			} catch (final Exception e) {
				Constantes.LOGGER.fatal("", e);
			}
		} else {
			this.placerJoueurSpawn();
		}

	}

	/**
	 * Permet de faire tomber les cases une a une.
	 */
	private final class MyPositionComparator implements Comparator<Position> {

		@Override
		public int compare(final Position o1, final Position o2) {
			if (o1.getY() > o2.getY()) {
				return +1;
			}
			if (o1.getY() == o2.getY()) {
				if (o1.getX() == o2.getX()) {
					return 0;
				}
				if (o1.getX() > o2.getY()) {
					return -1;
				}
				return +1;
			}
			return -1;
		}

	}

	/**
	 * 
	 */
	final public class MyStandarPositionComparator implements Comparator<Position> {

		@Override
		public int compare(final Position o1, final Position o2) {
			if (o1.getY() > o2.getY()) {
				return -1;
			}
			if (o1.getY() == o2.getY()) {
				if (o1.getX() == o2.getX()) {
					return 0;
				}
				if (o1.getX() > o2.getY()) {
					return -1;
				}
				return +1;
			}
			return +1;
		}

	}

	/**
	 * Permet de faire tomber les cases en bloc.
	 */
	final public class MyReversePositionComparator implements Comparator<Position> {
		private final Comparator<Position> c = new MyPositionComparator();

		@Override
		public int compare(final Position o1, final Position o2) {

			return this.c.compare(o1, o2) * -1;
		}

	}

}
