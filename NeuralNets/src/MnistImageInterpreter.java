import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A class to load a network from the text file network.txt, or create one if it doesn't
 * already contain text indicating a network. The class then trains the network from the
 * MNIST database.
 * 
 * @author rsyed0
 * @since 1.0
 */
public class MnistImageInterpreter {

	public static void main(String[] args) throws IOException{

		Network network = null;
		
		// load a saved network from network.txt
		try {
			network = Network.loadNetworkFromResource("network.txt");
		} catch (NetworkNotFoundException e) {
			e.printStackTrace();
		}
		
		final double ETA = 0.5;
		final int START = 2000;
		final int END = 10000;
		final int EPOCHS = 1;
		
		// if network.txt is empty
		if (network == null){
			// create network, save it at network.txt
			network = new Network(new int[]{784,28,10},"network.txt");
			// train with MNIST images
			network.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte",ETA,START,END,EPOCHS);
		} else {
			// train with MNIST images
			network.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte",ETA,START,END,EPOCHS);
		}
		
		network.saveNetworkAtResource("network.txt");
		
	}

}
