package view;

import controller.PortfolioListener;
import model.IPortfolioTracker;
import model.ViewUpdateType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FolioFrame extends JFrame implements Observer, IFolioFrame {

    private JTabbedPane tabbedPane;
    private JDialog waitingScreen;

    private IPortfolioTracker portfolioTracker;
    private List<String> hiddenFolios = new ArrayList<>();
    private Map<String, StockTable> profiles = new HashMap<>();

    public FolioFrame(IPortfolioTracker portfolioTracker){
        this.portfolioTracker = portfolioTracker;
        setupFrame();
        setupMenuBar();
        setupComponents();

        for(String name : portfolioTracker.getPortfolioNames()){
            insertProfile(name);
        }
    }

    private void setupFrame(){
        setLayout(new GridLayout(1, 1));
        setTitle("FolioTracker");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                String ObjButtons[] = {"Yes","No"};
                int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit?\n Anything work not saved before exiting will be lost","Exit FolioTracker",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });
        Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
        int frameWidth = 900;
        int frameHeight = 550;

        int frameX = SCREEN_DIMENSION.width / 2 - frameWidth / 2;
        int frameY = SCREEN_DIMENSION.height / 2 - frameHeight / 2;
        setBounds(frameX, frameY, frameWidth, frameHeight);
        setVisible(true);
    }

    private void insertProfile(String name){
        StockTable table = new StockTable(portfolioTracker.getPortfolioByName(name), this);
        portfolioTracker.addObserverToFolio(name, table);
        portfolioTracker.addObserverToPrices(table);
        profiles.put(name, table);
        tabbedPane.addTab(name, null, table,
                "");
    }

    private void removeProfile(String name){
        tabbedPane.remove(profiles.get(name));
        profiles.remove(name);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        ActionListener listener = new PortfolioListener(portfolioTracker, this);
        JMenuItem load = new JMenuItem("Load Folio From File");
        load.addActionListener(listener);
        file.add(load);

        JMenuItem save = new JMenuItem("Save Folios");
        save.addActionListener(listener);
        file.add(save);

        JMenu edit = new JMenu("Folio Actions");
        menuBar.add(edit);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(listener);
        edit.add(open);

        JMenuItem hide = new JMenuItem("Hide");
        hide.addActionListener(listener);
        edit.add(hide);

        edit.addSeparator();

        JMenuItem newFolio = new JMenuItem("New");
        newFolio.addActionListener(listener);
        edit.add(newFolio);
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(listener);
        edit.add(delete);

        setJMenuBar(menuBar);
    }

    private void setupComponents() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (ViewUpdateType.DELETION.equals(arg)) {

            Set<String> namesNotSeen = new HashSet<>();
            namesNotSeen.addAll(profiles.keySet());
            for(String name : portfolioTracker.getPortfolioNames()){
                namesNotSeen.remove(name);
            }
            for(String folioName : namesNotSeen){
                removeProfile(folioName);
            }

        } else if (ViewUpdateType.CREATION.equals(arg)) {
            refreshFolioTabs();
        }
    }

    private void refreshFolioTabs(){
        for(String name : portfolioTracker.getPortfolioNames()){
            if(!profiles.containsKey(name) && !hiddenFolios.contains(name)){
                insertProfile(name);
            }
        }
    }

    @Override
    public String getSelectedFolio() {
        int i = tabbedPane.getSelectedIndex();
        if(i != -1){
            System.out.println(i);
            return tabbedPane.getTitleAt(i);
        }
        return null;
    }

    @Override
    public boolean hideSelectedFolio() {
        int i = tabbedPane.getSelectedIndex();
        if(i != -1){
            String name = tabbedPane.getTitleAt(i);
            hiddenFolios.add(name);
            tabbedPane.removeTabAt(i);
            profiles.remove(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean showFolio(String name){
        if(hiddenFolios.contains(name)){
            hiddenFolios.remove(name);
            refreshFolioTabs();
            return true;
        }
        return false;
    }

    @Override
    public void displayError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Try again",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showLoadingScreen(String msg) {
        waitingScreen = new JDialog(this, "Busy", Dialog.ModalityType.APPLICATION_MODAL);
        waitingScreen.setBounds(getX()+(this.getWidth()/4), getY()+(this.getHeight()/4), 441, 300);
        waitingScreen.setContentPane(buildLoadingScreen(msg));
        waitingScreen.setUndecorated(true);
        waitingScreen.setVisible(true);
    }

    private JPanel buildLoadingScreen(String msg){
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("resources/Loading_icon.gif"));
        JLabel lblTitle = new JLabel(img);
        lblTitle.setBounds(0, 0, 441, 291);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setVisible(true);
        JLabel lblText = new JLabel(msg);
        lblText.setFont(new java.awt.Font("Tahoma", Font.BOLD, 18));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lblText, BorderLayout.NORTH);
        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void hideLoadingScreen() {
        waitingScreen.dispose();
    }

    @Override
    public String promptForString(String message) {
        JPanel myPanel = new JPanel();
//        GridLayout gridLayout = new GridLayout(2, 2);
        myPanel.add(new JLabel(message));
        JTextField nameField = new JTextField(15);
        myPanel.add(nameField);

        int i = JOptionPane.showConfirmDialog(this, myPanel,
                message, JOptionPane.OK_CANCEL_OPTION);

        if(i == JOptionPane.OK_OPTION) {
            return nameField.getText();
        }
        return null;
    }

    @Override
    public void promptFolioToShow() {

        JPanel myPanel = new JPanel();
        if(hiddenFolios.size() == 0){
            displayError("There are no hidden folios to show.");
        }else{
            myPanel.add(new JLabel("Folio To Show:"));
            String[] fields = new String[hiddenFolios.size()];
            for(int i = 0; i < hiddenFolios.size(); i++){
                fields[i] = hiddenFolios.get(i);
            }
            JComboBox<String> comp = new JComboBox<>(fields);
            myPanel.add(comp);


            int i = JOptionPane.showConfirmDialog(this, myPanel,
                    "Show Folio", JOptionPane.OK_CANCEL_OPTION);

            if(i == JOptionPane.OK_OPTION){
                if(hiddenFolios.size() != 0){
                    this.showFolio((String) comp.getSelectedItem());
                }

            }
        }
    }

    @Override
    public boolean promptConfirmation(String title, String message) {
        int result = JOptionPane.showConfirmDialog(this, message,
                title, JOptionPane.OK_CANCEL_OPTION);

        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public File promptFileChooser(boolean load) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter ftFilter = new FileNameExtensionFilter("Folio Tracker Files (*.foliot)", "foliot");

        fc.addChoosableFileFilter(ftFilter);
        fc.setFileFilter(ftFilter);
        int result;
        if(load){
            result = fc.showOpenDialog(this);
        }else{
            result = fc.showSaveDialog(this);
        }
        if (result == JFileChooser.APPROVE_OPTION) {
            if(!load){
                try {
                    File file = new File(fc.getSelectedFile().getCanonicalPath() + ".foliot");
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return fc.getSelectedFile();
        }
        return null;
    }
}
