package maps;
//import maps.Line;
//import maps.Stop;
//import maps.Street;

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


    public static Coordinate create(int x, int y)
    {
        if (x < 0 || y < 0) {
            return null;
        }


        Coordinate coordinates = new Coordinate(x, y);

        return coordinates;
    }

    public int diffX(Coordinate c)
    {
        diff_x = this.x - c.x;
        return diff_x;
    }

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

    @Override
    public int hashCode()
    {
        return 1;
    }





}