package field;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TicTacToeField {
    private final int size;
    private  List<Comb> allXCombs = new ArrayList<>();
    private  List<Comb> allOCombs = new ArrayList<>();
    private Cell[][] field;

    public TicTacToeField (int size) {
        this.size = size;
        //создание матрицы для хранения клеток
        field = new Cell[size][size];
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
        //проверка на заполненность клетки
        if (field[y][x].getSymbol() != Symbol.NULL) {
            return;
        }
        List<Comb> allCombs;
        //определяем в список линий каких символов записывать изменения
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        //задаем символ клетки
        field[y][x].setSymbol(symbol);
        //задаем начальные линии для клетки длиной 1
        field[y][x].setComb(new Comb(CombType.HORIZONTAL, symbol), CombType.HORIZONTAL);
        field[y][x].setComb(new Comb(CombType.VERTICAL, symbol), CombType.VERTICAL);
        field[y][x].setComb(new Comb(CombType.DIAGONAL_UP, symbol), CombType.DIAGONAL_UP);
        field[y][x].setComb(new Comb(CombType.DIAGONAL_DOWN, symbol), CombType.DIAGONAL_DOWN);
        //добавляем эти линии в список всех линий данного символа
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
                    //если линия, частью которой является клетка-соседа меньше, чем длина линии, в которую входим мы,
                    //то увчеличимваем длину нашей линии на 1 и записываем в клетку-соседа нашу линию
                    // (удаляем неиспользуемую линию из списка всех линий)
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
        //Проверка на заполненность клетки
        if (field[y][x].getSymbol() == Symbol.NULL) {
            return;
        }
        Symbol symbol = field[y][x].getSymbol();
        List<Comb> allCombs;
        //определяем в список линий каких символов записывать изменения
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        //вызываем функцию удаления клетки из линии (уменьшение ее длины на 1)
        field[y][x].getComb(CombType.HORIZONTAL).removeCell();
        //если длина линии стала равна 0 - удаляем эту линию из списка
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
        //записываем в клетку знак пустоты
        field[y][x].setSymbol(Symbol.NULL);
        //опусташаем линии, которые хранились в клетке
        field[y][x].setComb(null, CombType.HORIZONTAL);
        field[y][x].setComb(null, CombType.VERTICAL);
        field[y][x].setComb(null, CombType.DIAGONAL_UP);
        field[y][x].setComb(null, CombType.DIAGONAL_DOWN);


    }

    //получение длиннейшей линии
    public int getTheLongestComb (Symbol symbol) {
        List<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        //сортируем нужный списко непрерывных линий символов по длине
        allCombs.sort(Comparator.comparing(Comb::getLength));
        //забираем последний элемент списка (с самой большой длиной) или возвращаем 0, если список пуст
        return allCombs.size() > 0? allCombs.get(allCombs.size() - 1).getLength(): 0;
    }

    //метод для получения символа клетки (нужен для тестирования)
    public Symbol getSymbol (int x, int y) {
        return field[y][x].getSymbol();
    }
}






