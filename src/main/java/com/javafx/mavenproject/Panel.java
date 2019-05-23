package com.javafx.mavenproject;

import com.javafx.mavenproject.morfologicalTransfLibrary.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends StartWindow {

    public javafx.scene.control.Label label_go;
    @FXML
    private ImageView imageView; //Miejsce wyświetlania zdjęcia
    @FXML
    private javafx.scene.control.Button normalizacja;
    @FXML
    private javafx.scene.control.Button vmf;
    @FXML
    private javafx.scene.control.Button zamknieciekolowe;
    @FXML
    private javafx.scene.control.Button odlegloscgeodezyjna;
    @FXML
    private TextField progbinaryzacji;
    @FXML
    private TextField ax;
    @FXML
    private TextField ay;
    @FXML
    private TextField bx;
    @FXML
    private TextField by;
    @FXML
    private TextField cx;
    @FXML
    private TextField cy;
    @FXML
    private TextField oknovmf;
    @FXML
    private TextField promien;
    @FXML
    private TextField odlX;
    @FXML
    private TextField odlY;

    /**
     * Ustawienie który w tym momencie przycisk jest włączony
     * Zależność ta umożliwia wykonanie odpowiedniego przekształcenia
     * wykonywanego w metodzie params() wywoływanej poprzez kliknięcie "Analizuj"
     */
    private int activeButton;
    /**
     * Przechowuje załadowane zdjęcie w formacie BufferedImage
     */
    private BufferedImage bufferedImage;

    /**
     * Inicjacja stanu okna panelu
     * Wyłączenie określonych kontrolek w zależności od odpowiedniego typu obrazu
     * Wyświetlenie zdjęcia w odpowiednim miejscu na panelu
     */
    @FXML
    public void initialize() {
        try {
            this.bufferedImage = ImageIO.read(StartWindow.obraz);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        imageView.setImage(SwingFXUtils.toFXImage(this.bufferedImage, null));

        lockAllClearAll();

        if (which == 1) {
            normalizacja.setDisable(true);
            vmf.setDisable(true);
        }
        if (which == 2) {
            vmf.setDisable(true);
            odlegloscgeodezyjna.setDisable(true);

        }
        if (which == 3) {
            zamknieciekolowe.setDisable(true);
            odlegloscgeodezyjna.setDisable(true);
        }
    }

    /**
     * Zmienna naliczająca ilość zapisanych zdjęć
     * Od rozpoczącia działania programu
     */
    private int a = 0;

    /**
     * @param image wczytany obraz
     * Zapisz zdjęcie do pliku
     * Dotyczy obecnej lokalizacji projetku
     */
    private void saveToFile(Image image) {

        File outputFile = new File("JavaFxImages" + a + ".png");
        a++;
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Główna metoda, która wywołuje się po kliknięciu przycisku "Analizuj"
     * Obsługuje całą gamę przekształceń mofrologicznych z biblioteki
     * Zczytuje tekst z pól tekstowych
     * Obsługuje wyjątki w przypadku niepoprawnych parametrów
     */
    public void params() {
        String axx = ax.getText();
        String ayy = ay.getText();
        String bxx = bx.getText();
        String byy = by.getText();
        String cxx = cx.getText();
        String cyy = cy.getText();
        String pr = promien.getText();
        String x = odlX.getText();
        String y = odlY.getText();

        switch (activeButton) {
            //---------------------------------------------------------------------------------------------
            case 0: //normalizacja 3 punktami
                try {
                    Image ima = imageView.getImage();
                    BufferedImage im = SwingFXUtils.fromFXImage(ima, null);

                    Norm norm = new Norm(im);

                    BufferedImage bufferedImage = ImageIO.read(StartWindow.obraz);

                    int ax2 = Integer.parseInt(axx);
                    int ay2 = Integer.parseInt(ayy);
                    int bx2 = Integer.parseInt(bxx);
                    int by2 = Integer.parseInt(byy);
                    int cx2 = Integer.parseInt(cxx);
                    int cy2 = Integer.parseInt(cyy);

                    try {
                        norm.image = norm.Normalize(bufferedImage, ax2, ay2, bx2, by2, cx2, cy2);
                        Image img = SwingFXUtils.toFXImage(norm.image, null);
                        imageView.setImage(img);
                    }
                    catch(Exception e) {
                        System.out.println("Wrong parameters: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println("Caught exception: " + e.getMessage());
                }

                break;
            //---------------------------------------------------------------------------------------------
            case 1: //VMF
                try {
                    this.bufferedImage = VMF.vmfFilter(ImageIO.read(StartWindow.obraz), Integer.parseInt(x), Integer.parseInt(y));

                    Image image = SwingFXUtils.toFXImage(this.bufferedImage, null);
                    imageView.setImage(image);
                } catch (IOException e) {
                    System.out.println("Caught exception: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Parsing error, please set proper value");
                }

                break;
            //---------------------------------------------------------------------------------------------
            case 2: //zamkniecie elem. kołowym
                try {
                    CloseWithCircle withcircle = new CloseWithCircle(ImageIO.read(StartWindow.obraz));

                    BufferedImage bufferedImage = ImageIO.read(StartWindow.obraz);
                    withcircle.image = withcircle.closing(bufferedImage, Integer.parseInt(pr));
                    Image img = SwingFXUtils.toFXImage(withcircle.image, null);
                    imageView.setImage(img);
                } catch (IOException e) {
                    System.out.println("Caught exception: " + e.getMessage());
                }

                break;
            //---------------------------------------------------------------------------------------------
            case 3: //mapa odl geodezyjnej
                try {
                    bufferedImage = GeodesicDistance.geodesicDistance(ImageIO.read(StartWindow.obraz), new Point(Integer.parseInt(x), Integer.parseInt(y)));
                    Image imga = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(imga);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                break;
            //---------------------------------------------------------------------------------------------
        }
    }

    /**
     * Zapisz zdjęcie
     * Wykorzystuje metodę saveToFile
     * Akcja onClick()
     */
    public void save(ActionEvent actionEvent) {
        Image ima = imageView.getImage();
        saveToFile(ima);
    }

    /**
     * Poniżej znajdują się wszelkie metody wywoływane przy kliknięciu odpowiedniego przycisku na panelu
     * Ich nazwy sugerują który przycisk został naciśnięty oraz jakie akcje zostaną wywołane w kolejności
     * Każda metoda blokuje oraz czyści pola tekstowe panelu
     * Ustawia odpowiedni "aktywny" przycisk
     */

    public void normOnClick(ActionEvent actionEvent) {
        lockAllClearAll();
        ax.setDisable(false);
        ay.setDisable(false);
        bx.setDisable(false);
        by.setDisable(false);
        cx.setDisable(false);
        cy.setDisable(false);
        this.activeButton = 0;
    }

    public void vmfOnClick(ActionEvent actionEvent) {
        lockAllClearAll();
        odlX.setDisable(false);
        odlY.setDisable(false);
        this.label_go.setText("wielkosc okna");
        this.activeButton = 1;
    }

    public void zamkKoloOnClick(ActionEvent actionEvent) {
        lockAllClearAll();
        promien.setDisable(false);
        this.activeButton = 2;
    }

    public void odlGeoOnClick(ActionEvent actionEvent) {
        lockAllClearAll();
        this.label_go.setText("odl. geodezyjna");
        odlX.setDisable(false);
        odlY.setDisable(false);
        this.activeButton = 3;

    }

    /**
     * Medota umożliwia wyczyszczenie wszystkich kontrolek oraz zablokowanie
     * w trakcie zamieniania przekształceń
     */
    private void lockAllClearAll() {
        progbinaryzacji.setDisable(true);
        ax.setDisable(true);
        ay.setDisable(true);
        bx.setDisable(true);
        by.setDisable(true);
        cx.setDisable(true);
        cy.setDisable(true);
        oknovmf.setDisable(true);
        promien.setDisable(true);
        odlX.setDisable(true);
        odlY.setDisable(true);

        progbinaryzacji.clear();
        ax.setDisable(true);
        ay.setDisable(true);
        bx.setDisable(true);
        by.setDisable(true);
        cx.setDisable(true);
        cy.setDisable(true);
        oknovmf.clear();
        promien.clear();
        odlX.clear();
        odlY.clear();
    }

    /**
     * Metoda do ponownego zaladowania obrazu
     * Powoduje ponowne otworzenie okna StartWindow
     */
    public void zaladujPonownie(ActionEvent actionEvent) {
        lockAllClearAll();

        MainJavaFx ms = new MainJavaFx();
        Stage s = new Stage();
        try {
            ms.start(s);
        } catch (Exception e) {
            System.out.println("Starting new window error: " + e.getMessage());
        }
    }
}