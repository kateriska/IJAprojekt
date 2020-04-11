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
import javafx.scene.shape.Circle;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args); // launch app
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TASK - add sliding along map and zoom of map and other controllers of GUI
        BorderPane root = new BorderPane(); // create BorderPane as root element
        ScrollPane scroll_pane_map = new ScrollPane(); // create scroll pane for map
        AnchorPane anchor_pane_menu = new AnchorPane(); // create anchor pane for side menu
        ScrollPane scroll_pane_downbox = new ScrollPane(); // create down box for info about lines

        // put two components to BorderPane - scroll_pane_map for window with map and anchor_pane_menu for menu
        root.setLeft(scroll_pane_map);
        root.setRight(anchor_pane_menu);
        root.setBottom(scroll_pane_downbox);

        scroll_pane_downbox.setPrefWidth(400);
        scroll_pane_downbox.setPrefHeight(150);

        // create text field in scroll_pane_downbox for info about lines
        Text lines_info = new Text();
        scroll_pane_downbox.setContent(lines_info);

        AnchorPane anchor_pane_map = new AnchorPane(); // set anchor pane in scroll_pane_map
        scroll_pane_map.setContent(anchor_pane_map);
        anchor_pane_map.setPrefWidth(500);
        anchor_pane_map.setPrefHeight(500);
        scroll_pane_map.setPannable(true); // pannable map
        scroll_pane_map.setPrefViewportWidth(500);
        scroll_pane_map.setPrefViewportHeight(500);

        // zoom map
        anchor_pane_map.setOnScroll(
                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        double zoomFactor = 1.05;
                        double deltaY = event.getDeltaY();

                        if (deltaY < 0){
                            zoomFactor = 0.95;
                        }
                        anchor_pane_map.setScaleX(anchor_pane_map.getScaleX() * zoomFactor);
                        anchor_pane_map.setScaleY(anchor_pane_map.getScaleY() * zoomFactor);
                        event.consume();
                    }
                });

        Scene scene = new Scene(root, 800, 700); // set width and height of window

        File file = new File("C:/Users/forto/IdeaProjects/proj/lib/map.png");

        BackgroundImage myBI = new BackgroundImage(new Image(file.toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        anchor_pane_map.setBackground(new Background(myBI)); // set map as background, with no repeat and also some free space for TO DO GUI components

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

        ArrayList<Street> streets_list = setMapStreets(); // Street objects created from file

        /*
        highlight all street objects in map - for right angle streets need to create two lines instead one for this type of streets
         */
        ArrayList<Line> all_streets_lines = new ArrayList<Line>();

        for (Street s : streets_list) {
            s.highlightStreet(anchor_pane_map,all_streets_lines, Color.LIGHTGREY);
        }


        ArrayList<Stop> stops_list = setMapStops(streets_list); // objects of all Stop are created from file

        for (Stop stop : stops_list) //  highlight all stop objects in map
        {
            stop.highlightStop(anchor_pane_map, Color.LIGHTGREY);
        }

        // create ScheduleLine objects from files
        TransportLine transportLine = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule.txt");
        TransportLine transportLine2 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule2.txt");
        TransportLine transportLine3 = setScheduleLines(streets_list, stops_list, "C:/Users/forto/IdeaProjects/proj/lib/transportSchedule3.txt");

        // each line is marked with different color
        transportLine.setTransportLineColor(Color.SKYBLUE);
        transportLine2.setTransportLineColor(Color.SANDYBROWN);
        transportLine3.setTransportLineColor(Color.PINK);

        ArrayList<TransportLine> all_transport_lines_list = new ArrayList<TransportLine>();
        // create list of all TransportLine objects
        all_transport_lines_list.add(transportLine);
        all_transport_lines_list.add(transportLine2);
        all_transport_lines_list.add(transportLine3);
        for (Stop stop : stops_list) // highlight stop object from transport lines with their own color
        {
            for (TransportLine t : all_transport_lines_list) {
                if (t.getStopsMap().contains(stop)) {
                    stop.highlightStop(anchor_pane_map, t.getTransportLineColor());
                }
            }

        }

        /*
        highlight the journey of lines with their own color -
        it means highlight all street from beginning to end when the line is travel through all street
        and highlight only part from stop to end coordinate of street for beginning and end street, because
        the line is not travel through all street but only part of it
         */

        for (TransportLine t : all_transport_lines_list) {
            t.highlightTransportLine(anchor_pane_map, streets_list, all_streets_lines);
        }

        //ArrayList<Circle> all_line_original_vehicles = new ArrayList<Circle>();
        /*
        create a vehicle (circle) for every TransportLine object and move along the path of TransportLine
         */
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
            vehicle.setFill(t.getTransportLineColor());
            vehicle.setStrokeWidth(5);
            t.setVehicle(vehicle);

            Timeline timeline = new Timeline();

            // add all keyframes to timeline - one keyframe means path from one coordinate to another coordinate
            // vehicle waits in stop for 1 seconds and go to another coordinate for 2 seconds
            int delta_time = 0;
            KeyFrame waiting_in_stop = null;
            for (int i = 0; i < line_coordinates.size() - 1; i++) {
                for (Stop s : line_stops) {
                    if (line_coordinates.get(i).getX() == s.getCoordinate().getX() && line_coordinates.get(i).getY() == s.getCoordinate().getY()) {
                        waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 1), // this means waiting in stop for some time
                                new KeyValue(vehicle.centerXProperty(), line_coordinates.get(i).getX()),
                                new KeyValue(vehicle.centerYProperty(), line_coordinates.get(i).getY()));

                        delta_time = delta_time + 1;
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

            timeline.setCycleCount(Timeline.INDEFINITE); // infinity number of repetition
            t.setLineMovement(timeline); // set movement of specified line
            timeline.play(); // play final animation

            anchor_pane_map.getChildren().add(vehicle);
        }

        /*
        after clicking on original vehicle of line show some info on console
        TASK - Show this info about vehicle in some textbox under the map
         */
        // create an event after mouse click if the vehicle of line is clicked
        for (TransportLine t : all_transport_lines_list) {
                t.getLineVehicle().setOnMouseClicked(new EventHandler<MouseEvent>() { // create an event after mouse click
                            @Override
                            public void handle(MouseEvent event) {
                                // change color of clicked vehicle
                                if (t.getLineVehicle().getFill() == t.getTransportLineColor()) {
                                    t.getLineVehicle().setFill(Color.LIGHTGREEN);
                                }
                                else {
                                    t.getLineVehicle().setFill(t.getTransportLineColor());
                                }

                                System.out.println("This is line number " + t.getLineId() + " with route " + t.printRoute() );
                                lines_info.setText("This is line number " + t.getLineId() + "\n");
                                lines_info.setText(lines_info.getText() + "Route: " + t.printRoute() + "\n");

                                // get actual coordinates of vehicle
                                int vehicle_actual_x = (int) Math.round(t.getLineVehicle().getCenterX());
                                int vehicle_actual_y = (int) Math.round(t.getLineVehicle().getCenterY());

                                Coordinate vehicle_actual_coordinates = new Coordinate(vehicle_actual_x, vehicle_actual_y);

                                // print next stop and previous stops of line
                                for (int i = 0; i < t.transportLinePath().size() - 1; i++) {
                                    Coordinate coordinates1 = t.transportLinePath().get(i);
                                    Coordinate coordinates2 = t.transportLinePath().get(i + 1);
                                    String id_coordinates_2 = t.transportLinePathIDs().get(i + 1);

                                    if (vehicle_actual_coordinates.isBetweenTwoCoordinates(coordinates1, coordinates2) == true) {
                                        System.out.println("Previous stops:");
                                        lines_info.setText(lines_info.getText() + "Previous stops:" + "\n");
                                        for (int j = 0; j < t.transportLinePathIDs().size(); j++) {
                                            if (j < t.transportLinePathIDs().indexOf(id_coordinates_2) && t.transportLinePathIDs().get(j).contains("Stop")) {
                                                System.out.println(t.transportLinePathIDs().get(j));
                                                lines_info.setText(lines_info.getText() + t.transportLinePathIDs().get(j) + "\n");
                                            } else {
                                                if (t.transportLinePathIDs().get(j).contains("Stop")) {
                                                    System.out.println("Next stop is " + t.transportLinePathIDs().get(j));
                                                    lines_info.setText(lines_info.getText() + "Next stop is " + t.transportLinePathIDs().get(j) + "\n");
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
            l.setOnMouseClicked(new EventHandler<MouseEvent>() { // if mouse clicked on some Street object
                @Override
                public void handle(MouseEvent event) {
                    for (TransportLine t : all_transport_lines_list) {
                        Timeline timeline_changed = t.getLineMovement();
                        for (Street s : t.getStreetsMap()) // if Street is in some TransportLine
                        {
                            if (s.begin().getX() == l.getStartX() && s.begin().getY() == l.getStartY() && s.end().getX() == l.getEndX() && s.end().getY() == l.getEndY()) {
                                System.out.println("Street is slower now from line");
                                t.getLineMovement().stop(); // stop old animation of particular TransportLine
                                //root.getChildren().remove(t.getLineVehicles().get(0));

                                anchor_pane_map.getChildren().remove(t.getLineVehicle());
                                t.clearLineVehicle();
                                // set new vehicle for specified line
                                Circle vehicle_changed = new Circle(t.getStopsMap().get(0).getCoordinate().getX(), t.getStopsMap().get(0).getCoordinate().getY(), 10);
                                vehicle_changed.setStroke(Color.AZURE);
                                vehicle_changed.setFill(Color.BLACK);
                                vehicle_changed.setStrokeWidth(5);
                                t.setVehicle(vehicle_changed); // set new vehicle for particular TransportLine
                                anchor_pane_map.getChildren().addAll(vehicle_changed);

                                l.setStroke(Color.BLACK); // mark affected slower street with black color

                                ArrayList<Integer> affected_points_indexes = new ArrayList<Integer>(); // get which points of path are affected with slowing the traffic
                                for (int i = 0; i < t.transportLinePath().size(); i++) {
                                    if (t.transportLinePath().get(i).isBetweenTwoCoordinates(s.begin(), s.end()) || (t.transportLinePath().get(i).getX() == s.begin().getX() && t.transportLinePath().get(i).getY() == s.begin().getY()) || (t.transportLinePath().get(i).getX() == s.end().getX() && t.transportLinePath().get(i).getY() == s.end().getY())) {
                                        System.out.println("Affected points: " + t.transportLinePath().get(i).getX() + ", " + t.transportLinePath().get(i).getY());
                                        affected_points_indexes.add(i);
                                    }
                                }

                                /*
                                simulation of slowing traffic and set 8 seconds as duration of movement along to next coordinate and 2 seconds for waiting in stop - this duration can user set
                                streets without traffic slowing has the same duration (1 second waiting in stop, 2 seconds duration between coordinates)
                                 */

                                int delta_time = 0;
                                KeyFrame waiting_in_stop = null;

                                // path from beginning to beginning of affected street - normal duration
                                for (int i = 0; i < affected_points_indexes.get(0); i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 1), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 1;
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

                                // path around affected street - slower duration
                                for (int i = affected_points_indexes.get(0); i < affected_points_indexes.get(affected_points_indexes.size() - 1); i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 2), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 2;
                                            break;
                                        }
                                    }

                                    KeyFrame end = new KeyFrame(Duration.seconds(delta_time + 8), // this means that the path from one coordinate to another lasts 2 seconds
                                            new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i + 1).getX()),
                                            new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i + 1).getY()));

                                    if (waiting_in_stop != null) {
                                        timeline_changed.getKeyFrames().addAll(end, waiting_in_stop);
                                    } else {
                                        timeline_changed.getKeyFrames().addAll(end);
                                    }

                                    delta_time = delta_time + 8;
                                }

                                // path of the rest of TransportLine to their end stop - normal duration
                                for (int i = affected_points_indexes.get(affected_points_indexes.size() - 1); i < t.transportLinePath().size() - 1; i++) {
                                    for (Stop stop : t.getStopsMap()) {
                                        if (t.transportLinePath().get(i).getX() == stop.getCoordinate().getX() && t.transportLinePath().get(i).getY() == stop.getCoordinate().getY()) {
                                            waiting_in_stop = new KeyFrame(Duration.seconds(delta_time + 1), // this means waiting in stop for some time
                                                    new KeyValue(vehicle_changed.centerXProperty(), t.transportLinePath().get(i).getX()),
                                                    new KeyValue(vehicle_changed.centerYProperty(), t.transportLinePath().get(i).getY()));

                                            delta_time = delta_time + 1;

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
                        timeline_changed.play(); // play animation
                        t.setLineMovement(timeline_changed);
                        //System.out.println(timeline_changed);
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
}
