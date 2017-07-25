package model;

import java.util.ArrayList;
import java.util.Random;

public class MatrixSudoku {
	
	private int scrollLine, scrollColumn, amountNumbers;
	private int matrixSudoku[][] = new int[9][9];
    private static MatrixSudoku instance;
	
	private MatrixSudoku() {
		this.amountNumbers = generateRandomNumber();
    }
    
    public static MatrixSudoku getMatrixSudoku() {
        if(instance == null){
            instance = new MatrixSudoku();
        }
        return instance;
    }
    
    public int[][] getMatrix() {
		return this.matrixSudoku;
	}

    public void insertNumber(int l, int c, int value){
    	if(this.matrixSudoku[l][c] == 0 && (value >= 1 && value <=9))
    		this.amountNumbers += 1;
    	else if(this.matrixSudoku[l][c] != 0 && value == 0 )
    		this.amountNumbers -= 1;
    	
    	this.matrixSudoku[l][c] = value;
    	
    	System.out.println(this.amountNumbers);
    }
    
	private int generateRandomNumber() {
        Random rand = new Random();
        int n = 3;
        final int[][] sudokuAux = new int[n*n][n*n];
        int x = rand.nextInt(1000);
        
        for(int i = 0; i < n; i++, x++)
            for(int j = 0; j < n; j++, x+=n)
                for(int k = 0; k < n*n; k++, x++)
                    sudokuAux[n*i+j][k] = (x % (n*n)) + 1;
        
        changeLine(sudokuAux, 0, 2);
        changeLine(sudokuAux, 3, 5);
        changeLine(sudokuAux, 6, 8);
        
        changeColumn(sudokuAux, 0, 2);
        changeColumn(sudokuAux, 3, 5);
        changeColumn(sudokuAux, 6, 8);

        changeBlock3x9(sudokuAux, 0, 3);
        changeBlock3x9(sudokuAux, 0, 6);

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                matrixSudoku[i][j] = sudokuAux[i][j];
                System.out.print(matrixSudoku[i][j]);
            }
            System.out.println();
        }
        
        return removeSomeNumbers();
    }
    
    private void changeLine(int[][] sudoku, int line1, int line2) {
        int aux;
        for(int i = 0; i < 9; i++) {
            aux = sudoku[line1][i];
            sudoku[line1][i] = sudoku[line2][i];
            sudoku[line2][i] = aux;
        }
    }
    
    private void changeColumn(int[][] sudoku, int column1, int column2) {
        int aux;
        for(int i = 0; i < 9; i++) {
            aux = sudoku[i][column1];
            sudoku[i][column1] = sudoku[i][column2];
            sudoku[i][column2] = aux;
        }
    }
    
    private void changeBlock3x9(int[][] sudoku, int line1, int line2) {
        int aux;
        int column2 = 0;
        for(int i = line1; i < line1 + 3; i++) {
            for(int j = 0; j < 9; j++) {
                aux = sudoku[i][j];
                sudoku[i][j] = sudoku[line2][column2];
                sudoku[line2][column2++] = aux;
                if(column2 == 9) {
                    line2++;
                    column2 = 0;
                }
            }
        }
    }
    
    public int removeSomeNumbers(){
        int amountNumbersSudoku = 0;
        Random rand = new Random();
        int numbersIn3x3 = rand.nextInt(8);
        ArrayList<Integer> vAux = new ArrayList<>();
        int i, j, k;
        
        ArrayList<Integer> characters = new ArrayList<>();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        
        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 0; i < 3; i++){
            for(j = 0; j < 3; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }

        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 0; i < 3; i++){
            for(j = 3; j < 6; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 0; i < 3; i++){
            for(j = 6; j < 9; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 3; i < 6; i++){
            for(j = 0; j < 3; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 3; i < 6; i++){
            for(j = 3; j < 6; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 3; i < 6; i++){
            for(j = 6; j < 9; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 6; i < 9; i++){
            for(j = 0; j < 3; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 6; i < 9; i++){
            for(j = 3; j < 6; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        
        
        vAux = new ArrayList();
        for(i = 1; i <= 9; i++)
            characters.add(i);
        numbersIn3x3 = rand.nextInt(8);

        for(i = 0; i < numbersIn3x3; i++){
            j = rand.nextInt(characters.size());
            vAux.add(characters.get(j));
            characters.remove(characters.get(j));
            j++;
        }

        for(i = 6; i < 9; i++){
            for(j = 6; j < 9; j++){
                k = matrixSudoku[i][j];
                if(!vAux.contains(k))
                    matrixSudoku[i][j] = 0;
                else {
                    amountNumbersSudoku++;
                }
            }
        }
        return amountNumbersSudoku;
    }
    
    public boolean checkSudoku(int line, int column) {
        boolean checkLine = checkLine(line, column);
        boolean checkColumn = checkColumn(line, column);
        boolean checkMatrix;
        
        if(line < 3) {
            if(column < 3)
                checkMatrix = checkMatrix3x3(line, column, 0, 0);
            else if (column >=3 && column < 6)
                checkMatrix = checkMatrix3x3(line, column, 0, 3);
            else
                checkMatrix = checkMatrix3x3(line, column, 0, 6);
        } else if (line >=3 && line < 6) {
            if(column < 3)
                checkMatrix = checkMatrix3x3(line, column, 3, 0);
            else if (column >=3 && column < 6)
                checkMatrix = checkMatrix3x3(line, column, 3, 3);
            else
                checkMatrix = checkMatrix3x3(line, column, 3, 6);
        } else {
            if(column < 3)
                checkMatrix = checkMatrix3x3(line, column, 6, 0);
            else if (column >=3 && column < 6)
                checkMatrix = checkMatrix3x3(line, column, 6, 3);
            else
                checkMatrix = checkMatrix3x3(line, column, 6, 6);
        }
        
        if (!checkLine || !checkColumn || !checkMatrix){
        	return false;
        }
        
        return true;
    }
    
    public boolean checkFinalGame() {
    	if(this.amountNumbers >= 81) {
            for(scrollLine = 0; scrollLine < 9; scrollLine++){
                for(scrollColumn = 0; scrollColumn < 9; scrollColumn++){
                    if(matrixSudoku[scrollLine][scrollColumn]== 0)
                        return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean checkLine(int line, int column) {
        for(scrollColumn = 0; scrollColumn < 9; scrollColumn++) {
            if(scrollColumn == column)
                scrollColumn++;
            if(scrollColumn < 9) {
            	if(matrixSudoku[line][scrollColumn] == matrixSudoku[line][column])
                    return false;
            }
        }
        return true;
    }
    
    private boolean checkColumn(int line, int column) {
        for(scrollLine = 0; scrollLine < 9; scrollLine++) {
            if(scrollLine == line)
                scrollLine++;
            if(scrollLine < 9) {
            	if(matrixSudoku[scrollLine][column] == matrixSudoku[line][column])
                    return false;
            }
        }
        return true;
    }
    
    private boolean checkMatrix3x3(int line, int column, int lineMatrix3x3, int columnMatrix3x3) {
        for(scrollLine = lineMatrix3x3; scrollLine < lineMatrix3x3 + 3; scrollLine++) {
            for(scrollColumn = columnMatrix3x3; scrollColumn < columnMatrix3x3 + 3; scrollColumn++) {
                if(scrollLine == line && scrollColumn == column)
                    scrollColumn++;
                if(scrollColumn < columnMatrix3x3 + 3) {
                    if(matrixSudoku[scrollLine][scrollColumn] == matrixSudoku[line][column])
                        return false;
                }
            }
        }
        return true;
    }
    
    public String toString() {
    	StringBuilder matrix = new StringBuilder();
    	for(int l = 0; l < 9; l++) {
    		for(int c = 0; c < 9; c++)
    			matrix.append(String.valueOf(this.matrixSudoku[l][c]));
    	}
    	return matrix.toString();
    }
    
    public void setMatrix(int[][] matrix) {
    	this.matrixSudoku = matrix;
    }
    
}
