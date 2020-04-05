// Import classes
import javafx.animation.Animation;
import maps.Coordinate;
import maps.Street;
import maps.Stop;
import maps.TransportLine;
// Import java classes
import javafx.scene.image.Image;
import java.awt.geom.Point2D;
import java.io.*;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.input.*;
import java.awt.MouseInfo;
import javafx.event.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args); // launch app
    }

    @Override
    public void start(Stage stage) throws Exception {
        ArrayList<Street> streets_list = new ArrayList<Street>();
        ArrayList<Stop> stops_list = new ArrayList<Stop>();

        BorderPane root = new BorderPane(); // create new pane for GUI
        Scene scene = new Scene(root, 1000, 771); // set width and height of window

        Line line1 = null;
        Line line2 = null;

        File file = new File("C:/Users/forto/IdeaProjects/proj/lib/map.png");
        BackgroundImage myBI = new BackgroundImage(new Image(file.toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI)); // set map as background, with no repeat and also some free space for TO DO GUI components
        // beginning of coordinates [0,0] is in left upon corner of whole window and also it is beginning for image

        /*
        Point2D p = MouseInfo.getPointerInfo().getLocation();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // show specific coordinates after mouse clicking in console - useful for positioning of streets and stops
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
            }
        });
        */

        streets_list = setMapStreets();

        /*
        highlight all street objects in map - streets with two coordinates with yellow and
        streets with three coordinates (right angle streets) with red - need to create two lines instead one for this type of streets
         */
        ArrayList<Line> all_streets_lines = new ArrayList<Line>();

        for (Street s : streets_list) {
            if (s.getCoordinates().get(1) != null) {
                line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(), s.getCoordinates().get(1).getX(), s.getCoordinates().get(1).getY());
                line2 = new Line(s.getCoordinates().get(1).getX(), s.getCoordinates().get(1).getY(), s.getCoordinates().get(2).getX(), s.getCoordinates().get(2).getY());
                line1.setStroke(Color.DARKRED);
                line1.setStrokeWidth(5);
                line2.setStroke(Color.DARKRED);
                line2.setStrokeWidth(5);
                all_streets_lines.add(line1);
                root.getChildren().addAll(line1, line2);
            } else {
                line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(), s.getCoordinates().get(2).getX(), s.getCoordinates().get(2).getY());
                line1.setStroke(Color.YELLOW);
                line1.setStrokeWidth(5);
                all_streets_lines.add(line1);
                root.getChildren().addAll(line1);
            }
        }

        stops_list = setMapStops(streets_list);

        for (Stop stop : stops_list) //  highlight all stop objects in map
        {
            Circle circle = new Circle(stop.getCoordinate().getX(), stop.getCoordinate().getY(), 5);
            circle.setStroke(Color.YELLOWGREEN);
            circle.setStrokeWidth(5);
            Text text = new Text(stop.getCoordinate().getX() + 10, stop.getCoordinate().getY() - 7, stop.getId());
            text.setStroke(Color.YELLOWGREEN);
            root.getChildren().addAll(circle, text);
        }

        TransportLine transportLine = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule.txt");
        TransportLine transportLine2 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule2.txt");
        TransportLine transportLine3 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule3.txt");
        ArrayList<TransportLine> all_transport_lines_list = new ArrayList<TransportLine>();
        all_transport_lines_list.add(transportLine);
        all_transport_lines_list.add(transportLine2);
        all_transport_lines_list.add(transportLine3);
        for (Stop stop : stops_list) // highlight stop object from transport lines with pink color
        {
            for (TransportLine t : all_transport_lines_list) {
                if (t.getStopsMap().contains(stop)) {
                    Circle circle = new Circle(stop.getCoordinate().getX(), stop.getCoordinate().getY(), 5);
                    circle.setStroke(Color.PINK);
                    circle.setStrokeWidth(5);
                    root.getChildren().addAll(circle);
                }
            }

        }

        /*
        highlight the journey of lines with pink color -
        it means highlight all street from beginning to end when the line is travel through all street
        and highlight only part from stop to end coordinate of street for beginning and end street, because
        the line is not travel through all street but only part of it
         */
        for (TransportLine t : all_transport_lines_list) {
            for (Street s : streets_list) {
                if (t.getStreetsMap().get(0).equals(s)) // first street of line
                {
                    int begin_stop_x = t.getStopsMap().get(0).getCoordinate().getX();
                    int begin_stop_y = t.getStopsMap().get(0).getCoordinate().getY();

                    Coordinate begin_street_1 = s.getCoordinates().get(0);
                    Coordinate begin_street_2 = s.getCoordinates().get(2);

                    Coordinate second_street_1 = t.getStreetsMap().get(1).getCoordinates().get(0);
                    Coordinate second_street_2 = t.getStreetsMap().get(1).getCoordinates().get(2);

                    if (begin_street_1.equals(second_street_1) || begin_street_1.equals(second_street_2)) {
                        System.out.println("Highlight part of first street from first stop");
                        line1 = new Line(begin_stop_x, begin_stop_y, begin_street_1.getX(), begin_street_1.getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        root.getChildren().addAll(line1);
                    } else if (begin_street_2.equals(second_street_1) || begin_street_2.equals(second_street_2)) {
                        System.out.println("Highlight part of end street from end stop");
                        line1 = new Line(begin_stop_x, begin_stop_y, begin_street_2.getX(), begin_street_2.getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        root.getChildren().addAll(line1);
                    }
                } else if (t.getStreetsMap().get(t.getStreetsMap().size() - 1).equals(s)) // end street of line
                {
                    int end_stop_x = t.getStopsMap().get(t.getStopsMap().size() - 1).getCoordinate().getX();
                    int end_stop_y = t.getStopsMap().get(t.getStopsMap().size() - 1).getCoordinate().getY();

                    Coordinate end_street_1 = s.getCoordinates().get(0);
                    Coordinate end_street_2 = s.getCoordinates().get(2);

                    Coordinate nexttolast_street_1 = t.getStreetsMap().get(t.getStreetsMap().size() - 2).getCoordinates().get(0);
                    Coordinate nexttolast_street_2 = t.getStreetsMap().get(t.getStreetsMap().size() - 2).getCoordinates().get(2);

                    if (end_street_1.equals(nexttolast_street_1) || end_street_1.equals(nexttolast_street_2)) {
                        System.out.println("Highlight last street from stop1");
                        line1 = new Line(end_stop_x, end_stop_y, end_street_1.getX(), end_street_1.getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        root.getChildren().addAll(line1);
                    } else if (end_street_2.equals(nexttolast_street_1) || end_street_2.equals(nexttolast_street_2)) {
                        System.out.println("Highlight last street from stop1");
                        line1 = new Line(end_stop_x, end_stop_y, end_street_2.getX(), end_street_2.getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        root.getChildren().addAll(line1);
                    }
                } else if (t.getStreetsMap().contains(s)) {
                    if (s.getCoordinates().get(1) != null) {
                        line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(), s.getCoordinates().get(1).getX(), s.getCoordinates().get(1).getY());
                        line2 = new Line(s.getCoordinates().get(1).getX(), s.getCoordinates().get(1).getY(), s.getCoordinates().get(2).getX(), s.getCoordinates().get(2).getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        line2.setStroke(Color.PINK);
                        line2.setStrokeWidth(5);
                        all_streets_lines.add(line1);
                        root.getChildren().addAll(line1, line2);
                    } else {
                        line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(), s.getCoordinates().get(2).getX(), s.getCoordinates().get(2).getY());
                        line1.setStroke(Color.PINK);
                        line1.setStrokeWidth(5);
                        all_streets_lines.add(line1);
                        root.getChildren().addAll(line1);
                    }
                }
            }
        }

        ArrayList<Circle> all_line_original_vehicles = new ArrayList<Circle>();
        for (TransportLine t : all_transport_lines_list) {
            // coordinates of path for vehicle on transportline
            ArrayList<Coordinate> line_coordinates = t.transportLinePath();
            // ids of coordinates of path for vehicle on transportline
            ArrayList<String> line_coordinates_ids = t.transportLinePathIDs();
            // all stops for transportline
            List<Stop> line_stops = t.getStopsMap();
            // create original vehicle for line
            Circle vehicle = new Circle(t.getStopsMap().get(0).getCoordinate().getX(), t.getStopsMap().get(0).getCoordinate().getY(), 10);
            vehicle.setStroke(Color.AZURE);
            vehicle.setFill(Color.PINK);
            vehicle.setStrokeWidth(5);
            t.setVehicle(vehicle);

            Timeline timeline = new Timeline();

            // add all keyframes to timeline - one keyframe means path from one coordinate to another coordinate
            int delta_time = 0;
            //int next_position_index = 0;
            KeyFrame waiting_in_stop = null;
            for (int i = 0; i < line_coordinates.size() - 1; i++) {
                for (Stop s : line_stops) {
                    if (line_coordinates.get(i).getX() == s.getCoordinate().getX() && line_coordinates.get(i).getY() == s.getCoordinate().getY()) {
                        waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 2), // this means waiting in stop for some time
                                new KeyValue(vehicle.centerXProperty(), line_coordinates.get(i).getX()),
                                new KeyValue(vehicle.centerYProperty(), line_coordinates.get(i).getY()));

                        delta_time = delta_time + 2;
                        break;
                    }
                }
                KeyFrame end = new KeyFrame(Duration.seconds(delta_time + 2), // this means that the path from one coordinate to another lasts 2 seconds
                        new KeyValue(vehicle.centerXProperty(), line_coordinates.get(i + 1).getX()),
                        new KeyValue(vehicle.centerYProperty(), line_coordinates.get(i + 1).getY()));

                if (waiting_in_stop != null) {
                    timeline.getKeyFrames().addAll(end, waiting_in_stop);
                } else {
                    timeline.getKeyFrames().addAll(end);
                }

                delta_time = delta_time + 2;
            }

            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Location after relocation = " + vehicle.centerXProperty()
                            + "," + vehicle.centerYProperty() + ")");
                }
            });
            timeline.setCycleCount(Timeline.INDEFINITE); // infinity number of repetations
            t.setLineMovement(timeline); // set movement of specified line
            timeline.play(); // play final animation

            root.getChildren().add(vehicle);
        }

        /*
        after clicking on original vehicle of line show some info on console
        TASK - Show this info about vehicle in some textbox under the map
         */
        for (TransportLine t : all_transport_lines_list) {
            System.out.println(t.getLineVehicle());
                //Circle vehicle = t.getLineVehicle();
                t.getLineVehicle().setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if (t.getLineVehicle().getFill() == Color.PINK) {
                                    t.getLineVehicle().setFill(Color.ORANGE);
                                }
                                else {
                                    t.getLineVehicle().setFill(Color.PINK);
                                }

                                System.out.println("This is line number " + t.getLineId() + " with route " + t.printRoute());

                                int vehicle_actual_x = (int) Math.round(t.getLineVehicle().getCenterX());
                                int vehicle_actual_y = (int) Math.round(t.getLineVehicle().getCenterY());

                                Coordinate vehicle_actual_coordinates = new Coordinate(vehicle_actual_x, vehicle_actual_y);

                                for (int i = 0; i < t.transportLinePath().size() - 1; i++) {
                                    Coordinate coordinates1 = t.transportLinePath().get(i);
                                    Coordinate coordinates2 = t.transportLinePath().get(i + 1);
                                    String id_coordinates_2 = t.transportLinePathIDs().get(i + 1);

                                    if (vehicle_actual_coordinates.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {
                                        System.out.println("Previous stops:");
                                        for (int j = 0; j < t.transportLinePathIDs().size(); j++) {
                                            if (j < t.transportLinePathIDs().indexOf(id_coordinates_2) && t.transportLinePathIDs().get(j).contains("Stop")) {
                                                //System.out.println(t.transportLinePathIDs().get(j));
                                            } else {
                                                if (t.transportLinePathIDs().get(j).contains("Stop")) {
                                                    System.out.println("Next stop is " + t.transportLinePathIDs().get(j));
                                                    break;
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                );

        }

        /*
        simulation of slowing the traffic with specified street
         */
        for (Line l : all_streets_lines)
        {
            l.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    for (TransportLine t : all_transport_lines_list) {
                        Timeline timeline_changed = t.getLineMovement();
                        //System.out.println(timeline_changed);
                        for (Street s : t.getStreetsMap()) {
                            if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY()) {
                                System.out.println("Street is slower now from line");
                                t.getLineMovement().stop();
                                //root.getChildren().remove(t.getLineVehicles().get(0));

                                root.getChildren().remove(t.getLineVehicle());
                                t.clearLineVehicle();
                                Circle vehicle_changed = new Circle(t.getStopsMap().get(0).getCoordinate().getX(), t.getStopsMap().get(0).getCoordinate().getY(), 10);
                                vehicle_changed.setStroke(Color.AZURE);
                                vehicle_changed.setFill(Color.BLACK);
                                vehicle_changed.setStrokeWidth(5);
                                t.setVehicle(vehicle_changed);
                                root.getChildren().addAll(vehicle_changed);

                                l.setStroke(Color.BLACK);

                                ArrayList<Integer> affected_points_indexes = new ArrayList<Integer>();
                                for (int i = 0; i < t.transportLinePath().size(); i++) {
                                    if (t.transportLinePath().get(i).isBetweenTwoCoordinates(s.begin(), s.end()) || (t.transportLinePath().get(i).getX() == s.begin().getX() && t.transportLinePath().get(i).getY() == s.begin().getY()) || (t.transportLinePath().get(i).getX() == s.end().getX() && t.transportLinePath().get(i).getY() == s.end().getY())) {
                                        System.out.println("Affected points: " + t.transportLinePath().get(i).getX() + ", " + t.transportLinePath().get(i).getY());
                                        //System.out.println(i);
                                        affected_points_indexes.add(i);
                                    }
                                }

                                int delta_time = 0;
                                KeyFrame waiting_in_stop = null;
                                for (int i = 0; i < affected_points_indexes.get(0); i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 2), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 2;
                                            break;
                                        }
                                    }

                                    KeyFrame end = new KeyFrame(Duration.seconds(delta_time + 2), // this means that the path from one coordinate to another lasts 2 seconds
                                            new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i + 1).getX()),
                                            new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i + 1).getY()));

                                    if (waiting_in_stop != null) {
                                        timeline_changed.getKeyFrames().addAll(end, waiting_in_stop);
                                    } else {
                                        timeline_changed.getKeyFrames().addAll(end);
                                    }

                                    delta_time = delta_time + 2;
                                }

                                for (int i = affected_points_indexes.get(0); i < affected_points_indexes.get(affected_points_indexes.size() - 1); i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 5), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 5;
                                            break;
                                        }
                                    }

                                    KeyFrame end = new KeyFrame(Duration.seconds(delta_time + 5), // this means that the path from one coordinate to another lasts 2 seconds
                                            new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i + 1).getX()),
                                            new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i + 1).getY()));

                                    if (waiting_in_stop != null) {
                                        timeline_changed.getKeyFrames().addAll(end, waiting_in_stop);
                                    } else {
                                        timeline_changed.getKeyFrames().addAll(end);
                                    }

                                    delta_time = delta_time + 5;
                                }

                                for (int i = affected_points_indexes.get(affected_points_indexes.size() - 1); i < t.transportLinePath().size() - 1; i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 2), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 2;

                                            break;
                                        }
                                    }

                                    KeyFrame end = new KeyFrame(Duration.seconds(delta_time + 2), // this means that the path from one coordinate to another lasts 2 seconds
                                            new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i + 1).getX()),
                                            new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i + 1).getY()));

                                    if (waiting_in_stop != null) {
                                        timeline_changed.getKeyFrames().addAll(end, waiting_in_stop);
                                    } else {
                                        timeline_changed.getKeyFrames().addAll(end);
                                    }
                                    delta_time = delta_time + 2;
                                }
                            }
                        }
                        timeline_changed.setCycleCount(Animation.INDEFINITE);
                        timeline_changed.play();
                        t.setLineMovement(timeline_changed);
                        System.out.println(timeline_changed);
                    }
                }
            }
            );
        }
        stage.setScene(scene);
        stage.show(); // show GUI scene
    }

    /*
    method for loading streets IDs and their coordinates from file, and create objects of all Streets in map
    @return list of all Street objects
     */
    public ArrayList<Street> setMapStreets() throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/forto/IdeaProjects/proj/lib/streetsCoordinates.txt"));
        String line = null;
        ArrayList<Street> streets_list = new ArrayList<Street>();

        while ((line = br.readLine()) != null)
        {
            Coordinate c3 = null;
            String street_coordinates3 = null;
            int street_coordinates3_x = -1;
            int street_coordinates3_y = -1;
            boolean right_angle_street = false;

            String street_id = line.substring(0,(line.indexOf("-"))).trim();
            String street_coordinates1 = line.substring((line.indexOf("-") + 1),line.indexOf(";")).trim();
            String street_coordinates2 = line.substring((line.indexOf(";") + 1)).trim();

            if (street_coordinates2.contains(";"))
            {
                street_coordinates3 = (street_coordinates2.substring((street_coordinates2.indexOf(";") + 1)).trim());
                street_coordinates2 = (street_coordinates2.substring(0,(street_coordinates2.indexOf(";"))).trim());
                right_angle_street = true;
            }

            int street_coordinates1_x = Integer.parseInt(street_coordinates1.substring(1, (street_coordinates1.indexOf(","))));
            int street_coordinates1_y = Integer.parseInt(street_coordinates1.substring(street_coordinates1.indexOf(",")+1,street_coordinates1.length()-1));

            int street_coordinates2_x = Integer.parseInt(street_coordinates2.substring(1, (street_coordinates2.indexOf(","))));
            int street_coordinates2_y = Integer.parseInt(street_coordinates2.substring(street_coordinates2.indexOf(",")+1,street_coordinates2.length()-1));

            if (right_angle_street == true) // street with right angle - three init points
            {
                street_coordinates3_x = Integer.parseInt(street_coordinates3.substring(1, (street_coordinates3.indexOf(","))));
                street_coordinates3_y = Integer.parseInt(street_coordinates3.substring(street_coordinates3.indexOf(",")+1,street_coordinates3.length()-1));
            }

            Coordinate c1 = new Coordinate(street_coordinates1_x, street_coordinates1_y);
            Coordinate c2 = new Coordinate(street_coordinates2_x, street_coordinates2_y);

            if (right_angle_street == true)
            {
                c3 = new Coordinate(street_coordinates3_x, street_coordinates3_y);

            }

            // create street object in map
            if (right_angle_street == false)
            {
                streets_list.add(Street.defaultStreet(street_id, c1, c2));
            }
            else if (right_angle_street == true)
            {
                streets_list.add(Street.defaultStreet(street_id, c1, c2, c3));
            }
        }

        return streets_list;
    }

    /*
    method for loading stops IDs and their coordinates from file, and create objects of all Stops in map
    @arguments list of all streets in map
    @return list of all Stop objects
     */
    public ArrayList<Stop> setMapStops(ArrayList<Street> streetArrayList) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader("C:/Users/forto/IdeaProjects/proj/lib/stopsCoordinates.txt"));
        String line = null;
        ArrayList<Stop> stops_list = new ArrayList<Stop>();

        while ((line = br.readLine()) != null)
        {
            String stop_id = line.substring(0,(line.indexOf("-"))).trim();
            String stop_coordinates = line.substring((line.indexOf("-") + 1),line.indexOf(";")).trim();

            int stop_coordinates_x = Integer.parseInt(stop_coordinates.substring(1, (stop_coordinates.indexOf(","))));
            int stop_coordinates_y = Integer.parseInt(stop_coordinates.substring(stop_coordinates.indexOf(",")+1,stop_coordinates.length()-1));

            String street_of_stop_id = line.substring(line.indexOf(";") + 1).trim();

            Coordinate c1 = new Coordinate(stop_coordinates_x, stop_coordinates_y);
            Stop stop = Stop.defaultStop(stop_id, c1);

            for (Street s : streetArrayList)
            {
                if (s.getId().equals(street_of_stop_id))
                {
                    if (s.getCoordinates().get(1) == null)
                    {
                        System.out.println("Normal street");
                        s.addStop(stop, false);
                        stops_list.add(stop);
                    }
                    else
                    {
                        System.out.println("Right angle street");
                        s.addStop(stop, true);
                        stops_list.add(stop);
                    }

                }
            }
        }

        return stops_list;
    }

    /*
    method for loading line ID and their stops and streets without stops from file, and create objects TransportLine (aka Line in hw2)
    @arguments list of all stops in map
    @arguments list of all Street objects
    @return TransportLine object
     */
    public TransportLine setScheduleLines(ArrayList<Street> streetArrayList, ArrayList<Stop> stopArrayList, String filename_path) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(filename_path));
        String line = null;

        line = br.readLine();
        String line_id = line.substring(line.indexOf("-")+1).trim();
        System.out.println(line_id);
        TransportLine transport_line = TransportLine.defaultLine();
        transport_line.setLineId(line_id);
        System.out.println(transport_line.getLineId());

        while ((line = br.readLine()) != null) {
            if (line.contains("Stop"))
            {
                for (Stop stop : stopArrayList)
                {
                    if (stop.getId().equals(line)) {
                        transport_line.addStop(stop);
                    }
                }
            }
            else if (line.contains("Street"))
            {
                for (Street s : streetArrayList)
                {
                    if (s.getId().equals(line))
                    {
                        transport_line.addStreet(s);
                    }
                }
            }
        }

        transport_line.printRoute();

        return transport_line;
    }

    // only simulation of new path of line after closing specified street, firstly GUI and controlers for it needs to be done
    public void closeStreet(ArrayList<Street> streetArrayList)
    {
        for (Street s : streetArrayList)
        {
            if (s.getId().equals("Street4"))
            {
                Street closed_street = s;
            }
        }

    }
}
