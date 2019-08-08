import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {

    private Picture picture;
    private int[] edgeTo;
    private double[] distTo;
    private double[] pixelEnergy;

    // create a seam carver object based on the given picture
    public SeamCarver(final Picture picture) {
        assertNull(picture);
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        assertBoundaries(x, y);
        return computePixelEnergy(x, y);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        buildGraphForImage();

        // build SPT
        for (int col = 0; col < width() - 1; col++) {
            for (int row = 0; row < height(); row++) {
                int curPixel = pixel(col, row);

                for (int adj : adjHoriz(col, row)) {
                    relax(curPixel, adj);
                }
            }
        }

        // find seam
        int pixelWithMinDist = findPixelWithMinDistOnTheRight();
        return findHorizontalSeamForPixel(pixelWithMinDist);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        buildGraphForImage();

        // build SPT
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                int curPixel = pixel(col, row);

                for (int adj : adjVert(col, row)) {
                    relax(curPixel, adj);
                }
            }
        }

        // find seam
        int pixelWithMinDist = findPixelWithMinDistOnTheBottom();
        return findVerticalSeamForPixel(pixelWithMinDist);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int width = picture.width();
        int height = picture.height();

        assertNull(seam);
        assertSeamLength(seam, width);
        assertIfItIsSeam(seam);
        assertCanRemoveSeam(height);

        Picture newPicture = new Picture(width, height - 1);

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < seam[col]; row++) {
                newPicture.set(col, row, picture.get(col, row));
            }
            for (int row = seam[col] + 1; row < height(); row++) {
                newPicture.set(col, row - 1, picture.get(col, row));
            }
        }

        picture = newPicture;
        edgeTo = null;
        distTo = null;
        pixelEnergy = null;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int width = picture.width();
        int height = picture.height();

        assertNull(seam);
        assertSeamLength(seam, height);
        assertIfItIsSeam(seam);
        assertCanRemoveSeam(width);

        Picture newPicture = new Picture(width - 1, height);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < seam[row]; col++) {
                newPicture.set(col, row, picture.get(col, row));
            }

            for (int col = seam[row] + 1; col < width(); col++) {
                newPicture.set(col - 1, row, picture.get(col, row));
            }
        }

        picture = newPicture;

        edgeTo = null;
        distTo = null;
        pixelEnergy = null;
    }

    private void buildGraphForImage() {
        int width = width();
        int height = height();

        edgeTo = new int[width * height];
        distTo = new double[width * height];
        pixelEnergy = new double[width * height];

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int curPixel = pixel(col, row);

                if (col > 0) {
                    distTo[curPixel] = Double.POSITIVE_INFINITY;
                } else {
                    distTo[curPixel] = 0.0;
                }

                edgeTo[curPixel] = -1;
                pixelEnergy[curPixel] = computePixelEnergy(col, row);
            }
        }
    }

    private List<Integer> adjVert(int col, int row) {
        List<Integer> adj = new ArrayList<>();
        // left bottom pixel
        if (col > 0) {
            adj.add(pixel(col - 1, row + 1));
        }

        // bottom pixel
        adj.add(pixel(col, row + 1));

        // right bottom pixel
        if (col < width() - 1) {
            adj.add(pixel(col + 1, row + 1));
        }

        return adj;
    }

    private List<Integer> adjHoriz(int col, int row) {
        List<Integer> adj = new ArrayList<>();
        // top right pixel
        if (row > 0) {
            adj.add(pixel(col + 1, row - 1));
        }

        // right pixel
        adj.add(pixel(col + 1, row));

        // bottom right pixel
        if (row < height() - 1) {
            adj.add(pixel(col + 1, row + 1));
        }

        return adj;
    }


    private int[] findVerticalSeamForPixel(int pixelWithMinDist) {
        int[] seam = new int[height()];

        int curPixel = pixelWithMinDist;
        while (curPixel != -1) {
            int pixelRow = pixelRow(curPixel);
            int pixelCol = pixelCol(curPixel);
            seam[pixelRow] = pixelCol;

            curPixel = edgeTo[curPixel];
        }

        return seam;
    }

    private int[] findHorizontalSeamForPixel(int pixelWithMinDist) {
        int[] seam = new int[width()];

        int curPixel = pixelWithMinDist;
        while (curPixel != -1) {
            int pixelRow = pixelRow(curPixel);
            int pixelCol = pixelCol(curPixel);
            seam[pixelCol] = pixelRow;

            curPixel = edgeTo[curPixel];
        }

        return seam;
    }

    private int pixelRow(int pixelWithMinDist) {
        return pixelWithMinDist / width();
    }

    private int pixelCol(int pixelWithMinDist) {
        return pixelWithMinDist % width();
    }

    private int findPixelWithMinDistOnTheBottom() {
        double minDist = Double.POSITIVE_INFINITY;
        int height = height();
        int pixelWithMinDist = -1;
        for (int col = 0; col < width(); col++) {
            int curPixel = pixel(col, height - 1);
            if (distTo[curPixel] < minDist) {
                minDist = distTo[curPixel];
                pixelWithMinDist = curPixel;
            }
        }

        return pixelWithMinDist;
    }

    private int findPixelWithMinDistOnTheRight() {
        double minDist = Double.POSITIVE_INFINITY;
        int width = width();
        int pixelWithMinDist = -1;
        for (int row = 0; row < height(); row++) {
            int curPixel = pixel(width - 1, row);
            if (distTo[curPixel] < minDist) {
                minDist = distTo[curPixel];
                pixelWithMinDist = curPixel;
            }
        }

        return pixelWithMinDist;
    }

    private void relax(int v, int w) {
        if (distTo[w] > distTo[v] + pixelEnergy[w]) {
            distTo[w] = distTo[v] + pixelEnergy[w];
            edgeTo[w] = v;
        }
    }

    private int pixel(int col, int row) {
        return width() * row + col;
    }

    private double computePixelEnergy(int col, int row) {
        if (isBorderPixel(col, row)) {
            return 1000;
        }
        Color colorLeft = picture.get(col - 1, row);
        Color colorRight = picture.get(col + 1, row);
        Color colorTop = picture.get(col, row - 1);
        Color colorBottom = picture.get(col, row + 1);

        double xGradient = calculateGradient(colorLeft, colorRight);
        double yGradient = calculateGradient(colorTop, colorBottom);

        return Math.sqrt(xGradient + yGradient);
    }

    private double calculateGradient(Color either, Color another) {
        int redDiff = either.getRed() - another.getRed();
        int greenDiff = either.getGreen() - another.getGreen();
        int blueDiff = either.getBlue() - another.getBlue();

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

    private boolean isBorderPixel(int x, int y) {
        return x == 0 || y == 0 || x == width() - 1 || y == height() - 1;
    }

    private void assertBoundaries(int x, int y) {
        int width = width();
        int height = height();
        if (x > width - 1 || x < 0 || y > height - 1 || y < 0 ) {
            throw new IllegalArgumentException(String.format("Index (x,y) = (%d, %d) is out of the boundaries. Range is (%d, %d)",
                    x, y, width, height));
        }
    }

    private void assertNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Passing null argument to the method is not acceptable");
        }
    }

    private void assertSeamLength(int[] seam, int length) {
        int seamLength = seam.length;
        if (seamLength != length) {
            throw new IllegalArgumentException(String.format("Seam length must be \"%d\". Actual size is \"%d\"", seamLength, length));
        }
    }

    private void assertIfItIsSeam(int[] seam) {
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new IllegalArgumentException(String.format("Seam is not correct, please validate it on index \"%d\"", i));
            }
        }
    }

    private void assertCanRemoveSeam(int pictureSize) {
        if (pictureSize <= 1) {
            throw new IllegalArgumentException("Can't remove a seam because the picture dimension size is 1");
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("SeamCarving/src/main/resources/6x5.png");
        SeamCarver sc = new SeamCarver(picture);

        int[] vertSeam = sc.findVerticalSeam();
        /*sc.removeVerticalSeam(vertSeam);*/

        int[] horizSeam = sc.findHorizontalSeam();

        //sc.removeHorizontalSeam(horizSeam);
    }


}
