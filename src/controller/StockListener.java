package controller;

import model.IPortfolio;
import model.IStock;
import view.IStockTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockListener implements ActionListener, TableModelListener {

    private static final String ADD = "Add Stock";
    private static final String BUY = "Buy Shares";
    private static final String SELL = "Sell Shares";

    private IPortfolio folioModel;
    private IStockTable stockTable;

    private JDialog waiting;
    private boolean passed = false;

    private JTextField tickerField = new JTextField(15);
    private JTextField sharesAmountField = new JTextField(15);

    public StockListener(IPortfolio folioModel, IStockTable stockTable) {
        this.stockTable = stockTable;
        this.folioModel = folioModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        String ticker = stockTable.getSelectedTicker();

        if(ticker == null && !action.equals(ADD)){
            displayError("Row not selected\nPlease select the row with the desired stock.");
            return;
        }
        while(!passed) {
            int result = JOptionPane.showConfirmDialog(null, getPanel(action.equals(ADD)),
                    action, JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                doAction(action, ticker);
            } else {
                passed = true;
            }
        }
        passed = false;
    }

    private void doAction(String action, String ticker){
        try{
            int i = Integer.parseInt(sharesAmountField.getText());
            if (i > 1000000000 || i < 1) {
                displayError("Please enter a number within the range 1 to 1000000000.");
            }else{
                doT(action, ticker, i);
            }
        }catch (NumberFormatException err){
            displayError("Please enter a number for the number of shares.");
        }
    }

    private void doT(String action, String ticker, int i){

        SwingWorker<Boolean,Void> worker = new SwingWorker<Boolean, Void>()
        {
            @Override
            protected Boolean doInBackground()
            {
                if(action.equals(ADD)){
                    String ticker = tickerField.getText().replaceAll("\\s+","");
                    return folioModel.buyStock(ticker, i);
                }else if(action.equals(BUY)){
                    return folioModel.buyStock(ticker, i);
                }else{
                    assert action.equals(SELL) : action;
                    return folioModel.sellStock(ticker, i);
                }
            }

            @Override
            protected void done()
            {
                try {
                    Boolean pass = get();
                    waiting.dispose();
                    if(pass == null){
                        displayError("Not enough shares \nyou have tried to sell more shares than you own!");
                    }else if(pass){
                        passed = true;
                    }else{
                        String thisTicker = tickerField.getText().replaceAll("\\s+", "");
                        displayError("No such Ticker Found. [" + thisTicker + "]. The ticker doesn't exist.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
        startLoading();
    }

    private void startLoading(){
        waiting = new JDialog(null, "Busy", Dialog.ModalityType.APPLICATION_MODAL);
        Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        int dialogWidth = 441;
        int dialogHeight = 300;

        int dialogX = SCREEN_DIMENSION.width / 2 - dialogWidth / 2;
        int dialogY = SCREEN_DIMENSION.height / 2 - dialogHeight / 2;

        waiting.setBounds(dialogX, dialogY, dialogWidth, dialogHeight);
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/Loading_icon.gif"));
        JLabel lblTitle = new JLabel(img);
        lblTitle.setBounds(0, 0, 441, 291);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVisible(true);
        JLabel lblText = new JLabel("Looking for Ticker Price");
        lblText.setFont(new java.awt.Font("Tahoma", 1, 18));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lblText, BorderLayout.NORTH);
        panel.add(lblTitle, BorderLayout.CENTER);
        waiting.setContentPane(panel);
        waiting.setUndecorated(true);
        waiting.setVisible(true);
    }

    private JPanel getPanel(boolean withTickerField) {
        JPanel myPanel = new JPanel();
        GridLayout gridLayout;

        if (withTickerField) {
            gridLayout = new GridLayout(2, 2);
            myPanel.add(new JLabel("Ticker:"));
            myPanel.add(tickerField);
        } else {
            gridLayout = new GridLayout(1, 2);
        }
        myPanel.setLayout(gridLayout);

        myPanel.add(new JLabel("Number of shares:"));
        myPanel.add(sharesAmountField);

        return myPanel;
    }

    private void displayError(String msg) {
        JOptionPane.showMessageDialog(
                null,
                msg,
                "Try again",
                JOptionPane.ERROR_MESSAGE);
        passed=false;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            if (e.getColumn() == 1) {
                String ticker = stockTable.getSelectedTicker();
                IStock stock = folioModel.getStockByTicker(ticker);
                stock.setStockName((String) stockTable.getValueAt(e.getFirstRow(), e.getColumn()));
            }
        }
    }
}
