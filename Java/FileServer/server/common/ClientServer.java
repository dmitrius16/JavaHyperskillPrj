package common;

import java.io.*;
import java.nio.file.Paths;

public class ClientServer {
    public static int CHUNK_SIZE = 4096;

    private byte[] tempBuffer = new byte[CHUNK_SIZE];  //make for every object

    public boolean receiveFile(String fileName, DataInputStream input) {
        boolean result = false;
        try (FileOutputStream fileOutput = new FileOutputStream(new File(fileName))) {
            int rxSize = 0;
            int sizeFile = input.readInt();

            while(rxSize < sizeFile) {
                int rxBytes = input.read(tempBuffer);
                if (rxBytes == -1)
                    break;
                rxSize += rxBytes;
                fileOutput.write(tempBuffer,0, rxBytes);
            }
            result = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }



    /**
     * Transmit file to destination. Client call this func when PUT execute put request. Server call this
     * func when execute GET request
     * @param readFile - file needed to send
     * @param output - socket stream
     * @return
     */
    public boolean transmitFile(FileInputStream readFile, DataOutputStream output) throws IOException {
            int numSendBytes = readFile.available();
            output.writeInt(numSendBytes);
            int rdBytes = 0;
            while ((rdBytes = readFile.read(tempBuffer)) != - 1) {
                output.write(tempBuffer, 0 ,rdBytes);
            }
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


