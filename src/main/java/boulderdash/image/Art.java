package boulderdash.image;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import boulderdash.Constantes;

/**
 * Quelques fonctions Pour les Images.
 */
public class Art implements Constantes {
	private Art() {
	}

	/**
	 * Returns a BufferedImage that is compatible with the current display
	 * settings.
	 * 
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @param translucency
	 *            the translucency of the image. It can be any integer from the
	 *            java.awt.Transparency class.
	 * @return The compatible BufferedImage.
	 */
	private static BufferedImage createCompatibleImage(final int width, final int height, final int translucency) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height, translucency);
	}

	/**
	 * Converts the specified image to one that is compatible with the current
	 * display settings. If it is an instance of BufferedImage, the
	 * BufferedImage's transparency is used, else the returned image will have a
	 * transparency of type TRANSLUCENT.
	 * 
	 * @param image
	 *            The image to be converted.
	 * @return The converted compatible BufferedImage.
	 */
	public static BufferedImage createCompatibleImage(final Image image) {
		return Art.createCompatibleImage(image,
				image instanceof BufferedImage ? ((BufferedImage) image).getTransparency() : Transparency.TRANSLUCENT);
	}

	/**
	 * Converts the specified image to one that is compatible with the current
	 * display settings.
	 * 
	 * @param image
	 *            The image to be converted.
	 * @param transparency
	 *            The transparency of the new image.
	 * @return The converted compatible BufferedImage.
	 */
	private static BufferedImage createCompatibleImage(final Image image, final int transparency) {
		final BufferedImage newImage = Art.createCompatibleImage(image.getWidth(null), image.getHeight(null),
				transparency);
		final Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	/**
	 * Effet Mirroir.
	 */
	public static Image mirror(final Image image, final boolean horizontally) {
		final BufferedImage newImage = Art.createCompatibleImage(image.getWidth(null), image.getHeight(null),
				image instanceof BufferedImage ? ((BufferedImage) image).getTransparency() : Transparency.BITMASK);
		final Graphics2D g = newImage.createGraphics();
		final int offset = horizontally ? -1 : 1;
		g.scale(offset, -offset);
		if (horizontally) {
			g.drawImage(image, -image.getWidth(null), 0, null);
		} else {
			g.drawImage(image, 0, -image.getHeight(null), null);
		}
		g.dispose();
		return newImage;
	}
}