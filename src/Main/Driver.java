package Main;

import View.*;
import Model.Model;

public class Driver {

    private View view;
    private Model model;
    private Controller controller;

    public Driver() {
        this.view = new FolioFrame();
//        this.model = model;
//        this.controller = controller;
    }

    public static void main(String[] args) {

        Driver control = new Driver();

    }
}
