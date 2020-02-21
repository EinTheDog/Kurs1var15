package FieldPackage;

import java.util.ArrayList;
import java.util.Comparator;

public class TicTacToeField {
    private final int size;
    private  ArrayList<Comb> allXCombs = new ArrayList<>();
    private  ArrayList<Comb> allOCombs = new ArrayList<>();
    private Cell [][] field;

    public TicTacToeField (int size) {
        this.size = size;
        //создание матрицы для хранения клеток
        field = new Cell [size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                field[i][j] = new Cell(j, i, Symbol.NULL); // каждая клетка пока пустая
                addNeighbours(field[i][j]);
            }
        }
    }

    //добавление всех соседей клетки
    private void addNeighbours (Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        for (int i = y - 1; i <= y + 1; i++) {
            if (i < 0 || i >= size) continue;
            for (int j = x - 1; j <= x + 1; j++) {
                if (j < 0 ||  j >= size || (i == y && j ==x)) continue;
                cell.addNeighbour(field[i][j]);
            }
        }
    }

    //функция для добавления символа в клетку
    public void addSymbol (Symbol symbol, int x, int y) {
        ArrayList<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        field[y][x].setSymbol(symbol);
        field[y][x].setComb(new Comb(CombType.HORIZONTAL, symbol), CombType.HORIZONTAL);
        field[y][x].setComb(new Comb(CombType.VERTICAL, symbol), CombType.VERTICAL);
        field[y][x].setComb(new Comb(CombType.DIAGONAL_UP, symbol), CombType.DIAGONAL_UP);
        field[y][x].setComb(new Comb(CombType.DIAGONAL_DOWN, symbol), CombType.DIAGONAL_DOWN);
        allCombs.add(field[y][x].getComb(CombType.HORIZONTAL));
        allCombs.add(field[y][x].getComb(CombType.VERTICAL));
        allCombs.add(field[y][x].getComb(CombType.DIAGONAL_UP));
        allCombs.add(field[y][x].getComb(CombType.DIAGONAL_DOWN));
        //просматриваем всех соседей данной клетки
        for (Cell c: field[y][x].getNeighbours()) {
            if (c == null) break;
            //если символ соседней клетки совпадает с нашим символом
            if (c.getSymbol() == symbol) {
                //определяем взаимное расположение клеток
                CombType relativePos;
                if (x == c.getX()) { relativePos = CombType.HORIZONTAL; }
                else {
                    if (y == c.getY()) { relativePos = CombType.VERTICAL; }
                    else {
                        if (x - c.getX() == y - c.getY()) {
                            relativePos = CombType.DIAGONAL_UP;
                        } else {
                            relativePos = CombType.DIAGONAL_DOWN;
                        }
                    }
                }
                if (c.getComb(relativePos).getLength() < field[y][x].getComb(relativePos).getLength()) {
                    //если линия клетки-соседа меньше, чем длина линии, в которую входим мы,
                    //то увчеличимваем длину нашей линии на 1 и записываем в клетку-соседа нашу линию
                    field[y][x].getComb(relativePos).addCell();
                    allCombs.remove(c.getComb(relativePos));
                    c.setComb(field[y][x].getComb(relativePos), relativePos);
                } else {
                    //иначе увеличиваем длину линии соседа на 1 и записываем линию в нашу клетку
                    c.getComb(relativePos).addCell();
                    allCombs.remove(field[y][x].getComb(relativePos));
                    field[y][x].setComb(c.getComb(relativePos), relativePos);
                }
            }
        }
    }

    //функция для отчистки клетки
    public void clearCell (int x, int y) {
        Symbol symbol = field[y][x].getSymbol();
        ArrayList<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        field[y][x].getComb(CombType.HORIZONTAL).removeCell();
        if (field[y][x].getComb(CombType.HORIZONTAL).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.HORIZONTAL));
        }
        field[y][x].getComb(CombType.VERTICAL).removeCell();
        if (field[y][x].getComb(CombType.VERTICAL).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.VERTICAL));
        }
        field[y][x].getComb(CombType.DIAGONAL_UP).removeCell();
        if (field[y][x].getComb(CombType.DIAGONAL_UP).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.DIAGONAL_UP));
        }
        field[y][x].getComb(CombType.DIAGONAL_DOWN).removeCell();
        if (field[y][x].getComb(CombType.DIAGONAL_DOWN).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.DIAGONAL_DOWN));
        }
        field[y][x].setSymbol(Symbol.NULL);
        field[y][x].setComb(null, CombType.HORIZONTAL);
        field[y][x].setComb(null, CombType.VERTICAL);
        field[y][x].setComb(null, CombType.DIAGONAL_UP);
        field[y][x].setComb(null, CombType.DIAGONAL_DOWN);


    }

    //получение длиннейшей линии
    public int getTheLongestComb (Symbol symbol) {
        ArrayList<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        allCombs.sort(Comparator.comparing(Comb::getLength));
        return allCombs.get(allCombs.size() - 1).getLength();
    }

    //
    public Symbol getSymbol (int x, int y) {
        return field[y][x].getSymbol();
    }
}






