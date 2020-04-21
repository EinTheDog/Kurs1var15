package field;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

//класс для непрерывной линии символов
class Comb {
    //все параметры
    private CombType type;
    private Symbol symbol;
    private List<Cell> list = new ArrayList<>();
    //конструктор
    public Comb (CombType combType, Cell cell) {
        type = combType;
        list.add(cell);
        this.symbol = cell.getSymbol();
    }
    //добавление клетки
    private void addCell (Cell cell) {
        list.add(cell);
    }

    //объединение двух линий
    public void addComb (Comb comb) {
        this.list.addAll(comb.getCells());
    }

    private Comb subComb (int ind1, int ind2) {
        if (ind1 == ind2) return null;
        Comb newComb = new Comb(type, list.get(ind1));
        for (int i = ind1 + 1; i < ind2 && i < list.size(); i++) newComb.addCell(list.get(i));
        return newComb;
    }

    /**
     * @param cell удаляемая из комбинации клетка
     * @return возвращает пару линий,
     * 1-ая линия - левая (или верхняя) часть линии клетки
     * 2-ая линия - правая (или нижняя) часть линии клетки
     */
    public Pair<Comb> removeCell (Cell cell) {
        int ind = list.indexOf(cell);
        return new Pair<>(this.subComb(0, ind), this.subComb(ind + 1, list.size()) );
    }


    //getters
    public CombType getType () { return type; }
    public int getLength () { return list.size(); }
    public Symbol getSymbol () { return symbol;}
    public List<Cell> getCells () {return List.copyOf(list);}

    //переопределения
    @Override
    public int hashCode () {
        int result = 17;
        for (Cell c: list) { result = result * 31 + c.hashCode(); }
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (o instanceof Comb) {
            if (list.size() != ((Comb) o).getLength()) return false;
            for (int i = 0; i < list.size(); i ++) {
                if (list.get(i) != ((Comb) o).getCells().get(i)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        for (Cell c: list) { sb.append(c.toString()); }
        return sb.toString();
    }
}
