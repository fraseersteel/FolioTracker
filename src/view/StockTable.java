
//todo this class could store a reference to the portfolio it is based off.
package view;

import controller.StockListener;
import model.IPortfolio;
import model.IStock;
import model.ViewUpdateType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer, IStockTable {

    private static final int TickerField = 0;
    private static final int StockNameField = 1;
    private static final int NumSharesField = 2;
    private static final int SharePriceField = 3;
    private static final int TotalValueField = 4;

    private IPortfolio portfolio;
    private CustomScrollPane scrollPane;
    private JTable table;
    private JLabel totalValueLabel;
    private JButton buyStocks;
    private JButton sellStocks;


    public StockTable(IPortfolio portfolio) {
        this.portfolio = portfolio;
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
        for (String ticker : portfolio.getStockTickers()) {
            insertValues(ticker);
        }
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
        sellStocks.setToolTipText("Sell shares of the currently selected stock.");
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

        //formatting cells to 2dp for £ values
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
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);

        table.setShowGrid(true);
        table.setDragEnabled(false);
        table.setShowVerticalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane.setViewportView(table);
    }

    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
//        repaint();
    }

    public void filterTable(String string) {
        if (table != null) {
            TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowFilter);
            rowFilter.setRowFilter(RowFilter.regexFilter("(?i)" + string));
        }
//        repaint();
    }

    public void insertValues(String ticker) {
        IStock stock = portfolio.getStockByTicker(ticker);
        Object[] values = {ticker, stock.getStockName(), stock.getNumShares(), stock.getPricePerShare(), stock.getValueOfHolding()};
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.insertRow(model.getRowCount(), values);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("update: " + portfolio.getPortfolioName());
        if (arg.equals(ViewUpdateType.STOCKPRICE)) {
            totalValueLabel.setText("Total Value: " + updateField(SharePriceField));
        } else if (arg.equals(ViewUpdateType.NUMBEROFSHARES)) {
            totalValueLabel.setText("Total Value: " + updateField(NumSharesField));
        } else if (arg.equals(ViewUpdateType.CREATION) || arg.equals(ViewUpdateType.DELETION)) {
            clearTable();
            for (String ticker : portfolio.getStockTickers()) {
                insertValues(ticker);
            }
            //TODO think of a smarter way to know what hasnt changed
        }
        System.out.println("Updated StockTable: " + portfolio.getPortfolioName());
    }

    private double updateField(int field) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        double total = 0.0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String ticker = (String) model.getValueAt(i, 0);
            IStock stock = portfolio.getStockByTicker(ticker);
            double value = stock.getValueOfHolding();
            total += value;
            if (field == SharePriceField) {
                model.setValueAt(stock.getPricePerShare(), i, field);
            } else {
                model.setValueAt(stock.getNumShares(), i, field);
            }
            model.setValueAt(value, i, 4);
        }
        return total;
    }

//returns selected ticker in jtable, or null if unselected
    public String getSelectedTicker() {
        int selectedIndex = table.getSelectedRow();
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        if (selectedIndex >= 0) {
            String ticker = t.getValueAt(selectedIndex, 0).toString();

            return ticker;
        }

        return null;
    }


    //TODO move to controller
//    public void buyStocks(){
//        //option to enter ticker, name, and number
//        //option to purchase additional for highlighted (and message sayting what highlighted is) and number
//    }
//
//    public void sellStocks(){
//        //number of stocks and message on what highlighted is
//    }

    //todo the checks for stock, current stock count before selling etc. outside of this and before in controller
//    public void confirmBuy(String ticker, int numberOfShares) {
//        int tickerIndex = 0;
//
//        boolean stockOwned = false;
//        DefaultTableModel t = (DefaultTableModel) table.getModel();
//        for (int i = 0; i < t.getRowCount(); i++) {
//            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
//
//                JOptionPane.showMessageDialog(this, "Stock already owned for: " + ticker +
//                        ".\nPurchase an additional: " + numberOfShares + " shares?");
//                stockOwned = true;
//            }
//        }
//
//        if (!stockOwned) {
//            JOptionPane.showMessageDialog(this, "Purchase stock for: " + ticker +
//                    ".\nPurchase: " + numberOfShares + " shares?");
//        }
//
//    }

