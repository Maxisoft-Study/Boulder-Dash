package boulderdash.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import boulderdash.utils.Direction;

public final class Clavier {
	private final static BufferedReader flux = new BufferedReader(new InputStreamReader(System.in));

	private Clavier() {
	}

	public static byte saisirByte() {
		byte b = 0;
		boolean ko = true;
		while (ko) {
			try {
				b = Byte.valueOf(Clavier.flux.readLine()).byteValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un byte. Recommencez.");
			}
		}
		return b;
	}

	public static short saisirShort() {
		short s = 0;
		boolean ko = true;
		while (ko) {
			try {
				s = Short.valueOf(Clavier.flux.readLine()).shortValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un short. Recommencez.");
			}
		}
		return s;
	}

	public static int saisirInt() {
		int i = 0;
		boolean ko = true;
		while (ko) {
			try {
				i = Integer.valueOf(Clavier.flux.readLine()).intValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un int. Recommencez.");
			}
		}
		return i;
	}

	public static long saisirLong() {
		long l = 0;
		boolean ko = true;
		while (ko) {
			try {
				l = Long.valueOf(Clavier.flux.readLine()).longValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un long. Recommencez.");
			}
		}
		return l;
	}

	public static double saisirDouble() {
		double d = 0;
		boolean ko = true;
		while (ko) {
			try {
				d = Double.valueOf(Clavier.flux.readLine()).doubleValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un double. Recommencez.");
			}
		}
		return d;
	}

	public static float saisirFloat() {
		float f = 0;
		boolean ko = true;
		while (ko) {
			try {
				f = Float.valueOf(Clavier.flux.readLine()).floatValue();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un float. Recommencez.");
			}
		}
		return f;
	}

	public static char saisirChar() {
		char c = ' ';
		boolean ko = true;
		while (ko) {
			try {
				final String line = Clavier.flux.readLine();
				if (line.length() > 0) {
					c = line.charAt(line.length() - 1);
					ko = false;
				}
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas un char. Recommencez.");
			}
		}
		return c;
	}

	public static String saisirString() {
		String s = "";
		boolean ko = true;
		while (ko) {
			try {
				s = Clavier.flux.readLine();
				ko = false;
			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas une chaine. Recommencez.");
			}
		}
		return s;
	}

	public static Direction saisirDirection() {

		Direction[] tmp = Direction.values();

		final Map<String, Direction> map = new HashMap<String, Direction>(tmp.length);
		for (final Direction dir : tmp) {
			map.put(dir.name(), dir);
		}
		tmp = null;

		boolean ko = true;
		String s = "";
		while (ko) {
			try {
				s = Clavier.flux.readLine();
				s = StringUtils.upperCase(s);
				if (map.containsKey(s)) {
					ko = false;
				} else {
					throw new Exception();
				}

			} catch (final Exception e) {
				System.err.println("Erreur : la valeur saisie n'est pas une Dirrection. Recommencez.");
			}
		}
		return map.get(s);
	}
}