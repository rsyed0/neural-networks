import java.util.Scanner;
public class NflInterpreter {

	public static void main(String[] args) throws NetworkNotFoundException {
		
		// train new network
		NflPreprocessor processor = new NflPreprocessor(Network.loadNetworkFromResource("nflNetwork.txt"));
		//processor.trainFromCsv("nflGames.txt",0,256,100,0.5);
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter the home team: ");
		String homeTeam = sc.nextLine();
		System.out.print("Enter the away team: ");
		String awayTeam = sc.nextLine();
		
		String[] homeTeamArr = homeTeam.split(" "),awayTeamArr = awayTeam.split(" ");
		
		final String home = homeTeamArr[homeTeamArr.length-1],away = awayTeamArr[awayTeamArr.length-1];
		final String homeCity = homeTeam.substring(0,homeTeam.indexOf(home)-1),awayCity = awayTeam.substring(0,awayTeam.indexOf(away)-1);
		
		System.out.printf("The %s %s have a %5.2f%% chance of beating the %s %s at home.",homeCity,home,
				processor.predict(new String[]{home,away})*100,awayCity,away);
		
		// save network at nflNetwork.txt
		//processor.getNetwork().saveNetworkAtResource("nflNetwork.txt");
		
	}

}
