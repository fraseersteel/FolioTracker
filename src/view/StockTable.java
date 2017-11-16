package view;

import model.IPortfolio;
import model.IStock;
import model.ViewUpdateType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer, IStockTable {


    private IPortfolio portfolio;
    private CustomScrollPane scrollPane = new CustomScrollPane("Stocks");
    private JTable table;
    private JLabel sumLbl;

    public StockTable(IPortfolio portfolio) {

        this.portfolio = portfolio;
//        setLayout(new MigLayout("" , "[grow, fill]" ,"[][grow, fill]"));
        String[] columnNames = {"Ticker Symbol",
                "Stock Name",
                "Number of Shares",
                "Price per Share",
                "Value of Holding"};
        table = new JTable();
        sumLbl = new JLabel("");
        table.setModel(new DefaultTableModel(new Object[][]{}, columnNames){
            Class[] types = { String.class, String.class, Integer.class, Double.class, Double.class};

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
        setUpTableSpecs();
        scrollPane.setViewportView(table);
        add(sumLbl);
        add(scrollPane);
        for(String ticker : portfolio.getStockTickers()){
            insertValues(ticker);
        }
    }
    private void setUpTableSpecs(){
        table.setShowGrid(true);
        table.setDragEnabled(false);
        table.setShowVerticalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
    }

    public void clearTable(){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        repaint();
    }

    public void filterTable(String string) {
        if (table != null) {
            TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowFilter);
            rowFilter.setRowFilter(RowFilter.regexFilter("(?i)"+string));
        }
        repaint();
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
        if(arg.equals(ViewUpdateType.PRICE)){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            double total = 0.0;
            for(int i =0; i< model.getRowCount(); i++){
                String ticker = (String) model.getValueAt(i, 0);
                IStock stock =portfolio.getStockByTicker(ticker);
                double value =  stock.getValueOfHolding();
                total += value;
                model.setValueAt(stock.getPricePerShare(), i ,3);
                model.setValueAt(value, i ,4);
            }

            sumLbl.setText("Total Value: " + total);
            System.out.println("Updated StockTable: " + portfolio.getPortfolioName());
        }
    }
}
