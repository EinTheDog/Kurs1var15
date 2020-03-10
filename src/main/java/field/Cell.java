package field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Cell {
    //координаты клетки в массиве field
    private int x;
    private int y;
    //символ, хранящийся в клетке
    private Symbol symbol;
    //таблица, в которой хранятся длины линий разных типов (диагональ, горизонаталь, вертикаль),
    // проходящих через эту клетку
    private Map<CombType, Comb> combs = new HashMap<>();
    // список с соседями клетки
    private List<Cell> neighbours = new ArrayList<>();
    //конструктор
    public Cell (int x, int y, Symbol s) {
        this.x = x;
        this. y = y;
        symbol = s;
        combs.put(CombType.HORIZONTAL, null);
        combs.put(CombType.VERTICAL, null);
        combs.put(CombType.DIAGONAL_UP, null);
        combs.put(CombType.DIAGONAL_DOWN, null);
    }

    //методы для получения координат клетки
    public int getX () { return x;}
    public int getY () { return y;}

    /**
     * метод для добавления соседа клетки
     * @param neighbour клетка, которую мы добавляем в список соседей данной клетки
     */
    public void addNeighbour (Cell neighbour) {neighbours.add(neighbour);}

    /**
     * @return возвращает список (ArrayList) соседей клетки
     */
    public List<Cell> getNeighbours () { return neighbours;}

    /**
     * метод для получения непрерывной линии заданного типа, проходящей через эту клетку
     * @param combType тип непрерывной линии (по горизонатали, вертикали и т.д.)
     * @return возвращает линию класса Comb
     */
    public Comb getComb (CombType combType) {return combs.get(combType);}

    /**
     * метод для замены линии, которая проходит через данную клетку
     * @param comb линия, на которую мы заменяем линию, лежащую в данной клетке
     * @param combType тип заменяем линии (нужен, так как comb может быть равен null)
     */
    public  void  setComb (Comb comb, CombType combType) {combs.put(combType, comb );}
    //get и set для символа
    public Symbol getSymbol () { return symbol;}
    public void setSymbol (Symbol symbol) {this.symbol = symbol;}

    @Override
    public int hashCode () {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        result = result * 31 + symbol.hashCode();
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (o instanceof Cell) {
            Cell c = (Cell) o;
            return c.getX() == getX() && c.getY() == getY() && c.getSymbol() == getSymbol();
        }
        return false;
    }

    @Override
    public String toString () {
        switch (getSymbol()) {
            case O: return "O";
            case X: return "X";
            default: return "_";
        }
    }
}
