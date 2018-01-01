import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class NeuralNetsGUI extends JFrame{

	private List<Network> networks;
	
	public NeuralNetsGUI(){
		
		super("NeuralNets");
		setSize(800,600);
		setLocation(400,200);
		setContentPane(new NeuralNetsGUIPanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}
	
	private class NeuralNetsGUIPanel extends JPanel implements ActionListener{
		
		JPanel createForm,loadForm,trainForm,runForm;
		
		public NeuralNetsGUIPanel(){
			
			setBackground(Color.GRAY);
			setLayout(new GridLayout(1,2));
			
			JButton create = new JButton("Create new network"),
				load = new JButton("Load new network"),
				train = new JButton("Train network");
			
			create.addActionListener(this);
			load.addActionListener(this);
			train.addActionListener(this);
			
			JPanel netPanel = new JPanel();
			netPanel.setLayout(new GridLayout(3,1));
			netPanel.add(create);
			netPanel.add(load);
			netPanel.add(train);
			
			createForm = new JPanel();
			
			runForm = new JPanel();
			loadForm = new JPanel();
			trainForm = new JPanel();
			
			add(netPanel);
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			String cmd = e.getActionCommand();
			switch (cmd){
			case "Create new network":
				
				break;
			}
			
		}
		
	}
	
}
