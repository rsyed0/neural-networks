import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Class to read from .csv files and use data from teams to create inputs for neural network.
 * 
 * @author rsyed0
 */
public class NflPreprocessor {
	
	static private Network net;
	static private HashMap<String,List<NflPlayer>> nfl;
	static private HashMap<String,double[]> ratings;
	
	public NflPreprocessor(Network net){
		
		assert (net.getNetwork().get(0).length == 6 && net.getNetwork().get(net.getNetwork().size()-1).length == 1);
		this.net = net;
		
		// read Madden file for ratings
		BufferedReader sc = null;
		try{
			sc = new BufferedReader(new FileReader("nflPlayers.txt"));
		}catch (IOException e){
			System.err.println("Error reading player data.");
			System.exit(1);
		}
		
		nfl = new HashMap<>();
		ratings = new HashMap<>();
		
		String line = "";
		try{
			line = sc.readLine();
		}catch(IOException e){
			System.err.println("Error reading player data.");
			System.exit(1);
		}
		
		String teamName = "49ers";
		List<NflPlayer> team = new ArrayList<>();
		while (line != null){
			String[] lineArr = line.split(",");
			if (!teamName.equals(lineArr[0])){
				nfl.put(teamName,team);
				ratings.put(teamName,generateRatings(teamName));
				teamName = lineArr[0];
				team = new ArrayList<>();
			}
			
			team.add(new NflPlayer(Integer.parseInt(lineArr[4]),lineArr[3]));
			
			// read next line
			try {
				line = sc.readLine();
			} catch (IOException e) {
				System.err.println("Error reading player data.");
				System.exit(1);
			}
		}
		nfl.put(teamName,team);
		ratings.put(teamName,generateRatings(teamName));
		
	}
	
	public Network getNetwork(){return net;}
	public HashMap<String,List<NflPlayer>> getNfl(){return nfl;}
	public HashMap<String,double[]> getRatings(){return ratings;}
	
	public double predict(String[] gameRow){
		
		assert gameRow.length == 2;
		List<double[]> out = net.run(generateInput(gameRow));
		return out.get(out.size()-1)[0];
		
	}
	
	public void trainFromCsv(String fileName,int start,int end,int epochs,double eta){
		
		BufferedReader sc = null;
		assert (start >= 0 && end <= 256 && start <= end);
		
		// instantiate reader
		try{
			sc = new BufferedReader(new FileReader(fileName));
		}catch (IOException e){
			System.err.println("Cannot load training data from "+fileName+".");
			System.exit(1);
		}
		
		// read data from file
		String[][] fileData = new String[256][3];
		for (int r=0;r<256;r++){
			String line = "";
			try {
				line = sc.readLine();
			} catch (IOException e) {
				System.err.println("Cannot load training data from "+fileName+".");
				System.exit(1);
			}
			fileData[r] = line.split(",");
		}
		
		for (int epoch=0;epoch<epochs;epoch++){
			for (int r=start;r<end;r++){
				double[] input = generateInput(fileData[r]),
						expOutput = new double[1];
				expOutput[0] = (fileData[r][2].equals("W")) ? 1.0 : 0.0;
				net.train(input,expOutput,eta);
			}
		}
		
	}
	
	public static double[] generateInput(String[] gameRow){
		
		double[] input = new double[6];
		double[] t1 = ratings.get(gameRow[0]),t2 = ratings.get(gameRow[1]);
		
		for (int i=0;i<3;i++){
			input[i] = t1[i];
			input[3+i] = t2[i];
		}
		
		return input;
		
	}
	
	public static double[] generateRatings(String team){
		
		List<NflPlayer> t1 = nfl.get(team);
		
		double[] input = new double[3];
		int[] sum1 = new int[3],sum2 = new int[3];
		int[] num1 = new int[3],num2 = new int[3];
		
		for (NflPlayer player:t1){
			int i=0;
			if (player.getRole().equals("O")) i=0;
			else if (player.getRole().equals("D")) i=1;
			else i=2;
			
			if (num1[i] <= 11){
				sum1[i] += player.getRating();
				num1[i]++;
			}
		}
		
		for (int i=0;i<3;i++)
			input[i] = ((double)((double)sum1[i]/num1[i])/100 - 0.7)/0.16;
		
		return input;
		
	}

}
