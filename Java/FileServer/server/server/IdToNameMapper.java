package server;

import common.FileDescription;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public synchronized int add(String fileName) {
        //check if file has already exist
        int fileId = 0;
        if (!name2id.containsKey(fileName)) {
            fileId = generateID();
            id2name.put(fileId, fileName);
            name2id.put(fileName, fileId);
        }
        return fileId;
    }

    public synchronized boolean isExist(FileDescription file) {
        if (file.isFileAsName()) {
            return name2id.containsKey(file.getName());
        } else {
            return id2name.containsKey(file.getId());
        }
    }

    public synchronized String getFileNameFromId(int id) {
        return id2name.get(id);
    }

    public synchronized int getIdFromFileName(String fileName) {
        return name2id.get(fileName);
    }

    /**
     * remove file from filestorage
     * @param fileDescr - description removed file(combination name and id)
     * @return
     *  true - sucessfully remove else false
     *
     */
    public synchronized boolean remove(FileDescription fileDescr){
        int id = 0;
        String fileName = "";
        if (fileDescr.isFileAsName()) {
            fileName = fileDescr.getName();
            id = name2id.get(fileName);
        } else {
            id = fileDescr.getId();
            fileName = id2name.get(id);
        }

        name2id.remove(fileName);
        id2name.remove(id);

        return Objects.nonNull(fileName);
    }



    private int generateID() {
        int temp = currentID;
        currentID++;
        return temp;
    }
}