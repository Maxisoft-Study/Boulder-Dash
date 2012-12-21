package boulderdash.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestion des HighScores en Sqlite.
 * 
 */
public final class Stats {
	private Stats() {
	}

	/**
	 * La connection a la Bdd.
	 */
	private static Connection connection = null;
	static {
		try {
			Class.forName("org.sqlite.JDBC");
			Stats.connection = DriverManager.getConnection("jdbc:sqlite:boulderdash.db");
		} catch (final SQLException e) {
		} catch (final ClassNotFoundException e) {
		}

	}

	/**
	 * Definir qui a jouer en dernier.
	 * 
	 * @param nom
	 *            le nom du joueur
	 * @return 0 si erreur, nombre d'insertion sinon.
	 */
	public static int setLastNom(final String nom) {
		try {
			final Statement statement = Stats.connection.createStatement();
			statement.executeUpdate("delete from lastPlayer");
			statement.close();
			final String sql = "insert or replace into lastPlayer values(?, strftime('%s', 'now'))";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, nom);
			final int tmp = preparedStatement.executeUpdate();
			preparedStatement.close();
			return tmp;

		} catch (final SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 
	 * @return qui a jouer en dernier.
	 */
	public static String getLastNom() {
		try {
			final Statement statement = Stats.connection.createStatement();
			final ResultSet rs = statement
					.executeQuery("SELECT lastPlayer.Nom AS Nom FROM lastPlayer WHERE strftime('%s', 'now') + 10 * 60 * 100 > lastPlayer.Time ORDER BY lastPlayer.Time ASC LIMIT 1");

			final String ret = rs.getString("Nom");
			statement.close();
			return ret;

		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Set La derniere map jouer par le joueur.
	 * 
	 * @param mapHash
	 *            le hash de la map.
	 * @param player
	 *            le nom du joueur.
	 * @return 0 si erreur, nombre d'insertion sinon.
	 */
	public static int setLastMap(final String mapHash, final String player) {
		try {
			final String sql = "insert or replace into LastMap values(?, ?)";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, mapHash);
			preparedStatement.setString(2, player);
			final int tmp = preparedStatement.executeUpdate();
			preparedStatement.close();
			return tmp;

		} catch (final SQLException e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 
	 * @param player
	 *            le nom du joueur.
	 * @return la derniere map jouer par le joueur ou null.
	 */
	public static String getLastMap(final String player) {
		try {
			final String sql = "SELECT lastMap.Map AS Map FROM lastMap WHERE lastMap.Player = ? LIMIT 1";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, player);
			final ResultSet rs = preparedStatement.executeQuery();

			final String ret = rs.getString("Map");
			preparedStatement.close();
			return ret;

		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Inserer un nouveau score.
	 * 
	 * @param nom
	 *            le nom du joueur.
	 * @param score
	 *            le score.
	 * @param MapHash
	 *            le hash de la map.
	 * @return 0 si erreur, nombre d'insertion sinon.
	 */
	public static int insertNewScore(final String nom, final int score, final String MapHash) {
		try {
			final String sql = "INSERT INTO Score ('Nom', 'Score', 'Time', 'MapHash') VALUES (?, ?, strftime('%s', 'now'), ?)";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, nom);
			preparedStatement.setInt(2, score);
			preparedStatement.setString(3, MapHash);
			final int tmp = preparedStatement.executeUpdate();
			preparedStatement.close();
			return tmp;

		} catch (final Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param MapHash
	 *            le hash de la map.
	 * @return le meilleur score.
	 */
	public static int getBestScore(final String MapHash) {
		try {
			final String sql = "SELECT Max(Score.Score) as Max FROM Score WHERE Score.MapHash = ? LIMIT 1";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, MapHash);
			final ResultSet rs = preparedStatement.executeQuery();
			final int tmp = rs.getInt("Max");
			preparedStatement.close();
			return tmp;

		} catch (final Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 
	 * @param nom
	 *            le nom du joueur.
	 * @param MapHash
	 *            MapHash le hash de la map.
	 * @return le meilleur score du joueur
	 */
	public static int getBestScorePlayer(final String nom, final String MapHash) {
		try {
			final String sql = "SELECT Max(Score.Score) as Max FROM Score WHERE Score.MapHash = ? AND Score.Nom = ? LIMIT 1";
			final PreparedStatement preparedStatement = Stats.connection.prepareStatement(sql);
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, MapHash);
			final ResultSet rs = preparedStatement.executeQuery();
			final int tmp = rs.getInt("Max");
			preparedStatement.close();
			return tmp;

		} catch (final Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
