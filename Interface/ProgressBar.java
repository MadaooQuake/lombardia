/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;

import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 *
 * @author Domek
 */
public class ProgressBar {
    JFrame frame = null;
    JProgressBar pbar;
    JButton ok = null;
    final int MY_MINIMUM = 0;
    int MY_MAXIMUM = 00; // dafault value
    int value =0;

    public ProgressBar(int MY_MAXIMUM) {
        pbar = new JProgressBar();
        pbar.setMinimum(MY_MINIMUM);
        pbar.setMaximum(MY_MAXIMUM);
        generateProgressBar();
    }

    
    public void generateProgressBar() {
        frame = new JFrame("Ładowanie...");
        Container content = frame.getContentPane();
        Border border = BorderFactory.createTitledBorder("Wykonywanie operacji, prosze czekać...");
        pbar.setBorder(border);
        content.add(pbar, BorderLayout.NORTH);
        ok = new JButton("OK");
        ok.addActionListener(new CloseProgress());
        ok.setEnabled(false);
        content.add(ok, BorderLayout.AFTER_LINE_ENDS);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
    
    public void activeButton() {
        ok.setEnabled(true);
    }
    

    public void updateBar() {
        pbar.setValue(++value);
    }
    
    public void closePbar() {
        frame.dispose();
    }
    
    private class CloseProgress implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
             closePbar();
        }
        
    }

}
