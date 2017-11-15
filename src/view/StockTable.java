package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Observable;
import java.util.Observer;

public class StockTable extends JPanel implements Observer, IStockTable {

    private CustomScrollPane scrollPane = new CustomScrollPane("Stocks");
    private JTable table;
    private JLabel sumLbl;

    public StockTable() {
        setLayout(new MigLayout("" , "[grow, fill]" ,"[][grow, fill]"));
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
        add(sumLbl, "wrap");
        add(scrollPane);
        insert();
        insertValues("Sky", "Sky", 200, 3.0);
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

    public void insert(){
        insertValues("BT", "British Tele", 10, 1.5);
        insertValues("M&S", "Marks", 100, 1.0);

//        repaint();
    }

    public void insertValues(String ticker, String name, Integer shares, Double price){
        Object[] values = {ticker, name, shares, price, (price*shares)};
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.insertRow(model.getRowCount(), values);
        calcSum();
    }

    private void calcSum(){
        double sum = 0;
        int pcol=4;

        DefaultTableModel t = (DefaultTableModel) table.getModel();
        for (int i = 0; i < t.getRowCount(); i++) {
            sum = sum + Double.parseDouble(t.getValueAt(i, pcol).toString());

        }
        sumLbl.setText("Total Value: " + sum);
    }

    @Override
    public void update(Observable o, Object arg) {
        //updates to the stock?
    }
}
