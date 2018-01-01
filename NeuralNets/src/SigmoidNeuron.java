
/**
 * A class to represent neurons that use a sigmoid activation function.
 * Inherits from abstract parent class Neuron. Implemented in Network class.
 * 
 * @author rsyed0
 * @see Neuron
 * @since 1.0
 * @see Network
 */
public class SigmoidNeuron extends Neuron{
	
	public SigmoidNeuron(int layer, int index, double bias, double[] weights) {
		super(layer, index, bias, weights);
	}
	
	/**
	 * An evaluation method adhering to the sigmoid activation function.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double eval(double[] inputs){
		assert (inputs.length == weights.length);
		double z = 0;
		
		for (int i=0;i<inputs.length;i++){
			z += inputs[i]*weights[i];
		}
		
		z += bias;
		return 1/(1+Math.exp(-z));
	}
	
}
