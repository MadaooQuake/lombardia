/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Domek
 */
abstract public class Forms {

    public JPanel mainPanel;
    public Border blackline = BorderFactory.createLineBorder(Color.black);
    public GridBagConstraints c = new GridBagConstraints();
    public JFrame formFrame = new JFrame();
    public TitledBorder titleBorder;

    /**
     *
     */
    public abstract void generateGui();

    public abstract void generatePanels(GridBagConstraints c);

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }

    }
    
    public class SetMoney implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
           
        }
        
    }

}
