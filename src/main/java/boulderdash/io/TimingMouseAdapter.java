package boulderdash.io;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import boulderdash.Damier;
import boulderdash.utils.Position;

public class TimingMouseAdapter extends MouseAdapter {
	private Timer timer;
	private final Damier linkDamier;

	@Override
	public void mousePressed(final MouseEvent e) {
		this.timer = new Timer(120, new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e1) {
				// Get Pos
				final Point pos = e.getPoint();
				final Position clicked_pos = new Position((int) Math.round(pos.getX()), (int) Math.round(pos.getY()));
				TimingMouseAdapter.this.linkDamier.gestionClick(clicked_pos);
			}
		});
		this.timer.start();
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (this.timer != null) {
			this.timer.stop();
		}
	}

	public TimingMouseAdapter(final Damier link) {
		this.linkDamier = link;
	}

}