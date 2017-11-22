
//todo this class could store a reference to the portfolio it is based off.
package view;

import controller.StockListener;
import model.IPortfolio;
import model.IStock;
import model.ViewUpdateType;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer, IStockTable {

    private static final int TickerField = 0;
    private static final int StockNameField = 1;
    private static final int NumSharesField = 2;
    private static final int SharePriceField = 3;
    private static final int intitalPriceField = 4;
    private static final int TotalValueField = 5;
    private static final int ProfitLoss = 6;

    private double totalValueHoldings = 0.0;

    private IPortfolio portfolio;
    private JTable table;
    private JLabel totalValueLabel;


    public StockTable(IPortfolio portfolio) {
        this.portfolio = portfolio;
        setLayout(new BorderLayout());

        setupNorthMenu();
        setupSouthMenu();

        CustomScrollPane scrollPane = new CustomScrollPane("Stocks");
        add(scrollPane, BorderLayout.CENTER);
        setupTable();
        scrollPane.setViewportView(table);

        table.getModel().addTableModelListener(new StockListener(portfolio, this));
        setTotalValueLabel(totalValueHoldings);
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

        ActionListener listener = new StockListener(portfolio, this);

        JButton buyStocks = new JButton("Buy Shares");
        buyStocks.addActionListener(listener);
        buyStocks.setToolTipText("Buy shares for unowned stocks, or purchase additional shares of highlighted stock");
        buttonsPane.add(buyStocks);

        JButton sellStocks = new JButton("Sell Shares");
        sellStocks.addActionListener(listener);
        sellStocks.setToolTipText("Sell shares of the currently selected stock.");
        buttonsPane.add(sellStocks);

        JButton buyNewStocks = new JButton("Add Stock");
        buyNewStocks.addActionListener(listener);
        buyNewStocks.setToolTipText("Add a new Stock.");
        buttonsPane.add(buyNewStocks);

        bottomMenuPane.add(buttonsPane, BorderLayout.LINE_START);

        add(bottomMenuPane, BorderLayout.PAGE_END);
    }


    private void setupTable() {
        String[] columnNames = {"Ticker Symbol",
                "Stock Name",
                "No. of Shares",
                "Current Price per Share (£)",
                "Initial Price per Share (£)",
                "Value of Holding (£)",
                "Profit/Loss (£)"};

        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][]{}, columnNames) {
            Class[] types = {String.class, String.class, Integer.class, Double.class, Double.class, Double.class, Double.class};

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

        formatCol(SharePriceField);
        formatCol(intitalPriceField);
        formatCol(TotalValueField);

        table.getColumnModel().getColumn(TickerField).setPreferredWidth(60);
        table.getColumnModel().getColumn(StockNameField).setPreferredWidth(60);
        table.getColumnModel().getColumn(NumSharesField).setPreferredWidth(60);
        table.getColumnModel().getColumn(SharePriceField).setPreferredWidth(110);
        table.getColumnModel().getColumn(intitalPriceField).setPreferredWidth(50);
        table.getColumnModel().getColumn(TotalValueField).setPreferredWidth(80);
        table.getColumnModel().getColumn(ProfitLoss).setPreferredWidth(50);

        table.setShowGrid(true);
        table.setDragEnabled(false);
        table.setShowVerticalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    public void filterTable(String string) {
        if (table != null) {
            TableRowSorter<DefaultTableModel> rowFilter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowFilter);
            rowFilter.setRowFilter(RowFilter.regexFilter("(?i)" + string));
        }
    }

    public IStock insertValues(String ticker) {
        IStock stock = portfolio.getStockByTicker(ticker);
        Object[] values = {ticker, stock.getStockName(), stock.getNumShares(), stock.getPricePerShare(), stock.getInitalPricePerShare(), stock.getValueOfHolding(), stock.getProfitOfHolding()};
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.insertRow(model.getRowCount(), values);
        return stock;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals(ViewUpdateType.STOCKPRICE)) {

            totalValueHoldings = updateField(SharePriceField);
            setTotalValueLabel(totalValueHoldings);

        } else if (arg.equals(ViewUpdateType.NUMBEROFSHARES)) {

            totalValueHoldings = updateField(NumSharesField);
            setTotalValueLabel(totalValueHoldings);

        } else if (arg.equals(ViewUpdateType.CREATION) || arg.equals(ViewUpdateType.DELETION)) {
            clearTable();
            totalValueHoldings = 0.0;
            for (String ticker : portfolio.getStockTickers()) {
                totalValueHoldings += insertValues(ticker).getValueOfHolding();
            }
            setTotalValueLabel(totalValueHoldings);
        }
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
                model.setValueAt(stock.getProfitOfHolding(), i, ProfitLoss);
            } else {
                model.setValueAt(stock.getNumShares(), i, field);
            }
            model.setValueAt(value, i, TotalValueField);
        }
        return total;
    }

    public String getSelectedTicker() {
        int selectedIndex = table.getSelectedRow();
        DefaultTableModel t = (DefaultTableModel) table.getModel();

        if (selectedIndex >= 0) {
            String ticker = t.getValueAt(selectedIndex, 0).toString();

            return ticker;
        }
        return null;
    }

    private void setTotalValueLabel(Double totalValue){
        totalValueLabel.setText("Total Value: £" +String.format("%.02f", totalValue));
    }

    private void formatCol(int col){
        table.getColumnModel().getColumn(col).setCellRenderer(
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
    }

    @Override
    public Object getValueAt(int firstRow, int column) {
        return table.getModel().getValueAt(firstRow, column);
    }
}
