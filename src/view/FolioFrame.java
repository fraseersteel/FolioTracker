package view;

import controller.PortfolioListener;
import model.IPortfolioTracker;
//import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class FolioFrame extends JFrame implements Observer {


    private IPortfolioTracker model;

    private JPanel contentpane;
    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();
    private JTabbedPane tabbedPane = new JTabbedPane();

    public FolioFrame(){
        setTitle("FolioTracker");
        contentpane = new JPanel();
        contentpane.setLayout(new BoxLayout(contentpane, BoxLayout.PAGE_AXIS));
//        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));

        initMenuBar();
        initComponents();
        setUpFrame();
        insertProfile("Test1", new StockTable());
        StockTable table = new StockTable();
        table.insertValues("hi","there", 30, 1.0);
        insertProfile("Test2", table);
    }

    private void setUpFrame(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 825, 400);
        setVisible(true);
        this.setContentPane(contentpane);
    }

    private void insertProfile(String name, StockTable table){
        profiles.put(name, new StockTable());
        tabbedPane.addTab(name, null, table,
                "Does nothing");
    }

    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Folio");
        JMenuItem options = new JMenuItem("Options");
        JMenuItem open = new JMenuItem("Open");
            open.setMnemonic(KeyEvent.VK_E);
            open.addActionListener(new PortfolioListener(model));
        JMenuItem sellAll = new JMenuItem("Sell All");
            sellAll.setMnemonic(KeyEvent.VK_E);
            sellAll.addActionListener(new PortfolioListener(model));
        JMenuItem exit = new JMenuItem("Exit");
            exit.setMnemonic(KeyEvent.VK_E);
            exit.addActionListener(new PortfolioListener(model));
        JMenuItem save = new JMenuItem("Save");
            save.setMnemonic(KeyEvent.VK_E);
            save.addActionListener(new PortfolioListener(model));
        menuBar.add(menu);
        menu.add(open);
        menu.add(options);
        menu.add(sellAll);
        menu.add(save);
        menu.add(exit);
        setJMenuBar(menuBar);
    }




    private void initComponents(){
        contentpane.add(tabbedPane);
    }


    @Override
    public void update(Observable o, Object arg) {
        // updates to the folios?
    }


    public static void main(String[] args) {
        FolioFrame folioFrame = new FolioFrame();
    }
}
