
/**
 * An abstract parent class to represent a generalized neuron in the neural
 * network represented by the Network class.
 * 
 * @author rsyed0
 * @since 1.0
 * @see SigmoidNeuron
 * @see Perceptron
 * @see Network
 */
public abstract class Neuron {

	protected int layer;
	protected int index;
	protected double bias;
	
	// weights of connections coming in
	protected double[] weights;
	
	/**
	 * A parent class constructor to initialize all field variables based on given
	 * parameters.
	 * 
	 * @author rsyed0
	 * @since 1.0
	 * @param layer The layer which the neuron is in.
	 * @param index The index of the neuron in the layer.
	 * @param bias The bias of the neuron.
	 * @param weights The weights of the connections entering the neuron.
	 * @see SigmoidNeuron
	 * @see Perceptron
	 */
	public Neuron(int layer,int index,double bias,double[] weights){
		this.layer = layer;
		this.index = index;
		this.bias = bias;
		this.weights = weights;
	}
	
	public String toString(){
		String weightString = "";
		for (double weight:weights) weightString += weight+" ";
		weightString = weightString.substring(0,weightString.length()-1);
		return layer+" "+index+" "+bias+" "+weightString;
	}
	
	public int getLayer(){return layer;}
	public int getIndex(){return index;}
	public double getBias(){return bias;}
	public double[] getWeights(){return weights;}
	public double getWeight(int index){return weights[index];}
	
	public void setLayer(int layer){this.layer = layer;}
	public void setIndex(int index){this.index = index;}
	public void setBias(int bias){this.bias = bias;}
	public void setWeight(double newWeight,int index){this.weights[index] = newWeight;}
	
	/**
	 * A method to evaluate the output of the neuron based on the outputs
	 * of the previous layer, as well as the weights of the connections and
	 * the overall bias of the neuron. 
	 * 
	 * @author rsyed0
	 * @param inputs The outputs of the neurons in the preceding layer.
	 * @return The output of the neuron.
	 * @since 1.0
	 * @see SigmoidNeuron
	 * @see Perceptron
	 */
	abstract public double eval(double[] inputs);
	
}
