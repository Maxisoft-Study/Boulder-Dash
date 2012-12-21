/**
 * 
 */
package boulderdash.image;

import java.awt.image.BufferedImage;

/**
 * Inspiré du code de Mario Klingemann (incubator)
 * 
 * @author florent
 * 
 */
public class StackBlurFilter {

	private StackBlurFilter() {

	}

	public static BufferedImage flou(final BufferedImage input, final int rayon) throws Exception {
		final BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

		final int width = input.getWidth();
		final int height = input.getHeight();
		final int maxWH = Math.max(width, height);

		final int widthm = width - 1;
		final int heightm = height - 1;
		final int aire = width * height;
		final int div = 2 * rayon + 1;
		final int r[] = new int[aire];
		final int g[] = new int[aire];
		final int b[] = new int[aire];
		int rsum, gsum, bsum, p, yp, yi = 0, yw = 0;
		final int vmin[] = new int[maxWH];

		final int[] pixels = input.getRGB(0, 0, width, height, null, 0, width);

		int divsum = div + 1 >> 1;
		divsum *= divsum;
		final int dv[] = new int[256 * divsum];
		for (int i = 0; i < 256 * divsum; i++) {
			dv[i] = i / divsum;
		}

		final int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		final int r1 = rayon + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (int y = 0; y < height; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (int i = -rayon; i <= rayon; i++) {
				p = pixels[yi + Math.min(widthm, Math.max(i, 0))];
				sir = stack[i + rayon];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = rayon;

			for (int x = 0; x < width; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - rayon + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + rayon + 1, widthm);
				}
				p = pixels[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = p & 0x0000ff;

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += width;
		}
		for (int x = 0; x < width; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -rayon * width;
			for (int i = -rayon; i <= rayon; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + rayon];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < heightm) {
					yp += width;
				}
			}
			yi = x;
			stackpointer = rayon;
			for (int y = 0; y < height; y++) {
				pixels[yi] = 0xff000000 | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - rayon + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, heightm) * width;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += width;
			}
		}

		output.setRGB(0, 0, width, height, pixels, 0, width);
		return output;

	}
}