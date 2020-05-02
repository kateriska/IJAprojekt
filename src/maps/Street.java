/**
 * @author Katerina Fortova
 * @author Michal Machac
 * @since 2020-03-24
 */

package maps;

import java.util.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.paint.Paint;

public class Street {
    private String street_id;
    private Coordinate coordinate1;
    private Coordinate coordinate2;
    private Coordinate coordinate_end;
    private List<Coordinate> all_coordinates_list = new ArrayList<Coordinate>();
    private List<Stop> all_stops_list = new ArrayList<Stop>();

    /**
     * Constructor for creating Street object for right angle Streets with 3 init points
     * @param street_id - id of Street
     * @param coordinate1 - Begin coordinate for Street object
     * @param coordinate2 - Middle coordinate for right angle street
     * @param coordinate_end - End coordinate of Street object
     */
    public Street(String street_id, Coordinate coordinate1, Coordinate coordinate2, Coordinate coordinate_end)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
        this.coordinate_end = coordinate_end;
    }

    /**
     * Constructor for creating Street object for normal Streets with 2 init points
     * @param street_id - id of Street
     * @param coordinate1 - Begin coordinate for Street object
     * @param coordinate_end - End coordinate of Street object
     */
    public Street(String street_id, Coordinate coordinate1, Coordinate coordinate_end)
    {
        this.street_id = street_id;
        this.coordinate1 = coordinate1;
        this.coordinate_end = coordinate_end;

    }


    /**
     * Create Street object for normal streets
     * @param first - id of Street
     * @param coordinate1 - Begin coordinate for Street object
     * @param coordinate_end - End coordinate of Street object
     * @return
     */
    public static Street defaultStreet(String first, Coordinate coordinate1, Coordinate coordinate_end) {
        Street new_street = new Street(first, coordinate1, coordinate_end);
        return new_street;
    }

    /**
     * Create Street object for right angle streets - important to check whether two lines have 90 degrees (right angle)
     * @param first - id of Street
     * @param coordinate1 - Begin coordinate for Street object
     * @param coordinate2 - Middle coordinate for right angle street
     * @param coordinate_end - End coordinate of Street object
     * @return
     */
    public static Street defaultStreet(String first, Coordinate coordinate1, Coordinate coordinate2, Coordinate coordinate_end) {
        double length1 = Math.sqrt(Math.pow(coordinate2.diffX(coordinate1),2) + Math.pow(coordinate2.diffY(coordinate1),2));
        double length2 = Math.sqrt(Math.pow(coordinate_end.diffX(coordinate1),2) + Math.pow(coordinate_end.diffY(coordinate1),2));
        double length3 = Math.sqrt(Math.pow(coordinate2.diffX(coordinate_end),2) + Math.pow(coordinate2.diffY(coordinate_end),2));

        if (Math.pow(length1,2) + Math.pow(length3,2) != Math.pow(length2,2))
        {
            return null;
        }

        Street new_street = new Street(first, coordinate1, coordinate2, coordinate_end);
        return new_street;
    }

    /**
     * Get first Coordinate of Street
     * @return coordinate1 - First Coordinate of Street object
     */
    public Coordinate begin()
    {
        return coordinate1;
    }

    /**
     * Get last Coordinate of Street
     * @return coordinate_end - End coordinate of Street object
     */
    public Coordinate end()
    {
        return coordinate_end;
    }

    /**
     * Get list of init Coordinates of Street object
     * for classic Streets with two coordinates this list looks like coordinate1, null, coordinate2
     * for right angle streets with 3 coordinates this list looks like coordinate1, coordinate2, coordinate3
     * @return all_coordinates_list - list of all init Coordinates for particular Street
     */
    public java.util.List<Coordinate> getCoordinates()
    {
        all_coordinates_list.add(coordinate1);
        all_coordinates_list.add(coordinate2);
        all_coordinates_list.add(coordinate_end);
        return this.all_coordinates_list;
    }

    /**
     * Get id of street
     * @return street_id - id of Street object
     */
    public java.lang.String getId()
    {
        return street_id;
    }

    /**
     * Get list with all Stops on Street
     * @return all_stops_list - List with all Stops
     */
    public java.util.List<Stop> getStops()
    {
        return this.all_stops_list;
    }


    /**
     * Check whether two streets follow each other
     * @param s - Street object
     * @return true when two streets follow each other, otherwise false
     */
    public boolean follows(Street s) {
        java.util.List<Coordinate> this_street_coordinates = this.getCoordinates();
        java.util.List<Coordinate> s_street_coordinates = s.getCoordinates();

        Coordinate this_last_point = this_street_coordinates.get(2);
        Coordinate this_first_point = this_street_coordinates.get(0);
        Coordinate s_first_point = s_street_coordinates.get(0);
        Coordinate s_last_point = s_street_coordinates.get(2);

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


    /**
     * This method checks whether stop is on street or not
     * I just needed to round the distances to int from float with many numbers, because sometimes it was too strict
     * @param stop - Stop object which should be added
     * @param right_angle_street - check whether we want to put Stop object to right angle street
     * @return true, when Stop is added to list with all Stops of Street, otherwise false
     */
    public boolean addStop(Stop stop, boolean right_angle_street) {
        java.util.List<Coordinate> this_street_coordinates = this.getCoordinates();
        Coordinate stop_coordinates = stop.getCoordinate();

        if (right_angle_street == false)
        {
            double distance1 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - stop_coordinates.getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - stop_coordinates.getY()), 2));
            double distance2 = Math.sqrt(Math.pow((stop_coordinates.getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((stop_coordinates.getY() - this_street_coordinates.get(2).getY()), 2));
            double distance3 = Math.sqrt(Math.pow((this_street_coordinates.get(0).getX() - this_street_coordinates.get(2).getX()), 2) + Math.pow((this_street_coordinates.get(0).getY() - this_street_coordinates.get(2).getY()), 2));

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

    /**
     * Highlight street in map, for right angle street need to create two lines instead one for this type of streets
     * @param anchor_pane_map - GUI component
     * @param all_streets_lines - list of all Line objects which represents Street on map
     * @param color - Color for highlighted Street on map
     */
    public void highlightStreet(AnchorPane anchor_pane_map, ArrayList<Line> all_streets_lines, Paint color )
    {
        Line line1 = null;
        Line line2 = null;
        if (this.getCoordinates().get(1) != null) {
            line1 = new Line(this.getCoordinates().get(0).getX(), this.getCoordinates().get(0).getY(), this.getCoordinates().get(1).getX(), this.getCoordinates().get(1).getY());
            line2 = new Line(this.getCoordinates().get(1).getX(), this.getCoordinates().get(1).getY(), this.getCoordinates().get(2).getX(), this.getCoordinates().get(2).getY());
            line1.setStroke(color);
            line1.setStrokeWidth(5);
            line2.setStroke(color);
            line2.setStrokeWidth(5);
            all_streets_lines.add(line1);
            all_streets_lines.add(line2);
            anchor_pane_map.getChildren().addAll(line1, line2);
        } else {
            line1 = new Line(this.getCoordinates().get(0).getX(), this.getCoordinates().get(0).getY(), this.getCoordinates().get(2).getX(), this.getCoordinates().get(2).getY());
            line1.setStroke(color);
            line1.setStrokeWidth(5);
            all_streets_lines.add(line1);
            anchor_pane_map.getChildren().addAll(line1);
        }
    }

}