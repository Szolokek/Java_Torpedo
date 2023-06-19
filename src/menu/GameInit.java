package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
/*
Ez az osztály felelős a játékos pályájának az inicializálásáért, továbbá az ellenfél pályája itt kerül inicializálásra
pl:hajók lerakása, stb..
 */
public class GameInit extends JFrame {
    private JFrame menu;
    private int rows = 5, columns = 5;
    private JFrame game_init;
    private JPanel buttonspanel, boardPanel;
    private JButton field_button, backbtn, btn1x2, btn1x3, btn1x5, ship_alignment_button, startgame_button;
    private Board brd, brd_enemy;
    private ArrayList<Ship> playerships = new ArrayList<Ship>(Arrays.asList(new Ship("1x2"), new Ship("1x3"), new Ship("1x5")));
    private ArrayList<Ship> enemyships = new ArrayList<Ship>(Arrays.asList(new Ship("1x2"), new Ship("1x3"), new Ship("1x5")));
    private Ship selected_ship;
    private int ships_remaining;
    private String ship_alignment = new String("vertical");
    private boolean placing_ship = false;
    private  String difficulty;
    //képek
    private Icon water = new ImageIcon("water.png");
    private Icon boat = new ImageIcon("boat.png");

    public GameInit(JFrame m, String difficulty, int grids){
        this.menu = m;
        this.ships_remaining = playerships.size();
        this.difficulty = difficulty;
        rows = grids;
        columns = grids;
    }

    public void setSelectedShip(Ship ship){
        selected_ship = ship;
    }

    public void setAlignment(String a){
        this.ship_alignment = a;
    }

    public void setBrdEnemy(Board b){
        brd_enemy = b;
    }
    public Board getBrdEnemy(){
        return brd_enemy;
    }
    public ArrayList<Ship> getPlayerships(){
        return playerships;
    }

/*
megkreálja a pályát ahol a játékos lerakhatja a hajóit
 */
    public void game_init_create() {
        game_init = new JFrame("Initialization");
        game_init.setDefaultCloseOperation(EXIT_ON_CLOSE);
        game_init.setSize(1000, 500);
        //game_initk = new JPanel();

        brd = new Board();
        create_board(brd, rows, columns);
        System.out.println(brd);
        boardPanel = create_boardPanel(brd, rows, columns);

        game_init.setLayout(new BorderLayout());
        game_init.add(boardPanel, BorderLayout.CENTER);



        buttonspanel = new JPanel(new FlowLayout());

        btn1x2 = new JButton();
        btn1x2.setText("Destroyer: 1x2");
        btn1x2.setName("1x2");
        btn1x2.addActionListener(new Ship_selector(btn1x2));
        btn1x3 = new JButton();
        btn1x3.setText("Submarine: 1x3");
        btn1x3.setName("1x3");
        btn1x3.addActionListener(new Ship_selector(btn1x3));
        btn1x5 = new JButton();
        btn1x5.setText("Carrier: 1x5");
        btn1x5.setName("1x5");
        btn1x5.addActionListener(new Ship_selector(btn1x5));

        ship_alignment_button = new JButton();
        ship_alignment_button.setText(ship_alignment);
        ship_alignment_button.addActionListener(new ShipAlignmentListener(ship_alignment_button));
        JLabel ship_alignment_label = new JLabel();
        ship_alignment_label.setText("Ship Alignment:");
        //ship_alignment_label.setHorizontalTextPosition(JLabel.LEFT);

        buttonspanel.add(ship_alignment_label);
        buttonspanel.add(ship_alignment_button);

        startgame_button = new JButton();
        startgame_button.setText("Start Game");
        startgame_button.setEnabled(false);
        startgame_button.addActionListener(new StartGameListener());



        backbtn = new JButton();
        backbtn.addActionListener(new backtomenu());
        backbtn.setText("Back");

        buttonspanel.add(btn1x2);
        buttonspanel.add(btn1x3);
        buttonspanel.add(btn1x5);
        buttonspanel.add(startgame_button);
        buttonspanel.add(backbtn);


        game_init.add(buttonspanel, BorderLayout.SOUTH);


        game_init.setVisible(true);


    }
/*
Ha ki van választva egy hajó akkor a kijelölt mezőre ellenőrzi,hogy le lehet-e rakni a hajót, ha igen akkor meghívja az ezt lehelyező függvényt
 */
    private class PlaceShipListener implements ActionListener {
        JButton btn;

