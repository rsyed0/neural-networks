
public class Perceptron extends Neuron{

	public Perceptron(int layer, int index, double bias, double[] weights) {
		super(layer, index, bias, weights);
	}
	
	public double eval(double[] inputs){
		double sigma = 0;
		for (int i=0;i<weights.length;i++){
			sigma += inputs[i]*weights[i];
		}
		if (sigma>-bias) return 1.0;
		else return 0.0;
	}
	
}
