package lombardia2014.Interface.menu;

import com.itextpdf.text.DocumentException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import lombardia2014.dataBaseInterface.UserOperations;
import lombardia2014.generators.LombardiaLogger;
import lombardia2014.generators.PDFCreator;

/**
 *
 * @author Domek
 */
public class OperationList extends MenuElementsList {

    JPanel[] buttonPanels = null;
    JLabel dateRange = null;
    JList<Object> rangeOption = null;
    JButton deleteOperation = null, printOperations = null;
    TitledBorder title = null;
    GridBagConstraints[] cTab = new GridBagConstraints[2];
    JTable litsOperations = null;
    DefaultTableModel model = null;
    JScrollPane scrollPane = null;
    int selectRow = -1;
    UserOperations operationList = null;
    List<String[]> operations = new ArrayList<>();
    Date curretDate = null;
    SimpleDateFormat ft = new SimpleDateFormat("dd.MM.YYYY HH:mm");
    String[] headers = {"Użytkownik", "Data", "Operacja"};
    float[] headersWidth = {3.0f, 5.0f, 5.0f};
    String formname = "Operacje";
    String outputFileName = "Operacje.pdf";
    PDFCreator pdf = null;
    int windowWidth = 860;
    int windowHeigth = 500;
    int rowsPerPage = 40;

    public OperationList() {
        operationList = new UserOperations();
        operations.addAll(operationList.readOperation("Mięsiąc"));
        generateGui();
    }

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(500, 500));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Historia operacji");
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);
        rangeOption.setSelectedIndex(0);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);

    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        blackline = BorderFactory.createLineBorder(Color.black);
        c = new GridBagConstraints();

        buttonPanels = new JPanel[2];

        buttonPanels[0] = new JPanel(new GridBagLayout());
        title = BorderFactory.createTitledBorder(blackline, "Przyciski akcji");
        title.setTitleJustification(TitledBorder.RIGHT);
        title.setBorder(blackline);
        buttonPanels[0].setBorder(title);

        createButtons();

        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 80;
        mainPanel.add(buttonPanels[0], c);

        createTable();
    }

    // create buttons to new actions for swlwcted ot not selected users
    private void createButtons() {
        cTab[0] = new GridBagConstraints();
        cTab[0].insets = new Insets(5, 5, 5, 5);

        dateRange = new JLabel("Okres:");
        dateRange.setFont(new Font("Dialog", Font.BOLD, 20));
        cTab[0].ipadx = 0;
        cTab[0].ipady = 0;
        buttonPanels[0].add(dateRange, cTab[0]);

        Object[] elementList = {"Mięsiąc", "Rok"};
        rangeOption = new JList<>(elementList);
        rangeOption.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        rangeOption.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        rangeOption.addListSelectionListener(new ChangeDateRange());
        rangeOption.setVisibleRowCount(1);
        rangeOption.setPreferredSize(new Dimension(100, 30));

        cTab[0].ipadx = 1;
        cTab[0].ipady = 0;
        buttonPanels[0].add(rangeOption, cTab[0]);

        cTab[0].insets = new Insets(5, 15, 5, 5);
        printOperations = new JButton();
        printOperations.setText("Drukuj");
        printOperations.addActionListener(new GenerateDocument());
        printOperations.setFont(new Font("Dialog", Font.BOLD, 20));
        printOperations.setPreferredSize(new Dimension(200, 30));

        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 2;
        cTab[0].gridy = 0;
        buttonPanels[0].add(printOperations, cTab[0]);

        cTab[0].insets = new Insets(5, 15, 5, 5);
        deleteOperation = new JButton();
        deleteOperation.setText("Usuń");
        deleteOperation.addActionListener(new deteleElement());
        deleteOperation.setFont(new Font("Dialog", Font.BOLD, 20));
        deleteOperation.setPreferredSize(new Dimension(200, 30));

        cTab[0].fill = GridBagConstraints.HORIZONTAL;
        cTab[0].gridx = 3;
        cTab[0].gridy = 0;
        buttonPanels[0].add(deleteOperation, cTab[0]);
    }

    private void createTable() {
        cTab[1] = new GridBagConstraints();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//This causes all cells to be not editable
            }
        };
        model.addColumn("Użytkownik");
        model.addColumn("Data");
        model.addColumn("Operacja");
        model.addTableModelListener(litsOperations);

        //get operations
        litsOperations = new JTable(model);

        getOperations();

        litsOperations.addMouseListener(new GetSelectRow());
        litsOperations.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(litsOperations);
        litsOperations.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(300, 200));

        cTab[1].fill = GridBagConstraints.HORIZONTAL;
        cTab[1].insets = new Insets(10, 10, 10, 10);
        cTab[1].gridx = 0;
        cTab[1].gridy = 1;
        cTab[1].ipadx = 100;
        cTab[1].ipady = 320;
        mainPanel.add(scrollPane, cTab[1]);

    }

    public void getOperations() {
        model.setRowCount(0);

        for (String[] operation : operations) {
            model.addRow(operation);
        }

        revalidate();
        repaint();
    }

    // class for action listner :D
    private class deteleElement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectRow >= 0) {
                // to do
                operationList.removeOperation(
                        (String) model.getValueAt(selectRow, 0),
                        (String) model.getValueAt(selectRow, 1),
                        (String) model.getValueAt(selectRow, 2));
                selectRow = -1;
                operations.clear();
                operations.addAll(operationList.readOperation(
                        rangeOption.getSelectedValue().toString()));
                getOperations();
            } else {
                JOptionPane.showMessageDialog(formFrame,
                        "Nie wybrano operacji z listy",
                        "Nierawidłowa akcja!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ChangeDateRange implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            operations.clear();
            operations.addAll(operationList.readOperation(
                    rangeOption.getSelectedValue().toString()));
            getOperations();
        }

    }

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

    private class GenerateDocument implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                pdf = new PDFCreator(outputFileName, formname);
                pdf.SetLandscapeView();
                pdf.SetRowsPerPage(rowsPerPage);
                pdf.CreatePDF(model, headers, headersWidth);
                
                JOptionPane.showMessageDialog(null, "Operacje PDF został wygenerowany.",
                        "Generowanie PDF", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | DocumentException ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
                JOptionPane.showMessageDialog(null, "Błąd podczas generowania operacji.", 
                        "Generowanie PDF", JOptionPane.ERROR_MESSAGE);
            }
            
             try {
                String dir = System.getProperty("user.dir");
                
                File dirAgre = new File(dir);

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(dirAgre);
                }
            } catch (Exception ex) {
                LombardiaLogger startLogging = new LombardiaLogger();
                String text = startLogging.preparePattern("Error", ex.getMessage()
                    + "\n" + Arrays.toString(ex.getStackTrace()));
                startLogging.logToFile(text);
           }
        }

    }

}
