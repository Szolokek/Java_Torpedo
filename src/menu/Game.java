package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/*
A játékmenetet lebonyolító osztály
 */
public class Game extends JFrame{
    JFrame gameframe, menu;
    Board playerbrd, shootbrd;
    JPanel boardsPanel, playerBoardPanel, shootBoardPanel, buttonspanel;
    JButton quitbutton, savebutton;
    String difficulty;
    String found_field = new String("NF");  //az advanced bot itt tárolja az utolsó megtalált hajó mezőjét, "NF" ha még nme talált vagy már elsüllyesztette
    //képek
    private Icon hiticon = new ImageIcon("hit.png");
    private Icon sinkicon = new ImageIcon("sink.png");
    private Icon boathiticon = new ImageIcon("boathit.png");
    private Icon boatsinkicon = new ImageIcon("boatsink.png");
    private Icon missicon = new ImageIcon("miss.png");

    public Game(JFrame menu, Board playerbrd, Board enemybrd, String difficulty) {
        this.menu = menu;
        this.playerbrd = playerbrd;
        this.shootbrd = enemybrd;
        this.difficulty = difficulty;
    }

    public void setPlayerbrd(Board brd){
        playerbrd = brd;
    }
    public void setShootbrd(Board brd){
        shootbrd = brd;
    }
    public Board getShootbrd(){
        return shootbrd;
    }

/*
létrehozza a játékteret
 */
    public void createGameFrame() {
        //---------creating frame------------
        gameframe = new JFrame("Game");
        gameframe.setVisible(true);
        gameframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameframe.setSize(1000, 500);
        gameframe.setLayout(new BorderLayout());

        //---------creating boards------------
        playerBoardPanel = copy_board(playerbrd);
        shootBoardPanel = copy_board(shootbrd);
        //shootBoardPanel = create_board(shootbrd, playerbrd.getfields().size(), playerbrd.getfields().get(0).size());

        //-----adding boards to a panel---------
        boardsPanel = new JPanel(new GridLayout());
        boardsPanel.add(shootBoardPanel);
        boardsPanel.add(playerBoardPanel);

        //----------creating buttons---------------
        savebutton = new JButton();
        savebutton.setText("Save Game");
        savebutton.addActionListener(new SaveGameListener());

        quitbutton = new JButton();
        quitbutton.setText("Quit");
        quitbutton.addActionListener(new BackToMenuListener());

        buttonspanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonspanel.add(savebutton);
        buttonspanel.add(quitbutton);


        //----------adding panels to frame------------
        gameframe.add(buttonspanel, BorderLayout.SOUTH);
        gameframe.add(boardsPanel);


    }

    /*
    gombnyomásra visszalép a főmenübe
     */
    private class BackToMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            menu.setVisible(true);
            gameframe.dispose();
        }
    }
/*
gombnyomásra elmenti a játékot
 */
    private class SaveGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            Game.this.save_game();
        }
    }
/*
Board-ból panelt kreál
@param brd: ebből készíti
@return kész panel
 */
    public JPanel copy_board(Board brd) {
        GridLayout copy_board_lm = new GridLayout();
        copy_board_lm.setRows(brd.getfields().size());
        copy_board_lm.setColumns(brd.getfields().get(0).size());

        JPanel newBoardPanel = new JPanel();
        newBoardPanel.setLayout(copy_board_lm);

        for (int x = 0; x < brd.getfields().size(); x++) {
            for (int y = 0; y < brd.getfields().get(x).size(); y++) {
                if (brd == this.shootbrd) {
                    shootbrd.getfields().get(x).get(y).getButton().addActionListener(new ShootActionListener(shootbrd.getfields().get(x).get(y).getButton()));
                }
                newBoardPanel.add(brd.getfields().get(x).get(y).getButton());

            }
        }
        return newBoardPanel;
    }

    /*
    elmenti a pályák állását és a nehézséget fájlba
     */
    public void save_game() {


        try {
            playerbrd.save("playerbrd.txt");
        } catch (IOException ex) {
            System.out.println("Playerboard save failed.");
        }

        try {
            shootbrd.save("shootbrd.txt");
        } catch (IOException ex) {
            System.out.println("Shootboard save failed.");
        }
        try {
            FileOutputStream f = new FileOutputStream("difficulty.txt");
            ObjectOutputStream out = new ObjectOutputStream(f);
            out.writeObject(difficulty);
            out.close();
        } catch (IOException ex) {
            System.out.println("Difficulty save failed.");
        }

    }
