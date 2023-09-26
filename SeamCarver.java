import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;      // current picture
    private int width;            // current pictures width (cols) (x)
    private int height;           // current pictures height (rows) (y)
    private double[][] picEnergy; // current pictures energy array

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
        calculateEnergyArray();
    }

    // calculates the energy array of a picture
    private void calculateEnergyArray() {
        this.picEnergy = new double[height][width];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                Color[] adjColors = adjColors(w, h);
                calculateEnergy(w, h, adjColors[0], adjColors[1], adjColors[2], adjColors[3]);
            }
        }
    }

    // returns the colors of the 4 adjacent colors taking into acount corner and edge cases
    private Color[] adjColors(int w, int h) {
        Color[] adjColors = new Color[4];

        Color leftX;
        Color rightX;
        Color upY;
        Color downY;
        // edge or corner case
        if (w == 0 || w == width - 1 || h == 0 || h == height - 1) {
            // leftX
            int leftW = w - 1;
            if (leftW < 0) leftW = width - 1;
            leftX = picture.get(leftW, h);

            // rightX
            int rightW = w + 1;
            if (rightW > width - 1) rightW = 0;
            rightX = picture.get(rightW, h);

            // upY
            int upH = h - 1;
            if (upH < 0) upH = height - 1;
            upY = picture.get(w, upH);

            // downY
            int downH = h + 1;
            if (downH > height - 1) downH = 0;
            downY = picture.get(w, downH);
        }
        // middle case
        else {
            leftX = picture.get(w - 1, h);
            rightX = picture.get(w + 1, h);
            upY = picture.get(w, h - 1);
            downY = picture.get(w, h + 1);
        }

        adjColors[0] = leftX;
        adjColors[1] = rightX;
        adjColors[2] = upY;
        adjColors[3] = downY;

        return adjColors;
    }


    // calculates the energy of the given pixel
    private void calculateEnergy(int w, int h, Color leftX, Color rightX, Color upY, Color downY) {
        double xGradient = Math.pow(rightX.getRed() - leftX.getRed(), 2)
                + Math.pow(rightX.getBlue() - leftX.getBlue(), 2)
                + Math.pow(rightX.getGreen() - leftX.getGreen(), 2);
        double yGradient = Math.pow(upY.getRed() - downY.getRed(), 2)
                + Math.pow(upY.getBlue() - downY.getBlue(), 2)
                + Math.pow(upY.getGreen() - downY.getGreen(), 2);
        double energy = Math.sqrt(xGradient + yGradient);
        picEnergy[h][w] = energy;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("x and/or y out of range");
        }
        return picEnergy[y][x];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        class Seam {

            private int[][] edgeTo = new int[height][width]; // edgeTo for shortest path
            private double[][] distTo = new double[height][width]; // distTo for shortest path

            // finds a  vertical seam
            private void findVertSeam() {

                for (int h = 0; h < height; h++) {
                     for (int w = 0; w < width; w++) {
                        if (h == 0) {
                            distTo[h][w] = picEnergy[h][w];
                        }
                        else {
                            distTo[h][w] = Double.POSITIVE_INFINITY;
                        }
                    }
                }

                for (int h = 0; h < height - 1; h++) {
                    for (int w = 0; w < width; w++) {
                        // left
                        if (!(w == 0)) {
                            relax(w, h, w - 1, h + 1);
                        }
                        // middle
                        relax(w, h, w, h + 1);
                        // right
                        if (!(w == width - 1)) {
                            relax(w, h, w + 1, h + 1);
                        }
                    }
                }

            }

            // relaxes a given edge
            private void relax(int fromW, int fromH, int toW, int toH) {
                if (distTo[toH][toW] > distTo[fromH][fromW] + picEnergy[toH][toW]) {
                    distTo[toH][toW] = distTo[fromH][fromW] + picEnergy[toH][toW];
                    edgeTo[toH][toW] = fromW;
                }
            }

        }

        Seam vertSeam = new Seam();
        vertSeam.findVertSeam();

        int colOfSeam = 0;
        double minSeamLength = -1;
        for (int w = 0; w < width; w++) {
            if (w == 0) {
                minSeamLength = vertSeam.distTo[height - 1][w];
            }
            if (vertSeam.distTo[height - 1][w] < minSeamLength) {
                minSeamLength = vertSeam.distTo[height - 1][w];
                colOfSeam = w;
            }
        }

        int[] seam = new int[height];
        for (int h = height - 1; h > -1; h--) {
            seam[h] = colOfSeam;
            colOfSeam = vertSeam.edgeTo[h][colOfSeam];
        }

        return seam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null arg");
        if (width == 1) throw new IllegalArgumentException("width is 1");
        if (seam.length != height) throw new IllegalArgumentException("incorrect seam length");

        // resize the picture, don't include the pixels on the seam
        Picture newPic = new Picture(width - 1, height);
        for (int h = 0; h < height; h++) {
            boolean test = false;
            for (int w = 0; w < width; w++) {
                if (w != seam[h]) {
                    if (test) {
                        newPic.set(w-1, h, picture.get(w, h));
                    }
                    else {
                        newPic.set(w, h, picture.get(w, h));
                    }
                }
                else {
                    test = true;
                }
            }
        }
        picture = newPic;
        width = picture.width();
        height = picture.height();

        // resize energy array, recalculate energy around the seam
        calculateEnergyArraySeamed(seam);

    }

    // calculates the energy array of a picture with a given vertical seam
    private void calculateEnergyArraySeamed(int[] vertSeam) {
        for (int i = 0; i < vertSeam.length - 2; i++) {
            if (Math.abs(vertSeam[i] - vertSeam[i+1]) > 1) {
                throw new IllegalArgumentException("disconnected seam");
            }
        }
        double[][] oldPicEnergy = this.picEnergy;
        this.picEnergy = new double[height][width];
        for (int h = 0; h < oldPicEnergy.length; h++) {
            boolean seamCheck = false;
            for (int w = 0; w < width; w++) {
                if (w == vertSeam[h] || w == vertSeam[h] - 1) {
                    Color[] adjColors = adjColors(w, h);
                    calculateEnergy(w, h, adjColors[0], adjColors[1], adjColors[2], adjColors[3]);
                    seamCheck = true;
                }
                else {
                    if (seamCheck) {
                        picEnergy[h][w] = oldPicEnergy[h][w+1];
                    }
                    else {
                        picEnergy[h][w] = oldPicEnergy[h][w];
                    }
                }
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horzSeam = findVerticalSeam();
        transpose();
        return horzSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("null arg");
        if (height == 1) throw new IllegalArgumentException("height is 1");
        if (seam.length != width) throw new IllegalArgumentException("incorrect seam length");

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // transposes the current energy array and picture
    private void transpose() {
        int savedH = this.height;
        this.height = this.width;
        this.width = savedH;

        double[][] picEnergyT = new double[height][width];
        Picture pictureT = new Picture(width, height);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                pictureT.set(w, h, picture.get(h, w));
                picEnergyT[h][w] = picEnergy[w][h];
            }
        }
        picEnergy = picEnergyT;
        this.picture = pictureT;
    }

    // unit testing (required)
    public static void main(String[] args) {

        boolean test = true && false;


        // SeamCarver()
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);

        // // picture()
        // StdOut.println(sc.picture());

        // // width()
        // StdOut.println(sc.width());

        // // height()
        // StdOut.println(sc.height());

        // // energy()
        // StdOut.println(sc.energy(0, 0));

        // // findVerticalSeam()
        // int[] vertSeam = sc.findVerticalSeam();
        // StdOut.println(Arrays.toString(vertSeam));

        // // removeVerticalSeam()
        // StdOut.println(sc.picture);
        // double[][] picEnergy1 = sc.picEnergy;
        // // int[] test = {3,4,2,1,1};
        // // sc.removeVerticalSeam(test);
        // sc.removeVerticalSeam(vertSeam);
        //
        // StdOut.println(sc.picture);
        // double[][] picEnergy2 = sc.picEnergy;

        // // findHorizontalSeam()
        // int[] horzSeam = sc.findHorizontalSeam();
        // StdOut.println(Arrays.toString(horzSeam));

        // // removeHorizontalSeam()
        // sc.removeHorizontalSeam(horzSeam);
    }

}
