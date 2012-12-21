package boulderdash;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import boulderdash.enemy.EnnemyIA;
import boulderdash.enemy.Personnage;
import boulderdash.image.StackBlurFilter;
import boulderdash.image.Texture;
import boulderdash.io.TimingMouseAdapter;
import boulderdash.utils.Generator;
import boulderdash.utils.MD5Checksum;
import boulderdash.utils.Position;
import boulderdash.utils.Serialization;
import boulderdash.utils.Stats;
import boulderdash.utils.UseFileDialog;

public class Graphique extends JFrame implements Constantes {

	/**
	 * True si il existe une prochaine Map.
	 */
	private static boolean theresnextmap = false;
	private static final long serialVersionUID = 3L;

	/**
	 * Dernier Chemin vers le fichier
	 */
	private static String lastFilePath = null;
	/**
	 * Le damier.
	 */
	private Damier damier = null;
	/**
	 * true si jeu en pause.
	 */
	private boolean showstatic_view = false;
	/**
	 * stocke l'image de pause.
	 */
	private BufferedImage pauseImg = null;
	/**
	 * stocke l'image de fin. <br/>
	 */
	private BufferedImage finImg = null;
	/**
	 * true si l'image de fin doit être recalc.
	 */
	private boolean repaint_finImg = false;

	/**
	 * true si l'image de fin doit être recalc.
	 */
	private final String maphash;
	/**
	 * true si les stats sont ok.
	 */
	private boolean stat_effectuer = false;
	/**
	 * Stop les Thread.
	 */
	private boolean stop = false;
	/**
	 * stocke la reference de l'image de fin de base.
	 */
	private Image endImg = null;

	private final JPanel content;
	/**
	 * Vrai si souris au dessus du bouton de fin.
	 */
	private boolean isOverBtn = false;
	/**
	 * Position X du bouton de fin.
	 */
	private int btn_pos_x;
	/**
	 * Position Y du bouton de fin.
	 */
	private int btn_pos_y;

	/**
	 * Liste des ennemies.
	 */
	private static final ArrayList<Personnage> enemies = new ArrayList<Personnage>();

	/**
	 * @return le repaint_finImg
	 */
	synchronized final boolean isRepaint_finImg() {
		return this.repaint_finImg;
	}

	/**
	 * @param repaint_finImg
	 *            le repaint_finImg à définir
	 */
	synchronized final void setRepaint_finImg(final boolean repaint_finImg) {
		this.repaint_finImg = repaint_finImg;
	}

	private void calcPauseImg(final int width, final int height) throws Exception, InterruptedException {
		if (Graphique.this.pauseImg == null) {// recalc
												// image
			final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2 = image.createGraphics();
			this.content.print(g2);
			g2.dispose();

			Graphique.this.pauseImg = StackBlurFilter.flou(image, 25);
		}
		final int xstart = Graphique.this.content.getX();
		final int ystart = Graphique.this.content.getY();
		final Graphics2D g = (Graphics2D) Graphique.this.content.getGraphics();
		g.drawImage(Graphique.this.pauseImg, xstart, ystart, null);

		Thread.sleep(250);
	}

	private void calcWinImg(final Damier damier, final int width, final int height, final int score) {
		if (!Graphique.this.stat_effectuer) {
			// set LastFile
			String s = Graphique.lastFilePath.split("\\.bdxml")[0];
			final char last_in_s = s.charAt(s.length() - 1);
			if (StringUtils.isNumeric(String.valueOf(last_in_s))) {
				int level_nbr = last_in_s - '0';
				s = s.substring(0, s.length() - 1) + ++level_nbr + ".bdxml";
				// System.out.println(s);
				final File f = new File(s);
				Graphique.theresnextmap = f.exists() || f.getAbsoluteFile().exists();
				if (Graphique.theresnextmap) {
					Stats.setLastMap(s, damier.getJoueur().getNom());
				}

			}

			Graphique.this.stat_effectuer = true;
			// Stats Time !
			final String mapName2 = Graphique.this.maphash;

			final int bestscore = Stats.getBestScore(mapName2);
			final String nom = damier.getJoueur().getNom();
			if (nom != null && mapName2 != null) {

				Stats.insertNewScore(nom, score, mapName2);
			}
			if (score > bestscore && mapName2 != null) {
				// System.out.println("Winner ! ");
				this.endImg = Texture.record;
			} else {
				this.endImg = Texture.goodJob;
			}
		}

		final Graphics2D g2 = (Graphics2D) Graphique.this.content.getGraphics();
		final int xstart = Graphique.this.content.getX();
		final int ystart = Graphique.this.content.getY();
		if (Graphique.this.finImg == null || Graphique.this.isRepaint_finImg()) {// need
																					// repaint
			final BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			final Graphics2D gtmp = tmp.createGraphics();
			gtmp.drawImage(this.endImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), xstart, ystart, null);
			gtmp.drawImage(Texture.scoreban.getScaledInstance(width, height, Image.SCALE_SMOOTH), xstart, ystart, null);

			// paint score
			final Graphics2D g_score = (Graphics2D) gtmp.create();// reset
			final String str = "Score :  " + String.valueOf(score);
			g_score.setColor(Color.WHITE);
			g_score.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g_score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
			final FontMetrics fm = g_score.getFontMetrics();
			final int x = width / 2 - fm.stringWidth(str) / 2;
			final int y = fm.getHeight() / 2 + 5;
			g_score.drawString(str, x, y);
			g_score.dispose();

			// btn paint
			if (Graphique.theresnextmap) {
				Graphique.this.btn_pos_x = xstart + Graphique.this.damier.getLargeur() * Constantes.CASE_EN_PIXELS / 2
						- Texture.continuer_Btn_len_x / 2;

				Graphique.this.btn_pos_y = height - Texture.continuer_Btn_len_y - 12;
				gtmp.drawImage(Graphique.this.isOverBtn ? Texture.continuer_Btn_pressed : Texture.continuer_Btn,
						this.btn_pos_x, this.btn_pos_y, null);
				gtmp.dispose();
			}
			Graphique.this.finImg = tmp;
			Graphique.this.setRepaint_finImg(false);
		}
		g2.drawImage(Graphique.this.finImg, xstart, ystart, null);
		g2.dispose();