/*
Ez a gomb felelős a megnyomott mező megjelölésére, hogy talált-e a lövés vagy sem
Továbbá meghívja az ellenfél lépését
és ellenőrzi, hogy az eltalált hajó elsüllyedt-e
 */
    private class ShootActionListener implements ActionListener, Serializable {
        private JButton btn;

        public ShootActionListener(JButton btn) {
            this.btn = btn;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            String coordinates[] = btn.getName().split(",");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            System.out.println(shootbrd.getfields().get(row).get(column).toString());
            if (shootbrd.getfields().get(row).get(column).getHit() == false) {
                shootbrd.getfields().get(row).get(column).setHit(true);
                if (shootbrd.getfields().get(row).get(column).getOccupied() == true) {
                    btn.setIcon(hiticon);
                    //btn.setBackground(Color.RED);
                    Ship shiphit = shootbrd.getfields().get(row).get(column).getShip();
                    if (ship_sunk(shiphit)) {
                        for (int i = 0; i < shiphit.getLength(); i++) {
                            shiphit.getFields().get(i).getButton().setIcon(sinkicon);
                        }
                    }
                    step_opponent();
                } else {
                    btn.setIcon(missicon);
                    step_opponent();
                }
            }
        }
    }

    /*
    Ellenőrzi, hogy a hajó elsüllyedt-e, és ha elsüllyedt és ez volt az utolsó hajó akkor befejezi a játékot.
    @param ship: ezt a hajót ellenőrzi
    @return esüllyedt vagy sem
     */
    public boolean ship_sunk(Ship ship) {

        for (int i = 0; i < ship.getLength(); i++) {
            if (ship.getFields().get(i).getHit() == false) {
                return false;

            }
        }
        if (shootbrd.getShips().contains(ship)) {
            shootbrd.shipSunk();
            if (shootbrd.getRemaining_ships() == 0) {
                EndScreen es = new EndScreen(menu);
                es.end_of_game("Player");
                Game.this.gameframe.dispose();
            }
        } else {
            playerbrd.shipSunk();
            if (playerbrd.getRemaining_ships() == 0) {
                EndScreen es = new EndScreen(menu);
                es.end_of_game("Computer");
                Game.this.gameframe.dispose();
            }
        }
        return true;
    }

    /*
    kiválasztja, hogy melyik bot lépjen a nehézség szerint
     */
    public void step_opponent() {
        switch (difficulty) {
            case ("Easy"):
                easy_step();
                break;
            case ("Advanced"):
                advanced_step();
                break;
            default:
                easy_step();
                break;
        }
    }

    /*
    könnyű ellenfél lépésének a logikáját valósítja meg
     */
    public void easy_step() {
        Random rand = new Random();
        int ranPlaceX = rand.nextInt(playerbrd.getfields().size());
        int ranPlaceY = rand.nextInt(playerbrd.getfields().get(0).size());
        while (playerbrd.getfields().get(ranPlaceX).get(ranPlaceY).getHit() == true) {
            ranPlaceX = rand.nextInt(playerbrd.getfields().size());
            ranPlaceY = rand.nextInt(playerbrd.getfields().get(0).size());
        }
        mark_field(playerbrd, ranPlaceX, ranPlaceY);


    }

    /*
    A fejlett ellenfél lépését valósítja meg
     */
    public void advanced_step() {

        if (found_field.equals("NF")) {    //ha nem talált hajót csak rnadom találgat
            easy_step();
        } else { //vadászik a hajó többi részére
            String coordinates[] = found_field.split(",");
            int y = Integer.parseInt(coordinates[1]);
            int x = Integer.parseInt(coordinates[0]);

            //felül van-e a többi része
            if (x - 1 >= 0 && playerbrd.getfields().get(x - 1).get(y).getHit() == false) {
                mark_field(playerbrd, x-1, y);
            }

            //alul
            else if (x + 1 < playerbrd.getfields().size() && playerbrd.getfields().get(x + 1).get(y).getHit() == false) {
                mark_field(playerbrd, x+1, y);
            }

            //jobbra
            else if (y + 1 < playerbrd.getfields().get(x).size() && playerbrd.getfields().get(x).get(y + 1).getHit() == false) {
                mark_field(playerbrd, x, y+1);
            }

            //balra
            else if (y - 1 >= 0 && playerbrd.getfields().get(x).get(y - 1).getHit() == false) {
                mark_field(playerbrd, x, y-1);
            }

            //ha nem tudna semerre lőni
            else{
                easy_step();
            }
        }
    }

    /*
    megjelöl egy mezőt, ahova az ellenfél lő
    @params brd: ezen a pályán
            x: a mező x koordinátája
            y: a mező y koordinátája
     */
    public void mark_field(Board brd,int x,int  y){
        playerbrd.getfields().get(x).get(y).setHit(true);


        if (playerbrd.getfields().get(x).get(y).getOccupied() == true) {
            playerbrd.getfields().get(x).get(y).getButton().setIcon(boathiticon);
            Ship shiphit = playerbrd.getfields().get(x).get(y).getShip();
            found_field = new String(playerbrd.getfields().get(x).get(y).getButton().getName());
            if (ship_sunk(shiphit)) {
                for (int i = 0; i < shiphit.getLength(); i++) {
                    shiphit.getFields().get(i).getButton().setIcon(boatsinkicon);
                }
                found_field = new String("NF");

            }
        } else {
            playerbrd.getfields().get(x).get(y).getButton().setIcon(missicon);
        }
    }
}



