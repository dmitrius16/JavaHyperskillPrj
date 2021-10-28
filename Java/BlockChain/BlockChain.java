package blockchain;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.Instant;
import java.time.Duration;
import java.util.Random;

public class BlockChain implements Serializable {
    private static final long serialversionUID = 1L;
    private int currentId;
    private ArrayList<BlockElement> blockChain;
    private final String blockChainFileName;
    protected static int numLeadingZeros = 0;
    protected static String startHashTemplate = "";

    public static void setLeadingZerosCount(int zeroNum) {
        numLeadingZeros = zeroNum;
        startHashTemplate = generateLeadingZerosString(zeroNum);
    }

    private static String generateLeadingZerosString(int numZeros) {
        char ch = '0';
        StringBuilder sb = new StringBuilder();
        while(numZeros > 0) {
            sb.append(ch);
            numZeros--;
        }
        return sb.toString();
    }

    public BlockChain(String fileName) {
        currentId = 1;
        blockChain = new ArrayList<>();
        blockChainFileName = fileName;
    }

    public void addElem(Payload payload) {
        String hashPrevBlock = "0";
        int curInd = currentId - 2;
        if (currentId > 1) {    //after first adding
            hashPrevBlock = blockChain.get(curInd).getHash();
        }
        blockChain.add(new BlockElement(currentId, payload, hashPrevBlock));
        currentId++;

        try {
            SerializationUtils.serialize(this, blockChainFileName);
        } catch (IOException ex) {
            System.out.println("Exception occur: " + ex);
        }

    }

    public void outBlocks() {
        blockChain.forEach(BlockElement::outBlock);
    }


}
