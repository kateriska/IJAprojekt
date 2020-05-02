/**
 * @author Katerina Fortova
 * @author Michal Machac
 * @since 2020-03-24
 */

package maps;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;

public class Stop {
    private String id;
    private Coordinate street_coordinates;
    private Street street_name;

    /**
     * Constructor for creating Stop object
     * @param id - id of Stop
     * @param street_coordinates - Coordinates of stop
     */
    public Stop(String id, Coordinate street_coordinates)
    {
        this.id = id;
        this.street_coordinates = street_coordinates;
    }

    /**
     * Create new stop with its id and coordinates
     * @param stop1  - id of Stop
     * @param c1 - coordinate of Stop
     * @return new_stop - new Stop object
     */
    public static Stop defaultStop(String stop1, Coordinate c1) {
        Stop new_stop = new Stop(stop1,c1);
        return new_stop;
    }

    /**
     * Get Coordinate object of Stop
     * @return street_coordinates - Coordinates of stop
     */
    public Coordinate getCoordinate()
    {
        return this.street_coordinates;
    }

    /**
     * Return id of Stop object
     * @return id - id of particular Stop object
     */
    public java.lang.String getId()
    {
        return this.id;
    }

    /**
     * Return Street object when Stop is
     * @return street_name - Street object of street of particular Stop
     */
    public Street getStreet()
    {
        return this.street_name;
    }

    /**
     * Set the Street object of stop
     * @param s - Street object
     */
    public void setStreet(Street s)
    {
        this.street_name = s;
    }

    /**
     * Conversion because of getRoute()
     * @return stop_conversion - Conversion of Stop
     */
    @Override
    public String toString() {
        String stop_conversion = "stop(" + this.getId() + ")";
        return stop_conversion;
    }

    /**
     * Highlight stop object in map and mark their name on map
     * @param anchor_pane_map - GUI component
     * @param color - specified color for marking Stop and their name
     */
    public void highlightStop(AnchorPane anchor_pane_map, Paint color)
    {
        Circle circle = new Circle(this.getCoordinate().getX(), this.getCoordinate().getY(), 5);
        circle.setStroke(color);
        circle.setStrokeWidth(5);
        Text text = new Text(this.getCoordinate().getX() + 10, this.getCoordinate().getY() - 7, this.getId());
        text.setStroke(color);
        anchor_pane_map.getChildren().addAll(circle, text);
    }
}
