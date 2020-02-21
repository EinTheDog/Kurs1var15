package FieldPackage;

import java.util.ArrayList;
import java.util.HashMap;

class Cell {
    //координаты клетки в массиве field
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
        combs.put(CombType.DIAGONAL_UP, null);
        combs.put(CombType.DIAGONAL_DOWN, null);
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