//TODO move to controller.
//popup windows

//public void buyStocks() {
//    String ticker;
//    int numStocks = 0;
//
//    if (getSelectedTicker() == null) {
//        //no currently selected stock
//
//        JTextField tickerField = new JTextField();
//        JTextField stockNameField = new JTextField();
//        JTextField numberStocksField = new JTextField();
//
//        JPanel myPanel = new JPanel(new GridLayout(6, 1));
//
//
//        myPanel.add(new JLabel("Enter ticker symbol"));
//        myPanel.add(tickerField);
//        myPanel.add(new JLabel("Enter stock name"));
//        myPanel.add(stockNameField);
//        myPanel.add(new JLabel("Enter number of stocks to buy:"));
//        myPanel.add(numberStocksField);
//
//        Object[] options = {"Buy Shares", "Cancel"};
//        int n = JOptionPane.showOptionDialog(null,
//                myPanel,
//                "Purchase shares.",
//
//                //text field number of shares
//                //expandable section for ticker and name
//
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                options,
//                options[0]);
//        if (n == JOptionPane.YES_OPTION) {
//            //label.setText("al");
//        } else if (n == JOptionPane.NO_OPTION) {
//            //label.setText("ba");
//        } else {
//            //label.setText("Come on -- 'fess up!");
//        }
//
//        //todo placeholder call to confirm
//
//    } else {
//        //provide option for currently selected stock OR a manually entered stock
//        JTextField tickerField = new JTextField();
//        JTextField stockNameField = new JTextField();
//        JTextField numberStocksField = new JTextField();
//
//        JPanel myPanel = new JPanel(new GridLayout(8, 1));
//
//
//        myPanel.add(new JLabel("Currently selected stock: " + getSelectedTicker()));
//        myPanel.add(new JLabel("Selected stock name: " + getTickerName(getSelectedTicker())));
//
//        myPanel.add(new JLabel("Enter ticker symbol"));
//        myPanel.add(tickerField);
//        myPanel.add(new JLabel("Enter stock name"));
//        myPanel.add(stockNameField);
//        myPanel.add(new JLabel("Enter number of stocks to buy:"));
//        myPanel.add(numberStocksField);
//
//        Object[] options = {"Buy Shares", "Cancel"};
//        int n = JOptionPane.showOptionDialog(this,
//                myPanel,
//                "Purchase shares.",
//
//
//                //text field number of shares
//                //expandable section for ticker and name
//
//                JOptionPane.YES_NO_OPTION,
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                options,
//                options[0]);
//        if (n == JOptionPane.YES_OPTION) {
//            //label.setText("al");
//        } else if (n == JOptionPane.NO_OPTION) {
//            //label.setText("ba");
//        } else {
//            //label.setText("Come on -- 'fess up!");
//        }
//
//
//    }
//    //option to enter ticker, name, and number
//    //option to purchase additional for highlighted (and message sayting what highlighted is) and number
//    //todo placeholder call to confirm
//    confirmBuy("a", 1);
//
//}
//
//    public void confirmBuy(String ticker, int numberOfShares) {
//        int tickerIndex = 0;
//
//        boolean stockOwned = false;
//        DefaultTableModel t = (DefaultTableModel) table.getModel();
//        Object[] options = {"Buy Shares", "Cancel"};
//        for (int i = 0; i < t.getRowCount(); i++) {
//            if (t.getValueAt(i, tickerIndex).toString().equals(ticker)) {
//                int n = JOptionPane.showOptionDialog(this,
//                        "Purchase *addional* shares for:\n" +
//                                "Ticker: \t " + ticker + "\n" +
//                                "Name:" + " " + "\n" +
//                                "No. of Shares: \t" + numberOfShares,
//                        "Purchase shares.",
//
//
//                        //text field number of shares
//                        //expandable section for ticker and name
//
//                        JOptionPane.YES_NO_OPTION,
//                        JOptionPane.QUESTION_MESSAGE,
//                        null,
//                        options,
//                        options[0]);
//
//                stockOwned = true;
//            }
//        }
//
//        if (!stockOwned) {
//            int n = JOptionPane.showOptionDialog(this,
//                    "Purchase shares new stock:\n" +
//                            "Ticker: \t " + ticker + "\n" +
//                            "Name:" + " " + "\n" +
//                            "No. of Shares: \t" + numberOfShares,
//                    "Purchase shares.",
//
//
//                    //text field number of shares
//                    //expandable section for ticker and name
//
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null,
//                    options,
//                    options[0]);
//        }
//
//    }
//
//    //todo the paramater is questionable, only would sell what is owned
//    public void sellStocks() {
//        String ticker = getSelectedTicker();
//        if (ticker == null) {
//            JOptionPane.showMessageDialog(this,
//                    "No currently selected stock.");
//        } else {
//            getSelectedTicker();
//            //number of stocks and message on what highlighted is
//
//            Object[] options = {"Sell Shares", "Cancel"};
//            String s = (String) JOptionPane.showInputDialog(
//                    this,
//                    "Enter how many shares to sell for highlighted stock:\n" +
//                            "Ticker: \t" + ticker + "\n" +
//                            "Name: \t" + getTickerName(ticker) + "\n" +
//                            "Currently Own: \t" + getTickerShareCount(ticker) + "\n\n" +
//                            "No. of Shares to sell: \t ",
//                    "Sell Shares",
//                    JOptionPane.PLAIN_MESSAGE,
//                    null,
//                    null,
//                    "");
//
//            //If a string was returned, say so.
//            boolean validNumber = true;
//            int num = 0;
//
//            if (s != null) {
//                if (s.length() == 0) {
//                    validNumber = false;
//                    JOptionPane.showMessageDialog(this,
//                            "Please enter a valid number or press cancel.");
//                } else {
//                    try {
//                        num = Integer.parseInt(s.trim());
//                        //current stocks - num shouldnt be less than zero
//                        if (num <= 0) {
//                            JOptionPane.showMessageDialog(this,
//                                    "Please enter a number greater than 0.");
//                            validNumber = false;
//                        }
//                    } catch (NumberFormatException e) {
//                        JOptionPane.showMessageDialog(this,
//                                "Please enter a valid number of stocks.");
//                        validNumber = false;
//                    }
//                }
//
//                if (validNumber) {
//                    if (num > 0) {
//                        confirmSell(ticker, num);
//                    }
//                } else {
//                    sellStocks();
//                }
//            }
//        }
//    }
//
//    private void confirmSell(String ticker, int numberOfShares) {
//        //number of stocks and message on what highlighted is
//        int currentNumberShares = getSelectedNumberShares();
//        if ((currentNumberShares - numberOfShares) < 0) {
//            JOptionPane.showMessageDialog(this,
//                    "Folio does not contain enough shares.");
//            sellStocks();
//        } else {
//
//            Object[] options = {"Confirm sale", "Cancel"};
//
//            int n = JOptionPane.showOptionDialog(this,
//                    "Sell shares for highlighted stock:\n" +
//                            "Ticker: \t" + ticker + "\n" +
//                            "Name: \t" + getTickerName(ticker) + "\n" +
//                            "No. of Shares: \t " + numberOfShares,
//                    "Confirm sale.",
//
//                    JOptionPane.YES_NO_OPTION,
//                    JOptionPane.QUESTION_MESSAGE,
//                    null,
//                    options,
//                    options[0]);
//            if (n == JOptionPane.YES_OPTION) {
//                //todo code for when yes. likely call a method in controller, as cant have acctionlistner
//            } else if (n == JOptionPane.NO_OPTION) {
//                //todo code for when cancelled likely call a method in controller, as cant have acctionlistner
//            }
//        }
//    }



}
