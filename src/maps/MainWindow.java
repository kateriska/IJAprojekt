
import maps.Coordinate;
import maps.Street;


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
import javafx.scene.Group;



public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ArrayList<Street> streets_list = new ArrayList<Street>();


        Pane root = new Pane();

        Scene scene = new Scene(root, 1000, 771);

        Line line1 = null;
        Line line2 = null;

        /*
        Line line = new Line(540, 30,   540,   158);
        line.setStroke(Color.VIOLET);
        line.setStrokeWidth(5);
        root.getChildren().addAll(line);

         */
        /*
        Line line = new Line(540,30,540,158);
        line.setStroke(Color.VIOLET);
        line.setStrokeWidth(5);
        root.getChildren().addAll(line);

         */

        File file = new File("C:/Users/forto/IdeaProjects/proj/lib/map.png");
        BackgroundImage myBI= new BackgroundImage(new Image(file.toURI().toString()),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

        //stage.setScene(scene);
        //stage.show();

        Point2D p = MouseInfo.getPointerInfo().getLocation();

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
            }
        });

        streets_list = setMapStreets();

        for (Street s : streets_list)
        {
            System.out.println(s.getCoordinates());
            if (s.getCoordinates().get(1) != null)
            {
                System.out.println("Three coordinates street");
                System.out.println(s.getId());
                System.out.println(s.getCoordinates().get(0).getX());
                System.out.println(s.getCoordinates().get(0).getY());
                System.out.println(s.getCoordinates().get(1).getX());
                System.out.println(s.getCoordinates().get(1).getY());
                line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(),   s.getCoordinates().get(1).getX(),   s.getCoordinates().get(1).getY());
                line2 = new Line(s.getCoordinates().get(1).getX(), s.getCoordinates().get(1).getY(),   s.getCoordinates().get(2).getX(),   s.getCoordinates().get(2).getY());
                line1.setStroke(Color.DARKRED);
                line1.setStrokeWidth(5);
                line2.setStroke(Color.DARKRED);
                line2.setStrokeWidth(5);
                root.getChildren().addAll(line1, line2);

            }
            else
            {
                line1 = new Line(s.getCoordinates().get(0).getX(), s.getCoordinates().get(0).getY(),   s.getCoordinates().get(2).getX(),   s.getCoordinates().get(2).getY());
                line1.setStroke(Color.YELLOW);
                line1.setStrokeWidth(5);
                root.getChildren().addAll(line1);

            }






        }



        stage.setScene(scene);
        stage.show();
    }

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
            System.out.println(line);
            String street_id = line.substring(0,(line.indexOf("-"))).trim();
            String street_coordinates1 = line.substring((line.indexOf("-") + 1),line.indexOf(";")).trim();
            String street_coordinates2 = line.substring((line.indexOf(";") + 1)).trim();

            if (street_coordinates2.contains(";"))
            {
                System.out.println("Three points for street");
                street_coordinates3 = (street_coordinates2.substring((street_coordinates2.indexOf(";") + 1)).trim());
                street_coordinates2 = (street_coordinates2.substring(0,(street_coordinates2.indexOf(";"))).trim());
                //street_coordinates3 = (line.substring((street_coordinates2.indexOf(";") + 1)).trim());
                right_angle_street = true;


            }

            System.out.println(street_id);
            System.out.println(street_coordinates1);
            System.out.println(street_coordinates2);
            System.out.println(street_coordinates3);

            int street_coordinates1_x = Integer.parseInt(street_coordinates1.substring(1, (street_coordinates1.indexOf(","))));
            System.out.println(street_coordinates1_x);
            int street_coordinates1_y = Integer.parseInt(street_coordinates1.substring(street_coordinates1.indexOf(",")+1,street_coordinates1.length()-1));
            System.out.println(street_coordinates1_y);

            int street_coordinates2_x = Integer.parseInt(street_coordinates2.substring(1, (street_coordinates2.indexOf(","))));
            System.out.println(street_coordinates2_x);
            int street_coordinates2_y = Integer.parseInt(street_coordinates2.substring(street_coordinates2.indexOf(",")+1,street_coordinates2.length()-1));
            System.out.println(street_coordinates2_y);

            if (right_angle_street == true) // street with right angle - three init points
            {
                street_coordinates3_x = Integer.parseInt(street_coordinates3.substring(1, (street_coordinates3.indexOf(","))));
                System.out.println(street_coordinates3_x);
                street_coordinates3_y = Integer.parseInt(street_coordinates3.substring(street_coordinates3.indexOf(",")+1,street_coordinates3.length()-1));
                System.out.println(street_coordinates3_y);
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
                System.out.println("Three coordinates for street");
                streets_list.add(Street.defaultStreet(street_id, c1, c2, c3));
            }


        }

        for (Street s : streets_list)
        {
            //System.out.println(s.getCoordinates());


        }

        return streets_list;
    }
}
