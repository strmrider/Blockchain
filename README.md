# Blockchain
Blockchain Java implementation.

## Examples
The examples below demonstrates how to use the blockchain's library components.
### Block's data
Beside it's header, each block in the chain also stores data. This data could be of any type.
```Java
Data data<T> = new Data<T>([HERE IS THE DATA OBJECT OF TYPE T]);
```
Here's a demonstration using transactions as the data (each transaction is represented by a simple string format).
```Java
// creates list of string
ArrayList<String> txs = new ArrayList<String>();
Random rand = new Random();
int rand_int;
// create 10 sample transactions
for (int i=0; i<10; i++)
{
    rand_int = rand.nextInt(1000);
    tx = String.format("{recipient:%s, amount: %s}", String.valueOf(rand_int)+String.valueOf(i), String.valueOf(i));
    txs.add(tx);
}
```
Now wrap the transactions list in a Data object.
```Java
Data<ArrayList<String>> txsList = new Data<ArrayList<String>>(txs);

// extract the list
txsList.getData();
```
### Merkle tree
The data in the block in hashed for the blockchain's validation purposes. In case of multiple data elements use a Merkle tree root (see [Merkle tree](https://en.wikipedia.org/wiki/Merkle_tree))

First create hash values for the transactions
```Java
ArrayList<byte[]> hashes = new ArrayList<byte[]>();
byte[] currentBytes;
for (int i=0; i <txs.size(); i++)
{
    currentBytes = txs.get(i).getBytes();
    hashes.add(Util.hash(currentBytes));
}
```
Then calculate the root passing the hash list as an argument
```Java
byte[] root = MerkleTree.calculateRoot(hashes);
```
### Block
The block is composed by a header and the actual data the block chain is designated to store. The header is composed by the block's index, it's miner (see below), previous block's hash value, data's merkle root and the itself.
```Java
Block block = new Block(1, "miner_name", prevHash, merkleRoot, txsList);
```
#### Mining
Miners create new blocks by solving complex computational math problems. The result is a unique header's hash value used for the blockchain's validation process and a proof of work number (`nonce`) used to validate the hash value itself.
```Java
// simply use this method to calculate the block's header hash
byte[] hash = block.calculateHeaderHash();
// get nonce value
int nonce = block.getNonce();
// you can hash the block any time you want, without the complexity, once the nonce has been set
hash = block.hash();
// validate the hash
boolean isValid = block.isValid();
```
#### Difficulty
The proof of work complexity is defined by the difficulty, which is the condition that determines whether the hash value is suffice. The defualt difficulty is 3 zeros at the beginning of the hash value. It means that the calcualtion is concluded only when the hash value has 3 zeros (`0x00 0x00 0x00`) in it's first 3 bytes. In case a different difficulty is requiered, you can always override the difficulty method and define any other difficulty.
```Java
class NewBlock extends Block
{
    protected boolean difficulty(byte[] hash)
    {
        // here 4 zeros are required
        return (hash[0] == 0x00 && hash[1] == 0x00 && hash[2] == 0x00 && hash[3] == 0x00);
    }
}
```
#### API
**`Block(int index, String miner, byte[] prevhash, byte[] root, Data data)`**

  Creates new block. Parameters are block's index, miner's name or id, previous block's hash value, data's merkle root and the data wrapper itself.

**`Block generateGenesis()`**

  Static method that returns a genesis block (blockchain's first block) with the index 0.
  
**`byte[] hash()`**
  
  Return's the block's header ahs value.
  
**`byte[] calculateHeaderHash()`**

  Calculates header's hash value according to the defined difficulty and sets the resulted nonce value as proof of work.
  
**`boolean isValid()`**
  
  Returns whether the block is valid based on it's hash and the proof of work values.
  
**`Block copy()`**
  
  Returns a copy of the current block;

Block's getters:
```Java
// returns block's data wrapper
Data data = block.getData();
// returns index
int index = block.getIndex();
// returns timestamp
String time = block.getTime();
// returns miner
String miner = block.getMiner();
// returns previous block's hash value
byte[] hash = block.getPrevBlockHash();
// returns root
byte[] root = block.getRoot();
// returns nonce
int nonce = block.getNonce();
```
### Blockchain
The blockchain class cotains and manages a list of blocks.
```Java
// creates a new blockchain and initializes it with a new genesis block
Blockchain blockchain = new Blockchain();
// adds new block to the chain and returns true if it is successfully added
boolean result = blockchain.addBlock(block);
```
#### API
**`Blockchain()`**
  
  Creates a new blockchain and initializes it with a genesis block.

**`Blockchain(ArrayList<Block> blocks)`**

  Initializes a new blockchian from an ArrayList of blocks.

**`Blockchain(Block[] blocks)`**
  
   Builds a new blockchian from an array of blocks.

**`boolean addBlock(Block block)`**
  
  Adds a new block to the chain. Returns true or false if it is successfully/unsuccessfully added or throws an exception with more detailed explanation.
  
**`boolean verifyChain(Block block)`**
  
  Verifies blockchain's validity. Also verifies with a new block if provided.
  
**`Block getBlock(int index)`**
  
  Returns a block in the chain by a given index.

**`Block getLast()`**
  
  Returns last block in chain.
  
**`int size()`**
  
  Returns the number of blocks in chain.

**`Blockchain copy()`**

  Returns a copy of the blockchain.


