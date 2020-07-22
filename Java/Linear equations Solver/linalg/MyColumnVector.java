package linalg;

public class MyColumnVector extends MyMatrix {
    public  MyColumnVector(int size)
    {
        super(size, 1);
    }

    public MyColumnVector(MyMatrix mat) {
        super(mat.rows, 1);
        for(int i = 0; i < mat.rows; i++)
        {
            this.array2D[i][0] = mat.array2D[i][0];
        }
    }

    public void FillVector(int numElem, String elem) {
        FillMatrix(numElem, elem);
    }
/*
    public MyColumnVector multiply(MyMatrix mat) {
        MyMatrix resMatr = super.multiply(mat);
        return new MyColumnVector(resMatr);
    }
*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.rows; i++) {
            sb.append(String.format("%.5f", array2D[i][0])).append(' ');
        }
        return sb.toString();
    }
}

