package maps;

public class Stop {
    private String id;
    private Coordinate street_coordinates;
    private Street street_name;

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
        String stop_conversion = "stop(" + this.getId() + ")";
        return stop_conversion;
    }
}
