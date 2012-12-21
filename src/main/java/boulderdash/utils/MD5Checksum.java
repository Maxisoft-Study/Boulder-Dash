package boulderdash.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Calcul du MD5 d'un fichier.
 */
public final class MD5Checksum {

	private MD5Checksum() {
	}

	private static byte[] createChecksum(final String filename) throws Exception {
		final InputStream fis = new FileInputStream(filename);

		final byte[] buffer = new byte[1024];
		final MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		}
		while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(final String filename) throws Exception {
		final byte[] b = MD5Checksum.createChecksum(filename);
		String result = "";

		for (final byte element : b) {
			result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

}