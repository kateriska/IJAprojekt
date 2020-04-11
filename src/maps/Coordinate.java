package maps;

import java.util.ArrayList;

public class Coordinate {

    private int x;
    private int y;
    private int diff_x;
    private int diff_y;

    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }


    // create Coordinate object
    public static Coordinate create(int x, int y)
    {
        if (x < 0 || y < 0) {
            return null;
        }


        Coordinate coordinates = new Coordinate(x, y);

        return coordinates;
    }

    // difference between two x values
    public int diffX(Coordinate c)
    {
        diff_x = this.x - c.x;
        return diff_x;
    }

    // difference between two y values
    public int diffY(Coordinate c)
    {
        diff_y = this.y - c.y;
        return diff_y;
    }

    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        return true;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    // check whether some point is between two edge coordinates or not
    public boolean isBetweenTwoCoordinates(Coordinate coordinate1, Coordinate coordinate2) {

        double distance1 = Math.sqrt(Math.pow((coordinate1.getX() - this.getX()), 2) + Math.pow((coordinate1.getY() - this.getY()), 2));
        double distance2 = Math.sqrt(Math.pow((this.getX() - coordinate2.getX()), 2) + Math.pow((this.getY() - coordinate2.getY()), 2));
        double distance3 = Math.sqrt(Math.pow((coordinate1.getX() - coordinate2.getX()), 2) + Math.pow((coordinate1.getY() - coordinate2.getY()), 2));

        long result1 = Math.round(distance1 + distance2);
        long result2 = Math.round(distance3);
        if (result1 == result2)
        {

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isInArray(ArrayList<Coordinate> affected_points)
    {
        for (Coordinate c : affected_points)
        {
            if (c.getX() == this.getX() && c.getY() == this.getY())
            {
                return true;
            }
        }

        return false;
    }





}