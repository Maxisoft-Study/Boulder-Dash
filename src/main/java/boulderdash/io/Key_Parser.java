/**
 * 
 */
package boulderdash.io;

import org.apache.commons.lang.StringUtils;

import boulderdash.config.ConfigKey;
import boulderdash.utils.Direction;

/**
 * 
 */
public class Key_Parser {

	/**
	 * 
	 */
	private Key_Parser() {
	}

	public static Direction saisirDirection(final ConfigKey config_key) {
		String s = "";
		while (StringUtils.isBlank(s)) {
			s = StringUtils.lowerCase(Clavier.saisirString());
		}
		final char key = s.charAt(0);

		for (int i = 0; i < 10; i++) {// 5 chances

			if (key == config_key.getKey_moveup()) {
				return Direction.HAUT;
			}
			if (key == config_key.getKey_movedown()) {
				return Direction.BAS;
			}
			if (key == config_key.getKey_moveleft()) {
				return Direction.GAUCHE;
			}
			if (key == config_key.getKey_moverigth()) {
				return Direction.DROITE;
			}

			System.err.println("Entrez une touche correcte ...");
			s = StringUtils.lowerCase(Clavier.saisirString());
		}

		return null;

	}

	/**
	 * Lecture d'une direction.
	 * 
	 * @return la Direction Lu
	 */
	public static Direction saisirDirection() {
		return Key_Parser.saisirDirection(new ConfigKey());
	}

}
