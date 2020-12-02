package common;

import java.io.*;
import java.nio.file.Paths;

public class ClientServerTasks {
    public static int CHUNK_SIZE = 4096;

    private byte[] tempBuffer = new byte[CHUNK_SIZE];  //make for every object

    /**
     * Receive file from destination. Client calls this method when issue GET request. Server calls this
     * method when PUT request received
     * @param fileOutput
     * @param input
     * @return true if size trasmit equal received size
     * @throws IOException
     */
    public boolean receiveFile(FileOutputStream fileOutput, DataInputStream input) throws IOException {

        int rxSize = 0;
        int sizeFile = input.readInt();

        while(rxSize < sizeFile) {
            int rxBytes = input.read(tempBuffer);
            if (rxBytes == -1)
                break;
            rxSize += rxBytes;
            fileOutput.write(tempBuffer,0, rxBytes);
        }

        return rxSize == sizeFile;
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
            int cntWriteBytes = 0;
            while ((rdBytes = readFile.read(tempBuffer)) != - 1) {
                output.write(tempBuffer, 0 ,rdBytes);
                cntWriteBytes += rdBytes;
            }
            return cntWriteBytes == numSendBytes;
    }

    public enum ServerCode {
        OK_CODE("200"),
        FILE_EXIST("403"),
        ERR_CODE("404");

        private String repr;
        ServerCode(String strVal) {repr = strVal;}
        public String getRepr() {return repr;}
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

        public void setCode(ServerCode code) {this.code = code;}
        public void setAdditionalInfo(String info) {this.additionalInfo = info;}

        public ServerCode getCode() {return code;}

        public String toString() {
            return code.getRepr() + (this.additionalInfo.isEmpty() ? "" : (" " + additionalInfo));
        }
    }
}


