package blockchain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class BlockElement implements Serializable {
    private static final long serialversionUID = 1L;
    private final int id;
    private final long timeStamp;
    private final Payload payload;
    private final String prevElemHash;
    private final String curElemHash;
    private long magicNumber;
    private long timeForGenerateHash; // not in hash block
    ///private long timeStampFinished;     // debug var clear it
    public BlockElement(int id, Payload info, String prevBlockElemHash) {
        this.id = id;
        timeStamp = Instant.now().toEpochMilli();
        payload = info;
        prevElemHash = prevBlockElemHash;
        curElemHash = calcHash();
        magicNumber = 0;
    }

    public String getHash() {
        return curElemHash;
    }

    public void outBlock() {
        System.out.println();
        System.out.println("Block:");
        System.out.printf("Id: %d\n", id);
        System.out.printf("Timestamp: %d\n", timeStamp);
        ///System.out.printf("Time end : %d\n", timeStampFinished);    //debug clear it
        System.out.printf("Hash of the previous block:\n" + prevElemHash + "\n");
        System.out.printf("Hash of the block:\n" + curElemHash + "\n");
        System.out.println("Block was geterating for " + timeForGenerateHash + " seconds");
        //System.out.println();
    }



    private String calcHash() {
        StringBuilder sb = new StringBuilder();
        sb.append(id)
                .append(timeStamp)
                .append(payload.convertFieldToString())
                .append(prevElemHash);

        String resultHash;
        Instant startCalcHashTm = Instant.now();
        Random randNum = new Random(startCalcHashTm.toEpochMilli());
        do {
            magicNumber = randNum.nextLong();
            resultHash = StringUtil.applySha256(sb.toString() + magicNumber);
        } while(!resultHash.substring(0, BlockChain.numLeadingZeros).equals(BlockChain.startHashTemplate));

        ///timeStampFinished = Instant.now().toEpochMilli();
        timeForGenerateHash = Duration.between(startCalcHashTm, Instant.now()).toSeconds();
        //******* debug
        System.out.println("GeneratedHash: " + resultHash);
        //****
        return resultHash;
    }
}