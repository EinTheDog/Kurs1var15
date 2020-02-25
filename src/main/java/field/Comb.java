package field;

//класс для непрерывной линии символов
class Comb {
    //все параметры
    private CombType type;
    private Symbol symbol;
    private int length;
    //конструктор
    public Comb (CombType combType, Symbol symbol) {
        type = combType;
        length = 1;
        this.symbol = symbol;
    }
    //добавление/ удаление клетки из линии
    public void addCell () { length++; }
    public void removeCell () { length--; }

    //getters
    public CombType getType () { return type; }
    public int getLength () { return length; }
    public Symbol getSymbol () { return symbol;}
}
