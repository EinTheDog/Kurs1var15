package FieldPackage;

class Comb {
    private CombType type;
    private Symbol symbol;
    private int length;
    public Comb (CombType combType, Symbol symbol) {
        type = combType;
        length = 1;
        this.symbol = symbol;
    }
    public void addCell () { length++; }
    public void removeCell () { length--; }

    public CombType getType () { return type; }
    public int getLength () { return length; }
    public Symbol getSymbol () { return symbol;}
}
