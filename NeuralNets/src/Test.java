import java.util.List;

public class Test {

	public static void main(String[] args) throws NetworkNotFoundException {
		
		// test the neural networks GUI
		// NeuralNetsGUI gui = new NeuralNetsGUI();
		
		// create new network, save it at network.txt
		// Network net = new Network(784,28,10);
		// net.saveNetworkAtResource("network.txt");
		
		// load network from network.txt
		Network net = Network.loadNetworkFromResource("network.txt");
		
		final double ETA = 0.5;
		final int START = 0;
		final int END = 60000;
		
		// net.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte", ETA, START, END);
		// net.saveNetworkAtResource("network.txt");
		
		// test network from testing data
		net.testAll("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte", 0,100);
		
	}

}
