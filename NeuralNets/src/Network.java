import java.util.List;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A class to represent an artificial neural network (ANN), currently being used to classify
 * images from the MNIST dataset in conjunction with the ImageInterpreter class. 
 * 
 * @author rsyed0
 * @see SigmoidNeuron
 * @see MnistImageInterpreter
 */
public class Network {
	
	/**
	 * A list of arrays of sigmoid neurons representing an artificial neural network
	 * (ANN). Used to store the neurons in the Network class. Implementation of a no-
	 * argument constructor.
	 * 
	 * @see List
	 * @see SigmoidNeuron
	 * @see ArrayList
	 * @category List
	 */
	private List<SigmoidNeuron[]> network = new ArrayList<>();
	
	/**
	 * A constructor to create an empty artificial neural network (ANN).
	 * 
	 * @author rsyed0
	 * @since 1.0
	 */
	public Network(){network = new ArrayList<>();}
	
	/**
	 * A constructor to create an artificial neural network (ANN) with the given sizes 
	 * for each layer. The weights and biases for each connection/neuron are initially
	 * randomized, and thus, the network must be trained to perform any tasks.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param sizes The sizes of each layer.
	 */
	public Network(Integer... sizes){

		assert (sizes.length >= 3);
		int lastSize = 1;
		for (int s=0;s<sizes.length;s++){
			assert (sizes[s] > 0);
			SigmoidNeuron[] neurons = new SigmoidNeuron[sizes[s]];
			for (int i=0;i<sizes[s];i++){
				double[] weights = new double[lastSize];
				
				if (s==0) 
					weights = new double[]{1.0};
				else{
					for (int j=0;j<lastSize;j++)
						weights[j] = (1-2*Math.random());
				}
				neurons[i] = new SigmoidNeuron(s,i,-Math.random(),weights);
			}
			network.add(neurons);
			lastSize = sizes[s];
		}
		
	}
	
	/**
	 * A constructor to create an artificial neural network (ANN) with the given sizes
	 * for each layer, and save the network and its parameters as a text file with the
	 * given file name. The weights and biases for each connection/neuron are initially
	 * randomized, and thus, the network must be trained to perform any tasks.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param sizes The sizes of each layer as an integer array.
	 * @param fileName The name of the file to be written to when saving.
	 */
	public Network(int[] sizes,String fileName){
		
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(new File(fileName));
		}catch(IOException e){
			System.err.println("Failed to save network.");
			System.exit(1);
		}

		int layer = 0;
		int lastSize = 1;
		for (int size:sizes){
			assert size > 0;
			SigmoidNeuron[] neurons = new SigmoidNeuron[size];
			for (int i=0;i<size;i++){
				double[] weights = new double[lastSize];
				for (int j=0;j<lastSize;j++)
					weights[j] = (1-2*Math.random());
				neurons[i] = new SigmoidNeuron(layer,i,-Math.random(),weights);
				//System.out.println(neurons[i]);
				pw.println(neurons[i]);
			}
			network.add(neurons);
			layer++;
			lastSize = size;
		}
		
