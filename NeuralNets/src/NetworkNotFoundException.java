
/**
 * An exception that specifically indicates that a file from
 * which a network is being loaded exists and is accessible,
 * but doesn't contain text indicating a network.
 * 
 * @author rsyed0
 * @since December 27, 2017
 */
public class NetworkNotFoundException extends Exception{

	public NetworkNotFoundException(String textFile){
		super("No network found at "+textFile+".");
	}
	
}
