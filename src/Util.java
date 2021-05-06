import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;


public class Util {
	// converts an integer to an array of bytes
	public static byte[] IntToByteArray( int data ) {    
	    byte[] result = new byte[4];
	    result[0] = (byte) ((data & 0xFF000000) >> 24);
	    result[1] = (byte) ((data & 0x00FF0000) >> 16);
	    result[2] = (byte) ((data & 0x0000FF00) >> 8);
	    result[3] = (byte) ((data & 0x000000FF) >> 0);
	    return result;        
	}

	// returns hash value
	public static byte[] hash(byte[] data) throws NoSuchAlgorithmException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(data);
	}
	
	public static void printArray(byte[] array)
	{
		for (int i=0; i<array.length; i++)
		{
			System.out.print(array[i]);
		}
		System.out.println("");
	}
	
	// compares two hash values and returns true/false
	public static boolean compareHashes(byte[] firstHash, byte[] secondHash)
	{
		if (firstHash.length != secondHash.length)
			return false;
		
		for (int i=0; i<firstHash.length; i++)
		{
			if (firstHash[i] != secondHash[i])
				return false;
		}
		return true;
	}
	
}
