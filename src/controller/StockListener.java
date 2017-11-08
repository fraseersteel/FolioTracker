package controller;

import model.Model;
import view.View;

import java.awt.event.ActionEvent;

public class StockListener implements Controller {

    private Model model;

    private View view;

    public StockListener(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //
    }
}
