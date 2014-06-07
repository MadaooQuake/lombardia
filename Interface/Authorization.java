/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lombardia2014.Interface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author marcin
 * 
 * @see clas where user login to appliaction
 */
public class Authorization {
    // examples
    String user = "test";
    String password = "pass1";
    // rest elements
    JFrame loginFrame = new JFrame();
    GridBagConstraints c = new GridBagConstraints();
    JPanel loginPanel;
    // fields
    JTextField loginField = null;
    JPasswordField passField = null;
    // text in panel
    JLabel title = null;
    JLabel login = null;
    JLabel passwordLabel = null;
    // buttons
    JButton logIn = null;
    MainInterface mainGUI = null;
    
    /**
     * @see  Create panel
     */
    public void createPanel() {
        // frame configuration
        loginFrame.setSize(300, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setTitle("Lombardia v1.0.0");
        
        loginPanel = new JPanel(new GridBagLayout());
        
        loginPanel.setSize(new Dimension(300, 300));
        
        loginPanel.add(generateTitle(c),c);
        loginPanel.add(geneticloginText(c),c);
        loginPanel.add(genericLoginfield(c),c);
        loginPanel.add(generatePassword(c),c);
        loginPanel.add(genericPasswordField(c),c);
        loginPanel.add(createButtonLogIn(c),c);

        //now time to gridbadLayouth
        loginFrame.add(loginPanel);
        loginPanel.setVisible(true);
        loginFrame.setVisible(true);
        
    }
    
    /**
     * @see method to generate main test of this panel
     * @param c
     * @return title of this panel 
     */
    
    public JLabel generateTitle(GridBagConstraints c) {
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 20, 30, 20);
        c.gridwidth = 2;
        title = new JLabel();
        title.setText("Panel Logowania");
        title.setFont(new Font("Dialog", Font.BOLD, 20));
        return title;
    }
    
    /**
     * 
     * @param c
     * @return left text 
     */
    
    public JLabel geneticloginText(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        login = new JLabel();
        login.setText("Podaj login:");
        return login;
    }
    
    /**
     * 
     * @param c
     * @return field where we fill login 
     */
    
    public JTextField genericLoginfield(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        loginField = new JTextField();
        loginField.setSize(10, 20);
        return loginField;
    }
    
    /**
     * 
     * @param c
     * @return label on the left, which he informating about what this field on thr right
     */
    
    public JLabel generatePassword(GridBagConstraints c) {
       c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        passwordLabel = new JLabel();
        passwordLabel.setText("Podaj hasło:");
        return passwordLabel;
    }
    
    /**
     * 
     * @param c
     * @return fields to fill password 
     */
    
    public JPasswordField genericPasswordField(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        passField = new JPasswordField();
        passField.setSize(10, 20);
        return passField;
    }
    
    /**
     * @see create button
     * @param c
     * @return button 
     */
    
    public JButton createButtonLogIn(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        logIn = new JButton();
        logIn.addActionListener(new CheckAuthorization());
        logIn.setText("Zaloguj się");
        return logIn;
    }
    
    /**
     * class 
     */
    class CheckAuthorization implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // check login
            int ok = 0;
            if(loginField.getText().equals(user)) {
                ok++;
            } else {
               JOptionPane.showMessageDialog(loginFrame,
                "Zostało podane nieprawidłowy login",
                "Autoryzacja nie udana!",
                JOptionPane.ERROR_MESSAGE);
            }
            char[] passDefine = passField.getPassword();
            
            if(!String.valueOf(passDefine).equals(password)) {                
                JOptionPane.showMessageDialog(loginFrame,
                        "Zostało podane nieprawidłowe hasło",
                        "Autoryzacja nie udana!",
                        JOptionPane.ERROR_MESSAGE);
            } else {                
                ok++;
            }
            
            if(ok == 2) {
                 mainGUI = new MainInterface();
                 loginFrame.setVisible(false);
                 loginFrame.dispose();
            }
        }
        
    }
    
}
