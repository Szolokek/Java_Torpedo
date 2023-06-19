package menu;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import menu.Menu;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class MenuTest {
    public Menu menu = new Menu();

    //működik a játéktábla méretének átadása
    @Test
    public void test(){
        menu.setGrids("1");
        Assert.assertEquals(1, menu.getGridnumber());
    }

    //a catch megfogja a FileNotFound exeption-t
    @Test(expected = Test.None.class)
    public void loadGameTest_ExeptionCaugth(){
        menu.load_game("asd.txt");
    }


}