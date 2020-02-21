import FieldPackage.Symbol;
import FieldPackage.TicTacToeField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeFieldTest {

    @Test
    void addSymbol() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        assertEquals(Symbol.X,tField.getSymbol(2, 0));
    }

    @Test
    void clearCell() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        tField.clearCell(2, 0);
        assertEquals(Symbol.NULL,tField.getSymbol(2, 0));
    }

    @Test
    void getTheLongestComb() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        tField.addSymbol(Symbol.X, 1, 1);
        tField.addSymbol(Symbol.X, 0, 2);
        tField.addSymbol(Symbol.O, 0, 1);
        tField.addSymbol(Symbol.O, 0, 0);
        tField.addSymbol(Symbol.O, 1, 0);
        tField.addSymbol(Symbol.O, 2, 2);
        assertEquals(3, tField.getTheLongestComb(Symbol.X));

        tField = new TicTacToeField(4);
        tField.addSymbol(Symbol.X, 3, 0);
        tField.addSymbol(Symbol.X, 1, 0);
        tField.addSymbol(Symbol.X, 2, 1);
        tField.addSymbol(Symbol.X, 1, 2);
        tField.addSymbol(Symbol.X, 2, 2);
        tField.addSymbol(Symbol.X, 2, 3);
        tField.addSymbol(Symbol.O, 0, 0);
        tField.addSymbol(Symbol.O, 0, 1);
        tField.addSymbol(Symbol.O, 0, 2);
        tField.addSymbol(Symbol.O, 0, 3);
        tField.addSymbol(Symbol.O, 2, 0);
        tField.addSymbol(Symbol.O, 1, 3);
        tField.addSymbol(Symbol.O, 2, 0);
        assertEquals(4, tField.getTheLongestComb(Symbol.O));


    }
}