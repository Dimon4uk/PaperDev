package com.dev.core;
import java.util.LinkedList;

public class PartFinder {

    private static int rows;
    private static int cols;

    private static byte[][] boardArr;

    public PartFinder(byte[][] mas, int row, int col) {
        rows = row + 2;
        cols = col + 2;
        boardArr = getBoarsMas(mas);
    }

    public final LinkedList<LinkedList<Coordinate>> getResults () {
        Coordinate start;
        LinkedList<LinkedList<Coordinate>> parts = new LinkedList<>();
        start = findFirstNotZero();
        while (start.getX() > -1) {
            parts.add(findPart(start));
            start = findFirstNotZero();
        }
        return parts;
    }

    private static byte[][] getBoarsMas(byte[][] mas) {
        byte [][] copyMas = new byte[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || j == 0 || i == rows-1 || j == cols-1)
                    copyMas[i][j] = 0;
                else
                    copyMas[i][j] = mas[i-1][j-1];
            }
        }
        return copyMas;
    }

    private static Coordinate findFirstNotZero() {

        for (int i = 1; i < rows-1; i++) {
            for (int j = 1; j < cols-1; j++) {
                if(boardArr[i][j] != 0)
                    return new Coordinate(i,j);
            }
        }
        return new Coordinate(-1,-1);
    }

    private static LinkedList<Coordinate> findPart(Coordinate start) {
        int y = start.getY();
        int x = start.getX();
        LinkedList<Coordinate> onePart = new LinkedList<>();
        LinkedList<Coordinate> coords = new LinkedList<>();
        boolean found = false;

        while(!found){
            if(boardArr[y][x] == 1) {
                onePart.add(new Coordinate(y,x));
                boardArr[y][x] = 0;

                if(boardArr[y-1][x-1] == 1)
                    coords.add(new Coordinate(y-1,x-1));    // save top_left position
                if(boardArr[y-1][x] == 1)
                    coords.add(new Coordinate(y-1,x));      // save top position
                if(boardArr[y][x+1] == 1)
                    coords.add(new Coordinate(y,x+1));      // save right position
                if(boardArr[y-1][x+1] == 1)
                    coords.add(new Coordinate(y-1,x+1));    // save top_right position
                if(boardArr[y+1][x+1] == 1)
                    coords.add(new Coordinate(y+1,x+1));    // save bottom_right position
                if(boardArr[y+1][x] == 1)
                    coords.add(new Coordinate(y+1,x));      // save bottom position
                if(boardArr[y+1][x-1] == 1)
                    coords.add(new Coordinate(y+1,x-1));    // save bottom_left position
                if(boardArr[y][x-1] == 1)
                    coords.add(new Coordinate(y,x-1));      // save left position

                if(boardArr[y][x+1] == 1) {
                    x++;                                // move to right
                } else if(boardArr[y+1][x] == 1) {
                    y++;                                // move to bottom
                } else if(boardArr[y][x-1] == 1) {
                    x--;                                // move to left
                } else if(boardArr[y-1][x] == 1) {
                    y--;                                // move to top
                } else if(boardArr[y-1][x-1] == 1) {
                    y--;                                // move to top_left
                    x--;
                } else if(boardArr[y-1][x+1] == 1) {
                    y--;                                // move to top_right
                    x++;
                } else if(boardArr[y+1][x-1] == 1) {
                    y++;                                // move to bottom_left
                    x--;
                } else if(boardArr[y+1][x+1] == 1) {
                    y++;                                // move to bottom_right
                    x++;
                }

            } else {
                if(!coords.isEmpty()) {     // move to saved position
                    y = coords.getLast().getY();
                    x = coords.getLast().getX();
                    coords.remove(coords.getLast());

                } else
                    found = true;
            }
        }
        for (Coordinate Coordinate : onePart)
            Coordinate.decrementsYX();

        return onePart;
    }



}
