import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws NetworkNotFoundException,IOException {
		
		// test the neural networks GUI
		// NeuralNetsGUI gui = new NeuralNetsGUI();
		
		// create new network, save it at network.txt
		//Network net = new Network(784,75,10);
		//net.saveNetworkAtResource("network.txt");
		
		// fix NFL data file(s)
		Scanner pos = new Scanner(new File("nflPositions.txt"));
		List<String> off = new ArrayList<>(),def = new ArrayList<>(),spe = new ArrayList<>();
		while (pos.hasNextLine()){
			String[] line = pos.nextLine().split(" ");
			if (line[1].equals("Off")) off.add(line[0]);
			else if (line[1].equals("Def")) def.add(line[0]);
			else spe.add(line[0]);
		}
		
		HashSet<String> positions = new HashSet<>();
		String[][] out = new String[2353][5];
		Scanner play = new Scanner(new File("nflPlayers.txt"));
		int r=0;
		
		while (play.hasNextLine()){
			String[] line = play.nextLine().split(",");
			if (off.contains(line[3])) line[3] = "O";
			else if (def.contains(line[3])) line[3] = "D";
			else line[3] = "S";
			out[r] = line;
			r++;
		}
		play.close();
		
		PrintWriter pw = new PrintWriter(new File("nflPlayers.txt"));
		for (String[] player:out){
			int c=0;
			for (String entry:player){
				if (c!=4) pw.print(entry+",");
				else pw.println(entry);
				c++;
			}
		}
		pw.close();
		
		// render images
		//for (int i=0;i<10;i++){
			//MnistImageRenderer img = new MnistImageRenderer("data/emnist-mnist-train-images.idx3-ubyte", "data/emnist-mnist-train-images.idx1-ubyte",45,false);
		//}
		
		// load network from network.txt
		//Network net = Network.loadNetworkFromResource("network.txt");
			
		// segment image into usable parts
		//List<BufferedImage> imgs = (new Segmenter("mnist.jpeg")).horizontalSplit(net);
		
		final double ETA = 0.5;
		final int START = 0;
		final int END = 60000;
		final int EPOCHS = 50;
		final int NUM_IMAGES = 10000;
		
		// train network and save at network.txt
		//net.trainFromMnistData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte", ETA, START, END, EPOCHS, false);
		
		// test network from testing data
		//net.testAll("data/t10k-images.idx3-ubyte", "data/t10k-labels.idx1-ubyte",0,NUM_IMAGES,false,false);
		//net.saveNetworkAtResource("network.txt");
		
	}

}
