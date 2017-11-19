package view;

public interface IStockTable {

    String getSelectedTicker();

    Object getValueAt(int firstRow, int column);
}
