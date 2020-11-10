package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class IdToNameMapper implements Serializable {

    private static final long serialVersionUID = 7L;
    private Map<Integer, String> id2name = new HashMap<>();
    private Map<String, Integer> name2id = new HashMap<>();
    private int currentID;

    public IdToNameMapper() {
        currentID = 1;
    }

    /**
     add file name to mapper and return ID
     if id positive iperation sucessful else faild occur
     */
    public int add(String fileName){
        //check if file has already exist
        int fileId = 0;
        if (!name2id.containsKey(fileName)) {
            fileId = generateID();
            id2name.put(fileId, fileName);
            name2id.put(fileName, fileId);
        }
        return fileId;
    }

    boolean isExist(String fileName) {
        return name2id.containsKey(fileName);
    }

    boolean isExist(int id) {
        return id2name.containsKey(id);
    }

    String getFileNameFromId(int id) {
        return id2name.get(id);
    }

    int getIdFromFileName(String fileName) {
        return name2id.get(fileName);
    }

    /**
     * remove file from filestorage
     * @param fileName - name removed file
     * @return
     *  0 - file not found
     *  ID - if file successully deleted
     */
    public int remove(String fileName){
        int id = 0;
        if (name2id.containsKey(fileName)) {
            id = name2id.get(fileName);
            name2id.remove(fileName);
            id2name.remove(id);

        }
        return id;
    }



    private int generateID() {
        int temp = currentID;
        currentID++;
        return temp;
    }
}