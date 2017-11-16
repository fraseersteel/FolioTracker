

package view;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
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
        insert();
        insertValues("Sky", "Sky", 200, 3.0);
        editNumberStocks("Sky", 10000);
        editStockPrice("Sky", 50);
    }

    private void setupNorthMenu() {
        totalValueLabel = new JLabel("");
        add(totalValueLabel, BorderLayout.PAGE_START);
    }


    private void setupSouthMenu() {
        JPanel bottomMenuPane = new JPanel(new BorderLayout());
        JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.LEFT));

        buyStocks = new JButton("Buy Stocks");
        buttonsPane.add(buyStocks);

        sellStocks = new JButton("Sell Stocks");
        buttonsPane.add(sellStocks);
        bottomMenuPane.add(buttonsPane, BorderLayout.LINE_START);

        add(bottomMenuPane, BorderLayout.PAGE_END);
    }


    private void setupTable() {
        String[] columnNames = {"Ticker Symbol",
                "Stock Name",
                "No. of Shares",
                "Price per Share",
                "Value of Holding"};

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

        JPanel tablePanel = new JPanel();
        tablePanel.add(table);

        scrollPane.setViewportView(table);
    }

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
        totalValueLabel.setText("Total Value: " + sum);
    }

    @Override
    public void update(Observable o, Object arg) {
        //updates to the stock?
    }
}
