package menu;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameInitTest {
    private GameInit gameinit = new GameInit(new Menu(), "Easy", 10);
    private Board brd = new Board();
    @Before
    public void init() {
        gameinit.create_board(brd, 10, 10);
        gameinit.setBrdEnemy(brd);
    }
//helyes a board nagysága
    @Test
    public void testCreateBoard(){
        Board brdtest = new Board();
        gameinit.create_board(brdtest, 10, 10);
        assertEquals(10, brdtest.getfields().size());
    }

    //nem lehet lerakni a 11 hosszú hajót a 10 hosszú pályán
    @Test
    public void canbeplacedTest(){
        assertFalse(gameinit.can_be_placed(brd, 11, 0, 0));
    }

    //ha megjelölöm akkor átáll
    @Test
    public void hitTest(){
        brd.getfields().get(0).get(0).setHit(true);
        assertTrue(brd.getfields().get(0).get(0).getHit());
    }

    //az összes hajó le lett helyezve
    @Test
    public void allshipsplacedTest(){
        gameinit.place_enemy_ships(10, 10);
        int ship_containing_fields = 0;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                if(gameinit.getBrdEnemy().getfields().get(i).get(j).getOccupied()){
                    ship_containing_fields++;
                }
            }
        }
        assertEquals(10, ship_containing_fields);
    }
}