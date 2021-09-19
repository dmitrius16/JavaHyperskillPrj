package blockchain;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Person> person = Arrays.asList(new Person("Dmitriy", "Sysoev"),
                new Person("Andrey", "Pavlov"),
                new Person("Alexandr", "Konuhov"),
                new Person("Sergey", "Ivanov"),
                new Person("Alexey", "Bliznuk"));
        BlockChain testBlockChain = new BlockChain();

        person.forEach(testBlockChain::addElem);
        testBlockChain.outBlocks();


    }
}
