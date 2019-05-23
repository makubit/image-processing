package com.javafx.mavenproject.morfologicalTransfLibrary;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.javafx.mavenproject.morfologicalTransfLibrary.ImageUtils.convertFromRGBtoInt;

public class Norm {
    public BufferedImage image;

    public Norm(BufferedImage image) {
        this.image = image;
    }

    static int l = 0;
    static int m = 0;
    static int n = 0;
    static int u = 0;

    /**
     * Sprawdzenie w którym progu normalizacji znajduje się nasz punkt
     */
    public static void Check(int axx, int ayy, int bxx, int byy, int cxx, int cyy, int kolor) throws Exception {

        if (axx > bxx || bxx > cxx || cxx > 255 || axx < 0 || ayy > 255 || ayy < 0 || byy > 255 || byy < 0 || cyy > 255 || cyy < 0)
            throw new Exception("0<x1<x2<x3<255");

        if (kolor <= axx) {
            l = 0;
            m = 0;
            n = axx;
            u = ayy;
        } else if (kolor >= axx + 1 && kolor <= bxx) {
            m = axx + 1;
            n = bxx;
            l = ayy + 1;
            u = byy;
        } else if (kolor >= bxx && kolor < cxx) {
            m = bxx + 1;
            n = cxx;
            l = byy + 1;
            u = cyy;
        } else if (kolor >= cxx && kolor <= 255) {
            m = cxx + 1;
            n = 255;
            l = cyy + 1;
            u = 255;
        } else
            throw new Exception("Kolor Something went wrong...");
    }

    /**
     * @return Przeprowadza normalizację
     */
    public static BufferedImage Normalize(BufferedImage bufferedImage, int axx, int ayy, int bxx, int byy, int cxx, int cyy) throws Exception {
        BufferedImage normalImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[][] array2D = ImageUtils.convertTo2DArray(bufferedImage);


        for (int x = 0; x < bufferedImage.getWidth(); x++)
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int pixel = array2D[y][x];

                Color mycolor = new Color(bufferedImage.getRGB(x, y));

                int red = mycolor.getRed();
                int green = mycolor.getGreen();
                int blue = mycolor.getBlue();

                Check(axx, ayy, bxx, byy, cxx, cyy, red);
                int newRed = FromPoints(l, m, u, n, red);

                Check(axx, ayy, bxx, byy, cxx, cyy, green);
                int newGreen = FromPoints(l, m, u, n, green);

                Check(axx, ayy, bxx, byy, cxx, cyy, blue);
                int newBlue = FromPoints(l, m, u, n, blue);

                int converted = convertFromRGBtoInt(newRed, newGreen, newBlue);

                l = 0;
                m = 0;
                n = 0;
                u = 0;

                normalImage.setRGB(x, y, converted);
            }
        return normalImage;
    }

    /**
     * @return Oblicza wartość wynikową koloru, w zależności od progu w którym się punkt znajduje
     * wartości l, m, n, u ustawiane są w metodzie 'Check'
     */
    private static int FromPoints(int l, int m, int u, int n, int kolor) {
        return ((l - u) / (m - n)) * (kolor - m) + l;
    }


}
