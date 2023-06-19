package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
/*
A játék főmenüjét elkészítő osztály
 */
public class Menu extends JFrame{
    private JPanel menupanel, settingspanel, diffpanel;
    private JButton b1, b2, b3, diffbtn;
    private JLabel difflevel;
    private JFrame gui;
    private String nehezseg = new String("Easy");
    private int mag = 500, szel = 500;
    private int gridnumber = 10;
    //private Settings settings = null;

    public void setDiff(String newdiff){
        nehezseg = newdiff;
    }
    public void setGrids(String value){
        gridnumber = Integer.parseInt(value);
    }
    public int getGridnumber(){
        return gridnumber;
    }

    /*létrehozza a főmenüt, benne a gombokkal:
    -New Game: új játék kezdése
    -Load Game: betölti az utolsó mentett játék állását
    -Settings: megnyitja a beállításokat
     */
    public void letrehoz() {

        gui = new JFrame("Menu");
        gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gui.setSize(mag, szel);

        gui.setName("Menu");
        menupanel = new JPanel();

        b1 = new JButton();
        b1.setText("New Game");
        b1.setAlignmentX(Component.CENTER_ALIGNMENT);
        b1.setFocusable(false);
        b1.addActionListener(new NewGameButton());
        //b1.setBounds(100, 100, 100, 100);

        b2 = new JButton();
        b2.setText("Load Game");
        b2.setAlignmentX(Component.CENTER_ALIGNMENT);
        b2.setFocusable(false);
        b2.addActionListener(new LoadGameListener());
        //b2.setBounds(100, 200, 100, 100);

        b3 = new JButton();
        b3.setText("Settings");
        b3.setAlignmentX(Component.CENTER_ALIGNMENT);
        b3.setFocusable(false);
        b3.addActionListener(new SettingsButtonListener());
        //b3.setBounds(100, 300, 100, 100);

        BoxLayout bl = new BoxLayout(menupanel, BoxLayout.PAGE_AXIS);
        menupanel.setLayout(bl);
        menupanel.add(b1);
        menupanel.add(b2);
        menupanel.add(b3);

        gui.setLayout(new FlowLayout(FlowLayout.CENTER));
        gui.add(menupanel);
        setResizable(true);
        gui.setVisible(true);
        //menupanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        //gui.add(diffbtn, CENTER_ALIGNMENT);

    }

/*
Elindítja az új játékot, tehát a hajólerakást
 */
    private class NewGameButton implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent ae){
            //Menu.this.nehezseg = settings.getNehezseg();
            GameInit gm = new GameInit(Menu.this.gui, nehezseg, gridnumber);
            gm.game_init_create();
            Menu.this.gui.setVisible(false);
        }
    }
/*
Megnyitja a beállítások menüt.
 */
    private class SettingsButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae){
          Settings settings = new Settings(Menu.this, Menu.this.nehezseg, gridnumber);
          settings.create_settings();
          Menu.this.dispose();
          Menu.this.gui.setVisible(false);

        }
    }
/*
Betölti a mentett játékot és elindítja azt.
 */
    private class LoadGameListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            Board playerboard = load_game("playerbrd.txt");
            Board shootboard = load_game("shootbrd.txt");
            String diff = load_game_diff("difficulty.txt");

            Game game = new Game(Menu.this.gui, playerboard, shootboard, nehezseg);
            game.createGameFrame();
            Menu.this.gui.setVisible(false);
        }
    }
/*
Betölti fileból a Board-ot
@param filename: a beolvasandó file neve
@return beolvasott Board
 */
    public Board load_game(String filename){


        Board brd = new Board();
        try{
            FileInputStream f = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(f);
            brd = (Board)in.readObject();
        }catch(FileNotFoundException ex){System.out.println("Failed to find " + filename);}
        catch (IOException ex){System.out.println("Failed to load " + filename);}
        catch(ClassNotFoundException ex){System.out.println("Class of " + filename + " not found.");}
        return brd;


    }
    /*
    Betölti fileból a nehézséget
    @param filename: a beolvasandó file neve
    @return beolvasott nehézség
     */
    public String load_game_diff(String filename){
        String s = new String();
        try{
            FileInputStream f = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(f);
            s = (String)in.readObject();
        }catch(FileNotFoundException ex){System.out.println("Failed to find " + filename);}
        catch (IOException ex){System.out.println("Failed to load " + filename);}
        catch(ClassNotFoundException ex){System.out.println("Class of " + filename + " not found.");}
        return s;
    }



}


