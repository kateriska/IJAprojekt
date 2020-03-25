package maps;

//import maps.Stop;
//import maps.Line;

import maps.Coordinate;

import java.util.*;

public class Street {
    private String street_id;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private Coordinate coordinate_end;
    private List<Coordinate> all_coordinates_list = new ArrayList<Coordinate>();
    private List<Stop> all_stops_list = new ArrayList<Stop>();

    public Street(String street_id, Coordinate coordinate1, Coordinate coordinate2, Coordinate coordinate_end)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
        this.coordinate_end = coordinate_end;
    }

    public Street(String street_id, Coordinate coordinate1, Coordinate coordinate_end)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate_end = coordinate_end;

    }


    public static Street defaultStreet(String first, Coordinate coordinate1, Coordinate coordinate_end) {
        Street new_street = new Street(first, coordinate1, coordinate_end);
        return new_street;
    }

    public static Street defaultStreet(String first, Coordinate coordinate1, Coordinate coordinate2, Coordinate coordinate_end) {
        double length1 = Math.sqrt(Math.pow(coordinate2.diffX(coordinate1),2) + Math.pow(coordinate2.diffY(coordinate1),2));
        double length2 = Math.sqrt(Math.pow(coordinate_end.diffX(coordinate1),2) + Math.pow(coordinate_end.diffY(coordinate1),2));
        double length3 = Math.sqrt(Math.pow(coordinate2.diffX(coordinate_end),2) + Math.pow(coordinate2.diffY(coordinate_end),2));

        System.out.println(length1);
        System.out.println(length2);
        System.out.println(length3);
        System.out.println(Math.pow(length2,2));
        System.out.println(Math.pow(length1,2) + Math.pow(length3,2));

        if (Math.pow(length1,2) + Math.pow(length3,2) != Math.pow(length2,2))
        {
            return null;
        }

        Street new_street = new Street(first, coordinate1, coordinate2, coordinate_end);
        return new_street;
    }


    public Coordinate begin()
    {
        return coordinate1;
    }


    public Coordinate end()
    {
        return coordinate_end;
    }


    public java.util.List<Coordinate> getCoordinates()
    {
        all_coordinates_list.add(coordinate1);
        all_coordinates_list.add(coordinate2);
        all_coordinates_list.add(coordinate_end);
        return this.all_coordinates_list;
    }

    public java.lang.String getId()
    {
        return street_id;
    }

    public java.util.List<Stop> getStops()
    {
        return this.all_stops_list;
    }



    public boolean follows(Street s) {
        java.util.List<Coordinate> this_street_coordinates = this.getCoordinates();
        java.util.List<Coordinate> s_street_coordinates = s.getCoordinates();

        //System.out.println(this_street_coordinates);
        //System.out.println(s_street_coordinates);
        Coordinate this_last_point = this_street_coordinates.get(2);
        Coordinate this_first_point = this_street_coordinates.get(0);
        Coordinate s_first_point = s_street_coordinates.get(0);
        Coordinate s_last_point = s_street_coordinates.get(2);

        //System.out.println(this_last_point.getX());
        //System.out.println(s_first_point.getX());
        //System.out.println(this_last_point.getY());
        //System.out.println(s_first_point.getY());

        // 2,1
        if (this_last_point.getX() == s_first_point.getX() && this_last_point.getY() == s_first_point.getY())
        {
            return true;
        }
        // 1,2
        else if (this_first_point.getX() == s_last_point.getX() && this_first_point.getY() == s_last_point.getY())
        {
            return true;
        }
        // 1,1
        else if (this_first_point.getX() == s_first_point.getX() && this_first_point.getY() == s_first_point.getY())
        {
            return true;
        }
        // 2,2
        else if (this_last_point.getX() == s_last_point.getX() && this_last_point.getY() == s_last_point.getY())
        {
            return true;
        }
        else
        {
            return false;
        }


    }


    public boolean addStop(Stop stop, boolean right_angle_street) {
        java.util.List<Coordinate> this_street_coordinates = this.getCoordinates();
        Coordinate stop_coordinates = stop.getCoordinate();

        if (right_angle_street == false)
        {
            double distance1 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - stop_coordinates.getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - stop_coordinates.getY()), 2));
            double distance2 = Math.sqrt(Math.pow((stop_coordinates.getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((stop_coordinates.getY() - this_street_coordinates.get(2).getY()), 2));
            double distance3 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - this_street_coordinates.get(2).getY()), 2));

            System.out.println(distance1);
            System.out.println(distance2);
            System.out.println(distance3);
            System.out.println(distance1+distance2);

            long result1 = Math.round(distance1 + distance2);
            long result2 = Math.round(distance3);
            if (result1 == result2)
            {
                stop.setStreet(this);
                all_stops_list.add(stop);
                return true;
            }
            else
            {
                return false;
            }

        }
        else
        {
            double distance1 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - stop_coordinates.getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - stop_coordinates.getY()), 2));
            double distance2 = Math.sqrt(Math.pow((stop_coordinates.getX() - this_street_coordinates.get(1).getX()), 2) + Math.pow((stop_coordinates.getY() - this_street_coordinates.get(1).getY()), 2));
            double distance3 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - this_street_coordinates.get(1).getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - this_street_coordinates.get(1).getY()), 2));

            double distance4 = Math.sqrt(Math.pow((this_street_coordinates.get(1).getX() - stop_coordinates.getX()), 2) + Math.pow((this_street_coordinates.get(2).getY() - stop_coordinates.getY()), 2));
            double distance5 = Math.sqrt(Math.pow((stop_coordinates.getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((stop_coordinates.getY() - this_street_coordinates.get(2).getY()), 2));
            double distance6 = Math.sqrt(Math.pow((this_street_coordinates.get(1).getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((this_street_coordinates.get(1).getY() - this_street_coordinates.get(2).getY()), 2));

            long result1 = Math.round(distance1 + distance2);
            long result2 = Math.round(distance3);
            long result3 = Math.round(distance4 + distance5);
            long result4 = Math.round(distance6);

            if (result1 == result2 || result3 == result4)
            {
                stop.setStreet(this);
                all_stops_list.add(stop);
                return true;
            }
            else
            {
                return false;
            }

        }

    }


}