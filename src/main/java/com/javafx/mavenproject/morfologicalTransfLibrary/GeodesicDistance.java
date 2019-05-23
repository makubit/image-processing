package com.javafx.mavenproject.morfologicalTransfLibrary;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GeodesicDistance {

    /**
     * @param bufferedImage wczytane zdjęcie
     * @param point punkt, od którego zaczynamy liczenie odległości geodezyjnej
     * @return zdjęcie RGB z zaznaczoną odległością
     * Metoda umożliwia obliczenie odległości geodezyjnej przy zadaniu odpowiedniego punku
     * Miejsce punktu okreśale jest wartością położenia piksela, np. x: 150, y: 150
     */
    public static BufferedImage geodesicDistance(BufferedImage bufferedImage, Point point) {

        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int[][] binary = new int[width][height];
        int[][] dilated = new int[width][height];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                binary[j][i] = bufferedImage.getRGB(j, i);

                if (binary[j][i] <= (-16777216))
                    binary[j][i] = 0;
                else
                    binary[j][i] = 1;
            }
        }

        int[][] mask = new int[width][height];

        mask[point.x][point.y] = 1;

        int k = 0;
        while (k < (height + width) * 3) {
            mask = dilateArray2DtoGeoMap(mask, dilated, ((k/2) % 255));
            mask = logicalAND(mask, binary);
            k++;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.setRGB(j, i, dilated[j][i]);
            }
        }
        return result;
    }

    /**
     * @param marker wartość tablicy 'maski'
     * @param binary wejściowa tablica binarna
     * @return rezultat logicznej operazji AND
     * Operacja logicznego AND na dwóch tablicach o tych samych wymiarach
     * marker - nasza maska
     * binary - obraz binarny na którym pracujemy
     */
    private static int[][] logicalAND(int[][] marker, int[][] binary) {

        for (int i = 0; i < marker[0].length; i++) {
            for (int j = 0; j < marker.length; j++) {
                if ((marker[j][i] == binary[j][i]) && binary[j][i] == 1)
                    marker[j][i] = 1;
                else
                    marker[j][i] = 0;
            }
        }
        return marker;
    }

    /**
     * @param marker tablica maska
     * @param dilated tablica poddawana dylacji
     * @return rezultat dylacji
     * Przeprowadza dylację na tablicy 2D
     */
    private static int[][] dilateArray2DtoGeoMap(int[][] marker, int[][] dilated, int light) {
        for (int i = 0; i < marker[0].length; i++) {
            for (int j = 0; j < marker.length; j++) {
                if (marker[j][i] == 1) {
                    if (j > 0 && marker[j - 1][i] == 0)
                        marker[j - 1][i] = 3;
                    if (i > 0 && marker[j][i - 1] == 0)
                        marker[j][i - 1] = 3;
                    if (j + 1 < marker.length && marker[j + 1][i] == 0)
                        marker[j + 1][i] = 3;
                    if (i + 1 < marker[0].length && marker[j][i + 1] == 0)
                        marker[j][i + 1] = 3;
                }
            }
        }

        for (int i = 0; i < marker[0].length; i++) {
            for (int j = 0; j < marker.length; j++) {
                if (marker[j][i] == 3) {
                    dilated[j][i] = light << 16; //w oznaczonym miejscu ustawiamy wartosc koloru (jakąkolwiek, w moim przypadku dla lepszej widoczności ustawiam kolor czerwony
                    marker[j][i] = 1; //przywracamy poprzednia postac markera
                }
            }
        }

        return marker;
    }
}
