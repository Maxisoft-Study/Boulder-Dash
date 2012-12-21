/**
 * 
 */
package boulderdash.utils;

import java.util.ArrayList;
import java.util.Random;

import boulderdash.Damier;
import boulderdash.Joueur;
import boulderdash.cases.Case;
import boulderdash.enemy.Personnage;

/**
 * Permet de generer des int, Positions, Damiers et Cases.
 */
public final class Generator {
	private static final Random rand = new Random();

	/** @return Direction Aleatoire. */
	public static Direction random_Direction() {
		final Direction[] tmp = Direction.values();
		return tmp[Generator.rand.nextInt(tmp.length)];
	}

	/** @see java.util.Random#nextInt(int) */
	public static int random_int(final int n) {
		return Generator.rand.nextInt(n);
	}

	/**
	 * Calcule une nouvelle Position a partir d'une direction
	 * 
	 * @param dir
	 * @param pos
	 * @return nouvelle Position
	 */
	public static Position genNextPos(final Direction dir, final Position pos) {
		final Position ret = pos.clone();
		ret.add(dir.asDeltaPosition());
		return ret;
	}

	/**
	 * Retourne les positions autours de la position fournis en parametre.<br/>
	 * Considere que les composantes des positions sont obligatoirement compris
	 * entre min_coor et max_coord
	 * 
	 * @param pos
	 * @param min_coord
	 * @param max_coord
	 */
	public static Position[] closedPos(final Position pos, final int min_coord, final int max_coord) {
		final int[] toaddTab = new int[] { -1, 0, 1 };// liste des chiffres a
														// ajouter
		// pour trouver les pos:)

		final ArrayList<Position> tmp_positions = new ArrayList<Position>(9);
		Position tmp_pos = pos.clone();

		for (int toaddx : toaddTab) {
			for (int toaddy : toaddTab) {
				tmp_pos.add(new Position(toaddx, toaddy));
				toaddx = tmp_pos.getX();// re use var
				toaddy = tmp_pos.getY();// re use var
				if (toaddx >= min_coord && toaddx <= max_coord && toaddy >= min_coord && toaddy <= max_coord) {
					tmp_positions.add(tmp_pos);
				}
				tmp_pos = pos.clone(); // reset
			}
		}

		return tmp_positions.toArray(new Position[tmp_positions.size()]);

	}

	/** @see java.util.Random#nextBoolean() */
	public static boolean random_boolean() {
		return Generator.rand.nextBoolean();
	}

	/**
	 * Genere une Case Aleatoirement.
	 * 
	 * @return Case aleatoire.
	 */
	public static Case seek_Case() {
		return Case.all_standar_instance[Generator.rand.nextInt(Case.all_standar_instance.length)];
	}

	/**
	 * Genere le premier Damier
	 * 
	 * @param x
	 *            long
	 * @param y
	 *            hauteur
	 * @return nouveau Damier
	 */
	public static Damier gen_FirstDamier(final int x, final int y, final Joueur j) {
		return Generator.gen_Damier(x, y, j);
	}

	/**
	 * Genere un damier avec un joueur deja existant.
	 * 
	 * @param x
	 *            long
	 * @param y
	 *            hauteur
	 * @param joueur
	 * @return nouveau Damier
	 */
	public static Damier gen_Damier(final int x, final int y, final Joueur joueur) {
		return Generator.gen_Damier(x, y, joueur, null);
	}

	public static Damier gen_Damier(final int x, final int y, final Joueur joueur, final Personnage[] ennemies) {
		final Case[][] tmp = new Case[x][y];
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				tmp[i][j] = Generator.seek_Case().clone();
			}
		}
		// Overwrite with Mur
		// Horizontal
		for (int i = 0; i < tmp[0].length; i++) {
			tmp[0][i] = Case.MUR_CASE;
			tmp[tmp.length - 1][i] = Case.MUR_CASE;
		}
		// Vertical
		for (int i = 1; i < tmp.length - 1; i++) {
			tmp[i][0] = Case.MUR_CASE;
			tmp[i][tmp[0].length - 1] = Case.MUR_CASE;
		}

		// Overwrite with SPAWN & EXIT Case
		final Position initial_Pos = new Position(Generator.random_int(3) + 1, Generator.random_int(3) + 1);
		tmp[initial_Pos.getY()][initial_Pos.getX()] = Case.SPAWN_CASE;
		tmp[x - 1 - Generator.random_int(3)][y - 1 - Generator.random_int(3)] = Case.EXIT_CASE;

		// Update joueur pos
		final Position pos_joueur = joueur.getPos();
		pos_joueur.setX(initial_Pos.getX());
		pos_joueur.setY(initial_Pos.getY());
		return new Damier(tmp, joueur, ennemies);
	}

	private Generator() {
	}
}
