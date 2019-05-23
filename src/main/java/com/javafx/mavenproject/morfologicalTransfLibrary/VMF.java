package com.javafx.mavenproject.morfologicalTransfLibrary;

import java.awt.image.BufferedImage;

import static com.javafx.mavenproject.morfologicalTransfLibrary.ImageUtils.convertFromRGBtoInt;

public class VMF {

    /**
     * @param bufferedImage wczytane zdjęcie
     * @param windowWidth szerokość okna maski
     * @param windowHeight wysokość okna maski
     * @return Wektorowy Filtr Medianowy
     * Usuwa szum typu 'Salt And Pepper'
     * Wielkość okna, którym odszumiamy przekazywana w parametrach metody
     * Działa dla obrazów typu RGB
     */
    public static BufferedImage vmfFilter(BufferedImage bufferedImage, int windowWidth, int windowHeight) {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int edgeX = windowWidth / 2;
        int edgeY = windowHeight / 2;

        int[] R = new int[windowWidth * windowHeight];
        int[] B = new int[windowWidth * windowHeight];
        int[] G = new int[windowWidth * windowHeight];

        for (int i = edgeX; i < width - edgeX - 1; i++) {
            for (int j = edgeY; j < height - edgeY - 1; j++) {
                int k = 0;
                for (int fx = 0; fx < windowWidth; fx++) {
                    for (int fy = 0; fy < windowHeight; fy++) {
                        R[k] = bufferedImage.getRGB(i + fx - edgeX, j + fy - edgeY) >> 16 & 0xff;
                        G[k] = bufferedImage.getRGB(i + fx - edgeX, j + fy - edgeY) >> 8 & 0xff;
                        B[k] = bufferedImage.getRGB(i + fx - edgeX, j + fy - edgeY) & 0xff;
                        k++;
                    }
                    result.setRGB(i, j, calculateMinDistance(R, G, B));
                }
            }
        }
        return result;
    }

    /**
     * @param R tablica wartości red
     * @param G tablica wartości green
     * @param B tablica wartości blue
     * @return Metoda oblicza minimalną odległość w przestrzeni kolorów
     */
    private static int calculateMinDistance(int[] R, int[] G, int[] B) {
        double distanceMin = 0.0;
        int minDistR = 0;
        int minDistG = 0;
        int minDistB = 0;

        for (int i = 0; i < R.length; i++) {
            double localMinDistance = 0.0;
            for (int j = 0; j < R.length; j++) {
                //sumanyczna odległość w stosunku do inncyh wektorów
                localMinDistance += Math.sqrt(Math.pow(R[i] - R[j], 2) + Math.pow(G[i] - G[j], 2) + Math.pow(B[i] - B[j], 2));
            }
            if ((localMinDistance < distanceMin) || (i == 0)) {
                distanceMin = localMinDistance;
                minDistR = R[i];
                minDistG = G[i];
                minDistB = B[i];
            }
        }
        return convertFromRGBtoInt(minDistR, minDistG, minDistB);
    }

}
