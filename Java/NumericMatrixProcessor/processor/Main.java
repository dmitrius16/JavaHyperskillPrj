package processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static BufferedReader usrInp;
    public static void main(String[] args) throws IOException {
        usrInp = new BufferedReader(new InputStreamReader(System.in));
        MyMatrix A = inputMatrix();
        int constant = Integer.parseInt(usrInp.readLine());
        //// B = inputMatrix();
         MyMatrix res = A.multiply(constant);
        if(res != null) {
            System.out.println(res);
        } else {
            System.out.println("ERROR");
        }
    }

    public static MyMatrix inputMatrix() throws IOException {
        String[] dim = usrInp.readLine().split("\\s+");
        int row = Integer.parseInt(dim[0]);
        int col = Integer.parseInt(dim[1]);
        MyMatrix matrix = new MyMatrix(row,col);
        for(int i = 0; i < row; i++) {
            String temp = usrInp.readLine();
            matrix.FillMatrix(i,temp);
        }
        return matrix;
    }
}
