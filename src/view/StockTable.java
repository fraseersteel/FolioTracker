package view;

import controller.StockListener;
import model.IPortfolio;
import model.IStock;
import model.ViewUpdateType;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer, IStockTable {

    private static final int NumSharesField = 2;
    private static final int SharePriceField = 3;

    private IPortfolio portfolio;
    private CustomScrollPane scrollPane = new CustomScrollPane("Stocks");
    private JTable table;
    private JLabel totalValueLabel;

    public StockTable(IPortfolio portfolio) {
        setLayout(new BorderLayout());

        setupNorthMenu();
        setupSouthMenu();

        this.portfolio = portfolio;
        setupTable();
        totalValueLabel = new JLabel("");
        add(totalValueLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        for(String ticker : portfolio.getStockTickers()){
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

        JButton buyStocks = new JButton("Buy Stocks");
        buyStocks.addActionListener(new StockListener(portfolio, this));
        buyStocks.setToolTipText("Buy shares for unowned stocks, or purchase additional shares of highlighted stock");
        buttonsPane.add(buyStocks);

        JButton sellStocks = new JButton("Sell Stocks");
        sellStocks.addActionListener(new StockListener(portfolio, this));
        sellStocks.setToolTipText("Sell shares of the currently owned stock.");
        buttonsPane.add(sellStocks);

        bottomMenuPane.add(buttonsPane, BorderLayout.LINE_START);

        add(bottomMenuPane, BorderLayout.PAGE_END);
    }


    private void setupTable(){
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

        scrollPane.setViewportView(table);
    }

    public void buyStocks(){
        //option to enter ticker, name, and number
        //option to purchase additional for highlighted (and message sayting what highlighted is) and number
    }

    public void sellStocks(){
        //number of stocks and message on what highlighted is
    }

    public void clearTable(){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
//        repaint();
    }

    public void filterTable(String string) {
        if (table != null) {
            TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowFilter);
            rowFilter.setRowFilter(RowFilter.regexFilter("(?i)"+string));
        }
//        repaint();
    }

    public void insertValues(String ticker){
        IStock stock = portfolio.getStockByTicker(ticker);
        Object[] values = {ticker, stock.getStockName(), stock.getNumShares(), stock.getPricePerShare(), stock.getValueOfHolding()};
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.insertRow(model.getRowCount(), values);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("update: " + portfolio.getPortfolioName());
        if(arg.equals(ViewUpdateType.STOCKPRICE)){
            totalValueLabel.setText("Total Value: " + updateField(SharePriceField));
        }else if(arg.equals(ViewUpdateType.NUMBEROFSHARES)){
            totalValueLabel.setText("Total Value: " + updateField(NumSharesField));
        }else if(arg.equals(ViewUpdateType.CREATION) || arg.equals(ViewUpdateType.DELETION)){
            clearTable();
            for(String ticker : portfolio.getStockTickers()){
                insertValues(ticker);
            }
            //TODO think of a smarter way to know what hasnt changed
        }
        System.out.println("Updated StockTable: " + portfolio.getPortfolioName());
    }

    private double updateField(int field){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        double total = 0.0;
        for(int i =0; i< model.getRowCount(); i++){
            String ticker = (String) model.getValueAt(i, 0);
            IStock stock =portfolio.getStockByTicker(ticker);
            double value =  stock.getValueOfHolding();
            total += value;
            if(field == SharePriceField){
                model.setValueAt(stock.getPricePerShare(), i ,field);
            }else {
                model.setValueAt(stock.getNumShares(), i ,field);
            }
            model.setValueAt(value, i ,4);
        }
        return total;
    }
}
