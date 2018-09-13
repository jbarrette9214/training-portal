package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import training.Start;

public class navPanel extends JPanel{

	public navPanel(Start mainWindow) {
		
		this.setPreferredSize(new Dimension(mainWindow.getWidth(), 48));
		this.setLayout(new FlowLayout(0,0,0));
		this.setBackground(Color.white);
	

		JLabel search = new JLabel("Search by:");
		search.setFont(new Font(search.getFont().getFontName(), Font.BOLD, 20));
		this.add(search);
		
		
		JButton byEmployeeBtn = createButton("By Employee");
		byEmployeeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.changePanel("empSearch");
			}
			
		});
		
		this.add(byEmployeeBtn);

		this.add(createSep());
		
		JButton byDeptBtn = createButton("By Department");
		byDeptBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.changePanel("deptSearch");
			}
		});
		
		this.add(byDeptBtn);
		this.add(createSep());
	
		
		
		//read and setup the icons for the buttons
		Path iconPath = Paths.get(System.getProperty("user.dir")).resolve("src").resolve("training")
				.resolve("images");
		try {
			Image icon = ImageIO.read(new File(iconPath.toString() + "\\person.png"));
			byEmployeeBtn.setIcon(new ImageIcon(icon));
			icon = ImageIO.read(new File(iconPath.toString() +"\\people.png"));
			byDeptBtn.setIcon(new ImageIcon(icon));
		} catch(IOException e) {
			System.out.println("Couldn't open icon image");
		}

		
		
		
		this.setVisible(true);
	}
	
	
	
	
	private static JButton createButton(String tip) {
		JButton created = new JButton();
		created.setPreferredSize(new Dimension(72, 48));
		created.setToolTipText(tip);
		created.setBorder(null);
		created.setBackground(navColor);
		created.setForeground(Color.white);
		created.setFont(new Font(created.getFont().getFontName(), Font.BOLD, created.getFont().getSize()));
		created.setFocusPainted(false);
		
		return created;
	}
	
	private static JSeparator createSep() {
		JSeparator newSep = new JSeparator(SwingConstants.VERTICAL);
		newSep.setForeground(Color.black);
		newSep.setPreferredSize(new Dimension(1, 48));
		return newSep;
	}

	
	
	private static Color navColor = Color.white;
	//private JPanel panel;
}
