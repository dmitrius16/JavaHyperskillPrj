package solver;
import java.util.Scanner;
import linalg.MyMatrix;
import linalg.MyColumnVector;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MyMatrix mat = new MyMatrix(2, 2);
        MyColumnVector vec = new MyColumnVector(2);

        for(int i = 0; i < 2; i++) {
            String[] coeff = scanner.nextLine().split("\\s+");
            mat.FillMatrix(i, String.join(" ", coeff[0], coeff[1]));
            vec.FillVector(i, coeff[2]);
        }

        MyMatrix inv_mat = mat.inverse();
        MyColumnVector res = new MyColumnVector(inv_mat.multiply(vec));

        System.out.print(res);
    }
}
