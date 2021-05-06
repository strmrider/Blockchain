import java.util.ArrayList;
import java.util.Arrays;


public class Blockchain {
	private ArrayList<Block> chain;
	
	Blockchain()
	{
		chain = new ArrayList<Block>();
		chain.add(Block.generateGenesis());
	}
	
	Blockchain(ArrayList<Block> blocks)
	{
		chain = blocks;
	}
	
	Blockchain(Block[] blocks)
	{
		chain = new ArrayList<Block>(Arrays.asList(blocks));
	}
	
	public boolean addBlock(Block block) throws Exception
	{
		if (block.getIndex() != 0 && chain.isEmpty())
            throw new Exception("Chain was not initiated. Insert Genesis block first");
		else if (chain.size() < block.getIndex())
            throw new Exception("New block's position doesn't fit chain's order");
		else if (block.getIndex() == 0 && chain.size() == 1)
            throw new Exception("Genesis block is already exist");
		else if (block.getIndex() == 0)
		{
            chain.add(block);
            return true;
		}
        // validates the block and verifies the chain with the new block
        else if (block.isValid() && this.verifyChain(block))
        {
        	chain.add(block);
            return true;
        }
        else
            return false;
	}
	
	public boolean verifyChain(Block block)
	{
		// if empty or has only genesis block
		if (chain.size() <= 1)
			return true;
		try{
			// checks if provided new block matches chain
			if (block != null && 
					!Util.compareHashes(this.getLast().hash(), block.getPrevBlockHash()))
			{
				System.out.println("here 1");
				return false;
			}
			int size = chain.size()-1;
			byte[] currentPrevHash = chain.get(size).getPrevBlockHash();
			for (int i=size-1; i>=1; i--)
			{
					if (!Util.compareHashes(chain.get(i).hash(), currentPrevHash) )
					{
						System.out.println("here 2");
						return false;
					}
					currentPrevHash = chain.get(i).getPrevBlockHash();
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
		
		return true;
	}
	
	public Block getBlock(int index)
	{
		return this.chain.get(index);
	}
	
	public Block getLast()
	{
		return this.chain.get(this.size()-1);
	}
	
	public int size()
	{
		return this.chain.size();
	}
	
	public Blockchain copy()
	{
		try{
			Blockchain newChain = new Blockchain();
			for (int i=1; i<=this.chain.size()-1; i++)
			{
				newChain.addBlock(this.chain.get(i).copy());
			}
			return newChain;
		}
		catch (Exception e){
			return null;
		}
	}
}
