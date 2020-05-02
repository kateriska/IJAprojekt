/**
 * @author Katerina Fortova
 * @author Michal Machac
 * @since 2020-03-24
 */

package maps;

import java.util.ArrayList;

public class Coordinate {

    private int x;
    private int y;
    private int diff_x;
    private int diff_y;

    /**
     * Create Coordinate object constructor
     * @param x - x value of coordinate
     * @param y - y value of coordinate
     */
    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Compute difference between two x-values
     * @param c - Coordinate object
     * @return diff_x - Difference between two x-values
     */
    public int diffX(Coordinate c)
    {
        diff_x = this.x - c.x;
        return diff_x;
    }

    /**
     * Compute difference between two y-values
     * @param c - - Coordinate object
     * @return diff_y - Difference between two y-values
     */
    public int diffY(Coordinate c)
    {
        diff_y = this.y - c.y;
        return diff_y;
    }

    /**
     * Equals method
     * @param obj - Object obj
     * @return - true when objects are equal, otherwise false
     */
    @Override
    public boolean equals(java.lang.Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        return true;
    }

    /**
     * Get x-value of Coordinate
     * @return x - x-value of Coordinate
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Get y-value of Coordinate
     * @return y - y-value of Coordinate
     */
    public int getY()
    {
        return this.y;
    }

    /**
     * Check whether some point is between two edge coordinates or not
     * @param coordinate1 - First edge coordinate
     * @param coordinate2 - Second edge coordinate
     * @return true when Coordinate is between them, otherwise false
     */
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

    /**
     * Check whether coordinate is in array
     * @param affected_points - list of Coordinate objects
     * @return true when Coordinate is in array, otherwise false
     */
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