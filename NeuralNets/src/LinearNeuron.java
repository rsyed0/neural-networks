
/**
 * A class representing a neuron adhering to a linear activation function.
 * 
 * @author rsyed0
 * @since December 27, 2017
 */
public class LinearNeuron extends Neuron{

	public LinearNeuron(int layer, int index, double bias, double[] weights) {
		super(layer, index, bias, weights);
	}

	/**
	 * Evaluates the linear activation function.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double eval(double[] inputs) {
		assert (inputs.length == weights.length);
		double z = 0;
		for (int i=0;i<inputs.length;i++){
			z += inputs[i]*weights[i];
		}
		z += bias;
		return z;
	}

}
