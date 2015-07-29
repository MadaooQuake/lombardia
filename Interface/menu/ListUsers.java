/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import lombardia2014.dataBaseInterface.UserDB;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public final class ListUsers extends MenuElementsList {

    JButton addUser = null;
    JButton deleteUser = null;
    JButton changePassword = null;
    JPanel[] buttonPanels;
    TitledBorder title;
    JTable litsUsers = null;
    DefaultTableModel model;
    JScrollPane scrollPane = null;
    GridBagConstraints[] cTab = new GridBagConstraints[2];
    QueryDB setQuerry = null;
    ResultSet queryResult = null;
    Connection conDB = null;
    Statement stmt = null;
    int selectRow = -1;
    AddUser newUser = null;
    ChangePassword newPassword = null;
    SwingWorker worker = null;
    boolean priv = false;
    UserDB userQuery = new UserDB();

    public ListUsers(boolean priv_) {
        priv = priv_;
        generateGui();
    }

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(400, 500));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Lista użytkowników");

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {

        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        buttonPanels = new JPanel[2];

        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Przyciski akcji");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);

        // to do... method to create elements of defined panel above
        createButtons();

        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(buttonPanels[0], c);

        // to do... method to create elements of defined panel above
        createTable();
    }

    // create buttons to new actions for swlwcted ot not selected users
    private void createButtons() {
        //add new user
        if (priv == false) {
            cTab[0] = new GridBagConstraints();
            addUser = new JButton();
            addUser.setText("Nowy");
            addUser.addActionListener(new AddUserButton());
            addUser.setFont(new Font("Dialog", Font.BOLD, 20));
            addUser.setPreferredSize(new Dimension(150, 30));
            cTab[0].insets = new Insets(10, 10, 0, 0);
            cTab[0].gridx = 0;
            cTab[0].gridy = 0;
            cTab[0].ipadx = 0;
            cTab[0].ipady = 0;
            buttonPanels[0].add(addUser, cTab[0]);
            // change password for user
            changePassword = new JButton();
            changePassword.setText("Zmien hasło");
            changePassword.addActionListener(new ChangePassButton());
            changePassword.setPreferredSize(new Dimension(150, 30));
            changePassword.setFont(new Font("Dialog", Font.BOLD, 20));
            cTab[0].insets = new Insets(10, 10, 0, 0);
            cTab[0].gridx = 1;
            cTab[0].gridy = 0;
            cTab[0].ipadx = 0;
            cTab[0].ipady = 0;
            buttonPanels[0].add(changePassword, cTab[0]);
            // delete selected user
            deleteUser = new JButton();
            deleteUser.setText("Usuń");
            deleteUser.addActionListener(new DeleteUserButton());
            deleteUser.setPreferredSize(new Dimension(150, 30));
            deleteUser.setFont(new Font("Dialog", Font.BOLD, 20));
            cTab[0].insets = new Insets(10, 40, 0, 0);
            cTab[0].gridx = 2;
            cTab[0].gridy = 0;
            cTab[0].ipadx = 0;
            cTab[0].ipady = 0;
            buttonPanels[0].add(deleteUser, cTab[0]);
        } else {
            cTab[0] = new GridBagConstraints();
            addUser = new JButton();
            addUser.setText("Zmień uprawnienia");
            addUser.addActionListener(new ChangePrivileges());
            addUser.setFont(new Font("Dialog", Font.BOLD, 20));
            addUser.setPreferredSize(new Dimension(400, 30));
            cTab[0].insets = new Insets(10, 70, 0, 70);
            cTab[0].gridx = 0;
            cTab[0].gridy = 0;
            cTab[0].ipadx = 0;
            cTab[0].ipady = 0;
            buttonPanels[0].add(addUser, cTab[0]);
        }
    }

    // create table with users
    private void createTable() {
        // create table
        cTab[1] = new GridBagConstraints();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        model.addColumn("Imię");
        model.addColumn("Nazwisko");
        model.addColumn("Uprawnienia");
        model.addTableModelListener(litsUsers);

        getUsers();
        litsUsers = new JTable(model);

        litsUsers.addMouseListener(new GetSelectRow());
        litsUsers.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(litsUsers);
        litsUsers.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(300, 200));

        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].insets = new Insets(10, 10, 10, 10);
        cTab[1].gridx = 0;
        cTab[1].gridy = 1;
        cTab[1].ipadx = 10;
        cTab[1].ipady = 350;
        mainPanel.add(scrollPane, cTab[1]);
    }

    // get users list from database
    public void getUsers() {
        List<HashMap<String, String>> users = userQuery.getAllUsers();

        for (HashMap<String, String> user : users) {
            Object[] data = {user.get("NAME"),
                user.get("SURNAME"),
                user.get("POSITION")};
            model.addRow(data);

        }

    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            JTable target = (JTable) e.getSource();

            selectRow = target.getSelectedRow();
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            //nothing to do, but i must create this method :(
        }

    }

    // Buttons actions
    private class AddUserButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            newUser = new AddUser();
            newUser.generateGui();
            worker = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    while (newUser.getStatusNewUser() == false) {
                        Thread.sleep(300);
                    }
                    model.addRow(newUser.getUser());
                    return null;
                }
            };
            worker.execute();
        }
    }

    private class ChangePassButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectRow >= 0) {

                int idUser = userQuery.getIDUser((String) model.getValueAt(selectRow, 0), (String) model.getValueAt(selectRow, 1));

                newPassword = new ChangePassword(idUser);
                newPassword.generateGui();
                selectRow = -1;
            } else {
                JOptionPane.showMessageDialog(formFrame,
                        "Nie wybrano użytkownika",
                        "Nierawidłowa akcja!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private class DeleteUserButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userPosition = (String) model.getValueAt(selectRow, 2);
            if (userPosition.equals("Administrator")) {
                JOptionPane.showMessageDialog(formFrame,
                        "Adimnstratora nie można usunąć",
                        "Nierawidłowa akcja!",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                int selectedOption = JOptionPane.showConfirmDialog(formFrame,
                        "Czy na pewno cheszu usunąć tego użytkownika ?",
                        "uwaga unuwanie!",
                        JOptionPane.YES_NO_OPTION);
                if (selectedOption == JOptionPane.YES_OPTION) {

                    userQuery.deleteUser((String) model.getValueAt(selectRow, 0), (String) model.getValueAt(selectRow, 1));
                }
            }

        }

    }

    private class ChangePrivileges implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String userPosition = (String) model.getValueAt(selectRow, 2);
            if ((selectRow >= 0) && (!userPosition.equals("Administrator"))) {

                int idUser = userQuery.getIDUser((String) model.getValueAt(selectRow, 0), (String) model.getValueAt(selectRow, 1));
               
                newUser = new AddUser(true, idUser);
                newUser.generateGui();
                worker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        while (newUser.getStatusNewUser() == false) {
                            Thread.sleep(300);
                        }
                        model.setRowCount(0);
                        getUsers();
                        repaint();

                        return null;
                    }
                };
                worker.execute();
            } else {
                JOptionPane.showMessageDialog(formFrame,
                        "Nie wybrano użytkownika lub "
                        + "tego użytkownika nie można edytować",
                        "Nierawidłowa akcja!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
