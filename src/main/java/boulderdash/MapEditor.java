package boulderdash;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import boulderdash.cases.Case;
import boulderdash.cases.Diamant;
import boulderdash.cases.Exit;
import boulderdash.cases.Mur;
import boulderdash.cases.Pierre;
import boulderdash.cases.RainBow;
import boulderdash.cases.Spawn;
import boulderdash.cases.Terre;
import boulderdash.cases.Vide;
import boulderdash.enemy.NyanCat;
import boulderdash.enemy.Personnage;
import boulderdash.utils.Generator;
import boulderdash.utils.Position;
import boulderdash.utils.Serialization;
import boulderdash.utils.UseFileDialog;

/**
 * Editeur de Map. Fork de la version Graphique.
 * 
 */
public class MapEditor extends JFrame implements Constantes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * dimension d'une case en pixels
	 */
	private Damier damier;
	private final boolean showstatic_view = false;
	private final ArrayList<Personnage> ennemies = new ArrayList<Personnage>();
	private String filepath;
	private final boolean stop = false;

	final void setCase(final Case case_) {
		final Position pos = this.damier.getJoueur().getPos();
		final Case[][] new_content = this.damier.getContent();
		new_content[pos.getY()][pos.getX()] = case_.clone();
		this.damier = new Damier(new_content, this.damier.getJoueur(),
				this.ennemies.toArray(new Personnage[this.ennemies.size()]));

		// set player pos
		this.damier.getJoueur().setPos(pos);

		this.damier.init_checkedPosition();
	}

	private MapEditor(final int taille_damier) {

		// titre de la fenêtre
		super("Boulder Dash - CONFIG MAP EDITOR");
		Constantes.CONFIG.setMapEditorMode(true);
		// fermeture de l'application lorsque la fenêtre est fermée
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// pas de redimensionnement possible de la fenêtre
		this.setResizable(false);

		// créer un conteneur qui affichera le jeu
		final JPanel content = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				// affichage du modèle du jeu
				MapEditor.this.damier.affichage(g);
			}
		};
		// s'assurer du focus pour le listener clavier
		this.setFocusable(false);
		content.setFocusable(true);

		// ajouter le conteneur à la fenêtre
		this.setContentPane(content);
		// Le damier.
		this.damier = Generator.gen_FirstDamier(taille_damier, taille_damier, new Joueur(new Position(), ""));

		// dimension de ce conteneur
		// le listener gérant les entrées au clavier
		content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				final int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_F5) {// SET - UNSET CONFIG
					Constantes.CONFIG.setMapEditorMode(!Constantes.CONFIG.isMapEditorMode());
					if (!Constantes.CONFIG.isMapEditorMode()) {

						MapEditor.this.setTitle("Boulder Dash - MAP EDITOR TEST");

					} else {
						MapEditor.this.setTitle("Boulder Dash - MAP EDITOR");
					}
				} else if (keyCode == KeyEvent.VK_N) {
					MapEditor.this.ennemies.add(new NyanCat(MapEditor.this.damier.getJoueur().getPos()));
					MapEditor.this.setCase(RainBow.instance);
				} else if (keyCode == KeyEvent.VK_SPACE) {
					MapEditor.this.setCase(Diamant.instance);
				} else if (keyCode == KeyEvent.VK_E || keyCode == KeyEvent.VK_END) {
					MapEditor.this.setCase(Exit.instance);
				} else if (keyCode == KeyEvent.VK_M) {
					MapEditor.this.setCase(Mur.instance);
				} else if (keyCode == KeyEvent.VK_P) {
					MapEditor.this.setCase(Pierre.instance);
				} else if (keyCode == KeyEvent.VK_HOME || keyCode == KeyEvent.VK_INSERT) {
					MapEditor.this.setCase(Spawn.instance);
				} else if (keyCode == KeyEvent.VK_R) {
					MapEditor.this.setCase(RainBow.instance);
				} else if (keyCode == KeyEvent.VK_T) {
					MapEditor.this.setCase(Terre.instance);
				} else if (keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_V) {

					final Position pos = MapEditor.this.damier.getJoueur().getPos();
					for (final Iterator<Personnage> iterator = MapEditor.this.ennemies.iterator(); iterator.hasNext();) {
						final Personnage p = iterator.next();
						if (pos.equals(p.getPos())) {
							iterator.remove();
						}

					}
					MapEditor.this.setCase(Vide.instance);
				} else if (keyCode == KeyEvent.VK_PAGE_DOWN || keyCode == KeyEvent.VK_PAGE_UP) {
					MapEditor.this.setCase(Generator.seek_Case());
				} else if (keyCode == KeyEvent.VK_S && (e.getModifiers() & InputEvent.CTRL_MASK) != 0) {// ctrl
																										// +
																										// s

					if (MapEditor.this.filepath == null) {
						final UseFileDialog filedlg = new UseFileDialog();
						MapEditor.this.filepath = filedlg.saveFile(new Frame(), "Save...");
					}

					boolean ok = false;

					try {
						MapEditor.this.damier.placerJoueurSpawn();
						Serialization.validate_MAP(MapEditor.this.damier);
						ok = true;
					} catch (final Exception e2) {
						JOptionPane.showMessageDialog(MapEditor.this, "Erreur Map non Valide : " + e2.getMessage(),
								"Map non Valide", JOptionPane.ERROR_MESSAGE);
					}

					if (ok) {
						try {
							final Writer xmlfile = new FileWriter(MapEditor.this.filepath);
							Serialization.saveObjectTo(MapEditor.this.damier, xmlfile);
							xmlfile.close();
						} catch (final Exception e2) {
							e2.printStackTrace();
						}
					}

				} else if (keyCode == KeyEvent.VK_O && (e.getModifiers() & InputEvent.CTRL_MASK) != 0) {// ctrl
																										// +
																										// o
					final UseFileDialog filedlg = new UseFileDialog();
					MapEditor.this.filepath = filedlg.loadFile(new Frame(), "Open...");
					try {
						final Reader reader = new FileReader(MapEditor.this.filepath);
						MapEditor.this.damier = Serialization.restoreDamier(reader, MapEditor.this.damier.getJoueur());

						content.setPreferredSize(null);// clean
						MapEditor.this.pack();// resize
						MapEditor.this.setLocationRelativeTo(null);

						MapEditor.this.ennemies.clear();
						if (MapEditor.this.damier.getAll_Enemies() != null) {
							MapEditor.this.ennemies.addAll(java.util.Arrays.asList(MapEditor.this.damier
									.getAll_Enemies()));
						}
						try {
							reader.close();
						} catch (final IOException e1) {
							e1.printStackTrace();
						}
					} catch (final FileNotFoundException e1) {
						JOptionPane.showMessageDialog(MapEditor.this, "Erreur charchement Map : " + e1.getMessage(),
								"Charchement Map", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();

					} finally {

						content.setPreferredSize(new Dimension(MapEditor.this.damier.getLargeur()
								* Constantes.CASE_EN_PIXELS, MapEditor.this.damier.getHauteur()
								* Constantes.CASE_EN_PIXELS));

						MapEditor.this.pack();// resize
						MapEditor.this.setLocationRelativeTo(null);
					}

				}

				else if (keyCode == KeyEvent.VK_F7) {
					// sauvegarde en fichier image
					final BufferedImage image = new BufferedImage(taille_damier * Constantes.CASE_EN_PIXELS,
							taille_damier * Constantes.CASE_EN_PIXELS, BufferedImage.TYPE_INT_RGB);
					final Graphics2D g2 = image.createGraphics();
					content.print(g2);
					g2.dispose();
					try {
						ImageIO.write(image, "PNG", new File("screenshot.png"));
					} catch (final IOException e1) {
						e1.printStackTrace();
					}

				} else if (keyCode == KeyEvent.VK_ESCAPE) {
					// reopen file
					if (Constantes.CONFIG.isMapEditorMode()) {
						try {// TODO si Map pas sauvegarde
							final Reader reader = new FileReader(MapEditor.this.filepath);
							MapEditor.this.damier = Serialization.restoreDamier(reader,
									MapEditor.this.damier.getJoueur());

							content.setPreferredSize(null);// clean
							MapEditor.this.pack();// resize
							MapEditor.this.setLocationRelativeTo(null);

							MapEditor.this.ennemies.clear();
							if (MapEditor.this.damier.getAll_Enemies() != null) {
								MapEditor.this.ennemies.addAll(java.util.Arrays.asList(MapEditor.this.damier
										.getAll_Enemies()));
							}
							try {
								reader.close();
							} catch (final IOException e1) {
								e1.printStackTrace();
							}
						} catch (final FileNotFoundException e1) {
							JOptionPane.showMessageDialog(MapEditor.this,
									"Erreur charchement Map : " + e1.getMessage(), "Charchement Map",
									JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();

						} finally {

							content.setPreferredSize(new Dimension(MapEditor.this.damier.getLargeur()
									* Constantes.CASE_EN_PIXELS, MapEditor.this.damier.getHauteur()
									* Constantes.CASE_EN_PIXELS));

							MapEditor.this.pack();// resize
							MapEditor.this.setLocationRelativeTo(null);
						}
					}

				} else { // Passer au damier.
					MapEditor.this.damier.gestionDuClavier(e);

				}

			}
		});

		// les entrées souris
		content.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(final MouseEvent e) {

			}

			@Override
			public void mousePressed(final MouseEvent e) {
				// Get Pos
				final Point pos = e.getPoint();
				final Position clicked_pos = new Position((int) Math.round(pos.getX()), (int) Math.round(pos.getY()));
				// System.out.println(clicked_pos);
				MapEditor.this.damier.gestionClick(clicked_pos);

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

		content.setPreferredSize(new Dimension(taille_damier * Constantes.CASE_EN_PIXELS, taille_damier
				* Constantes.CASE_EN_PIXELS));
		// Créer thread
		final Thread update_model = new Thread(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName("update_model");
				while (!MapEditor.this.stop) { // boucle infinie
					try {
						if (!MapEditor.this.showstatic_view) {
							MapEditor.this.damier.detectAndMoveFallingObject();
						}

						Thread.sleep(100);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		final Thread update_view = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!MapEditor.this.stop) { // boucle infinie
					try {

						// demander à l'EDT de redessiner le conteneur
						if (!MapEditor.this.showstatic_view) {
							content.repaint();
						}

						MapEditor.this.damier.update();

						if (MapEditor.this.showstatic_view) {
						}

						Thread.sleep(30);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		// set Daemon
		update_view.setDaemon(true);
		update_model.setDaemon(true);

		// lancer les thread
		update_view.start();
		update_model.start();

		this.pack();// resize
		this.setLocationRelativeTo(null);

	}

	// Lancement du jeu
	public static void main(final String[] args) {
		int len = 0;
		while (len < 12 || len > 25) {
			try {
				final String ans = JOptionPane.showInputDialog(null, "Taille de la Map ?", "Taille de la Map",
						JOptionPane.QUESTION_MESSAGE);

				if (ans == null) {
					len = 15;
					break;
				}
				len = Integer.parseInt(ans);
			} catch (final Exception e) {

				Constantes.LOGGER.warn("", e);

			}
		}

		// création de la fenêtre
		final MapEditor fenetre = new MapEditor(len);
		// dimensionnement de la fenêre "au plus juste" suivant
		// la taille des composants qu'elle contient
		fenetre.pack();
		// centrage sur l'écran
		fenetre.setLocationRelativeTo(null);
		// affichage
		fenetre.setVisible(true);

	}

}