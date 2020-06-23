package processor;
import java.util.Arrays;

public class MyMatrix {
    private int rows;
    private int cols;
    private int[][] array2D;
    public MyMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        array2D = new int[rows][cols];
    }
    public void FillMatrix(int numRow, String elem) {
        if(numRow >= rows)
            return;
        String[] numsStr = elem.split("\\s+");
        for(int j = 0; j < cols; j++) {
            array2D[numRow][j] = Integer.parseInt(numsStr[j]);
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