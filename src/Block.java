import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Block {
	private int index;
	private String time;
	private String miner;
	private byte[] prevBlockHash;
	private int nonce = 0;
	private byte[] root;
	private byte[] headerHash;
	private Data data;
	
	// new Block constructor
	Block(int index, String miner, byte[] prevhash, byte[] root, Data data)
	{
		this.init(index,miner, prevhash, root, data, "", 0);
	}
	
	// full initialization constructor
	private Block(int index, String miner, byte[] prevhash, byte[] root, Data data, String time, int nonce)
	{
		this.init(index, miner, prevhash, root, data, time, nonce);
		try {
			this.headerHash = this.hash();
		} catch (NoSuchAlgorithmException | IOException e) {
		}
	}
	
	// initialization method
	private void init(int index, String miner, byte[] prevhash, byte[] root, Data data, String time, int nonce)
	{
		this.index = index;
		this.miner = miner;
		this.prevBlockHash = prevhash;
		this.root = root;
		this.data = data;
		this.time = time;
		this.nonce = nonce;
	}
	
	// static method returns a Genesis block
	public static Block generateGenesis()
	{
		byte[] dummyHash = {0x00};
		byte[] dummyRoot = {0x00};
		return new Block(0, "Genesis", dummyHash, dummyRoot, null);
	}
	
	// sets header's time stamp
	private void setTime()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		this.time = dtf.format(now);
	}
	
	// returns header in bytes
	private byte[] binHeader() throws IOException
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write( Util.IntToByteArray(this.index) );
		outputStream.write(this.time.getBytes());
		outputStream.write( this.miner.getBytes() );
		outputStream.write( Util.IntToByteArray(this.nonce) );
		outputStream.write( this.prevBlockHash );
		outputStream.write( this.root );

		return outputStream.toByteArray();
	}
	
	// returns block's hash value
	public byte[] hash() throws IOException, NoSuchAlgorithmException
	{
		return Util.hash(this.binHeader());
	}
	
	// calculates header's hash value and it's proof of work
	public byte[] calculateHeaderHash() throws IOException, NoSuchAlgorithmException
	{
		this.nonce = 0;
		this.setTime();
		byte[] currentHash;
		while (true)
		{
			currentHash = this.hash();
			if (this.difficulty(currentHash))
			{
				this.headerHash = currentHash;
				return currentHash;
			}
			this.nonce++;
		}
	}
	
	// proof of work difficulty condition
	protected boolean difficulty(byte[] hash)
	{
		return (hash[0] == 0x00 && hash[1] == 0x00);
	}
	
	// checks block validity
	public boolean isValid() throws NoSuchAlgorithmException, IOException
	{
		this.headerHash = this.hash();
		return this.difficulty(this.headerHash);
	}
	
	// returns a copy of the block
	public Block copy() throws NoSuchAlgorithmException, IOException
	{
		Block block = new Block(this.index, this.miner, this.prevBlockHash, this.root, data, this.time, this.nonce);
		return block;
	}
	
	/* Getters */
	public Data getData()
	{
		return this.data;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
	public String getTime()
	{
		return this.time;
	}
	
	public String getMiner()
	{
		return this.miner;
	}
	
	public byte[] getPrevBlockHash()
	{
		return this.prevBlockHash;
	}
	
	public byte[] getRoot()
	{
		return this.root;
	}
	
	public int getNonce()
	{
		return this.nonce;
	}
}
