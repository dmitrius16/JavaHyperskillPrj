package linalg;

public class MyMatrix {
    protected int rows;
    protected int cols;
    protected double[][] array2D;

    public MyMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        array2D = new double[rows][cols];
    }

    public void FillMatrix(int numRow, String elem) {
        if (numRow >= rows)
            return;
        String[] numsStr = elem.split("\\s+");
        for (int j = 0; j < cols; j++) {
            array2D[numRow][j] = Double.parseDouble(numsStr[j]);
        }
    }

    public MyMatrix sum(MyMatrix B) {
        MyMatrix res = null;
        if (this.rows == B.rows && this.cols == B.cols) {
            res = new MyMatrix(B.rows, B.cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res.array2D[i][j] = this.array2D[i][j] + B.array2D[i][j];
                }
            }
        }
        return res;
    }

    public MyMatrix multiply(int constant) {
        MyMatrix res = new MyMatrix(this.rows, this.cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.array2D[i][j] = this.array2D[i][j] * constant;
            }
        }
        return res;
    }

    public MyMatrix multiply(MyMatrix matrix) {
        MyMatrix res = null;
        if (this.cols == matrix.rows) {
            res = new MyMatrix(this.rows, matrix.cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < matrix.cols; j++) {
                    res.array2D[i][j] = dotProduct(matrix, i, j);
                }
            }
        }
        return res;
    }

    public MyMatrix transpose() {
        MyMatrix res = new MyMatrix(this.cols, this.rows); // switch num cols and num rows
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.array2D[j][i] = this.array2D[i][j];
            }
        }
        return res;
    }

    public MyMatrix transposeSideDiag() {
        MyMatrix res = new MyMatrix(this.cols, this.rows);
        int indLastCol = this.cols - 1;
        int indLastRow = this.rows - 1;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.array2D[j][i] = this.array2D[indLastRow - i][indLastCol - j];
            }
        }
        return res;
    }

    public MyMatrix transposeByVerticalLine() {
        MyMatrix res = new MyMatrix(this.rows, this.cols);
        int indLastCol = this.cols - 1;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.array2D[i][j] = this.array2D[i][indLastCol - j];
            }
        }
        return res;
    }

    public MyMatrix transposeByHorizontalLine() {
        MyMatrix res = new MyMatrix(this.rows, this.cols);
        int indLastRow = this.rows - 1;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.array2D[i][j] = this.array2D[indLastRow - i][j];
            }
        }
        return res;
    }

    public double det() {
        if (this.cols >= 2 && this.rows >= 2 && this.cols == this.rows)
            return calcDeterminant(this);
        else
            return 0.;
    }

    public MyMatrix inverse() {
        MyMatrix res = new MyMatrix(this.rows, this.cols);
        double det = 0;
        if(this.cols == 2 && this.rows == 2) {
            //[a b]     [d -c]     [d -b]
            //     ==>         ==>
            //[c d]     [-b a]     [-c a]
            det = this.det();
            res.array2D[0][0] = this.array2D[1][1] / det;
            res.array2D[0][1] = -this.array2D[0][1] / det;
            res.array2D[1][0] = -this.array2D[1][0] / det;
            res.array2D[1][1] = this.array2D[0][0] / det;
            return res;
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                res.array2D[j][i] = calcCoFactor(this, i, j);
            }
        }
        // calc det

        for (int i = 0; i < res.rows; i++) {
            det += res.array2D[i][0] * this.array2D[0][i];
        }
        if (det != 0.f) {   //### it's in most case true, may be set e = 0.0001
            for (int i = 0; i < res.rows; i++) {
                for (int j = 0; j < res.cols; j++) {
                    res.array2D[i][j] = res.array2D[i][j] / det;
                }
            }
        }
        return res;
    }

    private double calcDeterminant(MyMatrix mat) {
        double res;
        if (mat.cols == 2 && mat.rows == 2) {
            res = mat.array2D[0][0] * mat.array2D[1][1] - mat.array2D[1][0] * mat.array2D[0][1];
        } else {

            res = 0.f;
            final int row = 0; // naive algorithm use only first row
            for (int j = 0; j < mat.cols; j++) {
                res += Math.pow(-1, (row + 1) + (j + 1)) * mat.array2D[row][j] * calcDeterminant(getSubMatrix(mat, row, j));
            }
        }
        return res;
    }

    private double calcCoFactor(MyMatrix mat, int row, int col) {
        double res;
        if (mat.cols == 2 && mat.rows == 2) {
            res = mat.array2D[0][0] * mat.array2D[1][1] - mat.array2D[1][0] * mat.array2D[0][1];
        } else {
            res = 0.f;
            res += Math.pow(-1, (row + 1) + (col + 1)) * calcDeterminant(getSubMatrix(mat, row, col));
        }
        return res;
    }


    private MyMatrix getSubMatrix(MyMatrix mat, int throwRow, int throwCol) {
        MyMatrix A = new MyMatrix(mat.rows - 1, mat.cols - 1);
        int ii = 0, jj = 0; //indexes for new matrix
        for (int i = 0; i < mat.rows; i++) {
            if (i == throwRow) {
                continue;
            }
            for (int j = 0; j < mat.cols; j++) {
                if (j == throwCol) {
                    continue;
                }
                A.array2D[ii][jj] = mat.array2D[i][j];
                jj += 1;
                if (jj == A.cols) {
                    jj = 0;
                    ii += 1;
                }
            }
        }
        return A;
    }

    private double dotProduct(MyMatrix rMatrix, int rowLeftMatrix, int colRightMatrix) {
        double res = 0.f;
        for (int j = 0; j < this.cols; j++) {
            res += this.array2D[rowLeftMatrix][j] * rMatrix.array2D[j][colRightMatrix];
        }
        return res;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(String.format("% .3f", array2D[i][j])).append(' ');
            }
            sb.append('\n');
        }
        //sb.append(Arrays.toString(array2D[j])).append('\n');
        return sb.toString();
    }
}