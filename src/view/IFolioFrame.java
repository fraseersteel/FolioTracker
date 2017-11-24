package view;

import java.io.File;

public interface IFolioFrame {

    String getSelectedFolio();

    boolean hideSelectedFolio();

    boolean showFolio(String name);

    void displayError(String message);

    void showLoadingScreen(String message);

    void hideLoadingScreen();

    String promptForString(String message);

    void promptFolioToShow();

    boolean promptConfirmation(String title, String message);

    File promptFileChooser(boolean load);
}