        public PlaceShipListener(JButton btn){
            this.btn = btn;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println(btn.getName());
            if(GameInit.this.placing_ship) {
                String coordinates[] = btn.getName().split(",");
                int row = Integer.parseInt(coordinates[0]);
                System.out.println(row);
                int column = Integer.parseInt(coordinates[1]);
                System.out.println(column);
                String selctedshiplength[] = selected_ship.getSize().split("x");
                if (can_be_placed(brd,Integer.parseInt(selctedshiplength[1]), column, row)) {

                        neighbour_field(brd, GameInit.this.brd.getfields().get(row).get(column),
                                //Character.getNumericValue(GameInit.this.selected_ship.charAt(2)),
                                Integer.parseInt(selctedshiplength[1]),
                                column, row);
                        brd.getShips().add(selected_ship);
                        placing_ship = false;
                    if(ships_remaining == 0){
                        GameInit.this.startgame_button.setEnabled(true);
                    }
                }
            }
        }
    }
/*
állítja, hogy a hajók vertikásisan vagy horizontálisan legyenk lerakva
 */
    private class ShipAlignmentListener implements ActionListener{
        JButton btn;
        public ShipAlignmentListener(JButton btn){
            this.btn = btn;
        }
        @Override
        public void actionPerformed(ActionEvent ae){
            if(GameInit.this.ship_alignment.equals("vertical")){
                btn.setText("horizontal");
                GameInit.this.setAlignment("horizontal");
            }
            else{btn.setText("vertical"); GameInit.this.setAlignment("vertical");}
        }
    }
/*
megadja azt a hajót amelyet a játékos választott, hogy a következő esetben, amikor a játékos a pálya egy mezejére kattint akkor lerakható legyen
 */
    private class Ship_selector implements ActionListener{
        JButton btn;

        public Ship_selector(JButton btn){
            this.btn = btn;
        }

        @Override
        public void actionPerformed(ActionEvent ae){
            for(int i = 0; i < playerships.size(); i++) {
                if (btn.getName().equals(playerships.get(i).getSize())) {
                    selected_ship = playerships.get(i);
                    ships_remaining = ships_remaining-1;
                }
            }
            placing_ship = true;
            btn.setEnabled(false);

        }
    }
/*
Viszzalép a főmenübe, mentés nélkül
 */
    private class backtomenu implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            menu.setVisible(true);
            GameInit.this.game_init.dispose();
        }
    }
/*
Ha az összes hajó le lett helyezve ez elindítja a játékot
 */
    private class StartGameListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){

            place_enemy_ships(rows, columns);
            System.out.println(brd_enemy);

            brd.setRemaining_ships(brd.getShips().size());
            brd_enemy.setRemaining_ships(brd.getShips().size());

            Game game = new Game(GameInit.this.menu, GameInit.this.brd, GameInit.this.brd_enemy, difficulty);
            game.createGameFrame();
            GameInit.this.game_init.setVisible(false);
        }
    }
/*
Ez a függvény rekurzívan megkeresi a szomszéd mezőket a fent eltárolt "alignment" irányban és lerakja oda a hajót
@params newbrd: erre a pályára rakja a hajót
        f: erre a mezőre rakja le a hajót
        shiplength:ilyen hoszzú a hajó, ez minden egyes átadásnál csökken, hogy a végén ki lehessen lépni a függvényből
        colunm: a hajó helye az y tengelyen
        row: hajó helye az x tengelyen
@return a megtalált következő mező
 */
    public Field neighbour_field(Board newbrd, Field f, int shiplength, int column, int row){
        f.setOccupied(true);
        if(newbrd == brd){
            f.getButton().setIcon(boat);
            //f.getButton().setBackground(Color.BLACK);
        }
        selected_ship.getFields().add(f);
        f.setShip(selected_ship);
        System.out.println(shiplength);
    if (shiplength == 1){ return f;}
        else{
            if(ship_alignment.equals("vertical")){
                return neighbour_field(newbrd, newbrd.getfields().get(row+1).get(column), shiplength-1, column, row+1);}
            else{
                return neighbour_field(newbrd, newbrd.getfields().get(row).get(column+1), shiplength-1, column+1, row);
            }
        }


    }
