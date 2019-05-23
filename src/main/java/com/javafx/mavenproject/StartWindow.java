package com.javafx.mavenproject;

import com.javafx.mavenproject.morfologicalTransfLibrary.ImageUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;


public class StartWindow {
    @FXML
    private javafx.scene.control.Button closeButton;
    @FXML
    private javafx.scene.control.Button binarny;

    /**
     * Określa który typ panelu ma być wyświetlony
     * 1 - binarny
     * 2 - monochromatyczny
     * 3 - kolorowy
     */
    public static int which = 0;
    /**
     * Przechowuje obraz
     */
    public static File obraz;

    /**
     * Wybieranie pliku ze zdjęciem
     */
    public int chooseFile() throws Exception {
        System.out.println(which);
        Stage stage = (Stage) binarny.getScene().getWindow();
        int c = Choose(stage);
        if (c != 0)
            openPanel(stage);

        return 0;
    }

    /**
     * Poniższe metody: bin(), sz(), kol() umożliwiają wybranie pliku w zależności od wybranej konfiguracji
     */
    public int bin() throws Exception {
        which = 1;
        chooseFile();
        return which;
    }

    public int sz() throws Exception {
        which = 2;
        chooseFile();
        return which;
    }

    public int kol() throws Exception {
        which = 3;
        chooseFile();
        return which;
    }

    /**
     * Metoda tworzy nowe okno Panelu
     */
    public void openPanel(Stage stage) throws Exception {
        String fxmlFile = "/fxml/panel.fxml";
        FXMLLoader loader = new FXMLLoader();

        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(rootNode, 708, 540);
        scene.getStylesheets().add("/styles/style2.css");

        stage.setTitle("Panel window");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Otwiera okienko do wyboru pliku
     * Wyrzuca wyjątek w przypadku niepoprawnego formatu
     * Sprawdza, czy zdjęcie jest mono | binarne oraz rgb
     * W przypadku wybrania nieodpowiedniej konfiguracji nowy panel się nie wyświetla
     */
    public int Choose(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            this.obraz = selectedFile;
            try {
                if ((ImageUtils.isGreyscale(ImageIO.read(StartWindow.obraz)) && which != 3) || (!ImageUtils.isGreyscale(ImageIO.read(StartWindow.obraz)) && which == 3))
                    return 1;
            } catch (Exception e) {
                System.out.println("Cannot open image.");
            }
        }
        return 0;
    }

    /**
     * Metoda zamyka okno StartWindow
     */
    public void Close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
