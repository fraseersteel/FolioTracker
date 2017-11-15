package controller;

import model.IStock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockListener implements ActionListener {

    private IStock model;

    public StockListener(IStock model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
