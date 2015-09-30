/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import lombardia2014.core.ConfigRead;
import lombardia2014.Interface.forms.ItemForm;
import lombardia2014.dataBaseInterface.MainDBQuierues;
import lombardia2014.dataBaseInterface.UserOperations;

/**
 *
 * @author marcin
 */
public final class ObjectList extends javax.swing.JPanel {

    JPanel mainPanel;
    TitledBorder title;
    Border blackline;
    JPanel[] buttonPanels;
    JTextField searchText = null;
    JButton buttonSearch = null;
    JTable objectList = null;
    DefaultTableModel model;
    Object[] data = null;
    int id = 0;
    int selectRow = -1;
    SwingWorker worker = null;
    ItemForm sellForm = null;
    ConfigRead readVat = new ConfigRead();
    float vat = 0, value = 0;
    UserOperations sniffOperations = null;
    MainDBQuierues getQuery = new MainDBQuierues();

    public ObjectList(UserOperations sniffOperations_) {
        sniffOperations = sniffOperations_;
        readVat.readFile();
        vat = 1 + readVat.getVat();
        blackline = BorderFactory.createLineBorder(Color.black);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(860, 700);
        GridBagConstraints c = new GridBagConstraints();

        buttonPanels = new JPanel[2];

        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Wyszukiwarka po modelu lub po marce");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);

        // to do... method to create elements of defined panel above
        createSearch(c);

        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[0], c);

        // secound panel
        buttonPanels[1] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Lista depozytów");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[1].setBorder(title);

        // to do... method to create elements of defined panel above
        createTable(c);

        c.gridheight = 1;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        mainPanel.add(buttonPanels[1], c);

        this.add(mainPanel);
        setVisible(true);
    }

    /**
     * @see create fields and button to search customers from database
     * @param c
     */
    public void createSearch(GridBagConstraints c) {
        searchText = new JTextField();
        searchText.setToolTipText("Wyszukaj depozyt");
        searchText.setPreferredSize(new Dimension(250, 40));
        searchText.setFont(new Font("Dialog", Font.BOLD, 20));
        searchText.addActionListener(new ItemFilter());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 230, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        buttonPanels[0].add(searchText, c);

        buttonSearch = new JButton();
        buttonSearch.setText("Szukaj");
        buttonSearch.setPreferredSize(new Dimension(150, 40));
        buttonSearch.addActionListener(new ItemFilter());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 0, 10, 230);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[0].add(buttonSearch, c);

    }

    /**
     * @see create fields and button to search customers from database
     * @param c
     */
    public void createTable(GridBagConstraints c) {

        // create table
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  //This causes all cells to be not editable
            }
        };
        model.addColumn("ID");
        model.addColumn("Kategoria");
        model.addColumn("Model");
        model.addColumn("Marka");
        model.addColumn("Typ");
        model.addColumn("Waga");
        model.addColumn("Wartość (Netto)");
        model.addColumn("Wartość (Brutto)");
        model.addColumn("IMEI");
        model.addColumn("Uwagi");
        model.addColumn("Umowa");

        itemsTable();

        objectList = new JTable(model);
        objectList.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(objectList);
        objectList.setFillsViewportHeight(true);
        objectList.addMouseListener(new GetSelectRow());
        scrollPane.setPreferredSize(new Dimension(780, 450));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 1;
        c.gridy = 0;
        buttonPanels[1].add(scrollPane, c);

    }

    /**
     * @see load from db items which kategories
     */
    public void itemsTable() {
        List<HashMap<String, String>> IteList = getQuery.getAllItems();

        for (Map<String, String> item : IteList) {
            if (item.get("VALUE") != null) {
                value = Float.parseFloat(item.get("VALUE"));
            }
            Object[] data = {
                item.get("ID_ITEM"),
                item.get("NAME"),
                item.get("MODEL") == null ? "" : item.get("MODEL"),
                item.get("BAND") == null ? "" : item.get("BAND"),
                item.get("TYPE") == null ? "" : item.get("TYPE"),
                item.get("WEIGHT") == null ? "" : item.get("WEIGHT"),
                item.get("VALUE") == null ? "" : item.get("VALUE"),
                calcBrutto(),
                item.get("IMEI") == null ? "" : item.get("IMEI"),
                item.get("ATENCION") == null ? "" : item.get("ATENCION"),
                item.get("ID_AGREEMENTS") == null ? "" : item.get("ID_AGREEMENTS"),};
            model.addRow(data);

        }
    }

    /**
     * @see calc brutto
     */
    private float calcBrutto() {

        float brutto = 0;
        if (value >= 0) {
            brutto = value * vat;
            brutto *= 100;
            brutto = Math.round(brutto);
            brutto /= 100;
        }
        return brutto;
    }

    /**
     * @see update object list
     */
    public void updateItemTable() {
        // clear model 
        model.setRowCount(0);
        itemsTable();
        repaint();
    }

    /**
     * @return @see this method select item id
     */
    private class GetSelectRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JTable table = (JTable) e.getSource();
            Point p = e.getPoint();

            if (e.getClickCount() == 2) {
                selectRow = table.getSelectedRow();
                int row = table.rowAtPoint(p);
                boolean agreement = objectList.getModel().getValueAt(
                        objectList.convertRowIndexToView(selectRow), 10
                ).equals("");

                id = Integer.parseInt((String) objectList.getModel().getValueAt(
                        objectList.convertRowIndexToView(selectRow), 0));

                sellForm = new ItemForm(id, agreement);
                sellForm.generateGui();

                worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        while (true) {
                            if (sellForm.isClose() == true) {
                                Thread.sleep(100);
                                sniffOperations.saveOperations(
                                        "Produkt sprzedano za:"
                                        + sellForm.getAddRemoValue());
                                updateItemTable();
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

        @Override
        public void mousePressed(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // do nothing
        }

    }

    /**
     * @see this class implement filter option
     */
    private class ItemFilter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchText.getText().length() > 2) {
                // method
                searchItem();
            } else {
                updateItemTable();
            }
        }

        private void searchItem() {
            model.setRowCount(0);
            List<HashMap<String, String>> IteList = getQuery.searchItem(searchText.getText());

            for (Map<String, String> item : IteList) {
                if (item.get("VALUE") != null) {
                    value = Float.parseFloat(item.get("VALUE"));
                }
                Object[] data = {
                    item.get("ID_ITEM"),
                    item.get("NAME"),
                    item.get("MODEL") == null ? "" : item.get("MODEL"),
                    item.get("BAND") == null ? "" : item.get("BAND"),
                    item.get("TYPE") == null ? "" : item.get("TYPE"),
                    item.get("WEIGHT") == null ? "" : item.get("WEIGHT"),
                    item.get("VALUE") == null ? "" : item.get("VALUE"),
                    calcBrutto(),
                    item.get("IMEI") == null ? "" : item.get("IMEI"),
                    item.get("ATENCION") == null ? "" : item.get("ATENCION"),
                    item.get("ID_AGREEMENTS") == null ? "" : item.get("ID_AGREEMENTS"),};
                model.addRow(data);

            }

        }
    }
}
