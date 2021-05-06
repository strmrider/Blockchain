import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;


public class MerkleTree {
	public static byte[] sumHashes(byte[] firstHash, byte[] secondHash)
	{
		byte[] sum = new byte[firstHash.length];
		for (int i=0; i<firstHash.length; i++)
		{
			sum[i] = (byte) (firstHash[i] + secondHash[i]);
		}
		return sum;
	}
	
	public static byte[] calculateRoot(ArrayList<byte[]> hashes) throws NoSuchAlgorithmException
	{
		if (hashes.size() == 1)
			return hashes.get(0);
		byte[] hashSum;
		byte[] combinedHash;
		ArrayList<byte[]> newList = new ArrayList<byte[]>();
		for (int i=0; i<hashes.size(); i+=2)
		{
			if (i+1 < hashes.size()){
				hashSum = sumHashes(hashes.get(i), hashes.get(i+1));
				combinedHash = Util.hash(hashSum);
				newList.add(combinedHash);
			}
			else
				newList.add(hashes.get(i));
		}
		return calculateRoot(newList);
	}
}
