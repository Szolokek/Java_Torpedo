package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
A játék beállításait tároló osztály
 */
public class Settings  extends JFrame {
    private Menu menu;
    private JFrame settings;
    JPanel buttonspanel, diffpanel, gridspanel;
    private JButton backToMenuBtn;
    private JComboBox diff, grids;
    private String nehezseg, gridnumber;

    public Settings(Menu menu, String nehezseg, int gridnumber) {
        this.menu = menu;
        this.nehezseg = nehezseg;
        this.gridnumber = Integer.toString(gridnumber);
    }

    public String getNehezseg() {
        return nehezseg;
    }
/*
létrehozza a beállítások menüt
 */
    public void create_settings() {
        settings = new JFrame("Settings");
        settings.setVisible(true);
        settings.setDefaultCloseOperation(EXIT_ON_CLOSE);
        settings.setSize(500, 500);
        buttonspanel = new JPanel();
        BoxLayout sbl = new BoxLayout(buttonspanel, BoxLayout.PAGE_AXIS);

        buttonspanel.setLayout(sbl);

        JLabel difflevel = new JLabel();
        difflevel.setText("Game difficulty:");

        String difficulties[] = {"Easy", "Advanced"};
        diff = new JComboBox(difficulties);
        if(nehezseg.equals("Easy")){
            diff.setSelectedItem("Easy");
        }
        else{
        diff.setSelectedItem("Advanced");}
        diff.addActionListener(new DiffLevelSetter());
        diff.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sizelabel = new JLabel();
        sizelabel.setText("Board size:");

        //A pálya mérete elméletileg végtelen lehetne, viszont a gyakorlatban nem túl játszható úgy, ezéret megadtam pár értékert előre,
        //ezenfelül lehetne eltérő nagyságú sor-oszlopú a pálya
        String sizes[] = {"5", "7", "10", "20"};
        grids = new JComboBox(sizes);
        switch(gridnumber){
            case("7"):
                grids.setSelectedItem("7");
                break;
             case("10"):
                grids.setSelectedItem("10");
                break;
             case("20"):
                grids.setSelectedItem("20");
                break;
            default:
                grids.setSelectedItem("5");
                break;

        }
        grids.setAlignmentX(Component.CENTER_ALIGNMENT);
        grids.addActionListener(new GridNumberSetter());

        gridspanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gridspanel.add(sizelabel);
        gridspanel.add(grids);


        backToMenuBtn = new JButton();
        backToMenuBtn.setText("Back");
        backToMenuBtn.addActionListener(new BackToMenuListener());
        backToMenuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        diffpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diffpanel.add(difflevel);
        diffpanel.add(diff);

        buttonspanel.add(diffpanel);
        buttonspanel.add(gridspanel);
        buttonspanel.add(backToMenuBtn);
        settings.add(buttonspanel);
        settings.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    /*
    beállítja a nehézséget a kiválasztottra
     */
    private class DiffLevelSetter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
                nehezseg = diff.getSelectedItem().toString();
        }
    }
/*
beállítja a pályaméretet a kiválasztottra
 */
    public class GridNumberSetter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            gridnumber = grids.getSelectedItem().toString();
        }
    }


/*
visszalép a menübe, átadja a beállított értékeket.
 */
    private class BackToMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            menu.setDiff(nehezseg);
            menu.setGrids(gridnumber);
            menu.letrehoz();
            //menu.setVisible(true);
            settings.dispose();
        }
    }
}
