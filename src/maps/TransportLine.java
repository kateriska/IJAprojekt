package maps;

import java.util.ArrayList;
import java.util.List;

public class TransportLine {
    private String line_id;
    private List<Street> streets_map = new ArrayList<Street>();
    private List<Stop> stops_map = new ArrayList<Stop>();

    public TransportLine(String line_id)
    {
        this.line_id = line_id;
    }
    public boolean addStop(Stop stop)
    {
        stops_map.add(stop);
        addStreet(stop.getStreet());

        if (streets_map.size() > 1)
        {
            System.out.println("More than one street in line");
            if (streets_map.get(0).follows(streets_map.get(1)) == false || streets_map.get(0).follows(streets_map.get(1)) == false)
            {
                stops_map.remove(stop);
                streets_map.remove(stop.getStreet());
                return false;
            }
        }

        return true;
    }

    public boolean addStreet(Street street)
    {
        streets_map.add(street);
        return true;
    }

    public static TransportLine defaultLine(java.lang.String id)
    {
        TransportLine new_line = new TransportLine(id);
        return new_line;
    }

    public java.util.List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> getRoute()
    {
        List<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>> roads_map = new ArrayList<java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>>();
        java.util.AbstractMap.SimpleImmutableEntry<Street,Stop> entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(null, null);

        for (Street s : streets_map)
        {
            if (s.getStops().isEmpty() == false)
            {
                for (int i = 0; i < s.getStops().size(); i++)
                {
                    entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(s,s.getStops().get(i));
                }

            }
            else
            {
                entry = new java.util.AbstractMap.SimpleImmutableEntry<Street,Stop>(s,null);
            }

            roads_map.add(entry);

        }

        return roads_map;

    }

    public String getId()
    {
        return line_id;
    }

    public List<Stop> getStopsMap()
    {
        return stops_map;
    }

    public List<Street> getStreetsMap()
    {
        return streets_map;
    }


    /*
    @return arraylist of all coordinates of specified path, they are sorted in the way how vehicle is travelling through them,
    the right order is important
     */
    public ArrayList<Coordinate> transportLinePath()
    {
        Coordinate next_street1 = null;
        Coordinate next_street2 = null;
        Street next_street = null;
        ArrayList<Coordinate> line_coordinates = new ArrayList<Coordinate>();

        for (int i = 0; i < getStreetsMap().size(); i++)
        {
            Street s = getStreetsMap().get(i);
            Coordinate this_street1 = s.getCoordinates().get(0);
            Coordinate this_street2 = s.getCoordinates().get(2);

            if (i + 1 < getStreetsMap().size())
            {
                next_street = getStreetsMap().get(i+1);
                next_street1 = next_street.getCoordinates().get(0);
                next_street2 = next_street.getCoordinates().get(2);
            }
            else
            {
                break;
            }

            for (Stop stop : getStopsMap())
            {
                if (stop.getStreet().equals(s))
                {
                    line_coordinates.add(stop.getCoordinate());
                }
            }

            //11
            if (this_street1.getX() == next_street1.getX() && this_street1.getY() == next_street1.getY())
            {
                line_coordinates.add(this_street1);
            }
            //12
            else if (this_street1.getX() == next_street2.getX() && this_street1.getY() == next_street2.getY())
            {
                line_coordinates.add(this_street1);
            }
            // 21
            else if (this_street2.getX() == next_street1.getX() && this_street2.getY() == next_street1.getY())
            {
                line_coordinates.add(this_street2);
            }
            //22
            else if (this_street2.getX() == next_street2.getX() && this_street2.getY() == next_street2.getY())
            {
                line_coordinates.add(this_street2);
            }


        }

        line_coordinates.add(getStopsMap().get(getStopsMap().size()-1).getCoordinate());
        return line_coordinates;
    }


    /*
    @ return arraylist of IDs of Coordinates in the order which is specified path of bus, the correct order is important
     */
    public ArrayList<String> transportLinePathIDs()
    {
        Coordinate next_street1 = null;
        Coordinate next_street2 = null;
        Street next_street = null;
        ArrayList<String> line_coordinates_ids = new ArrayList<String>();

        for (int i = 0; i < getStreetsMap().size(); i++)
        {
            Street s = getStreetsMap().get(i);
            Coordinate this_street1 = s.getCoordinates().get(0);
            Coordinate this_street2 = s.getCoordinates().get(2);

            if (i + 1 < getStreetsMap().size())
            {
                next_street = getStreetsMap().get(i+1);
                next_street1 = next_street.getCoordinates().get(0);
                next_street2 = next_street.getCoordinates().get(2);
            }
            else
            {
                break;
            }

            for (Stop stop : getStopsMap())
            {
                if (stop.getStreet().equals(s))
                {
                    line_coordinates_ids.add(stop.getId());
                }
            }

            //11
            if (this_street1.getX() == next_street1.getX() && this_street1.getY() == next_street1.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            //12
            else if (this_street1.getX() == next_street2.getX() && this_street1.getY() == next_street2.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            // 21
            else if (this_street2.getX() == next_street1.getX() && this_street2.getY() == next_street1.getY())
            {
                line_coordinates_ids.add(s.getId());
            }
            //22
            else if (this_street2.getX() == next_street2.getX() && this_street2.getY() == next_street2.getY())
            {
                line_coordinates_ids.add(s.getId());
            }


        }

        line_coordinates_ids.add(getStopsMap().get(getStopsMap().size()-1).getId());
        return line_coordinates_ids;
    }


}
