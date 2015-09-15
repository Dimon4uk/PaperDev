package com.dev.core;


public class Coordinate implements Comparable<Coordinate> {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected void decrementsYX() {
        this.y--;
        this.x--;
    }

    public Coordinate(int y, int x) {
    this.y = y;
    this.x = x;
}

    @Override
    public int compareTo(Coordinate coords) {
        int cY = coords.getY();
        int cX = coords.getX();
        if (this.y > cY)
            return 1;
        else if(this.y == cY) {
            if (this.x > cX)
                return 1;
            else if(this.x == cX)
                return 0;
            else
            return -1;
        }
        else
            return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;

        Coordinate that = (Coordinate) o;

        return x == that.x && y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
