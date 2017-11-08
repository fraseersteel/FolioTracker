package View;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class FolioFrame extends JFrame implements View {

    private JPanel contentpane;
    private Map<String, StockTable> profiles = new HashMap<String, StockTable>();
    private JTabbedPane tabbedPane = new JTabbedPane();

    public FolioFrame(){
        setTitle("FolioTracker");
        contentpane = new JPanel();
        contentpane.setLayout(new MigLayout("", "[grow, fill]", "[grow, fill]"));

        initMenuBar();
        initComponents();
        setUpFrame();
        insertProfile("Test1");
        insertProfile("Test2");
    }

    private void setUpFrame(){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 825, 400);
        setVisible(true);
        this.setContentPane(contentpane);
    }

    private void insertProfile(String name){
        profiles.put(name, new StockTable());
        tabbedPane.addTab(name, null, new StockTable(),
                "Does nothing");
    }

    private void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Folio");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem options = new JMenuItem("Options");
        menuBar.add(menu);
        menu.add(open);
        menu.add(options);
        setJMenuBar(menuBar);
    }

    private void initComponents(){
        contentpane.add(tabbedPane);
    }



}
