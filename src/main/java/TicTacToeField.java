import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeField {
    private final int size;
    public TicTacToeField (int size) {
        this.size = size;
        this.init();
    }
    private Cell [][] field;

    //инициализация поля
    public void init () {
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
}

class Cell {

    private int x;
    private int y;
    private Symbol symbol;
    private Comb [] combs = new Comb[3]; // массив для линий,
    // в которых находится клетка 0 - гориз., 1 - верт., 2 - диаг.
    private ArrayList<Cell> neighbours = new ArrayList<>(); // список с соседями клетки
    public Cell (int x, int y, Symbol s) {
        this.x = x;
        this. y = y;
        symbol = s;

    }

    public int getX () { return x;}
    public int getY () { return y;}
    public void addNeighbour (Cell neighbour) {neighbours.add(neighbour);}

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
    private combType type;
    private int length;
}



enum Symbol {X, O, _}
enum combType {HORIZONTAL, VERTICAL, DIAGONAL}