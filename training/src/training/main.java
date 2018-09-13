package training;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.GridBagConstraints;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class main {

	private JFrame frmTraining;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frmTraining.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTraining = new JFrame();
		frmTraining.setTitle("Training");
		frmTraining.setBounds(100, 100, 900, 600);
		frmTraining.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTraining.getContentPane().setLayout(new BorderLayout(0, 0));
		

		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(frmTraining.getWidth(), 65));
		frmTraining.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setPreferredSize(new Dimension(64,64));
		panel.add(btnNewButton);
	}
}
