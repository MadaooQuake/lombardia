/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lombardia2014.Interface.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import lombardia2014.Interface.menu.migration.CheckFileToMigration;
import lombardia2014.Interface.menu.migration.MigrationFromOld;
import lombardia2014.generators.LombardiaLogger;

/**
 *
 * @author Domek
 */
public class AppMigration extends MenuElementsList {

    JPanel[] buttonPanels;
    JProgressBar progressBar;
    JLabel loadInfo;

    @Override
    public void generateGui() {
        formFrame.setSize(new Dimension(500, 300));
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setResizable(false);
        formFrame.setTitle("Wczytaj dane z poprzedniej aplikacji");

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(titleBorder);

        generatePanels(c);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    @Override
    public void generatePanels(GridBagConstraints c) {
        // create panels 
        buttonPanels = new JPanel[2];
        // panel for file chooser
        buttonPanels[0] = fillechooserButton();
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(buttonPanels[0], c);
        // panell for progress bar
        buttonPanels[1] = progressBar();
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(buttonPanels[1], c);

    }

    // filechooser panel
    private JPanel fillechooserButton() {
        JPanel FileChooserButton = new JPanel();

        JButton chooser = new JButton("Wybierz plik");
        chooser.setPreferredSize(new Dimension(180, 40));
        chooser.setFont(new Font("Dialog", Font.BOLD, 20));
        chooser.addActionListener(new OpenChooser());
        FileChooserButton.add(chooser);

        return FileChooserButton;
    }

    // progress bar panel
    private JPanel progressBar() {
        JPanel ProgresBarPanel = new JPanel(new GridBagLayout());

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(450, 20));
        progressBar.setToolTipText("Ładwanie danych, prosze czekać...");
        c.gridx = 0;
        c.gridy = 0;
        ProgresBarPanel.add(progressBar, c);
        loadInfo = new JLabel("Postęp ładowania danych: Dane nie są ładowane");
        c.gridx = 0;
        c.gridy = 1;
        ProgresBarPanel.add(loadInfo, c);
        return ProgresBarPanel;
    }

    private class OpenChooser implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter dbFileType
                    = new FileNameExtensionFilter("xls pliki (*.xls)", "xls");
            chooser.setFileFilter(dbFileType);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileFromOld = chooser.getSelectedFile().toString();
                // check file
                boolean fileOk = checkFileStructure(fileFromOld);
                if (fileOk == true) {
                    try {
                        // open file
                        MigrationFromOld migrateFile = new MigrationFromOld(fileFromOld);
                        migrateFile.readMainElements();
                    } catch (IOException ex) {
                        LombardiaLogger startLogging = new LombardiaLogger();
                        String text = startLogging.preparePattern("Error", ex.getMessage()
                                + "\n" + Arrays.toString(ex.getStackTrace()));
                        startLogging.logToFile(text);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Podany plik nie jest poprawny",
                            "Zły plik!",
                            JOptionPane.ERROR_MESSAGE);
                    formFrame.dispose();
                    loadInfo.setText("Postęp ładowania danych: "
                            + "Plik jest niepoprawny !!");
                }
            }
        }

        private boolean checkFileStructure(String file) {
            boolean ok;
            loadInfo.setText("Postęp ładowania danych: "
                    + "Sprawdzanie danych w pliku");

            CheckFileToMigration checkingfile = new CheckFileToMigration(file);
            ok = checkingfile.checkElements();

            return ok;
        }

    }

}
