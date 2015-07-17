/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.forms;

import lombardia2014.dataBaseInterface.QueryDB;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import static lombardia2014.Interface.MainInterface.userName;
import static lombardia2014.Interface.MainInterface.userSurename;
import lombardia2014.dataBaseInterface.NoticesDBQueries;

/**
 *
 * @author Mateusz
 */
public class Notices extends Forms {

    //Strings
    String frameTitle = "Uwagi";
    String tablePanelTitle = "Uwagi";
    String Title;
    String Content;
    int ID;
    String User = userName + userSurename;

    int selectRow = -1;
    //Frames, Panels and Table
    JFrame noticesFrame = new JFrame();
    JScrollPane scrollPane = null;
    JTable listNotices = null;
    DefaultTableModel model;
    JPanel buttonPanel;
    JPanel tablePanel;
    //Buttons
    JButton cancel = null;
    JButton add = null;
    JButton delete = null;

    //Database
    QueryDB setQuerry = null;
    Connection conDB = null;
    Statement stmt = null;
    SwingWorker worker = null;
    NoticesDBQueries getNOticesQuery = new NoticesDBQueries();

    //Date and Time usage
    Date now = new Date();

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    String date = DATE_FORMAT.format(now);

    @Override
    public void generatePanels(GridBagConstraints c) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        listNotices = new JTable(model);
        model.addColumn("Identyfikator");
        model.addColumn("Tytuł");
        model.addColumn("Uwaga");
        model.addColumn("Autor");
        model.addColumn("Data");
        model.addTableModelListener(listNotices);
        getNotices();

        listNotices.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(listNotices);
        listNotices.setFillsViewportHeight(true);

        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 2;
        c.insets = new Insets(10, 10, 10, 10);
        c.ipadx = 650;
        c.ipady = 600;
        listNotices.setPreferredSize(new Dimension(650, 600));

        scrollPane.setPreferredSize(new Dimension(650, 600));
        scrollPane.setVisible(true);

        tablePanel.add(scrollPane);
    }

    private void generatePanels2() {
        GridBagConstraints c2 = new GridBagConstraints();
        cancel = new JButton();
        cancel.setText("Anuluj");
        cancel.addActionListener(new CloseForm());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 1;
        c2.gridy = 6;

        add = new JButton();
        add.setText("Dodaj");
        add.addActionListener(new addNotice());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 0;
        c2.gridy = 5;

        delete = new JButton();
        delete.setText("Usuń");
        delete.addActionListener(new deleteNotice());
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.insets = new Insets(5, 5, 5, 5);
        c2.gridwidth = 1;
        c2.gridx = 2;
        c2.gridy = 5;

        mainPanel.add(tablePanel, c);
        buttonPanel.add(add, c2);
        buttonPanel.add(delete, c2);
        buttonPanel.add(cancel, c2);
        mainPanel.add(buttonPanel, c2);
    }

    @Override
    public void generateGui() {

        formFrame.setSize(800, 800);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle(frameTitle);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(800, 800));
        titleBorder = BorderFactory.createTitledBorder(blackline, frameTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        mainPanel.setBorder(titleBorder);

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(100, 700));

        tablePanel = new JPanel();
        tablePanel.setPreferredSize(new Dimension(600, 600));
        titleBorder = BorderFactory.createTitledBorder(blackline, tablePanelTitle);
        titleBorder.setTitleJustification(TitledBorder.RIGHT);
        titleBorder.setBorder(blackline);
        tablePanel.setBorder(titleBorder);

        generatePanels(c);
        generatePanels2();
        formFrame.add(mainPanel);

        formFrame.setVisible(true);

    }

    public class CloseForm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            formFrame.dispose();
        }

    }

    public void getNotices() {
        List<HashMap<String, String>> notices = getNOticesQuery.getNotices();

        for (HashMap<String, String> notice : notices) {
            Object[] data = {
                notice.get("ID"),
                notice.get("TITLE"),
                notice.get("CONTENT"),
                notice.get("NAME"),
                notice.get("DATE")};
            model.addRow(data);
        }
    }

    private void updateNoticeTable() {
        model.removeTableModelListener(listNotices);
        model.setRowCount(0);
        getNotices();
        model.addTableModelListener(listNotices);
        formFrame.repaint();
    }

    public class deleteNotice implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            getNOticesQuery.deleteNotices(ID);
        }
    }

    public class addNotice implements ActionListener {

        NewNotices noticeForm = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            noticeForm = new NewNotices();
            noticeForm.generateGui();
            worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    while (true) {
                        if (noticeForm.isClose() == true) {
                            updateNoticeTable();
                            break;
                        }
                        Thread.sleep(100);
                    }
                    return null;
                }

            };
            worker.execute();
        }
    }

    // actions for selected elements in table
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            selectRow = table.getSelectedRow();
            int row = table.rowAtPoint(p);

            ID = (int) listNotices.getModel().getValueAt(
                    listNotices.convertRowIndexToView(selectRow), 0);
            JTable target = (JTable) e.getSource();

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

}
