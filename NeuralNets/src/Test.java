import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) throws NetworkNotFoundException {
		
		// test the neural networks GUI
		// NeuralNetsGUI gui = new NeuralNetsGUI();
		
		// create new network, save it at network.txt
		//Network net = new Network(784,25,10);
		//net.saveNetworkAtResource("network.txt");
		
		// render images
		//for (int i=0;i<10;i++){
			//MnistImageRenderer img = new MnistImageRenderer("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte",0);
		//}
		
		// load network from network.txt
		Network net = Network.loadNetworkFromResource("network.txt");
		
		for (int i=0;i<2;i++){
			
			// segment image into usable parts
			//List<BufferedImage> imgs = (new Segmenter("mnist.jpeg")).horizontalSplit(net);
			
			final double ETA = 0.125;
			final int START = 0;
			final int END = 60000;
			final int EPOCHS = 10;
			final int NUM_IMAGES = 10000;
			
			// train network and save at network.txt
			net.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte", ETA, START, END, EPOCHS);
			
			// test network from testing data
			net.testAll("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte",0,NUM_IMAGES,false);
		}
		net.saveNetworkAtResource("network.txt");
		
	}

}
