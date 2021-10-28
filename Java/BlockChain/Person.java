package blockchain;

import java.io.Serializable;

public class Person implements Payload, Serializable {
    private static final long serialversionUID = 1L;
    private final String name;
    private final String surname;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String convertFieldToString() {
        return name + " " + surname;
    }
}
