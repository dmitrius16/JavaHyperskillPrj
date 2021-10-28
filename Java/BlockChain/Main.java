package blockchain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static blockchain.SerializationUtils.desearilize;

public class Main {
    static final String blockChainFileName = "blockchain.btc";
    static final String enterNumZeros = "Enter how many zeros the hash must start with: ";

    public static void main(String[] args) {
        List<Person> person = Arrays.asList(new Person("Dmitriy", "Sysoev"),
                new Person("Andrey", "Pavlov"));
                new Person("Alexandr", "Konuhov"),
                new Person("Sergey", "Ivanov"),
                new Person("Alexey", "Bliznuk"));

        BlockChain btc;
        Scanner scanner = new Scanner(System.in);
        System.out.println(enterNumZeros);
        int numZeros = scanner.nextInt();
        BlockChain.setLeadingZerosCount(numZeros);

        Optional<BlockChain> savedBlockchain = readBlockChain(blockChainFileName);
        if (savedBlockchain.isPresent()) {
             btc = savedBlockchain.get();
             btc.outBlocks();
        } else {
            btc = new BlockChain(blockChainFileName);
            person.forEach(btc::addElem);
        }
    }

    public static Optional<BlockChain> readBlockChain(String fileName) {
        try {
            BlockChain blockChain = (BlockChain) SerializationUtils.desearilize(fileName);
            return Optional.of(blockChain);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return Optional.empty();
    }
}
