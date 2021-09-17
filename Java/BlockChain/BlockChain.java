package blockchain;
import java.util.ArrayList;
import java.util.Date;

public class BlockChain {
    private int currentId;
    private ArrayList<BlockElement> blockChain;
    public BlockChain() {
        currentId = 1;
        blockChain = new ArrayList<>();
    }

    public void addElem(Payload payload) {
        String hashPrevBlock = "0";
        int curInd = currentId - 2;
        if (currentId > 1) {
            hashPrevBlock = blockChain.get(curInd).getHash();
        }
        blockChain.add(new BlockElement(currentId, payload, hashPrevBlock));
        currentId++;
    }

    public void outBlocks() {
        blockChain.forEach(BlockElement::outBlock);
    }




    public static class BlockElement {
        private final int id;
        private final long timeStamp;
        private final Payload payload;
        private final String prevElemHash;
        private final String curElemHash;

        public BlockElement(int id, Payload info, String prevBlockElemHash) {
            this.id = id;
            timeStamp = new Date().getTime();
            payload = info;
            prevElemHash = prevBlockElemHash;
            curElemHash = calcHash();
        }

        public String getHash() {
            return curElemHash;
        }

        public void outBlock() {
            System.out.println();
            System.out.println("Block:");
            System.out.printf("Id: %d\n", id);
            System.out.printf("Timestamp: %d\n", timeStamp);
            System.out.printf("Hash of the previous block:\n" + prevElemHash + "\n");
            System.out.printf("Hash of the block:\n" + curElemHash);
            System.out.println();
        }

        private String calcHash() {
            StringBuilder sb = new StringBuilder();
            sb.append(id)
              .append(timeStamp)
              .append(payload.convertFieldToString())
              .append(prevElemHash);
            return StringUtil.applySha256(sb.toString());
        }
    }

}
