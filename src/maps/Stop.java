package maps;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Stop {
    private String id;
    private Coordinate street_coordinates;
    private Street street_name;

    public Stop(String id, Coordinate street_coordinates)
    {
        this.id = id;
        this.street_coordinates = street_coordinates;
    }

    // create new stop with its id and coordinates
    public static Stop defaultStop(String stop1, Coordinate c1) {
        Stop new_stop = new Stop(stop1,c1);
        return new_stop;
    }

    public Coordinate getCoordinate()
    {
        return this.street_coordinates;
    }

    public java.lang.String getId()
    {
        return this.id;
    }

    public Street getStreet()
    {
        return this.street_name;
    }

    // set the Street object of stop
    public void setStreet(Street s)
    {
        this.street_name = s;
    }

    // conversion because of getRoute()
    @Override
    public String toString() {
        String stop_conversion = "stop(" + this.getId() + ")";
        return stop_conversion;
    }

    //  highlight stop object in map
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
