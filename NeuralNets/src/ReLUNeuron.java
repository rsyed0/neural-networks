
public class ReLUNeuron extends Neuron{

	public ReLUNeuron(int layer, int index, double bias, double[] weights) {
		super(layer, index, bias, weights);
	}
	
	@Override
	public double eval(double[] inputs){
		
		assert (inputs.length == weights.length);
		double z = 0;
		
		for (int i=0;i<inputs.length;i++){
			z += inputs[i]*weights[i];
		}
		
		return Math.max(0,z);
		
	}

}
