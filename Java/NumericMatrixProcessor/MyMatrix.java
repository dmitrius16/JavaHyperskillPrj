package processor;
import java.lang.reflect.Array;
import java.util.Arrays;

public class MyMatrix {
    private int rows;
    private int cols;
    private double[][] array2D;
    public MyMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        array2D = new double[rows][cols];//(T[][]) Array.newInstance(c,rows,cols);
    }
    public void FillMatrix(int numRow, String elem) {
        if(numRow >= rows)
            return;
        String[] numsStr = elem.split("\\s+");
        for(int j = 0; j < cols; j++) {
            array2D[numRow][j] = Double.parseDouble(numsStr[j]);
        }
    }
    public MyMatrix sum(MyMatrix B) {
        MyMatrix res = null;
        if(this.rows == B.rows && this.cols == B.cols) {
            res = new MyMatrix(B.rows, B.cols);
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    res.array2D[i][j] = this.array2D[i][j] + B.array2D[i][j];
                }
            }
        }
        return res;
    }
    public MyMatrix multiply(int constant) {
        MyMatrix res = new MyMatrix(this.rows,this.cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                res.array2D[i][j] = this.array2D[i][j] * constant;
            }
        }
        return res;
    }

    public MyMatrix multiply(MyMatrix matrix) {
        MyMatrix res = null;
        if(this.cols == matrix.rows) {
            res = new MyMatrix(this.rows,matrix.cols);
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < matrix.cols; j++) {
                    res.array2D[i][j] = dotProduct(matrix,i,j);
                }
            }
        }
        return res;
    }

    public MyMatrix transponse() {
        MyMatrix res = new MyMatrix(this.cols, this.rows); // switch num cols and num rows
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                res.array2D[j][i] = this.array2D[i][j];
            }
        }
        return res;
    }
/*
    public MyMatrix transponseSideDiag() {

    }
*/
    private double dotProduct(MyMatrix rMatrix, int rowLeftMatrix, int colRightMatrix) {
        double res = 0.f;
        for(int j = 0; j < this.cols; j++) {
            res += this.array2D[rowLeftMatrix][j] * rMatrix.array2D[j][colRightMatrix];
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(array2D[i][j]).append(' ');
            }
            sb.append('\n');
        }
        //sb.append(Arrays.toString(array2D[j])).append('\n');
        return sb.toString();
    }
}