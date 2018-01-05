import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

/**
 * A class to render MNIST images given a brightness array or an address
 * and index. Used in the NeuralNetsGUI class to render images from MNIST
 * data.
 * 
 * @author rsyed0
 * @since 1.1
 * @see NeuralNetsGUI
 */
public class MnistImageRenderer extends JFrame{

	public MnistImageRenderer(double[] brightness){
		
		super("MNIST Image");
		setSize(280,280);
		setLocation(400,400);
		setContentPane(new MnistRendererPanel(brightness));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}
	
	public MnistImageRenderer(String images,String labels,int index){
		
		super("MNIST Image");
		setSize(280,280);
		setLocation(400,400);
		setContentPane(new MnistRendererPanel(images,labels,index));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}
	
	public MnistImageRenderer(MnistMatrix img){
		
		super("MNIST Image");
		setSize(280,280);
		setLocation(400,400);
		setContentPane(new MnistRendererPanel(img));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
	}

}

class MnistRendererPanel extends JPanel{
	
	private double[] brightness = new double[784];
	private final int SCALE = 10;
	
	public MnistRendererPanel(double[] brightness){
		
		setBackground(Color.WHITE);
		this.brightness = brightness;
		
	}

	public MnistRendererPanel(String imagesName, String labelsName, int index) {
		
		setBackground(Color.WHITE);
		this.brightness = Network.convertToBrightness(imagesName, labelsName, index, index+1).get(0);
		
	}
	
	public MnistRendererPanel(MnistMatrix img) {
		
		setBackground(Color.WHITE);
		for (int i=1;i<784;i++)
			this.brightness[i-1] = img.getValue(i/28,i%28)/256.0;
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		for (int r=0;r<28;r++){
			for (int c=0;c<28;c++){
				g.setColor(new Color((int)(256*brightness[(r*28)+c]),(int)(256*brightness[(r*28)+c]),(int)(256*brightness[(r*28)+c])));
				g.fillRect(SCALE*c,SCALE*r,SCALE,SCALE);
			}
		}
		
	}
	
}
