import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

public class MnistDigitPrompt extends JFrame{

	public static void main(String[] args) {new MnistDigitPrompt();}
	
	public MnistDigitPrompt(){
		
		super("Digit Analyzer");
		setSize(500,280);
		setLocation(100,100);
		setContentPane(new MnistDigitPromptPanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}

}

class MnistDigitPromptPanel extends JPanel{
	
	private DisplayPanel display;
	private PromptPanel prompt;
	
	public MnistDigitPromptPanel(){
		super();
		//setBackground(Color.GRAY);
		
		prompt = new PromptPanel();
		display = new DisplayPanel();
		
		setLayout(new GridLayout(1,2));
		add(prompt);
		add(display);
	}
	
	class DisplayPanel extends JPanel implements ActionListener{
		
		private Network net;
		private double[] out;
		
		public DisplayPanel(){
			super();
			setBackground(new Color(220,220,220));
			
			out = new double[10];
			
			try {
				net = Network.loadNetworkFromResource("network.txt");
			} catch (NetworkNotFoundException e) {
				e.printStackTrace();
			}
			
			assert (net.getNetwork().get(0).length == 784 && net.getNetwork().get(net.getNetwork().size()-1).length == 10);
			
			JButton run = new JButton("Run Network");
			run.setBounds(125,225,120,25);
			run.addActionListener(this);
			JButton clear = new JButton("Clear");
			clear.setBounds(35,225,80,25);
			clear.addActionListener(this);
			
			setLayout(null);
			add(run);
			add(clear);
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			
			g.setFont(new Font("TimesNewRoman",Font.BOLD,20));
			for (int i=0;i<10;i++)
				g.drawString(i+": "+String.format("%.5f",out[i]),20,30+(20*i));
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String cmd = e.getActionCommand();
			if (cmd.equals("Run Network")){
				// invert brightnesses
				double[] invert = prompt.getBrightness();
				for (int i=0;i<784;i++) invert[i] = 1.0-invert[i];
				
				// run network, get result
				List<double[]> networkOutput = net.run(prompt.getBrightness());
				out = networkOutput.get(networkOutput.size()-1);
				
				// update displayed output
				repaint();
			} else if (cmd.equals("Clear")){
				prompt.clear();
			}
			
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
				brightness[((r+1)*28)+c+1] = 0.0;
				brightness[((r-1)*28)+c+1] = 0.0;
				brightness[((r-1)*28)+(c-1)] = 0.0;
				brightness[((r+1)*28)+(c-1)] = 0.0;
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


