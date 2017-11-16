
//todo this class could store a reference to the portfolio it is based off.
package view;


import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer {

    CustomScrollPane scrollPane;
    private JTable table;
    private JLabel totalValueLabel;
    private JButton buyStocks;
    private JButton sellStocks;


    public StockTable() {
        setLayout(new BorderLayout());

        setupNorthMenu();
        setupSouthMenu();

        scrollPane = new CustomScrollPane("Stocks");
        add(scrollPane, BorderLayout.CENTER);
        setupTable();

        //testing code
//        insert();
//        insertValues("Sky", "Sky", 200, 3.0);
//        editNumberStocks("Sky", 10000);
//        editStockPrice("Sky", 50);
//        confirmBuy("Sky", 50);
//        confirmBuy("Safadfay", 50);
//        confirmSell("Sky", 20);
    }

    private void setupNorthMenu() {
        totalValueLabel = new JLabel("");
        add(totalValueLabel, BorderLayout.PAGE_START);
    }


    private void setupSouthMenu() {
        JPanel bottomMenuPane = new JPanel(new BorderLayout());
        JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buyStocks = new JButton("Buy Stocks");
        buyStocks.setToolTipText("Buy shares for unowned stocks, or purchase additional shares of highlighted stock");
        buttonsPane.add(buyStocks);

        sellStocks = new JButton("Sell Stocks");
        sellStocks.setToolTipText("Sell shares of the currently owned stock.");
        buttonsPane.add(sellStocks);

        bottomMenuPane.add(buttonsPane, BorderLayout.LINE_START);

        add(bottomMenuPane, BorderLayout.PAGE_END);
    }


    private void setupTable() {
        String[] columnNames = {"Ticker Symbol",
                "Stock Name",
                "No. of Shares",
                "Price per Share (£)",
                "Value of Holding (£)"};

        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][]{}, columnNames) {
            Class[] types = {String.class, String.class, Integer.class, Double.class, Double.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                switch (col) {
                    case 1:
                        return true;
                    default:
                        return false;
                }
            }
        });

        table.getColumnModel().getColumn(3).setCellRenderer(
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {

                        setHorizontalAlignment(RIGHT);
                        value = new DecimalFormat("#.00").format((double) value);
                        return super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

                    }
                }


        );

        table.getColumnModel().getColumn(4).setCellRenderer(
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value, boolean isSelected,
                            boolean hasFocus, int row, int column) {

                        setHorizontalAlignment(RIGHT);
                        value = new DecimalFormat("#.00").format((double) value);
                        return super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

                    }
                }


        );

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);

        table.setShowGrid(true);
        table.setDragEnabled(false);
        table.setShowVerticalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel tablePanel = new JPanel();
        tablePanel.add(table);

        scrollPane.setViewportView(table);
    }


    public void insertValues(String ticker, String name, Integer shares, Double price) {
        Object[] values = {ticker, name, shares, price, (price * shares)};
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.insertRow(model.getRowCount(), values);
        calcSum();
    }

    private void editNumberStocks(String ticker, int numberOfSharesChange) {
        //if current number of shares - new shares < 0 throw illegal argument exception
        //then make current shares -= shareChange
        int tickerIndex = 0;
        int numSharesIndex = 2;

        DefaultTableModel t = (DefaultTableModel) table.getModel();
        for (int i = 0; i < t.getRowCount(); i++) {
            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
                //todo clean up this if statement
                int currentNumberShares = Integer.parseInt(t.getValueAt(i, numSharesIndex).toString());
                if ((currentNumberShares - numberOfSharesChange) < 0) {
                    currentNumberShares += numberOfSharesChange;
                    t.setValueAt(currentNumberShares, i, numSharesIndex);
                }

            }
        }
        updateValueHolding();
        calcSum();

    }

    private void editStockPrice(String ticker, int newStockValue) {
        //if current number of shares - new shares < 0 throw illegal argument exception
        //then make current shares -= shareChange
        int tickerIndex = 0;
        int stockPriceIndex = 3;

        DefaultTableModel t = (DefaultTableModel) table.getModel();
        for (int i = 0; i < t.getRowCount(); i++) {
            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
                //todo clean up this if statement
                t.setValueAt(newStockValue, i, stockPriceIndex);
            }
        }
        updateValueHolding();
        calcSum();
    }

    private void updateValueHolding() {
        int numSharesIndex = 2;
        int stockPriceIndex = 3;
        int vohIndex = 4;

        DefaultTableModel t = (DefaultTableModel) table.getModel();
        for (int i = 0; i < t.getRowCount(); i++) {
            int currentNumberShares = Integer.parseInt(t.getValueAt(i, numSharesIndex).toString());
            double currentStockPrice = Double.parseDouble(t.getValueAt(i, stockPriceIndex).toString());

            t.setValueAt((currentNumberShares * currentStockPrice), i, vohIndex);
        }
        calcSum();


    }

    private void calcSum() {
        double sum = 0;
        int vohIndex = 4;

        DefaultTableModel t = (DefaultTableModel) table.getModel();
        for (int i = 0; i < t.getRowCount(); i++) {
            sum = sum + Double.parseDouble(t.getValueAt(i, vohIndex).toString());
        }
        totalValueLabel.setText("Total Value: " + new DecimalFormat("£0.00").format(sum));
    }


    //testing/unused methods


    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        repaint();
    }

    public void filterTable(String string) {
        if (table != null) {
            TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowFilter);
            rowFilter.setRowFilter(RowFilter.regexFilter("(?i)" + string));
        }
        //   repaint();
    }

    private void insert() {
        insertValues("BT", "British Tele", 10, 1.5);
        insertValues("M&S", "Marks", 100, 1.0);

//        repaint();
    }


    //popup windows
    public void buyStocks(String ticker, String name, int numberOfShares) {
        //option to enter ticker, name, and number
        //option to purchase additional for highlighted (and message sayting what highlighted is) and number

        /** Sets the text displayed at the bottom of the frame. */
        JLabel label = new JLabel();


        Object[] options = {"Buy Shares", "Cancel"};
        int n = JOptionPane.showOptionDialog(this,
                "Purchase shares for highlighted stock:\n" +
                        "Ticker: \t " + ticker + "\n" +
                        "Name:" + "\n" +
                        "there should be a hidable menu here",
                "Purchase shares.",


                //text field number of shares
                //expandable section for ticker and name

                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (n == JOptionPane.YES_OPTION) {
            //label.setText("al");
        } else if (n == JOptionPane.NO_OPTION) {
            //label.setText("ba");
        } else {
            //label.setText("Come on -- 'fess up!");
        }


    }

    public void confirmBuy(String ticker, int numberOfShares) {
        int tickerIndex = 0;

        boolean stockOwned = false;
        DefaultTableModel t = (DefaultTableModel) table.getModel();
        Object[] options = {"Buy Shares", "Cancel"};
        for (int i = 0; i < t.getRowCount(); i++) {
            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
                int n = JOptionPane.showOptionDialog(this,
                        "Purchase *addional* shares for:\n" +
                                "Ticker: \t " + ticker + "\n" +
                                "Name:" + " " + "\n" +
                                "No. of Shares: \t" + numberOfShares,
                        "Purchase shares.",


                        //text field number of shares
                        //expandable section for ticker and name

                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                stockOwned = true;
            }
        }

        if (!stockOwned) {
            int n = JOptionPane.showOptionDialog(this,
                    "Purchase shares new stock:\n" +
                            "Ticker: \t " + ticker + "\n" +
                            "Name:" + " " + "\n" +
                            "No. of Shares: \t" + numberOfShares,
                    "Purchase shares.",


                    //text field number of shares
                    //expandable section for ticker and name

                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        }

    }

    //todo the paramater is questionable, only would sell what is owned
    public void sellStocks() {
        String ticker = getSelectedTicker();
        if (ticker == null) {
            JOptionPane.showMessageDialog(this,
                    "No currently selected stock.");
        } else {
            getSelectedTicker();
            //number of stocks and message on what highlighted is

            Object[] options = {"Sell Shares", "Cancel"};
            String s = (String) JOptionPane.showInputDialog(
                    this,
                    "Enter how many shares to sell for highlighted stock:\n" +
                            "Ticker: \t" + ticker + "\n" +
                            "Name: \t" + getTickerName(ticker) + "\n" +
                            "Currently Own: \t" + getTickerShareCount(ticker) + "\n\n" +
                            "No. of Shares to sell: \t ",
                    "Sell Shares",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");

            //If a string was returned, say so.
            boolean validNumber = true;
            int num = 0;

            if ((s != null) && (s.length() > 0)) {
                try {
                    num = Integer.parseInt(s.trim());
                    //current stocks - num shouldnt be less than zero
                    if (num <= 0) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a number greater than 0.");
                        validNumber = false;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid number of stocks.");
                    validNumber = false;
                }
            }

            if (validNumber) {
                confirmSell(ticker, num);
            } else {
                sellStocks();
            }
        }


    }

    private void confirmSell(String ticker, int numberOfShares) {
        //number of stocks and message on what highlighted is
        int currentNumberShares = getSelectedNumberShares();
        if ((currentNumberShares - numberOfShares) < 0) {
            JOptionPane.showMessageDialog(this,
                    "Folio does not contain enough shares.");
            sellStocks();
        } else {

            Object[] options = {"Confirm sale", "Cancel"};

            int n = JOptionPane.showOptionDialog(this,
                    "Sell shares for highlighted stock:\n" +
                            "Ticker: \t" + ticker + "\n" +
                            "Name: \t" + getTickerName(ticker) + "\n" +
                            "No. of Shares: \t " + numberOfShares,
                    "Confirm sale.",

                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (n == JOptionPane.YES_OPTION) {
                //todo code for when yes
            } else if (n == JOptionPane.NO_OPTION) {
                //todo code for when cancelled
            }
        }
    }


    public String getTickerName(String ticker) {
        int tickerIndex = 0;
        int nameIndex = 1;
        String correspondingName = null;
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        for (int i = 0; i < t.getRowCount(); i++) {
            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
                correspondingName = t.getValueAt(i, nameIndex).toString();
            }
        }

        return correspondingName;
    }

    public int getTickerShareCount(String ticker) {
        int tickerIndex = 0;
        int numberIndex = 2;
        int correspondingNum  = -1;
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        for (int i = 0; i < t.getRowCount(); i++) {
            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
                correspondingNum = Integer.parseInt(t.getValueAt(i, numberIndex).toString());
            }
        }


        return correspondingNum;
    }

    public int getSelectedNumberShares() {
        int numberSharesIndex = 2;
        int selectedIndex = table.getSelectedRow();
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        if (selectedIndex >= 0) {
            int numberShares = Integer.parseInt(t.getValueAt(selectedIndex, numberSharesIndex).toString());
            System.out.println(numberShares);
            return numberShares;
        }

        return -1;
    }

    public String getSelectedTicker() {
        int tickerIndex = 0;
        int selectedIndex = table.getSelectedRow();
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        if (selectedIndex >= 0) {
            String ticker = t.getValueAt(selectedIndex, tickerIndex).toString();

            System.out.println(ticker);

            return ticker;
        }

        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        //updates to the stock?
    }
}