		// Thread.sleep(30);
	}

	private void calcLooseImg(final int width, final int height, final int score) {
		this.endImg = Texture.gameover;
		Graphique.theresnextmap = true;
		Graphique.this.stat_effectuer = true;
		final Graphics2D g2 = (Graphics2D) Graphique.this.content.getGraphics();
		final int xstart = Graphique.this.content.getX();
		final int ystart = Graphique.this.content.getY();
		if (Graphique.this.finImg == null || Graphique.this.isRepaint_finImg()) {// need
																					// repaint
			final BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			final Graphics2D gtmp = tmp.createGraphics();
			gtmp.drawImage(this.endImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), xstart, ystart, null);
			// baniere score
			gtmp.drawImage(Texture.scoreban.getScaledInstance(width, height, Image.SCALE_SMOOTH), xstart, ystart, null);

			// paint score
			final Graphics2D g_score = (Graphics2D) gtmp.create();// reset
			final String str = "Score :  " + String.valueOf(score);
			g_score.setColor(Color.WHITE);
			g_score.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g_score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Graphique.this.damier.getHauteur() + 5));
			final FontMetrics fm = g_score.getFontMetrics();
			final int x = width / 2 - fm.stringWidth(str) / 2;
			final int y = fm.getHeight() / 2 + 5;
			g_score.drawString(str, x, y);
			g_score.dispose();

			// btn paint
			Graphique.this.btn_pos_x = xstart + Graphique.this.damier.getLargeur() * Constantes.CASE_EN_PIXELS / 2
					- Texture.continuer_Btn_len_x / 2;

			Graphique.this.btn_pos_y = height - Texture.continuer_Btn_len_y - 12;
			gtmp.drawImage(Graphique.this.isOverBtn ? Texture.continuer_Btn_pressed : Texture.continuer_Btn,
					this.btn_pos_x, this.btn_pos_y, null);
			gtmp.dispose();
			Graphique.this.finImg = tmp;
			Graphique.this.setRepaint_finImg(false);
		}
		g2.drawImage(Graphique.this.finImg, xstart, ystart, null);
		g2.dispose();

		// Thread.sleep(30);
	}

	private void exit() {
		Graphique.this.stop = true;
		Graphique.this.remove(Graphique.this.content);
		Graphique.this.setVisible(false);
		Graphique.this.damier = null;
		Graphique.this.finImg = null;
		Graphique.this.endImg = null;
		Graphique.this.pauseImg = null;
		Graphique.this.dispose();
		System.gc();
	}

	private Graphique(final Joueur j, @Nullable final Damier d, @Nullable final String hash) {

		// titre de la fenêtre
		super("Boulder Dash");

		Constantes.CONFIG.setMapEditorMode(false);

		/*
		 * Le joueur.
		 */
		final Joueur joueur = j;
		this.maphash = hash;

		this.setIconImage(Texture.toad_facing_0_Img);

		// Log User
		Stats.setLastNom(j.getNom());
		// this.damier = d;
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);

		// conteneur qui affichera le jeu
		this.content = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				this.setBackground(Constantes.BACKGROUND_COLOR);

				Graphique.this.damier.affichage(g);

				g.dispose();
			}

		};

		// focus pour les listeners.
		this.setFocusable(false);
		this.content.setFocusable(true);

		// ajouter le conteneur à la fenêtre
		this.setContentPane(this.content);
		// Le damier.
		this.damier = d != null ? d : Generator.gen_Damier(15, 15, joueur,
				Graphique.enemies.toArray(new Personnage[Graphique.enemies.size()]));

		// les entrées au clavier
		this.content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				final int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_F7) {
					// sauvegarde en fichier image
					final BufferedImage image = new BufferedImage(Graphique.this.damier.getLargeur()
							* Constantes.CASE_EN_PIXELS,
							Graphique.this.damier.getHauteur() * Constantes.CASE_EN_PIXELS, BufferedImage.TYPE_INT_RGB);
					final Graphics2D g2 = image.createGraphics();
					Graphique.this.content.print(g2);
					g2.dispose();
					try {
						ImageIO.write(image, "PNG", new File("screenshot.png"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}

				} else if (keyCode == KeyEvent.VK_ESCAPE) {
					if (Graphique.this.damier.getAnimfin() == 0) {
						Graphique.this.damier.pauseGame();
						Graphique.this.pauseImg = null;// reset pause image

						Graphique.this.showstatic_view = !Graphique.this.showstatic_view;
					} else {
						System.exit(0);
					}

				} else { // Passer au damier.
					if (!Graphique.this.showstatic_view && Graphique.this.damier.getAnimfin() == 0) {
						Graphique.this.damier.gestionDuClavier(e);
					} else if (Graphique.this.damier.getAnimfin() != 0 && keyCode == KeyEvent.VK_ENTER) {
						Graphique.this.isOverBtn = true;
						Graphique.this.exit();
					}

				}

			}
		});

		// les entrées souris
		if (Constantes.CONFIG.isClickToMove()) {
			this.content.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent e) {
				}

				@Override
				public void mousePressed(final MouseEvent e) {
					// Get Pos
					final Point pos = e.getPoint();
					final Position clicked_pos = new Position((int) Math.round(pos.getX()),
							(int) Math.round(pos.getY()));
					// System.out.println(clicked_pos);

					Graphique.this.damier.gestionClick(clicked_pos);

				}

				@Override
				public void mouseReleased(final MouseEvent e) {

				}

				@Override
				public void mouseEntered(final MouseEvent e) {

				}

				@Override
				public void mouseExited(final MouseEvent e) {

				}
			});

		} else {
			this.content.addMouseListener(new TimingMouseAdapter(this.damier));
		}
		this.content.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(final MouseEvent e) {
				final Point pos = e.getPoint();
				final boolean tmp = Graphique.this.isRepaint_finImg();

				if (pos.getX() >= Graphique.this.btn_pos_x
						&& pos.getX() < Graphique.this.btn_pos_x + Texture.continuer_Btn_len_x
						&& pos.getY() >= Graphique.this.btn_pos_y
						&& pos.getY() < Graphique.this.btn_pos_y + Texture.continuer_Btn_len_y
						&& Graphique.this.stat_effectuer) {
					Graphique.this.isOverBtn = true;

					Graphique.this.exit();

					if (!tmp) {
						Graphique.this.setRepaint_finImg(true);
					}
				} else {
					Graphique.this.isOverBtn = false;
					if (tmp) {
						Graphique.this.setRepaint_finImg(true);
					}
				}

			}

			@Override
			public void mouseDragged(final MouseEvent e) {

			}
		});
		// Dimension de la fenetre (Pour contenir tout)
		this.content.setPreferredSize(new Dimension(Graphique.this.damier.getLargeur() * Constantes.CASE_EN_PIXELS,
				Graphique.this.damier.getHauteur() * Constantes.CASE_EN_PIXELS));

		// Créer thread
		/*
		 * Thread Des chutes de pierres.
		 */
		final Thread chutePierre = new Thread(new Runnable() {

			@Override
			public void run() {
				while (Graphique.this.damier.getAnimfin() == 0 && !Graphique.this.stop) {
					try {
						if (!Graphique.this.showstatic_view) {
							Graphique.this.damier.detectAndMoveFallingObject();
						}

						Thread.sleep(100);
					} catch (final InterruptedException e) {
						if (Constantes.CONFIG.isLogWarn()) {
							Constantes.LOGGER.fatal("", e);
						}
					}
				}
			}
		}, "chutePierre");

		/*
		 * Thread De mise a jour model / view.
		 */
		final Thread update_view = new Thread(new Runnable() {

			@Override
			public void run() {

				// Permet de forcer un second paint apres un changement.
				boolean recalcGraphic = true;
				while (!Graphique.this.stop) {
					try {

						// demander à l'EDT de redessiner le conteneur
						final Damier damier2 = Graphique.this.damier;
						if (damier2.getAnimfin() == 0 && !Graphique.this.showstatic_view
								&& (damier2.isChangement() || recalcGraphic)) {
							recalcGraphic = damier2.isChangement();
							damier2.setChangement(false);
							Graphique.this.content.repaint();
						}

						damier2.update();
						final int width2 = Graphique.this.content.getWidth();// taille
						// du
						// conteneur
						final int height2 = Graphique.this.content.getHeight();// taille
						// du
						// conteneur
						if (Graphique.this.showstatic_view) {
							Graphique.this.calcPauseImg(width2, height2);
						} else if (damier2.getAnimfin() != 0) {

							final int score = damier2.getJoueur().getScore();
							if (!damier2.getJoueur().isMort()) {// fin du
								// parcours

								Graphique.this.calcWinImg(damier2, width2, height2, score);

							} else {
								Graphique.this.calcLooseImg(width2, height2, score);

							}

						}

						Thread.sleep(30);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					} catch (final Exception e) {
						Constantes.LOGGER.error("", e);
						e.printStackTrace();
					}
				}
			}
		}, "update_view");

		/*
		 * Thread pour les ennemies.
		 */
		final Thread update_enemies = new Thread(new EnnemyIA(this.damier.getAll_Enemies()), "update_enemies");

		// set Daemon
		update_view.setDaemon(true);
		chutePierre.setDaemon(true);
		update_enemies.setDaemon(true);

		// lancer les thread
		update_view.start();
		chutePierre.start();
		update_enemies.start();

		this.pack();// resize
		this.setLocationRelativeTo(null);
	}

	// Lancement du jeu
	public static void main(final String[] args) {
		// On log toute exception non Catch.
		try {
			Damier tmp = null;
			String player_name = null;
			String file = null;
			String file_hash = null;
			// get player name
			final String lastNom = Stats.getLastNom();

			if (!StringUtils.isBlank(lastNom)
					&& JOptionPane.showConfirmDialog(null, "Vous applez Vous " + lastNom + " ?") == JOptionPane.OK_OPTION) {
				player_name = lastNom;
			} else {
				try {
					player_name = JOptionPane.showInputDialog(null, "Nom ?", "nom", JOptionPane.QUESTION_MESSAGE);
				} catch (final Exception e) {
					Constantes.LOGGER.warn("", e);
				}

			}

			player_name = StringUtils.isBlank(player_name) ? "" : StringUtils.capitalize(StringEscapeUtils
					.escapeSql(player_name));

			// chargement fichier
			Graphique fenetre = null;
			do {
				if (fenetre == null || !fenetre.isVisible()) {
					System.gc();

					final Joueur j = new Joueur(null, player_name);
					if (args != null && args.length > 0) {
						file = args[0];
					} else {
						String lastMap = Stats.getLastMap(player_name);
						// System.out.println(lastMap);
						if (lastMap == null) {
							lastMap = "./Map/Niveau1.bdxml";
						}
						if (Graphique.theresnextmap
								|| JOptionPane.showConfirmDialog(null, "Voulez vous charger : " + lastMap + " ?") == JOptionPane.OK_OPTION) {
							file = lastMap;
						} else {
							final UseFileDialog filedlg = new UseFileDialog();
							file = filedlg.loadFile(new Frame(), "Open...");
						}

					}
					Graphique.theresnextmap = false;

					try {
						final Reader reader = new FileReader(file);
						tmp = Serialization.restoreDamier(reader, j);
						file_hash = MD5Checksum.getMD5Checksum(file);
						Graphique.lastFilePath = file;
						Stats.setLastMap(file, player_name);

						try {
							reader.close();
						} catch (final IOException e1) {
							e1.printStackTrace();
						}
					} catch (final FileNotFoundException e1) {
						JOptionPane.showMessageDialog(null, "Erreur charchement Map : " + e1.getMessage(),
								"Charchement Map", JOptionPane.ERROR_MESSAGE);
						System.exit(ImageObserver.ERROR);
						e1.printStackTrace();

					} catch (final Exception e) {
						e.printStackTrace();
					}

					// Graphique.enemies.add(new NyanCat(new Position(10, 10)));

					// création de la fenêtre
					fenetre = new Graphique(j, tmp, file_hash);

					// refaire le centrage de la fenetre
					fenetre.pack();
					// centrage
					fenetre.setLocationRelativeTo(null);
					// affichage
					fenetre.setVisible(true);
				}
				try {
					Thread.sleep(250);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			while (Graphique.theresnextmap || fenetre.isVisible());
		} catch (final Exception e) {
			Constantes.LOGGER.fatal("", e);
		}

	}
}