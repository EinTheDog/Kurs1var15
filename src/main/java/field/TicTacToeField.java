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
        if (size < 0) throw new IllegalArgumentException();
        this.size = size;
        //создание матрицы для хранения клеток
        field = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                field[i][j] = new Cell(j, i, Symbol.NULL); // каждая клетка пока пустая
            }
        }
        //добавим в каждую клетку информацию о ее соседях
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                addNeighbours(field[i][j]);
            }
        }
    }

    //методы get для переопределения equals
    private int getSize () { return size; }
    private Cell getCell (int x, int y) { return field[y][x];}

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
    public Result addSymbol (Symbol symbol, int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) throw new IllegalArgumentException();
        //проверка на заполненность клетки
        if (field[y][x].getSymbol() != Symbol.NULL) return Result.FAIL;
        //проверка на попытку добавить пустоту
        if (symbol == Symbol.NULL) return Result.FAIL;
        List<Comb> allCombs;
        //определяем в список линий каких символов записывать изменения
        allCombs = symbol == Symbol.X? allXCombs: allOCombs;
        //задаем символ клетки
        field[y][x].setSymbol(symbol);
        //задаем начальные линии для клетки длиной 1
        for (CombType combType: CombType.values()) {
            field[y][x].setComb(new Comb(combType, symbol), combType);
        }
        //добавляем эти линии в список всех линий данного символа
        for (CombType combType: CombType.values()) {
            allCombs.add(field[y][x].getComb(combType));
        }
        //просматриваем всех соседей данной клетки
        for (Cell c: field[y][x].getNeighbours()) {
            //если символ соседней клетки совпадает с нашим символом
            if (c.getSymbol() == symbol) {
                //определяем взаимное расположение клеток
                CombType relativePos;
                if (x == c.getX()) { relativePos = CombType.HORIZONTAL; }
                else {
                    if (y == c.getY()) { relativePos = CombType.VERTICAL; }
                    else {
                        relativePos = (x - c.getX() == y - c.getY())?CombType.DIAGONAL_UP: CombType.DIAGONAL_DOWN;
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
        return Result.SUCCESS;
    }

    //функция для отчистки клетки
    public Result clearCell (int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) throw new IllegalArgumentException();
        //Проверка на заполненность клетки
        if (field[y][x].getSymbol() == Symbol.NULL) {
            return Result.FAIL;
        }
        Symbol symbol = field[y][x].getSymbol();
        List<Comb> allCombs;
        //определяем в список линий каких символов записывать изменения
        allCombs = symbol == Symbol.X? allXCombs: allOCombs;
        //удаляем клетку из всех линий, в которых она состояла
        for (CombType combType:CombType.values()) {
            removeCellFromComb(allCombs, field[y][x].getComb(combType));
        }
        //записываем в клетку знак пустоты
        field[y][x].setSymbol(Symbol.NULL);
        //опусташаем линии, которые хранились в клетке
        for (CombType combType:CombType.values()) {
            field[y][x].setComb(null, combType);
        }
        return Result.SUCCESS;
    }

    //метод для удаления клетки из линии
    private void removeCellFromComb (List<Comb> allCombs, Comb comb) {
        //вызываем функцию удаления клетки из линии (уменьшение ее длины на 1)
        comb.removeCell();
        //если длина линии стала равна 0 - удаляем эту линию из списка
        if (comb.getLength() == 0) allCombs.remove(comb);
    }

    //получение длиннейшей линии
    public int getTheLongestComb (Symbol symbol) {
        List<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        //сортируем нужный список непрерывных линий символов по длине
        allCombs.sort(Comparator.comparing(Comb::getLength));
        //забираем последний элемент списка (с самой большой длиной) или возвращаем 0, если список пуст
        return allCombs.size() > 0? allCombs.get(allCombs.size() - 1).getLength(): 0;
    }

    //метод для получения символа клетки (нужен для проведения тестов)
    public Symbol getSymbol (int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) throw new IllegalArgumentException();
        return field[y][x].getSymbol();
    }

    //переопределения
    @Override
    public int hashCode () {
        int result = 17;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                result = result * 31 + field[i][j].hashCode();
            }
        }
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (o instanceof TicTacToeField) {
            TicTacToeField fieldO = (TicTacToeField) o;
            if (fieldO.getSize() == getSize()) {
                for (int i = 0; i < getSize(); i ++) {
                    for (int j = 0; j < getSize(); j++) {
                        if (!(fieldO.getCell(i, j).equals(this.getCell(i, j)))) return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                sb.append(field[i][j].toString());
            }
            sb.append(System.lineSeparator());
        }
        //удаляем последний лишний абзац
        sb.delete(size*(size + 1) - 1, size*(size + 1));
        return sb.toString();
    }
}






