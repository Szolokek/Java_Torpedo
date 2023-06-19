package menu;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    private Game game = new Game(new Menu(), new Board(), new Board(), "Easy");
    private Ship testship = new Ship("1x2");
    @Before
    public void init(){
        GameInit gameinit = new GameInit(new Menu(), "Easy", 10);
        Board brd = new Board();
        gameinit.create_board(brd, 10, 10);
        gameinit.setSelectedShip(testship);
        gameinit.neighbour_field(brd, brd.getfields().get(0).get(0), 2, 0, 0);
        game.setShootbrd(brd);
        game.setPlayerbrd(brd);
    }

    //le lett helyezve a hajó a bal felső sarokba
    @Test
    public void shipPlacedOnShootbrd(){
        assertTrue(game.shootbrd.getfields().get(0).get(0).getOccupied());
        assertTrue(game.shootbrd.getfields().get(1).get(0).getOccupied());
        assertFalse(game.shootbrd.getfields().get(3).get(0).getOccupied());
    }

    //ha megjelölöm az egész hajót elsüllyed
    @Test
    public void shipSank(){
        game.getShootbrd().getfields().get(0).get(0).setHit(true);
        game.getShootbrd().getfields().get(1).get(0).setHit(true);
        assertTrue(game.ship_sunk(testship));
    }

    //ha megjelölök egy mazőt a mark_field függvénnyel akkor az meg lesz jelölve
    @Test
    public void markFieldTest(){
        game.mark_field(game.playerbrd, 9, 9);
        assertTrue(game.playerbrd.getfields().get(9).get(9).getHit());
    }

}