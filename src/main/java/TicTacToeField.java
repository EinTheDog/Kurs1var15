import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class TicTacToeField {
    private final int size;
    private  ArrayList<Comb> allCombs = new ArrayList<>();
    private Cell [][] field;

    public TicTacToeField (int size) {
        this.size = size;
        //создание матрицы для хранения клеток
        field = new Cell [size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                field[i][j] = new Cell(j, i, Symbol._); // каждая клетка пока пустая
                addNeighbours(field[i][j]);
            }
        }
    }

    //добавление всех соседей клетки
    private void addNeighbours (Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        for (int i = y - 1; i <= y + 1; i++) {
            if (i < 0) continue;
            for (int j = x - 1; j <= x + 1; j++) {
                if (x < 0 || (i == y && j ==x)) continue;
                cell.addNeighbour(field[i][j]);
            }
        }
    }

    //функция для добавления символа в клетку
    public void addSymbol (Symbol symbol, int x, int y) {
        field[y][x].setSymbol(symbol);
        field[y][x].setComb(new Comb(CombType.HORIZONTAL), CombType.HORIZONTAL);
        field[y][x].setComb(new Comb(CombType.VERTICAL), CombType.VERTICAL);
        field[y][x].setComb(new Comb(CombType.DIAGONAL), CombType.DIAGONAL);
        //просматриваем всех соседей данной клетки
        for (Cell c: field[y][x].getNeighbours()) {
            //если символ соседней клетки совпадает с нашим символом
            if (c.getSymbol() == symbol) {
                //определяем взаимное расположение клеток
                CombType relativePos;
                if (x == c.getX()) { relativePos = CombType.HORIZONTAL; }
                else {
                    if (y == c.getY()) { relativePos = CombType.VERTICAL; }
                    else relativePos = CombType.DIAGONAL;
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
                allCombs.sort(Comparator.comparing(Comb::getLength));
            }
        }

    }

    //функция для отчистки клетки
    public void clearCell (int x, int y) {
        Symbol symbol = field[y][x].getSymbol();
        field[y][x].getComb(CombType.HORIZONTAL).removeCell();
        if (field[y][x].getComb(CombType.HORIZONTAL).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.HORIZONTAL));
        }
        field[y][x].getComb(CombType.VERTICAL).removeCell();
        if (field[y][x].getComb(CombType.VERTICAL).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.HORIZONTAL));
        }
        field[y][x].getComb(CombType.DIAGONAL).removeCell();
        if (field[y][x].getComb(CombType.DIAGONAL).getLength() == 0){
            allCombs.remove(field[y][x].getComb(CombType.HORIZONTAL));
        }
        field[y][x].setSymbol(Symbol._);
        field[y][x].setComb(null, CombType.HORIZONTAL);
        field[y][x].setComb(null, CombType.VERTICAL);
        field[y][x].setComb(null, CombType.DIAGONAL);
        allCombs.sort(Comparator.comparing(Comb::getLength));

    }

    //получение длиннейшей
}

class Cell {

    private int x;
    private int y;
    private Symbol symbol;
    private HashMap<CombType, Comb> combs = new HashMap<>();
    private ArrayList<Cell> neighbours = new ArrayList<>(); // список с соседями клетки
    public Cell (int x, int y, Symbol s) {
        this.x = x;
        this. y = y;
        symbol = s;
        combs.put(CombType.HORIZONTAL, null);
        combs.put(CombType.VERTICAL, null);
        combs.put(CombType.DIAGONAL, null);
    }

    public int getX () { return x;}
    public int getY () { return y;}
    public void addNeighbour (Cell neighbour) {neighbours.add(neighbour);}
    public ArrayList<Cell> getNeighbours () { return neighbours;}
    public Comb getComb (CombType combType) {return combs.get(combType);}
    public  void  setComb (Comb comb, CombType combType) {combs.put(combType, comb );}
    public Symbol getSymbol () { return symbol;}
    public void setSymbol (Symbol symbol) {this.symbol = symbol;}


}


//класс для пар
class Pair <T, U> {
    private T first;
    private U second;
    public Pair (T first, U second) {
        this.first = first;
        this.second = second;
    }
    public T getFirst () {return first;}
    public U getSecond() {return second;}
    public void setFirst(T first) { this.first = first;}
    public void setSecond(U second) { this.second = second;}
}

//класс для непрерывной линии
class Comb {
    private CombType type;
    private int length;
    public Comb (CombType combType) {
        type = combType;
        length = 1;
    }
    public void addCell () { length++; }
    public void removeCell () { length--; }
    public CombType getType () { return type; }
    public int getLength () { return length; }
}



enum Symbol {X, O, _}
enum CombType {HORIZONTAL, VERTICAL, DIAGONAL}