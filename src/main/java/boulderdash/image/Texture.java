package boulderdash.image;

import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import boulderdash.Constantes;

public class Texture implements Constantes {
	public static TexturePaint nyan_rigth_0 = null;
	public static TexturePaint nyan_left_0 = null;

	public static TexturePaint toad_idle_rigth_0 = null;
	public static TexturePaint toad_idle_left_0 = null;
	public static TexturePaint toad_up_0 = null;
	public static TexturePaint toad_up_1 = null;
	public static TexturePaint toad_facing_0 = null;
	public static TexturePaint toad_facing_1 = null;
	public static TexturePaint toad_facing_2 = null;
	public static TexturePaint toad_bad_0 = null;
	public static TexturePaint toad_bad_1 = null;
	public static TexturePaint toad_walk_rigth_1 = null;
	public static TexturePaint toad_walk_left_1 = null;

	public static TexturePaint diamant_0 = null;
	public static TexturePaint diamant_1 = null;
	public static TexturePaint wall_0 = null;
	public static TexturePaint pierre_0 = null;
	public static TexturePaint rainbow_0 = null;
	public static TexturePaint rainbow_1 = null;

	public static TexturePaint showDiam = null;
	public static int showDiam_len_x;
	public static int showDiam_len_y;

	public static BufferedImage toad_facing_0_Img = null;
	public static BufferedImage goodJob = null;
	public static BufferedImage gameover = null;
	public static BufferedImage record = null;
	public static BufferedImage scoreban = null;

	public static BufferedImage continuer_Btn = null;
	public static int continuer_Btn_len_x;
	public static int continuer_Btn_len_y;
	public static BufferedImage continuer_Btn_pressed = null;

	static {
		ZipFile zip = null;
		InputStream img;
		BufferedImage in = null;

		// Mode Copier coller.
		try {

			zip = new ZipFile("./ress.zip");
			// Nyan cat
			img = zip.getInputStream(zip.getEntry("nyan-cat_rigth0.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.nyan_rigth_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			// img = zip.getInputStream(zip.getEntry("nyan-cat_rigth0.png"));
			// in = ImageIO.read(img);
			in = (BufferedImage) Art.mirror(in, true);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.nyan_left_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("diamant_0.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.diamant_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("diamant_target.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.diamant_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("wall_0.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.5f);
			Texture.wall_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("pierre_0.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.pierre_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("rainbow.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.5f);
			Texture.rainbow_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			in = (BufferedImage) Art.mirror(in, false);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.rainbow_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			// Toad
			img = zip.getInputStream(zip.getEntry("toad_idle.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_idle_rigth_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			in = (BufferedImage) Art.mirror(in, true);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.toad_idle_left_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_up.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_up_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_walk_up.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_up_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_facing.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_facing_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));
			Texture.toad_facing_0_Img = in;

			img = zip.getInputStream(zip.getEntry("toad_facing_1.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_facing_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_facing_2.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_facing_2 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_bad.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_bad_0 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			in = (BufferedImage) Art.mirror(in, true);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.toad_bad_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			img = zip.getInputStream(zip.getEntry("toad_walk1.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.9f);
			Texture.toad_walk_rigth_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			in = (BufferedImage) Art.mirror(in, true);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(1);
			Texture.toad_walk_left_1 = new TexturePaint(in, new Rectangle(0, 0, Constantes.CASE_EN_PIXELS,
					Constantes.CASE_EN_PIXELS));

			// showDiam
			img = zip.getInputStream(zip.getEntry("ShowDiams.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.5f);
			Texture.showDiam_len_x = in.getWidth();
			Texture.showDiam_len_y = in.getHeight();
			Texture.showDiam = new TexturePaint(in, new Rectangle(0, 0, Texture.showDiam_len_x, Texture.showDiam_len_y));

			// GoodJob
			img = zip.getInputStream(zip.getEntry("goodjob.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.4f);
			Texture.goodJob = in;

			// record
			img = zip.getInputStream(zip.getEntry("record.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.4f);
			Texture.record = in;

			// GameOver
			img = zip.getInputStream(zip.getEntry("gameover.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.4f);
			Texture.gameover = in;

			// scoreban
			img = zip.getInputStream(zip.getEntry("scoreban.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.4f);
			Texture.scoreban = in;

			// continuer btn
			img = zip.getInputStream(zip.getEntry("Continuer_Btn.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.5f);
			Texture.continuer_Btn_len_x = in.getWidth();
			Texture.continuer_Btn_len_y = in.getHeight();
			Texture.continuer_Btn = in;
			// new TexturePaint(in, new Rectangle(0, 0,
			// showDiam_len_x, showDiam_len_y));

			img = zip.getInputStream(zip.getEntry("Continuer_Btn_press.png"));
			in = ImageIO.read(img);
			in = Art.createCompatibleImage(in);
			in.setAccelerationPriority(0.5f);
			Texture.continuer_Btn_pressed = in;
			// new TexturePaint(in, new Rectangle(0, 0,
			// showDiam_len_x, showDiam_len_y));

		} catch (final IOException e1) {
			Constantes.LOGGER.fatal("", e1);
		} finally {
			try {
				zip.close();
			} catch (final IOException e) {
				Constantes.LOGGER.warn(e);
			}
		}
	}

	private Texture() {
	}

}
