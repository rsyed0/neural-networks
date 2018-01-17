
public class NflInterpreter {

	public static void main(String[] args) throws NetworkNotFoundException {
		
		// train new network
		NflPreprocessor processor = new NflPreprocessor(Network.loadNetworkFromResource("nflNetwork.txt"));
		//processor.trainFromCsv("nflGames.txt",0,256,100,0.5);
		
		String home = "Patriots",away = "Jaguars";
		
		System.out.printf("The %s have a %5.2f%% chance of beating the %s.",home,processor.predict(new String[]{home,away})*100,away);
		
		// save network at nflNetwork.txt
		//processor.getNetwork().saveNetworkAtResource("nflNetwork.txt");
		
	}

}
