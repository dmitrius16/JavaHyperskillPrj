package processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static BufferedReader usrInp;
    static MyMenu mainMenu;
    static MyMenu transponseMenu;
    static MyMenu currentMenu;

    public static void main(String[] args) throws IOException {
        usrInp = new BufferedReader(new InputStreamReader(System.in));
        currentMenu = CreateMainMenu();
        CreateTransponseMenu();
        HandleMenuItems();
    }

    public static MyMatrix inputMatrix(int numMatrix) throws IOException {
        String[] numMatrixStr = {"first", "second"};

        if (numMatrix == 0)
            System.out.printf("Enter matrix size: ");
        else
            System.out.printf("Enter size of %s matrix: ", numMatrix == 1 ? numMatrixStr[0] : numMatrixStr[1]);


        String[] dim = usrInp.readLine().split("\\s+");
        int row = Integer.parseInt(dim[0]);
        int col = Integer.parseInt(dim[1]);
        MyMatrix matrix = new MyMatrix(row, col);

        if (numMatrix == 0)
            System.out.println("Enter matrix: ");
        else
            System.out.printf("Enter %s matrix:\n", numMatrix <= 1 ? numMatrixStr[0] : numMatrixStr[1]);

        for (int i = 0; i < row; i++) {
            String temp = usrInp.readLine();
            matrix.FillMatrix(i, temp);
        }
        return matrix;
    }

    public static void outputResult(MyMatrix A) {
        System.out.println("The result is:");
        System.out.println(A);
    }

    public static void outputResult(double res) {
        System.out.println("The result is:");
        System.out.println(res);
    }


    private static MyMenu CreateMainMenu() {
        mainMenu = new MyMenu();
        mainMenu.addItem("1. Add matrices", () -> {
            MyMatrix A, B, C;
            try {
                A = inputMatrix(1);
                B = inputMatrix(2);
                C = A.sum(B);
                if (C != null) {
                    System.out.println(C);
                } else {
                    System.out.println("ERROR");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.addItem("2. Multiply matrix to a constant,", () -> {
            MyMatrix A, C;
            try {
                A = inputMatrix(0);
                System.out.println("Input constant: ");
                int constant = Integer.parseInt(usrInp.readLine());
                C = A.multiply(constant);
                System.out.println(C);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.addItem("3. Multiply matrices", () -> {
            MyMatrix A, B, C;
            try {
                A = inputMatrix(1);
                B = inputMatrix(2);
                C = A.multiply(B);
                if (C != null)
                    System.out.println(C);
                else
                    System.out.println("ERROR");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.addItem("4. Transponse matrix", () -> {
            currentMenu = transponseMenu;
        });

        mainMenu.addItem("5. Calculate determinant", () -> {
            MyMatrix A;
            try {
                A = inputMatrix(0);
                double res = A.det();
                outputResult(res);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.addItem("6. Inverse matrix", () -> {
            MyMatrix A;
            try {
                A = inputMatrix(0);
                MyMatrix Am1 = A.inverse();
                outputResult(Am1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.addItem("0. Exit", () -> {

        }); // set global flag for exit
        return mainMenu;
    }

    private static MyMenu CreateTransponseMenu() {
        transponseMenu = new MyMenu(mainMenu);
        transponseMenu.addItem("1. Main diagonal", () -> {
            try {
                MyMatrix A = inputMatrix(0);
                MyMatrix C = A.transpose();
                outputResult(C);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transponseMenu.addItem("2. Side diagonal", () -> {
            try {
                MyMatrix A = inputMatrix(0);
                MyMatrix C = A.transposeSideDiag();
                outputResult(C);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transponseMenu.addItem("3. Vertical line", () -> {
            try {
                MyMatrix A = inputMatrix(0);
                MyMatrix C = A.transposeByVerticalLine();
                outputResult(C);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transponseMenu.addItem("4. Horizontal line", () -> {
            try {
                MyMatrix A = inputMatrix(0);
                MyMatrix C = A.transposeByHorizontalLine();
                outputResult(C);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transponseMenu.addItem("0. Exit", () -> {
            currentMenu = transponseMenu.getParent();
        });
        return transponseMenu;
    }

    private static void HandleMenuItems() throws IOException {
        boolean exit = false;
        try {
            while (!exit) {
                currentMenu.display();
                String temp = usrInp.readLine();
                int numItem = Integer.parseInt(temp);
                exit = currentMenu.menuHandler(numItem);
            }
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
