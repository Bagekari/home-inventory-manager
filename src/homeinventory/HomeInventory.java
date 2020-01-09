package homeinventory;

import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HomeInventory extends JFrame{
    // ToolBar
    JToolBar inventoryToolBar = new JToolBar();
    JButton newButton = new JButton();
    JButton deleteButton = new JButton();
    JButton saveButton = new JButton();
    JButton previousButton = new JButton();
    JButton nextButton = new JButton();
    JButton printButton = new JButton();
    JButton cancelButton = new JButton();
    JButton exitButton = new JButton();
    JButton openButton = new JButton();
    // Frame
    JLabel itemLabel = new JLabel();
    JTextField itemTextField = new JTextField();
    JLabel locationLabel = new JLabel();
    JComboBox<Object> locationComboBox = new JComboBox<>();
    JCheckBox markedCheckBox = new JCheckBox();
    JLabel serialLabel = new JLabel();
    JTextField serialTextField = new JTextField();
    JLabel priceLabel = new JLabel();
    JTextField priceTextField = new JTextField();
    JLabel dateLabel = new JLabel();
    JDateChooser dateChooser = new JDateChooser();
    JLabel storeLabel = new JLabel();
    JTextField storeTextField = new JTextField();
    JLabel noteLabel = new JLabel();
    JTextField noteTextField = new JTextField();
    JLabel photoLabel = new JLabel();
    static JTextArea photoTextArea = new JTextArea();
    JButton photoButton = new JButton();
    JPanel searchPanel = new JPanel();
    JButton[] searchButtons = new JButton[26];
    
    PhotoPanel photoPanel = new PhotoPanel();
    
    static final int MAX = 300;
    static int numberEntries;
    int currentEntry;
    static InventoryItem[] myInventory = new InventoryItem[MAX];
    
    static final int ENTRIES_PER_PAGE = 2;
    static int lastPage;
    
    public static void main(String[] args) {
        // create frame
        new HomeInventory().show();
    }
    
    public HomeInventory() {
        // frame constructor
        super.setTitle("Home Inventory Manager");
        super.setResizable(false);
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitForm(e);
            }
        });
        super.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints;
        
        inventoryToolBar.setFloatable(false);
        inventoryToolBar.setBackground(Color.BLUE);
        inventoryToolBar.setOrientation(SwingConstants.VERTICAL);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridheight = 8;
        gridConstraints.fill = GridBagConstraints.VERTICAL;
        super.getContentPane().add(inventoryToolBar, gridConstraints);
        
        inventoryToolBar.addSeparator();
        Dimension bSize = new Dimension(70, 50);
        
        newButton.setText("New");
        sizeButton(newButton, bSize);
        newButton.setFocusable(false);
        newButton.setToolTipText("Add New Item");
        newButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(newButton);
        newButton.addActionListener((ActionEvent e) -> {
            newButtonActionPerformed(e);
        });
        
        openButton.setText("Open");
        sizeButton(openButton, bSize);
        openButton.setFocusable(false);
        openButton.setToolTipText("Open a file");
        openButton.setHorizontalTextPosition(SwingConstants.CENTER);
        openButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(openButton);
        openButton.addActionListener((ActionEvent e) -> {
            openButtonActionPerformed(e);
        });
        
        deleteButton.setText("Delete");
        sizeButton(deleteButton, bSize);
        deleteButton.setFocusable(false);
        deleteButton.setToolTipText("Delete Current Item");
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(deleteButton);
        deleteButton.addActionListener((ActionEvent e) -> {
            deleteButtonActionPerformed(e);
        });
        
        saveButton.setText("Save");
        sizeButton(saveButton, bSize);
        saveButton.setFocusable(false);
        saveButton.setToolTipText("Save Current Item");
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(saveButton);
        saveButton.addActionListener((ActionEvent e) -> {
            saveButtonActionPerformed(e);
        });
        
        inventoryToolBar.addSeparator();
        
        previousButton.setText("Previous");
        sizeButton(previousButton, bSize);
        previousButton.setFocusable(false);
        previousButton.setToolTipText("Display Previous Item");
        previousButton.setHorizontalTextPosition(SwingConstants.CENTER);
        previousButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(previousButton);
        previousButton.addActionListener((ActionEvent e) -> {
            previousButtonActionPerformed(e);
        });
        
        nextButton.setText("Next");
        sizeButton(nextButton, bSize);
        nextButton.setFocusable(false);
        nextButton.setToolTipText("Display Next Item");
        nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
        nextButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(nextButton);
        nextButton.addActionListener((ActionEvent e) -> {
            nextButtonActionPerformed(e);
        });
        
        inventoryToolBar.addSeparator();
        
        printButton.setText("Print");
        sizeButton(printButton, bSize);
        printButton.setFocusable(false);
        printButton.setToolTipText("Print Inventory List");
        printButton.setHorizontalTextPosition(SwingConstants.CENTER);
        printButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(printButton);
        printButton.addActionListener((ActionEvent e) -> {
            printButtonActionPerformed(e);
        });
        
        cancelButton.setText("Cancel");
        sizeButton(cancelButton, bSize);
        cancelButton.setFocusable(false);
        cancelButton.setEnabled(false);
        cancelButton.setToolTipText("Back to Inventory");
        cancelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        inventoryToolBar.add(cancelButton);
        cancelButton.addActionListener((ActionEvent e) -> {
            cancelButtonActionPerformed(e);
        });
        
        exitButton.setText("Exit");
        sizeButton(exitButton, bSize);
        exitButton.setFocusable(false);
        exitButton.setToolTipText("Exit Program");
        inventoryToolBar.add(exitButton);
        exitButton.addActionListener((ActionEvent e) -> {
            exitButtonActionPerformed(e);
        });
        
        itemLabel.setText("Inventory Item");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(itemLabel, gridConstraints);
        
        itemTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(itemTextField, gridConstraints);
        itemTextField.addActionListener((ActionEvent e) -> {
            itemTextFieldActionPerformed(e);
        });
        itemTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                itemTextFieldKeyReleased(e);
            }
        });
        
        locationLabel.setText("Location");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(locationLabel, gridConstraints);
        
        locationComboBox.setPreferredSize(new Dimension(270, 25));
        locationComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        locationComboBox.setEditable(true);
        locationComboBox.setBackground(Color.WHITE);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 1;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(locationComboBox, gridConstraints);
        locationComboBox.addActionListener((ActionEvent e) -> {
            locationComboBoxActionPerformed(e);
        });
        
        markedCheckBox.setText("Marked?");
        markedCheckBox.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 5;
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(markedCheckBox, gridConstraints);
        
        serialLabel.setText("Serial Number");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 2;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(serialLabel, gridConstraints);
        
        serialTextField.setPreferredSize(new Dimension(270, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 2;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(serialTextField, gridConstraints);
        serialTextField.addActionListener((ActionEvent e) -> {
            serialTextFieldActionPerformed(e);
        });
        
        priceLabel.setText("Purchase Price");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(priceLabel, gridConstraints);
        
        priceTextField.setPreferredSize(new Dimension(160, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(priceTextField, gridConstraints);
        priceTextField.addActionListener((ActionEvent e) -> {
            priceTextFieldActionPerformed(e);
        });
        
        dateLabel.setText("Date Purchased");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 3;
        gridConstraints.insets = new Insets(10, 10, 0, 0);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(dateLabel, gridConstraints);
        
        dateChooser.setPreferredSize(new Dimension(120, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 5;
        gridConstraints.gridy = 3;
        gridConstraints.gridwidth = 2;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(dateChooser, gridConstraints);
        dateChooser.addPropertyChangeListener((PropertyChangeEvent e) -> {
            dateChooserPropertyChange(e);
        });
        
        storeLabel.setText("Store/Website");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 4;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(storeLabel, gridConstraints);
        
        storeTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 4;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(storeTextField, gridConstraints);
        storeTextField.addActionListener((ActionEvent e) -> {
            storeTextFieldActionPerformed(e);
        });
        
        noteLabel.setText("Note");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 5;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(noteLabel, gridConstraints);
        
        noteTextField.setPreferredSize(new Dimension(400, 25));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 5;
        gridConstraints.gridwidth = 5;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(noteTextField, gridConstraints);
        noteTextField.addActionListener((ActionEvent e) -> {
            noteTextFieldActionPerformed(e);
        });
        
        photoLabel.setText("Photo");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 6;
        gridConstraints.insets = new Insets(10, 10, 0, 10);
        gridConstraints.anchor = GridBagConstraints.EAST;
        super.getContentPane().add(photoLabel, gridConstraints);
        
        photoTextArea.setPreferredSize(new Dimension(350, 35));
        photoTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        photoTextArea.setEditable(false);
        photoTextArea.setLineWrap(true);
        photoTextArea.setWrapStyleWord(true);
        photoTextArea.setBackground(new Color(255, 255, 192));
        photoTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        photoTextArea.setFocusable(false);
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 2;
        gridConstraints.gridy = 6;
        gridConstraints.gridwidth = 4;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(photoTextArea, gridConstraints);
        
        photoButton.setText("...");
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 6;
        gridConstraints.gridy = 6;
        gridConstraints.insets = new Insets(10, 0, 0, 10);
        gridConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(photoButton, gridConstraints);
        photoButton.addActionListener((ActionEvent e) -> {
            photoButtonActionPerformed(e);
        });
        
        searchPanel.setPreferredSize(new Dimension(240, 160));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Item Search"));
        searchPanel.setLayout(new GridBagLayout());
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 1;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 0);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        super.getContentPane().add(searchPanel, gridConstraints);
        
        int x = 0, y = 0;
        // create and position 26 buttons
        for (int i = 0; i < 26; i++) {
            // create new button
            searchButtons[i] = new JButton();
            // set text property
            searchButtons[i].setText(String.valueOf((char)(65 + i)));
            searchButtons[i].setFont(new Font("Arial", Font.BOLD, 12));
            searchButtons[i].setMargin(new Insets(-10, -10, -10, -10));
            sizeButton(searchButtons[i], new Dimension(37, 27));
            searchButtons[i].setFocusable(false);
            searchButtons[i].setBackground(Color.YELLOW);
            gridConstraints = new GridBagConstraints();
            gridConstraints.gridx = x;
            gridConstraints.gridy = y;
            searchPanel.add(searchButtons[i], gridConstraints);
            // add method
            searchButtons[i].addActionListener((ActionEvent e) -> {
                searchButtonActionPerformed(e);
            });
            x++;
            // six buttons per row
            if (x % 6 == 0) {
                x = 0;
                y++;
            }
        }
        
        photoPanel.setPreferredSize(new Dimension(240, 160));
        gridConstraints = new GridBagConstraints();
        gridConstraints.gridx = 4;
        gridConstraints.gridy = 7;
        gridConstraints.gridwidth = 3;
        gridConstraints.insets = new Insets(10, 0, 10, 10);
        gridConstraints.anchor = GridBagConstraints.CENTER;
        super.getContentPane().add(photoPanel, gridConstraints);
        
        init();
        
        super.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        super.setBounds((int)(0.5 * (screenSize.width - super.getWidth())), (int)(0.5 * (screenSize.height - super.getHeight())), super.getWidth(), super.getHeight());
    }
    
    private void exitForm(WindowEvent e) {
        if (saveButton.isEnabled() && JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nAre you sure you want to exit?", "Exit Program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
            return;
        System.exit(0);
    }
    
    private void itemTextFieldKeyReleased(KeyEvent e) {
        saveButton.setEnabled(!itemTextField.getText().equals(""));
    }
    
    private void sizeButton(JButton b, Dimension d) {
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);
    }
    
    private void newButtonActionPerformed(ActionEvent e) {
        if (itemTextField.isEnabled())
            checkSave();
        reInit();
        blankValues();
        openButton.setEnabled(false);
    }
    
    private void reInit() {
        deleteButton.setEnabled(true);
        previousButton.setEnabled(true);
        nextButton.setEnabled(true);
        printButton.setEnabled(true);
        itemTextField.setEnabled(true);
        locationComboBox.setEnabled(true);
        markedCheckBox.setEnabled(true);
        serialTextField.setEnabled(true);
        priceTextField.setEnabled(true);
        storeTextField.setEnabled(true);
        noteTextField.setEnabled(true);
        photoTextArea.setEnabled(true);
        photoButton.setEnabled(true);
        searchPanel.setEnabled(true);
        photoPanel.setEnabled(true);
        dateChooser.setEnabled(true);
    }
    
    private void openButtonActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(new File("C:\\"));
        fileChooser.setDialogTitle("Open a file");
        fileChooser.setFileFilter(new FileTypeFilter(".txt", "Text File"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try(BufferedReader reader = new BufferedReader(new FileReader(file.getPath()))) {
                numberEntries = Integer.parseInt(reader.readLine().trim());
                if (numberEntries != 0) {
                    for (int i = 0; i < numberEntries; i++) {
                        reader.readLine();
                        myInventory[i] = new InventoryItem();
                        myInventory[i].description = reader.readLine().trim();
                        myInventory[i].location = reader.readLine().trim();
                        myInventory[i].serialNumber = reader.readLine().trim();
                        myInventory[i].marked = Boolean.parseBoolean(reader.readLine().trim());
                        myInventory[i].purchasePrice = reader.readLine().trim();
                        myInventory[i].purchaseDate = reader.readLine().trim();
                        myInventory[i].purchaseLocation = reader.readLine().trim();
                        myInventory[i].note = reader.readLine().trim();
                        myInventory[i].photoFile = reader.readLine().trim();
                    }
                }
                reader.readLine();
                // read in combo box elements
                int n = Integer.parseInt(reader.readLine().trim());
                for (int i = 0; i < n; i++) {
                    boolean flag = false;
                    String string = reader.readLine().trim();
                    for (int j = 0; j < locationComboBox.getItemCount(); j++)
                        if (string.equals("") || locationComboBox.getItemAt(j).toString().equals(string))
                            flag = true;
                    if (!flag)
                        locationComboBox.addItem(string);
                }
                if (reader != null)
                    reader.close();
                currentEntry = 1;
                showEntry(currentEntry);
            } catch(IOException ex) {
                numberEntries = 0;
                currentEntry = 0;
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            if (numberEntries == 0) {
                newButton.setEnabled(false);
                deleteButton.setEnabled(false);
                nextButton.setEnabled(false);
                previousButton.setEnabled(false);
                printButton.setEnabled(false);
            } else {
                newButton.setEnabled(true);
                deleteButton.setEnabled(true);
                saveButton.setEnabled(false);
                cancelButton.setEnabled(true);
                printButton.setEnabled(true);
                exitButton.setEnabled(true);
                openButton.setEnabled(false);
            }
        }
    }
    
    private void deleteButtonActionPerformed(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item?", "Delete Inventory Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
            return;
        deleteEntry(currentEntry);
        if (numberEntries == 0) {
            currentEntry = 0;
            blankValues();
        } else {
            currentEntry--;
            if (currentEntry == 0)
                currentEntry = 1;
            showEntry(currentEntry);
        }
    }
    
    private void saveButtonActionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(new File("C:\\"));
        fileChooser.setDialogTitle("Save a file");
        fileChooser.setFileFilter(new FileTypeFilter(".txt", "Text File"));
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            // capitalize first letter
            String s = itemTextField.getText();
            itemTextField.setText(s.substring(0, 1).toUpperCase() + s.substring(1));
            numberEntries++;
            // determine new current entry location based on description
            currentEntry = 1;
            if (numberEntries != 1) {
                do {
                    if (itemTextField.getText().compareTo(myInventory[currentEntry - 1].description) < 0)
                        break;
                    currentEntry++;
                } while(currentEntry < numberEntries);
            }
            // move all entries below new value down one position unless at end
            if (currentEntry != numberEntries)
                for (int i = numberEntries; i >= currentEntry + 1; i--) {
                    myInventory[i - 1] = myInventory[i - 2];
                    myInventory[i - 2] = new InventoryItem();
                }
            myInventory[currentEntry - 1] = new InventoryItem();
            myInventory[currentEntry - 1].description = itemTextField.getText();
            myInventory[currentEntry - 1].location = locationComboBox.getSelectedItem().toString();
            myInventory[currentEntry - 1].marked = markedCheckBox.isSelected();
            myInventory[currentEntry - 1].serialNumber = serialTextField.getText();
            myInventory[currentEntry - 1].purchasePrice = priceTextField.getText();
            myInventory[currentEntry - 1].purchaseDate = dateToString(dateChooser.getDate());
            myInventory[currentEntry - 1].purchaseLocation = storeTextField.getText();
            myInventory[currentEntry - 1].photoFile = photoTextArea.getText();
            myInventory[currentEntry - 1].note = noteTextField.getText();
            File file = fileChooser.getSelectedFile();
            try(FileWriter writer = new FileWriter(file.getPath())) {
                writer.write(numberEntries + "\n\n");
            for (int i = 0; i < numberEntries; i++) {
                writer.write(myInventory[i].description + "\n");
                writer.write(myInventory[i].location + "\n");
                writer.write(myInventory[i].serialNumber + "\n");
                writer.write(myInventory[i].marked + "\n");
                writer.write(myInventory[i].purchasePrice + "\n");
                writer.write(myInventory[i].purchaseDate + "\n");
                writer.write(myInventory[i].purchaseLocation + "\n");
                writer.write(myInventory[i].note + "\n");
                writer.write(myInventory[i].photoFile + "\n\n");
            }
            // write combo box entries
            writer.write(locationComboBox.getItemCount() + "\n");
            for (int i = 0; i < locationComboBox.getItemCount(); i++)
                writer.write(locationComboBox.getItemAt(i) + "\n");
            writer.flush();
            writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            if (numberEntries < MAX)
                newButton.setEnabled(true);
            else
                newButton.setEnabled(false);
            saveButton.setEnabled(false);
            deleteButton.setEnabled(true);
            printButton.setEnabled(true);
        }
    }
    
    private void previousButtonActionPerformed(ActionEvent e) {
        checkSave();
        currentEntry--;
        showEntry(currentEntry);
    }
    
    private void nextButtonActionPerformed(ActionEvent e) {
        checkSave();
        currentEntry++;
        showEntry(currentEntry);
    }
    
    private void printButtonActionPerformed(ActionEvent e) {
        lastPage = (int)(1 + (numberEntries - 1) / ENTRIES_PER_PAGE);
        PrinterJob inventoryPrinterJob = PrinterJob.getPrinterJob();
        inventoryPrinterJob.setPrintable(new InventoryDocument());
        if (inventoryPrinterJob.printDialog()) {
            try {
                inventoryPrinterJob.print();
            } catch(PrinterException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Print Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelButtonActionPerformed(ActionEvent e) {
        blankValues();
        init();
        openButton.setEnabled(true);
    }
    
    private void init() {
        deleteButton.setEnabled(false);
        saveButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        printButton.setEnabled(false);
        itemTextField.setEnabled(false);
        locationComboBox.setEnabled(false);
        markedCheckBox.setEnabled(false);
        serialTextField.setEnabled(false);
        priceTextField.setEnabled(false);
        storeTextField.setEnabled(false);
        noteTextField.setEnabled(false);
        photoTextArea.setEnabled(false);
        photoButton.setEnabled(false);
        searchPanel.setEnabled(false);
        photoPanel.setEnabled(false);
        dateChooser.setEnabled(false);
        newButton.setEnabled(true);
        cancelButton.setEnabled(false);
    }
    
    private void exitButtonActionPerformed(ActionEvent e) {
        exitForm(null);
    }
    
    private void photoButtonActionPerformed(ActionEvent e) {
        JFileChooser openChooser = new JFileChooser();
        openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        openChooser.setDialogTitle("Open Photo File");
        openChooser.addChoosableFileFilter(new FileNameExtensionFilter("Photo Files", "jpg"));
        if (openChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            showPhoto(openChooser.getSelectedFile().toString());
        saveButton.setEnabled(!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText()));
    }
    
    private void searchButtonActionPerformed(ActionEvent e) {
        if (numberEntries == 0)
            return;
        // search for item letter
        String letterClicked = e.getActionCommand();
        int i = 0;
        do {
            if (myInventory[i].description.substring(0, 1).equals(letterClicked)) {
                currentEntry = i + 1;
                showEntry(currentEntry);
                return;
            }
            i++;
        } while(i < numberEntries);
        JOptionPane.showConfirmDialog(null, "No " + letterClicked + " inventory items.", "None Found", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void itemTextFieldActionPerformed(ActionEvent e) {
        locationComboBox.requestFocus();
    }
    
    private void locationComboBoxActionPerformed(ActionEvent e) {
        // if in list - exit method
        for (int i = 0; i < locationComboBox.getItemCount(); i++)
            if (locationComboBox.getSelectedItem().toString().equals(locationComboBox.getItemAt(i).toString())) {
                serialTextField.requestFocus();
                return;
            }
        locationComboBox.addItem(locationComboBox.getSelectedItem());
        serialTextField.requestFocus();
    }
    
    private void serialTextFieldActionPerformed(ActionEvent e) {
        priceTextField.requestFocus();
    }
    
    private void priceTextFieldActionPerformed(ActionEvent e) {
        dateChooser.requestFocus();
    }
    
    private void dateChooserPropertyChange(PropertyChangeEvent e) {
        storeTextField.requestFocus();
    }
    
    private void storeTextFieldActionPerformed(ActionEvent e) {
        noteTextField.requestFocus();
    }
    
    private void noteTextFieldActionPerformed(ActionEvent e) {
        photoButton.requestFocus();
    }
    
    private void showEntry(int j) {
        // display entry j (1 to numberEntries)
        itemTextField.setText(myInventory[j - 1].description);
        locationComboBox.setSelectedItem(myInventory[j - 1].location);
        markedCheckBox.setSelected(myInventory[j - 1].marked);
        serialTextField.setText(myInventory[j - 1].serialNumber);
        priceTextField.setText(myInventory[j - 1].purchasePrice);
        dateChooser.setDate(stringToDate(myInventory[j - 1].purchaseDate));
        storeTextField.setText(myInventory[j - 1].purchaseLocation);
        noteTextField.setText(myInventory[j - 1].note);
        showPhoto(myInventory[j - 1].photoFile);
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
        if (j == 1)
            previousButton.setEnabled(false);
        if (j == numberEntries)
            nextButton.setEnabled(false);
        itemTextField.requestFocus();
    }
    
    private Date stringToDate(String s) {
        int m = Integer.parseInt(s.substring(0, 2)) - 1;
        int d = Integer.parseInt(s.substring(3, 5));
        int y = Integer.parseInt(s.substring(6)) - 1900;
        return new Date(y, m, d);
    }
    
    private String dateToString(Date dd) {
        String yString = String.valueOf(dd.getYear() + 1900);
        int m = dd.getMonth() + 1;
        String mString = new DecimalFormat("00").format(m);
        int d = dd.getDate();
        String dString = new DecimalFormat("00").format(d);
        return mString + "/" + dString + "/" + yString;
    }
    
    private void showPhoto(String photoFile) {
        if (!photoFile.equals("")) {
            try {
                photoTextArea.setText(photoFile);
            } catch(Exception e) {
                photoTextArea.setText("");
            }
        } else {
            photoTextArea.setText("");
        }
        photoPanel.repaint();
    }
    
    private void blankValues() {
        // blank input screen
        newButton.setEnabled(false);
        deleteButton.setEnabled(false);
        saveButton.setEnabled(false);
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
        printButton.setEnabled(false);
        cancelButton.setEnabled(true);
        itemTextField.setText("");
        locationComboBox.setSelectedItem("");
        markedCheckBox.setSelected(false);
        serialTextField.setText("");
        priceTextField.setText("");
        dateChooser.setDate(new Date());
        storeTextField.setText("");
        noteTextField.setText("");
        photoTextArea.setText("");
        photoPanel.repaint();
        itemTextField.requestFocus();
    }
    
    private void deleteEntry(int j) {
        // delete entry j
        if (j != numberEntries)
            // move all entries under j up one level
            for (int i = j; i < numberEntries; i++) {
                myInventory[i - 1] = new InventoryItem();
                myInventory[i - 1] = myInventory[i];
            }
        numberEntries--;
    }
    
    private void checkSave() {
        boolean edited = false;
        if (!myInventory[currentEntry - 1].description.equals(itemTextField.getText()))
            edited = true;
        else if (!myInventory[currentEntry - 1].location.equals(locationComboBox.getSelectedItem().toString()))
            edited = true;
        else if (myInventory[currentEntry - 1].marked != markedCheckBox.isSelected())
            edited = true;
        else if (!myInventory[currentEntry - 1].serialNumber.equals(serialTextField.getText()))
            edited = true;
        else if (!myInventory[currentEntry - 1].purchasePrice.equals(priceTextField.getText()))
            edited = true;
        else if (!myInventory[currentEntry - 1].purchaseDate.equals(dateToString(dateChooser.getDate())))
            edited = true;
        else if (!myInventory[currentEntry - 1].purchaseLocation.equals(storeTextField.getText()))
            edited = true;
        else if (!myInventory[currentEntry - 1].note.equals(noteTextField.getText()))
            edited = true;
        else if (!myInventory[currentEntry - 1].photoFile.equals(photoTextArea.getText()))
            edited = true;
        if (edited && JOptionPane.showConfirmDialog(null, "You have edited this item. Do you want to save the changes?", "Save Item", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            saveButton.doClick();
        saveButton.setEnabled(false);
    }
}

class PhotoPanel extends JPanel{
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        super.paintComponent(g2D);
        
        // draw border
        g2D.setPaint(Color.BLACK);
        g2D.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
        
        //show photo
        Image photoImage = new ImageIcon(HomeInventory.photoTextArea.getText()).getImage();
        int w = getWidth();
        int h = getHeight();
        double rWidth = (double)getWidth() / (double)photoImage.getWidth(null);
        double rHeight = (double)getHeight() / (double)photoImage.getHeight(null);
        if (rWidth > rHeight)
            // leave height at display height, change width by amount height is changed
            w = (int)(photoImage.getWidth(null) * rHeight);
        else
            // leave width at display width, change height by amount width is changed
            h = (int)(photoImage.getHeight(null) * rWidth);
        // center in panel
        g2D.drawImage(photoImage, (int)(0.5 * (getWidth() - w)), (int)(0.5 * (getHeight() - h)), w, h, null);
        g2D.dispose();
    }
}

class InventoryDocument implements Printable {
    
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2D = (Graphics2D)g;
        if (pageIndex + 1 > HomeInventory.lastPage)
            return NO_SUCH_PAGE;
        // here you decide what goes on each page and draw it
        // header
        g2D.setFont(new Font("Arial", Font.BOLD, 14));
        g2D.drawString("Home Inventory Items - Page " + String.valueOf(pageIndex + 1), (int)pf.getImageableX(), (int)(pf.getImageableY() + 25));
        // get starting y
        int dy = (int)g2D.getFont().getStringBounds("S", g2D.getFontRenderContext()).getHeight();
        int y = (int)(pf.getImageableY() + 4 * dy);
        int iEnd = HomeInventory.ENTRIES_PER_PAGE * (pageIndex + 1);
        if (iEnd > HomeInventory.numberEntries)
            iEnd = HomeInventory.numberEntries;
        for (int i = HomeInventory.ENTRIES_PER_PAGE * pageIndex; i < iEnd; i++) {
            // dividing line
            Line2D.Double dividingLine = new Line2D.Double(pf.getImageableX(), y, pf.getImageableX() + pf.getImageableWidth(), y);
            g2D.draw(dividingLine);
            y += dy;
            g2D.setFont(new Font("Arial", Font.BOLD, 12));
            
            g2D.drawString(HomeInventory.myInventory[i].description, (int)pf.getImageableX(), y);
            y += dy;
            g2D.setFont(new Font("Arial", Font.PLAIN, 12));
            g2D.drawString("Location: " + HomeInventory.myInventory[i].location, (int)(pf.getImageableX() + 25), y);
            y += dy;
            if (HomeInventory.myInventory[i].marked)
                g2D.drawString("Item is marked with identifying information.", (int)(pf.getImageableX() + 25), y);
            else
                g2D.drawString("Item is NOT marked with identifying information.", (int)(pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Serial Number: " + HomeInventory.myInventory[i].serialNumber, (int)(pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Price: $" + HomeInventory.myInventory[i].purchasePrice + ", Purchased on: " + HomeInventory.myInventory[i].purchaseDate, (int)(pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Purchased at: " + HomeInventory.myInventory[i].purchaseLocation, (int)(pf.getImageableX() + 25), y);
            y += dy;
            g2D.drawString("Note: " + HomeInventory.myInventory[i].note, (int)(pf.getImageableX() + 25), y);
            y += dy;
            try {
                // maintain original width/height ratio
                Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage();
                double ratio = (double)(inventoryImage.getWidth(null)) / (double)(inventoryImage.getHeight(null));
                g2D.drawImage(inventoryImage, (int)(pf.getImageableX() + 25), y, (int)(100 * ratio), 100, null);
            } catch(Exception ex) {/* have place to go in case image file doesn't open*/}
            y += 2 * dy + 100;
        }
        return PAGE_EXISTS;
    }
}