/*
Megnézi, hogy a kiválasztott hajó a kiválasztott irányban lerakható-e (nem ütközik semmivel vagy nem fut le a pályáról)
@params newbrd: erre a pályára akarja rakni a hajót
        shiplength:ilyen hoszzú a hajó
        colunm: a hajó helye az y tengelyen
        row: hajó helye az x tengelyen
@return le lehet-e rakni a hajót
 */
    public boolean can_be_placed(Board newbrd, int shiplength, int column, int row){
        if(ship_alignment.equals("vertical")) {
            if(row+shiplength > GameInit.this.rows){return false;} // ha kilógna a pályáról
            for (int i = 0; i < shiplength; i++) {
                if(newbrd.getfields().get(row+i).get(column).getOccupied()){ // ha a következő mezők egyik e hajót tartalmaz
                    return false;
                }
            }
        }
        else{
            if(column+shiplength > GameInit.this.columns){return false;} // ha kilógna a pályáról
            for (int i = 0; i < shiplength; i++) {
                if(newbrd.getfields().get(row).get(column+i).getOccupied()){ // ha a következő mezők egyik e hajót tartalmaz
                    return false;
                }
            }
        }
        return true;

    }
/*
Egy pálya létrehozása és eltárolása egy Board osztályban
@params brd: ebben tárolja a létrehozott pályát
        rows: pálya mérte x tengelyen
        columns: pálya mérete y tengelyen
 */
    public void create_board(Board brd, int rows, int columns){

        String x_axis;
        String y_axis;
        for (int x = 0; x < rows; x++) {
            brd.getfields().add(new ArrayList<Field>());
            for (int y = 0; y < columns; y++) {
                x_axis = new String(Integer.toString(x));
                y_axis = new String(Integer.toString(y));
                field_button = new JButton();
                field_button.setName(new String(x_axis + ',' + y_axis));
                field_button.setIcon(water);
                //field_button.setBackground(Color.BLUE);
                if(brd == this.brd){
                    field_button.addActionListener(new PlaceShipListener(field_button));
                }
                Field field = new Field(field_button);
                brd.getfields().get(x).add(field);
            }
        }

    }

/*
JPanel-t készít egy pályából
@params brd: ebből a pályából készül
        rows: pálya mérete x tengelyen
        columns:pálya mérete y tengelyen
@return az elkészült panel
 */
    public JPanel create_boardPanel(Board brd, int rows, int columns){

        JPanel newpanel = new JPanel();

        GridLayout board_lm = new GridLayout();
        board_lm.setRows(rows);
        board_lm.setColumns(columns);
        newpanel.setLayout(board_lm);

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                newpanel.add(brd.getfields().get(x).get(y).getButton());
            }
        }
        return newpanel;
    }

    /*
    lerakja az ellenfél hajóit, véletlenszerűen kisorsolja a helyüket és megpróbálja oda lerakni. A legnagyobb hajóval kezd és addig próbálja lerakni őket,
    emíg nem sikerül szabad helyet találni
    @params rows: pálya mérete x tengelyen
            columns:pálya mérete y tengelyen
     */
    public void place_enemy_ships(int rows, int columns){
        Random rand = new Random();
        brd_enemy = new Board();
        create_board(brd_enemy, rows, columns);
        int allships = enemyships.size()-1;
        while(allships >= 0){
            selected_ship = enemyships.get(allships);
            int ranPlaceX = rand.nextInt(rows);
            int ranPlaceY = rand.nextInt(columns);
            int ranalignment = rand.nextInt(2);
            if(ranalignment == 0) {
                GameInit.this.setAlignment("vertical");
            }
            else{
                GameInit.this.setAlignment("horizontal");
            }
                String selected_ship_length[] = enemyships.get(allships).getSize().split("x");
                if (can_be_placed(brd_enemy, Integer.parseInt(selected_ship_length[1]), ranPlaceY, ranPlaceX)) {
                    neighbour_field(brd_enemy, brd_enemy.getfields().get(ranPlaceX).get(ranPlaceY),
                            //Character.getNumericValue(GameInit.this.selected_ship.charAt(2)),
                            Integer.parseInt(selected_ship_length[1]),
                            ranPlaceY, ranPlaceX);
                    brd_enemy.getShips().add(selected_ship);
                    allships--;
                }
        }
        GameInit.this.setAlignment("vertical");
    }



}