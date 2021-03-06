import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class MnistDigitPrompt extends JFrame{

	public static void main(String[] args) {new MnistDigitPrompt();}
	
	public MnistDigitPrompt(){
		
		super("Digit Analyzer");
		setSize(500,280);
		setLocation(100,100);
		setContentPane(new MnistDigitPromptPanel(this));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}

}

class MnistDigitPromptPanel extends JPanel{
	
	private DisplayPanel display;
	private PromptPanel prompt;
	private Container frame;
	
	public MnistDigitPromptPanel(JFrame frame){
		super();
		setBackground(new Color(220,220,220));
		
		prompt = new PromptPanel();
		display = new DisplayPanel();
		
		setLayout(new GridLayout(1,2));
		add(prompt);
		add(display);
		
		frame.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e)
		    {
		    	display.getNetwork().saveNetworkAtResource("network.txt");
		    }
		});
		
		this.frame = frame;
	}
	
	public MnistDigitPromptPanel(JApplet frame){
		super();
		setBackground(new Color(220,220,220));
		
		prompt = new PromptPanel();
		display = new DisplayPanel();
		
		setLayout(new GridLayout(1,2));
		add(prompt);
		add(display);
		
		this.frame = frame;
	}
	
	class DisplayPanel extends JPanel implements ActionListener{
		
		private Network net;
		private double[] out;
		private JRadioButton[] buttons;
		private JCheckBox train;
		
		public Network getNetwork(){return net;}
		
		public DisplayPanel(){
			super();
			setBackground(new Color(220,220,220));
			
			out = new double[10];
			
			try {
				net = Network.loadNetworkFromResource("network.txt");
			} catch (NetworkNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			assert (net.getNetwork().get(0).length == 784 && net.getNetwork().get(net.getNetwork().size()-1).length == 10);
			
			JButton run = new JButton("Run Network");
			run.setBounds(100,225,120,25);
			run.addActionListener(this);
			
			JButton clear = new JButton("Clear");
			clear.setBounds(30,225,80,25);
			clear.addActionListener(this);
			
			train = new JCheckBox("Train");
			train.setBounds(150,100,75,30);
			train.addActionListener(this);
			
			setLayout(null);
			add(run);
			add(clear);
			add(train);
			
			buttons = new JRadioButton[10];
			ButtonGroup bg = new ButtonGroup();
			
			for (int i=0;i<10;i++){
				buttons[i] = new JRadioButton(i+"");
				buttons[i].addActionListener(this);
				buttons[i].setBounds(110,10+(20*i),25,25);
				bg.add(buttons[i]);
				add(buttons[i]);
			}
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			
			g.setFont(new Font("TimesNewRoman",Font.BOLD,20));
			
			int max = 0;
			double maxOut = 0.0;
			for (int i=0;i<10;i++){
				g.drawString(i+": "+String.format("%.3f",out[i]),20,30+(20*i));
				if (maxOut<out[i]){
					max = i;
					maxOut = out[i];
				}
			}
			
			g.setFont(new Font("TimesNewRoman",Font.BOLD,12));
			g.drawString("ANN Guess:",150,50);
			g.setFont(new Font("TimesNewRoman",Font.BOLD,25));
			g.drawString(max+"",175,85);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String cmd = e.getActionCommand();
			if (cmd.equals("Run Network")){
				int answer = answerGiven();
				
				if (answer == -1 && train.isSelected()){
					JOptionPane.showMessageDialog(frame,
						    "You must specify the digit drawn before training the network.");
					return;
				}
				
				double[] invert = new double[784];
				double[] brightness = prompt.getBrightness();
				for (int i=0;i<784;i++) invert[i] = 1.0-brightness[i];
				
				// run network, get result
				List<double[]> networkOutput = net.run(invert);
				out = networkOutput.get(networkOutput.size()-1);
				
				// update displayed output
				repaint();
				
				if (train.isSelected()){
					// train network
					final double ETA = 0.5;
					net.train(invert,answer,ETA);
				}
				
			} else if (cmd.equals("Clear")){
				prompt.clear();
				for (int i=0;i<10;i++) 
					buttons[i].setSelected(false);
			} else if (cmd.equals("Train")){
				for (int i=0;i<10;i++) 
					buttons[i].setSelected(false);
			}
			
		}
		
		public int answerGiven(){
			int out = -1;
			
			for (int i=0;i<10;i++){
				if (buttons[i].isSelected()){
					out = i;
					break;
				}
			}
			
			return out;
		}
		
	}

	class PromptPanel extends JPanel implements MouseListener, MouseMotionListener{
		
		private double[] brightness;
		
		public double[] getBrightness(){return brightness;}
		
		public void clear(){
			brightness = new double[784];
			for (int i=0;i<784;i++) brightness[i] = 1.0;
			repaint();
		}
		
		public PromptPanel(){
			super();
			//setSize(280,280);
			setBackground(Color.GRAY);
			addMouseListener(this);
			addMouseMotionListener(this);
			
			brightness = new double[784];
			for (int i=0;i<784;i++) brightness[i] = 1.0;
		}
		
		@Override
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			for (int r=0;r<28;r++){
				for (int c=0;c<28;c++){
					double pixel = brightness[(r*28)+c];
					g.setColor(new Color((int)(255*pixel),(int)(255*pixel),(int)(255*pixel)));
					g.fillRect(10*c,10*r,10,10);
				}
			}
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			
			int r = e.getY()/10;
			int c = e.getX()/10;
			
			try{
				brightness[(r*28)+c] = 0.0;
				brightness[((r+1)*28)+c] = 0.0;
				brightness[((r-1)*28)+c] = 0.0;
				brightness[(r*28)+(c+1)] = 0.0;
				brightness[(r*28)+(c-1)] = 0.0;
				/*brightness[((r+1)*28)+c+1] = 0.0;
				brightness[((r-1)*28)+c+1] = 0.0;
				brightness[((r-1)*28)+(c-1)] = 0.0;
				brightness[((r+1)*28)+(c-1)] = 0.0;*/
			}catch (ArrayIndexOutOfBoundsException exc){}
			
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {}

		@Override
		public void mouseClicked(MouseEvent e) {requestFocus();}

		@Override
		public void mousePressed(MouseEvent e) {requestFocus();}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
}


