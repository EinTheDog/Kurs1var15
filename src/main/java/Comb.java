public class Comb {
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
