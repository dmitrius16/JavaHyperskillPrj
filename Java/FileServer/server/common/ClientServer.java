package common;

import java.io.*;

public class ClientServer {
    public static int CHUNK_SIZE = 4096;
     private static byte[] rxBuffer = new byte[CHUNK_SIZE];  //make for every object

    public static boolean receiveFile(String fileName, DataInputStream input) {
        boolean result = false;
        try (FileOutputStream fileOutput = new FileOutputStream(new File(fileName))) {
            int rxSize = 0;
            int sizeFile = input.readInt();

            while(rxSize < sizeFile) {
                int rxBytes = input.read(rxBuffer);
                if (rxBytes == -1)
                    break;
                rxSize += rxBytes;
                fileOutput.write(rxBuffer,0, rxBytes);
            }
            result = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //TODO write this method
    public static boolean transmitFile(String fileName, DataOutputStream output) {

        return true;
    }

    public enum ServerCode {
        OK_CODE("200"),
        FILE_EXIST("403"),
        ERR_CODE("404");

        private String repr;
        ServerCode(String strVal) {repr = strVal;}
        String getRepr() {return repr;}
    }

    public static class ServerRespond {
        private ServerCode code;
        private String additionalInfo;

        public ServerRespond(ServerCode code) {
            this.code = code;
            this.additionalInfo = "";
        }

        public ServerRespond(ServerCode code, int id) {
            this.code = code;
            this.additionalInfo = Integer.toString(id);
        }

        public ServerCode getCode() {return code;}

        public String toString() {
            return code.getRepr() + " " + additionalInfo;
        }
    }
}


