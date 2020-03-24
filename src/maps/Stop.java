package maps;

//import maps.Line;
import maps.Street;
import maps.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Stop {
    private String id;
    private Coordinate street_coordinates;
    private Street street_name;
    private List<Street> streets_map = new ArrayList<Street>();

    public Stop(String id, Coordinate street_coordinates)
    {
        this.id = id;
        this.street_coordinates = street_coordinates;
    }

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

    public void setStreet(Street s)
    {
        this.street_name = s;
    }

    @Override
    public String toString() {
        String stop_converstion = "stop(" + this.getId() + ")";
        return stop_converstion;
    }
}