		pw.close();
	}
	
	/**
	 * A constructor to create an artificial neural network (ANN) with the given neurons
	 * for each layer, and save the network and its parameters as a text file with the
	 * given file name. The weights and biases for each connection/neuron are initially
	 * randomized, and thus, the network must be trained to perform any tasks.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @see loadNetworkFromResource
	 * @see SigmoidNeuron
	 * @param neurons A list of sigmoid neurons containing each neuron in the network.
	 */
	public Network(List<SigmoidNeuron> neurons){
		
		int layer = 0;
		List<SigmoidNeuron> holder = new ArrayList<>();
		for (SigmoidNeuron neuron:neurons){
			if (layer != neuron.getLayer()){
				SigmoidNeuron[] newLayer = new SigmoidNeuron[holder.size()];
				for (int i=0;i<holder.size();i++)
					newLayer[i] = holder.get(i);
				network.add(newLayer);
				holder = new ArrayList<>();
				layer++;
			}
			holder.add(neuron);
		}
		SigmoidNeuron[] newLayer = new SigmoidNeuron[holder.size()];
		for (int i=0;i<holder.size();i++)
			newLayer[i] = holder.get(i);
		network.add(newLayer);
		
	}
	
	/**
	 * Takes an array of floating point values as inputs for the ANN, and returns an array
	 * of floating point values representing the outputs of each layer of the ANN.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param inputs A double array giving the inputs to the first layer of the network.
	 * @return The output from each layer of the network as a list of double arrays.
	 */
	public List<double[]> run(double[] inputs){
		assert (network.get(0).length == inputs.length);
		
		List<double[]> outputs = new ArrayList<>();
		outputs.add(inputs);
		
		for (int layer=1;layer<network.size();layer++){
			double[] layerOutput = new double[network.get(layer).length];
			
			for (int index=0;index<network.get(layer).length;index++){
				//System.out.println(layer+" "+outputs.get(layer-1).length+" "+network.get(layer)[index].getWeights().length);
				layerOutput[index] = network.get(layer)[index].eval(outputs.get(layer-1));
			}
			
			outputs.add(layerOutput);
		}
		
		return outputs;
	}
	
	/**
	 * A method that takes a set of MNIST images from the starting index at the given address up to,
	 * but discluding, the ending index, and converts them into double arrays based on their brightnesses.
	 * A value of 1.0 indicates full darkness, while a value of 0.0 indicates complete light.
	 * These double arrays are then compiled into a list, and returned.
	 * 
	 * @param imagesName The address of the images being loaded.
	 * @param labelsName The address of the labels for the images being loaded.
	 * @param start The starting index of the image to be converted into a brightness array.
	 * @param end The ending index of the image to be converted into a brightness array.
	 * @param bw A boolean specifying if grays should be converted to blacks.
	 * @return A double array listing every pixel's brightness, listed row by row.
	 */
	public static List<double[]> convertToBrightness(String imagesName,String labelsName,int start,int end,boolean bw){
		
		// load training image data from MNIST dataset
		MnistMatrix[] mnistMatrix = null;
		try {
			mnistMatrix = new MnistDataReader().readData(imagesName,labelsName);
		} catch (IOException e) {
			System.err.println("Failed to find "+imagesName+".");
			System.exit(1);
		}
		
		List<double[]> out = new ArrayList<>();
		for (int index=start;index<end;index++){
			MnistMatrix img = mnistMatrix[index];
			
			int r = img.getNumberOfRows();
			int c = img.getNumberOfColumns();
			
			double[] brightness = new double[r*c];
	
			// populate brightness array
			if (bw){
				for (int i=1;i<r*c;i++)
					brightness[i-1] = img.getValue(i/c,i%c) == 0 ? 0.0 : 1.0;
			} else {
				for (int i=1;i<r*c;i++)
					brightness[i-1] = (img.getValue(i/c,i%c)/256.0);
			}
			
			out.add(brightness);
		}
		return out;
		
	}
	
	/**
	 * Trains the ANN using data at the given location, which must be in the form given in
	 * the MNIST dataset. Uses the MnistDataReader class to transform the MNIST data into
	 * the form of instances of the MnistMatrix class. This method implements the stochastic
	 * gradient descent (SGD) algorithm with backpropagation to edit weights and biases such 
	 * that the network can classify images with a lower percentage of error.
	 * 
	 * @author rsyed0
	 * @param imagesName A string giving the address of the training images.
	 * @param labelsName A string giving the address of the training labels.
	 * @param eta The rate at which the network should learn; the learning rate.
	 * @param start The starting index of the MNIST images that should be shown to the network.
	 * @param end The ending index of the MNIST images that should be shown to the network. 
	 * @param epochs The number of times that the set of images should be shown.
	 * @param bw A boolean specifying whether or not grays should be reflected as black.
	 * @since 1.0
	 * @see MnistDataReader
	 * @see MnistMatrix
	 */
	public void trainFromMnistData(String imagesName,String labelsName,double eta,int start,int end,int epochs,boolean bw){
		
		// load training image data from MNIST dataset
		MnistMatrix[] mnistMatrix = null;
		try {
			mnistMatrix = new MnistDataReader().readData(imagesName,labelsName);
		} catch (IOException e) {
			System.err.println("Failed to find "+imagesName+" or "+labelsName+".");
			System.exit(1);
		}
		
		// assert that the last layer's length is ten and the start and end parameters are within range
		assert (network.get(network.size()-1).length == 10);
		assert (start>=0 && end<=mnistMatrix.length);
		
		// run a certain number of epochs
		for (int epoch=0;epoch<epochs;epoch++){
			
			System.out.println("Starting epoch "+(epoch+1)+"...");
			
			// for each MNIST image within range
			for (int image=start;image<end;image++){
				MnistMatrix img = mnistMatrix[image];
				
				int r = img.getNumberOfRows();
				int c = img.getNumberOfColumns();
				int label = img.getLabel();
				
				double[] target = new double[10];
				double[] brightness = new double[r*c];
	
				// populate brightness array
				if (bw){
					for (int i=1;i<r*c;i++)
						brightness[i-1] = img.getValue(i/c,i%c) == 0 ? 0.0 : 1.0;
				} else {
					for (int i=1;i<r*c;i++)
						brightness[i-1] = (img.getValue(i/c,i%c)/256.0);
				}
				
				// populate target array
				for (int i=0;i<10;i++){
					if (i==label) target[i] = 1.0;
					else target[i] = 0.0;
				}
				
				// run ANN, store values as out
				List<double[]> out = run(brightness);
				
				/* 
				 * Start of backpropagation training of ANN. The algorithm computes a quantity called
				 * the node delta for each neuron, equal to the partial derivative of the total error
				 * of the neural network with respect to the net input of that neuron. The partial
				 * derivative of the total error with respect to the value of the weight of a connection
				 * terminating at that neuron is equal to its node delta multiplied by the output of
				 * the originating neuron. This rate of change is used to update the weight of each
				 * connection by subtracting from that weight a quantity equal to the learning rate
				 * eta multiplied by the rate of change, or partial derivative.
				 * 
				 * See also: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
				 * 
				 * Written by Reedit Shahriar on December 28, 2017.
				 */
				
				// node deltas for each neuron in previous layer
				List<Double> nodeDeltas = new ArrayList<>();
				
				// compute first layer of weights
				// for each neuron in the output layer
				for (int index=0;index<network.get(network.size()-1).length;index++){
					
					// compute neuron node delta
					double neuralOutput = out.get(network.size()-1)[index];
					double nodeDelta = (neuralOutput-target[index])*((neuralOutput)*(1-neuralOutput));
					nodeDeltas.add(nodeDelta);
					
					// get weights for this output neuron
					double[] weights = network.get(network.size()-1)[index].getWeights(); 
					
					// for each connection weight connected to the output neuron, update that weight
					for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
						network.get(network.size()-1)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(network.size()-2)[weightIndex]),weightIndex);
					
				}
				
				// go through each layer of network backward
				for (int layer=network.size()-2;layer>0;layer--){
					
					List<Double> newNodeDeltas = new ArrayList<>();
					
					for (int index=0;index<network.get(layer).length;index++){
						
						// get weights for this neuron
						double[] weights = network.get(layer)[index].getWeights(); 
						
						// compute neuron node delta
						double sum = 0;
						for (int n=0;n<nodeDeltas.size();n++)
							sum += nodeDeltas.get(n)*network.get(layer+1)[n].getWeight(index);
						
						double nodeDelta = sum*out.get(layer)[index]*(1-out.get(layer)[index]);
						newNodeDeltas.add(nodeDelta);
						
						// for each connection weight connected to the neuron, update that weight
						for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
							network.get(layer)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(layer-1)[weightIndex]),weightIndex);
						
					}
					
					nodeDeltas = newNodeDeltas;
					
				}
			}	
		}
	}
	
	/**
	 * A method that trains the network using backpropagation given a set of inputs and 
	 * the correct output. It trains the network "at" the given rate.
	 * 
	 * @author rsyed0
	 * @since 1.2
	 * @param brightness The input to the network.
	 * @param label The correct output from the network.
	 * @param eta The rate at which the network should learn; the learning rate.
	 */
	public void train(double[] brightness,int label,double eta){
		
		assert (brightness.length == network.get(0).length);
		
		double[] target = new double[10];
		
		// populate target array
		for (int i=0;i<10;i++){
			if (i==label) target[i] = 1.0;
			else target[i] = 0.0;
		}
		
		// run ANN, store values as out
		List<double[]> out = run(brightness);
		
		/* 
		 * Start of backpropagation training of ANN. The algorithm computes a quantity called
		 * the node delta for each neuron, equal to the partial derivative of the total error
		 * of the neural network with respect to the net input of that neuron. The partial
		 * derivative of the total error with respect to the value of the weight of a connection
		 * terminating at that neuron is equal to its node delta multiplied by the output of
		 * the originating neuron. This rate of change is used to update the weight of each
		 * connection by subtracting from that weight a quantity equal to the learning rate
		 * eta multiplied by the rate of change, or partial derivative.
		 * 
		 * See also: https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
		 * 
		 * Written by Reedit Shahriar on December 28, 2017.
		 */
		
		// node deltas for each neuron in previous layer
		List<Double> nodeDeltas = new ArrayList<>();
		
		// compute first layer of weights
		// for each neuron in the output layer
		for (int index=0;index<network.get(network.size()-1).length;index++){
			
			// compute neuron node delta
			double neuralOutput = out.get(network.size()-1)[index];
			double nodeDelta = (neuralOutput-target[index])*((neuralOutput)*(1-neuralOutput));
			nodeDeltas.add(nodeDelta);
			
			// get weights for this output neuron
			double[] weights = network.get(network.size()-1)[index].getWeights(); 
			
			// for each connection weight connected to the output neuron, update that weight
			for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
				network.get(network.size()-1)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(network.size()-2)[weightIndex]),weightIndex);
			
		}
		
		// go through each layer of network backward
		for (int layer=network.size()-2;layer>0;layer--){
			
			List<Double> newNodeDeltas = new ArrayList<>();
			
			for (int index=0;index<network.get(layer).length;index++){
				
				// get weights for this neuron
				double[] weights = network.get(layer)[index].getWeights(); 
				
				// compute neuron node delta
				double sum = 0;
				for (int n=0;n<nodeDeltas.size();n++)
					sum += nodeDeltas.get(n)*network.get(layer+1)[n].getWeight(index);
				
				double nodeDelta = sum*out.get(layer)[index]*(1-out.get(layer)[index]);
				newNodeDeltas.add(nodeDelta);
				
				// for each connection weight connected to the neuron, update that weight
				for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
					network.get(layer)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(layer-1)[weightIndex]),weightIndex);
				
			}
			
			nodeDeltas = newNodeDeltas;
			
		}
	}
	
	/**
	 * A method that tests the neural network with MNIST training data. The network must
	 * have an output layer of size 10 and input layer of size 784.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param imagesName The address at which the MNIST testing images can be found.
	 * @param labelsName The address at which the MNIST testing labels can be found.
	 * @param start The starting index of the training images to be "shown" to the network.
	 * @param end The starting index of the training images to be "shown" to the network.
	 * @param showMistakes A boolean indicating whether or not to render all MNIST images where the network made a mistake.
	 * @param bw A boolean specifying whether or not grays should be reflected as blacks.
	 */
	public void testAll(String imagesName,String labelsName,int start,int end,boolean showMistakes,boolean bw){
		
		// load training image data from MNIST dataset
		MnistMatrix[] mnistMatrix = null;
		try {
			mnistMatrix = new MnistDataReader().readData(imagesName,labelsName);
		} catch (IOException e) {
			System.err.println("Failed to find "+imagesName+" or "+labelsName+".");
			System.exit(1);
		}
		
		int total = end-start;
		int correct = 0;
		
		for (int index=start;index<end;index++){
			
			// for the MNIST image
			MnistMatrix img = mnistMatrix[index];
				
			int r = img.getNumberOfRows();
			int c = img.getNumberOfColumns();
			int label = img.getLabel();
			
			double[] brightness = new double[r*c];
	
			// populate brightness array
			if (bw){
				for (int i=1;i<r*c;i++)
					brightness[i-1] = img.getValue(i/c,i%c) == 0 ? 0.0 : 1.0;
			} else {
				for (int i=1;i<r*c;i++)
					brightness[i-1] = (img.getValue(i/c,i%c)/256.0);
			}
			
			//System.out.print("Label: "+label+", Output: ");
			List<double[]> output = run(brightness);
			
			int out = 0;
			double max = 0.0;
			for (int i=0;i<10;i++){
				double d = output.get(output.size()-1)[i];
				if (max<d){
					out = i;
					max = d;
				}
			}
			//System.out.print(out+"\n");
			
			if (out == label) correct++;
			else if (showMistakes) new MnistImageRenderer(img);
		}
		
		System.out.println("Correct: "+correct+"/"+total+", Error: "+100*(1.0-((double)correct/total))+"%");
		
	}
	
	/**
	 * Saves a network description at the given file resource address.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param fileName A string giving the address of the file resource that the network
	 * is to be saved to.
	 */
	public void saveNetworkAtResource(String fileName){
		
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(new File(fileName));
		}catch(IOException e){
			System.err.println("Failed to save network.");
			System.exit(1);
		}
		
		for (int layer=0;layer<network.size();layer++){
			for (int index=0;index<network.get(layer).length;index++)
				pw.println(network.get(layer)[index]);
		}
		
		pw.close();
		
	}
	
	/**
	 * Loads a saved network from a given file name pointing to a text file resource.
	 * Returns the network obtained from analyzing the text file. Throws a NetworkNot
	 * FoundException if there is no network at the specified resource.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @see SigmoidNeuron
	 * @see NetworkNotFoundException
	 * @param fileName A string giving the address of the file resource to be loaded.
	 * @return An object of the Network class representing the network from the loaded resource.
	 * @throws NetworkNotFoundException
	 */
	public static Network loadNetworkFromResource(String fileName) throws NetworkNotFoundException{
		
		List<SigmoidNeuron> neurons = new ArrayList<>();
		
		String line = "";
		BufferedReader sc = null;
		try{
			sc = new BufferedReader(new FileReader(fileName));
			line = sc.readLine();
		}catch (IOException e){
			System.err.println("Error accessing file.");
			System.exit(1);
		}
		
		while (line != null){
			String[] nums = line.split(" ");
			double[] weights = new double[nums.length-3];
			
			try{
				for (int i=0;i<weights.length;i++)
					weights[i] = Double.parseDouble(nums[3+i]);
				
				neurons.add(new SigmoidNeuron(Integer.parseInt(nums[0]),Integer.parseInt(nums[1]),Double.parseDouble(nums[2]),weights));
				try{
					line = sc.readLine();
				}catch (IOException e){
					System.err.println("Error reading from file.");
				}
			} catch (Exception e){
				throw new NetworkNotFoundException(fileName);
			}
		}
		
		return new Network(neurons);
		
	}
	
	/**
	 * A generalized method for training a network given a set of input values
	 * and the expected, or ideal, set of output values using the backpropagation
	 * algorithm.
	 * 
	 * @param input The input values to the network.
	 * @param expOutput The expected output values from the network.
	 * @param eta The rate at which the network should learn.
	 */
	public void train(double[] input, double[] expOutput, double eta){
		
		assert (input.length == network.get(0).length && expOutput.length == network.get(network.size()-1).length);
		
		// run ANN, store values as out
		List<double[]> out = run(input);
				
		// node deltas for each neuron in previous layer
		List<Double> nodeDeltas = new ArrayList<>();
		
		// compute first layer of weights
		// for each neuron in the output layer
		for (int index=0;index<network.get(network.size()-1).length;index++){
			
			// compute neuron node delta
			double neuralOutput = out.get(network.size()-1)[index];
			double nodeDelta = (neuralOutput-expOutput[index])*((neuralOutput)*(1-neuralOutput));
			nodeDeltas.add(nodeDelta);
			
			// get weights for this output neuron
			double[] weights = network.get(network.size()-1)[index].getWeights(); 
			
			// for each connection weight connected to the output neuron, update that weight
			for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
				network.get(network.size()-1)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(network.size()-2)[weightIndex]),weightIndex);
			
		}
		
		// go through each layer of network backward
		for (int layer=network.size()-2;layer>0;layer--){
			
			List<Double> newNodeDeltas = new ArrayList<>();
			
			for (int index=0;index<network.get(layer).length;index++){
				
				// get weights for this neuron
				double[] weights = network.get(layer)[index].getWeights(); 
				
				// compute neuron node delta
				double sum = 0;
				for (int n=0;n<nodeDeltas.size();n++)
					sum += nodeDeltas.get(n)*network.get(layer+1)[n].getWeight(index);
				
				double nodeDelta = sum*out.get(layer)[index]*(1-out.get(layer)[index]);
				newNodeDeltas.add(nodeDelta);
				
				// for each connection weight connected to the neuron, update that weight
				for (int weightIndex=0;weightIndex<weights.length;weightIndex++)
					network.get(layer)[index].setWeight(weights[weightIndex]-(eta*nodeDelta*out.get(layer-1)[weightIndex]),weightIndex);
				
			}
			
			nodeDeltas = newNodeDeltas;
			
		}
		
	}
	
	/**
	 * A method that loads a network from a List of Strings.
	 * 
	 * @param file The file as a List of Strings.
	 * @param fileName The name of the file, to be mentioned in the case of malfunction.
	 * @return Network stored at the given file.
	 * @throws NetworkNotFoundException
	 */
	public static Network loadNetworkFromResource(List<String> file,String fileName) throws NetworkNotFoundException{
		
		List<SigmoidNeuron> neurons = new ArrayList<>();
		
		for (String line:file){
			String[] nums = line.split(" ");
			double[] weights = new double[nums.length-3];
			
			try{
				for (int i=0;i<weights.length;i++)
					weights[i] = Double.parseDouble(nums[3+i]);
				
				neurons.add(new SigmoidNeuron(Integer.parseInt(nums[0]),Integer.parseInt(nums[1]),Double.parseDouble(nums[2]),weights));
			} catch (Exception e){
				throw new NetworkNotFoundException(fileName);
			}
		}
		
		return new Network(neurons);
		
	}
	
	/**
	 * A method that returns the sizes of each layer in the network in the form
	 * of an integer array. 
	 * 
	 * @return An integer array giving the sizes of each layer in the network.
	 */
	public int[] sizes(){
		int[] out = new int[network.size()];
		for (int i=0;i<network.size();i++)
			out[i] = network.get(i).length;
		return out;
	}
	
	public List<SigmoidNeuron[]> getNetwork(){return network;}
	
}
