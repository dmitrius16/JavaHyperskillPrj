package blockchain;

public class Person implements Payload {
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
