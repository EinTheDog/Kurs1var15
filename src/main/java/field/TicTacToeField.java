package field;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Maxim Alpatski
 * @version 1.0
 */
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

    /**
     * добавление всех соседей клетки
     * @param cell клетка, для которой мы указываем соседей
     */

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

    /**
     * метод для добавления символа в клетку
     * @param symbol добавляемый символ (крестик или нолик)
     * @param x координата клетки по горизонтали
     * @param y координата клетки по вертикали
     * @return возвращает TRUE, если метод был выполнен успешно и FALSE,
     * если возникли проблемы (попытка добавить пустоту или добавить символ в непустую клетку)
     */
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
            field[y][x].setComb(new Comb(combType, field[y][x]), combType);
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
                if (x == c.getX()) { relativePos = CombType.VERTICAL; }
                else {
                    if (y == c.getY()) { relativePos = CombType.HORIZONTAL; }
                    else {
                        relativePos = (x - c.getX() == y - c.getY())?CombType.DIAGONAL_DOWN: CombType.DIAGONAL_UP;
                    }
                }
                //далее мы будем прибавлять линию правой (верхней) клетки к линии левой (нижней) клетки
                Cell ltCell, rbCell;
                if (relativePos == CombType.HORIZONTAL) {
                    if (x < c.getX()){
                        ltCell = field[y][x];
                        rbCell = c;
                    } else {
                        ltCell = c;
                        rbCell = field[y][x];
                    }
                } else {
                    if (relativePos == CombType.VERTICAL) {
                        if (y < c.getY()){
                            ltCell = field[y][x];
                            rbCell = c;
                        } else {
                            ltCell = c;
                            rbCell = field[y][x];
                        }
                    } else {
                        if (x < c.getX()){
                            ltCell = field[y][x];
                            rbCell = c;
                        } else {
                            ltCell = c;
                            rbCell = field[y][x];
                        }
                    }
                }
                ltCell.getComb(relativePos).addComb(rbCell.getComb(relativePos));
                allCombs.remove(rbCell.getComb(relativePos));
                rbCell.setComb(ltCell.getComb(relativePos), relativePos);
            }
        }
        return Result.SUCCESS;
    }

    /**
     * метод для удаления символа из клетки
     * @param x координата клетки по горизонтали
     * @param y координата клетки по вертикали
     * @return возвращает TRUE, если метод был выполнен успешно и FALSE,
     * если возникли проблемы (попытка отчистить пустую клетку)
     */
    public Result clearCell (int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) throw new IllegalArgumentException();
        //Проверка на заполненность клетки
        if (field[y][x].getSymbol() == Symbol.NULL) {
            return Result.FAIL;
        }
        //удаляем клетку из всех линий, в которых она состояла
        for (CombType combType:CombType.values()) {
            removeCellFromComb(field[y][x], combType);
        }
        //записываем в клетку знак пустоты
        field[y][x].setSymbol(Symbol.NULL);
        //опусташаем линии, которые хранились в клетке
        for (CombType combType:CombType.values()) {
            field[y][x].setComb(null, combType);
        }
        return Result.SUCCESS;
    }


    /**
     * метод для удаления клетки из линии
     * @param cell клетка, из которой нужно удалить символ
     * @param combType тип линии, из которой мы удаляем клетку
     */
    private void removeCellFromComb (Cell cell, CombType combType) {
        Comb comb = cell.getComb(combType);
        List <Comb> allCombs = comb.getSymbol() == Symbol.X? allXCombs: allOCombs;
        Pair<Comb> combPair = comb.removeCell(cell);
        //удаляем старую линию из списка и добавляем две новообразованные
        // (если удаляемая клетка была крайней, то пустую линию не добавляем)
        allCombs.remove(comb);
        if (combPair.first != null) allCombs.add(combPair.first);
        if (combPair.second != null) allCombs.add(combPair.second);
    }

    /**
     * метод для получения длиннейшей линии
     * @param symbol определяет длиннейшую линию каких символов мы хотим получить (крстиков или ноликов)
     * @return возвращает целое число - кол-во символов в длиннейшей линии
     */
    public int getTheLongestComb (Symbol symbol) {
        List<Comb> allCombs;
        if (symbol == Symbol.X) allCombs = allXCombs; else allCombs = allOCombs;
        //сортируем нужный список непрерывных линий символов по длине
        allCombs.sort(Comparator.comparing(Comb::getLength));
        //забираем последний элемент списка (с самой большой длиной) или возвращаем 0, если список пуст
        return allCombs.size() > 0? allCombs.get(allCombs.size() - 1).getLength(): 0;
    }

    /**
     * метод для получения символа клетки (нужен для проведения тестов)
     * @param x координата клетки по горизонтали
     * @param y координата клетки по вертикали
     * @return возвращает символ из клетки, координаты которой мы задали
     */
    public Symbol getSymbol (int x, int y) {
        if (x >= size || y >= size || x < 0 || y < 0) throw new IllegalArgumentException();
        return field[y][x].getSymbol();
    }

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
        for (int i = 0; i < size - 1; i ++) {
            for (int j = 0; j < size; j++) {
                sb.append(field[i][j].toString());
            }
            sb.append(System.lineSeparator());
        }
        for (int j = 0; j < size; j++) {
            sb.append(field[size - 1][j].toString());
        }
        //удаляем последний лишний абзац
        return sb.toString();
    }
}






