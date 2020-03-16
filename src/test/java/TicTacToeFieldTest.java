import field.Result;
import field.Symbol;
import field.TicTacToeField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeFieldTest {

    @Test
    void addSymbol() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        assertEquals(Symbol.X,tField.getSymbol(2, 0));

        //попытка добавить символ в заполненную клетку
        tField = new TicTacToeField(2);
        tField.addSymbol(Symbol.X, 1, 0);
        assertEquals(Result.FAIL, tField.addSymbol(Symbol.O, 1, 0));
        assertEquals(Result.FAIL, tField.addSymbol(Symbol.X, 1, 0));

        //Попытка добавить пустоту
        tField = new TicTacToeField(20);
        assertEquals(Result.FAIL, tField.addSymbol(Symbol.NULL, 2, 0));

    }

    @Test
    void clearCell() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        tField.clearCell(2, 0);
        assertEquals(Symbol.NULL,tField.getSymbol(2, 0));

        //Попытка отчистить пустую клетку
        tField = new TicTacToeField(2);
        assertEquals(Result.FAIL, tField.clearCell(1, 0));
    }

    @Test
    void testForWrongArguments() {
        //Попытка задать некорректные координаты клетки
        final TicTacToeField finalField1 = new TicTacToeField(1);
        assertThrows(IllegalArgumentException.class, () -> finalField1.clearCell(1, 0));
        assertThrows(IllegalArgumentException.class, () -> finalField1.addSymbol(Symbol.X, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> finalField1.getSymbol(-2, -0));

        //Попытка задать полю некорректный размер
        assertThrows(IllegalArgumentException.class, () -> new TicTacToeField(-2));
    }

    @Test
    void getTheLongestXComb() {
        TicTacToeField tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 2, 0);
        tField.addSymbol(Symbol.X, 1, 1);
        tField.addSymbol(Symbol.X, 0, 2);
        tField.addSymbol(Symbol.O, 0, 1);
        tField.addSymbol(Symbol.O, 0, 0);
        tField.addSymbol(Symbol.O, 1, 0);
        tField.addSymbol(Symbol.O, 2, 2);
        assertEquals(3, tField.getTheLongestComb(Symbol.X));

        tField = new TicTacToeField(100);
        tField.addSymbol(Symbol.X, 0, 0);
        assertEquals(1, tField.getTheLongestComb(Symbol.X));

        tField = new TicTacToeField(1000);
        assertEquals(0, tField.getTheLongestComb(Symbol.X));

        tField = new TicTacToeField(5);
        tField.addSymbol(Symbol.X, 0, 0);
        tField.addSymbol(Symbol.X, 1, 0);
        tField.addSymbol(Symbol.X, 3, 0);
        tField.addSymbol(Symbol.X, 4, 0);
        tField.addSymbol(Symbol.X, 2, 0);
        assertEquals(5, tField.getTheLongestComb(Symbol.X));
        tField.clearCell(1, 0);
        assertEquals(3, tField.getTheLongestComb(Symbol.X));
    }

    @Test
    void getTheLongestOComb() {
        TicTacToeField tField = new TicTacToeField(4);
        tField.addSymbol(Symbol.O, 0, 0);
        tField.addSymbol(Symbol.O, 0, 1);
        tField.addSymbol(Symbol.O, 0, 2);
        tField.addSymbol(Symbol.O, 0, 3);
        tField.addSymbol(Symbol.O, 2, 0);
        tField.addSymbol(Symbol.O, 1, 3);
        tField.addSymbol(Symbol.O, 2, 0);
        tField.addSymbol(Symbol.X, 3, 0);
        tField.addSymbol(Symbol.X, 1, 0);
        tField.addSymbol(Symbol.X, 2, 1);
        tField.addSymbol(Symbol.X, 1, 2);
        tField.addSymbol(Symbol.X, 2, 2);
        tField.addSymbol(Symbol.X, 2, 3);
        assertEquals(4, tField.getTheLongestComb(Symbol.O));

        tField = new TicTacToeField(1000);
        for (int i = 0; i < 100; i ++) {
            for (int j = 0; j < 100; j ++) {
                tField.addSymbol(Symbol.X, i, j);
            }
        }
        assertEquals(0, tField.getTheLongestComb(Symbol.O));

        //случай, когда c.getComb(relativePos).getLength() < field[y][x].getComb(relativePos).getLength()
        tField = new TicTacToeField(4);
        tField.addSymbol(Symbol.O, 0, 0);
        tField.addSymbol(Symbol.O, 1, 1);
        tField.addSymbol(Symbol.O, 3, 3);
        tField.addSymbol(Symbol.O, 2, 2);
        assertEquals(4, tField.getTheLongestComb(Symbol.O));

        tField = new TicTacToeField(3);
        tField.addSymbol(Symbol.X, 0, 0);
        tField.addSymbol(Symbol.X, 1, 1);
        tField.addSymbol(Symbol.X, 2, 2);
        tField.clearCell(1,1);
        assertEquals(1, tField.getTheLongestComb(Symbol.X));
    }

    @Test
    void fieldEquals () {
        TicTacToeField tField1 = new TicTacToeField(4);
        TicTacToeField tField2 = new TicTacToeField(4);
        for (int i = 0; i < 4; i++) {
            tField1.addSymbol(Symbol.O, i, 1);
            tField2.addSymbol(Symbol.O, i, 1);
        }
        assertEquals(tField1, tField2);
        assertEquals(tField2, tField1);

        tField1 = new TicTacToeField(6);
        tField2 = new TicTacToeField(6);
        for (int i = 0; i < 4; i++) {
            tField1.addSymbol(Symbol.O, i, 1);
            tField2.addSymbol(Symbol.X, i, 1);
        }
        assertNotEquals(tField1, tField2);
        assertNotEquals(tField2, tField1);

        tField1 = new TicTacToeField(4);
        tField2 = new TicTacToeField(6);
        for (int i = 0; i < 4; i++) {
            tField1.addSymbol(Symbol.X, i, 1);
            tField2.addSymbol(Symbol.X, i, 1);
        }
        assertNotEquals(tField1, tField2);
        assertNotEquals(tField2, tField1);

        assertNotEquals("hello", tField1);
    }

    @Test
    void fieldToString () {
        TicTacToeField tField = new TicTacToeField(4);
        tField.addSymbol(Symbol.X, 3, 0);
        for (int i = 0; i < 4; i++) {
            tField.addSymbol(Symbol.O, i, 1);
        }
        String answer = "___X" + System.lineSeparator()
                      + "OOOO" + System.lineSeparator()
                      + "____" + System.lineSeparator()
                      + "____";
        assertEquals(answer, tField.toString());
    }

}