package common;


public class FileDescription
{
    private String fileName;
    private int    id;

    public FileDescription(String name) {
        this.fileName = name;
        this.id = 0;
    }

    public FileDescription(int id) {
        this.fileName = "";
        this.id = id;
    }

    public boolean isFileAsName() {
        return !this.fileName.isEmpty();
    }

    public boolean isFileAsID() {
        return this.id != 0;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public String getName() {
        return this.fileName;
    }

    public int getId() {
        return this.id;
    }

}