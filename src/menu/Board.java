package menu;

import java.io.*;
import java.util.ArrayList;
/*
a pályát tároló osztály
 */
public class Board implements Serializable {
    private ArrayList<ArrayList<Field>> fields;     //mezők a pályán mártixban tárolva
    private ArrayList<Ship> ships;                  //pályán lévő hajók
    private int remaining_ships;                    //még játékban lévő hajók száma

    public Board(Board brd){
        this.fields = brd.fields;
        this.ships = brd.ships;
        this.remaining_ships = brd.remaining_ships;
    }
    public Board(){
        fields = new ArrayList<>();
        ships = new ArrayList<>();
    }

    public ArrayList<ArrayList<Field>> getfields(){
        return fields;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public int getRemaining_ships() {
        return remaining_ships;
    }

    public void setRemaining_ships(int value){
        remaining_ships = value;
    }

    //ha egy hajó elsüllyed csökken ezt meghívva lehet csökkenteni a megmaradt hajók értékét
    public void shipSunk(){
        remaining_ships = remaining_ships-1;
    }


/*
fájlba menti a pályát
 */
    public void save(String filename) throws FileNotFoundException, IOException {
        FileOutputStream f = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(this);
        out.close();
    }
}
