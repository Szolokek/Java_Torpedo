package menu;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShipTest {
    private Ship testship = new Ship("1x2");

    //helyesen visszaadja a hajó hosszát
    @Test
    public void shipLengthTest(){
        assertEquals(2, testship.getLength());
    }

}