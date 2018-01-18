import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.*;

public class NflPredictorGUI extends JFrame{

	public static void main(String[] args) {new NflPredictorGUI();}
	
	public NflPredictorGUI(){
		
		super("NFL Game Predictor");
		setSize(600,250);
		setLocation(300,100);
		setContentPane(new NflPredictorPanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}
	
	class NflPredictorPanel extends JPanel implements ActionListener{
		
		private String[] allNflTeams;
		private String homeTeam,awayTeam;
		private double probability;
		private NflPreprocessor processor;
		private JComboBox<String> home,away;
		
		public NflPredictorPanel(){
			
			super();
			setBackground(new Color(220,220,220));
			setLayout(null);
			
			homeTeam = "home team";
			awayTeam = "away team";
			probability = 0.5;
			allNflTeams = new String[32];
			
			try {
				processor = new NflPreprocessor(Network.loadNetworkFromResource("nflNetwork.txt"));
			} catch (NetworkNotFoundException e) {
				System.err.println("Network could not be loaded from network file.");
				System.exit(1);
			}
			
			Scanner sc = null;
			try{
				sc = new Scanner(new File("nflTeams.txt"));
			}catch (IOException e){
				System.err.println("Cannot read from teams file.");
				System.exit(1);
			}
			
			int i=0;
			while (sc.hasNextLine()){
				allNflTeams[i] = sc.nextLine();
				i++;
			}
			
			JLabel homeLabel = new JLabel("Home:");
			homeLabel.setBounds(50,35,100,50);
			JLabel awayLabel = new JLabel("Away:");
			awayLabel.setBounds(50,60,100,50);
			
			home = new JComboBox<>(allNflTeams);
			away = new JComboBox<>(allNflTeams);
			JButton submit = new JButton("Submit");
			
			home.setBounds(90,35,200,50);
			away.setBounds(90,60,200,50);
			submit.setBounds(60,125,150,75);
			
			submit.addActionListener(this);
			
			add(homeLabel);
			add(home);
			add(awayLabel);
			add(away);
			add(submit);
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			g.setFont(new Font("TimesNewRoman",Font.BOLD,20));
			g.drawString("The "+homeTeam+" have a ",300,60);
			g.setFont(new Font("TimesNewRoman",Font.BOLD,40));
			g.drawString(Math.round(probability*100)+"%",300,125);
			g.setFont(new Font("TimesNewRoman",Font.BOLD,20));
			g.drawString("chance of winning.",300,175);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			homeTeam = (String)home.getSelectedItem();
			awayTeam = (String)away.getSelectedItem();
			
			String[] homeTeamArr = homeTeam.split(" ");
			String[] awayTeamArr = awayTeam.split(" ");
			
			// run network
			probability = processor.predict(new String[]{homeTeamArr[homeTeamArr.length-1],awayTeamArr[awayTeamArr.length-1]});
			
			repaint();
			
		}
		
	}

}
