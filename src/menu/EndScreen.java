package menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
A játék végén közli a játékossal ki nyert
 */
public class EndScreen extends JFrame{
    private JFrame menu, endscreen;

    public EndScreen(JFrame menu){
        this.menu = menu;
    }

    public void end_of_game(String winner){
        endscreen = new JFrame("Game Over");
        endscreen.setDefaultCloseOperation(EXIT_ON_CLOSE);
        endscreen.setSize(500, 500);

        JLabel label = new JLabel();
        label.setText(winner + " wins.");
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(label);


        JButton return_to_menu = new JButton();
        return_to_menu.setText("Return to Menu");
        return_to_menu.addActionListener(new ReturnToMenu());
        JPanel rtmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rtmPanel.add(return_to_menu);

        endscreen.setLayout(new BorderLayout());
        endscreen.add(labelPanel, BorderLayout.CENTER);
        endscreen.add(rtmPanel, BorderLayout.SOUTH);

        endscreen.setVisible(true);
    }

    public class ReturnToMenu implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            menu.setVisible(true);
            endscreen.dispose();
        }
    }
}
