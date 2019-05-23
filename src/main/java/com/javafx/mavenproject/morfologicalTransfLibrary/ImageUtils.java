package com.javafx.mavenproject.morfologicalTransfLibrary;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageUtils {
    /**
     * @param image wczytane zdjęcie
     * @return Konwersja z typu BufferedImage do tablicy 2D
     * Do zastosowania w przypadku obrazów RGB
     * Nie działa przy obrazach monochromatycznych
     */
    public static int[][] convertTo2DArray(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth();
        int height = image.getHeight();

        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];

        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }

    /**
     * @param R int red
     * @param G int green
     * @param B int blue
     * @return Konwertowanie wartości int na 3 kanałach do postaci jendej liczby typu int
     */
    public static int convertFromRGBtoInt(int R, int G, int B) {
        int rgb = R;
        rgb = (rgb << 8) + G;
        rgb = (rgb << 8) + B;

        return rgb;
    }

    /**
     * @param image wczytane zdjęcie
     * @return Metoda sprawdza, czy zdjęcie jest monochromatyczne
     * Na każdnym kanale sprawdza, czy wartość piksela jest taka sama
     */
    public static boolean isGreyscale(BufferedImage image)
    {
        int pixel,red, green, blue;
        for (int i = 0; i < image.getWidth(); i++)
        {
            for (int j = 0; j < image.getHeight(); j++)
            {
                pixel = image.getRGB(i, j);
                red = (pixel >> 16) & 0xff;
                green = (pixel >> 8) & 0xff;
                blue = (pixel) & 0xff;
                if (red != green || green != blue ) return false;
            }
        }
        return true;
    }
}
