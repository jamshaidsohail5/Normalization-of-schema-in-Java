package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Acer on 5/27/2017.
 */
public class DynamicWindows implements EventHandler<ActionEvent> {

    Button[] ButtonArray = new Button[4];
    Button ThreeNFDetails = new Button();
    Button BCNFDetails = new Button();

    Button[] SQL = new Button[2];
    DropShadow shadow = new DropShadow();
    TextArea sql = new TextArea();
    TextField sql2 = new TextField();


    public DynamicWindows(){

    }

    private void setStyle(Stage stage, BorderPane borderpane, Scene scene, GridPane flow, String WindowTitle){

        stage.setTitle(WindowTitle);

        HBox hbox = new HBox();
        hbox.setAlignment((Pos.TOP_CENTER));
        hbox.setStyle("-fx-background-color: darkgray");
        hbox.setPadding(new Insets(25,10,10,10));

        Text text1 = new Text(WindowTitle);
        text1.setFill(Color.CORNSILK);
        text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 60));
        hbox.getChildren().add(text1);

        borderpane.setTop(hbox);

        flow.setStyle("-fx-background-color: edebe5  ;");
        flow.setHgap(15);
        flow.setVgap(5);
        flow.setPadding(new Insets(10,10,10,10));
        flow.setAlignment(Pos.TOP_CENTER);

        borderpane.setCenter(flow);
        stage.setScene(scene);
    }

    private Text getHeadings(String text, int size){
        Text text1 = new Text(text);
        text1.setFill(Color.BROWN);
        text1.setFont(Font.font("Adobe Caslon Pro Bold", FontWeight.BOLD, FontPosture.REGULAR, size));
        return text1;
    }



    public void SQLGenerator(){
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 1200, 800);
        GridPane flow = new GridPane();



        for (int i = 0; i < SQL.length; i++) {
            SQL[i] = new Button();
            SQL[i].setPadding(new Insets(9));
            SQL[i].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");
            SQL[i].setAlignment(Pos.CENTER);
            SQL[i].setOnAction(this);
        }

        SQL[0].setText("GENERATE SQL");
        SQL[1].setText("SAVE TO DESKTOP");
        for(int events =0; events<2; events++) {
            int k = events;
            SQL[k].addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            SQL[k].setEffect(shadow);
                            SQL[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: white; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                        }
                    });

            SQL[k].addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            SQL[k].setEffect(null);
                            SQL[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                        }
                    });
        }



        setStyle(stage,borderpane,scene,flow,"Generate SQL from ERwin");
        sql.setPadding(new Insets(1));    //**
        sql.setStyle("-fx-Arc-Width: 10; -fx-Arc-Height: 10; -fx-background-color: aqua;"); //**


        flow.add( sql,0,5);
        flow.add(getHeadings("Enter File Name\n", 35), 0, 0);
        flow.add(sql2, 0, 1);
        flow.add(SQL[0], 0,2);
        flow.add(SQL[1], 0, 8);

        stage.show();
    }


    public void AttributeClosure(){

        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 1200, 800);
        GridPane flow = new GridPane();

        setStyle(stage,borderpane,scene,flow,"Attribute Closure");

        int size = RelationalDatabaseTools.ClosureSet.size();

        for(int i = 0, j = 0; i < size; i++){
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2,10,10,10));
            Text text1 = new Text(RelationalDatabaseTools.ClosureSet.get(i));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));
            hbox.getChildren().add(text1);
            if (i%16 == 0) j++;
            flow.add(hbox,j+1,(i)%16);
        }

        stage.show();
    }

    public void CandidateKeys(){
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();

        int size = RelationalDatabaseTools.KeySet.size();

        if (size == 1)
            setStyle(stage,borderpane,scene,flow,"Primary Key");
        else
            setStyle(stage,borderpane,scene,flow,"Candidate Keys");

        for(int i = 0, j = 0; i < size; i++){
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2,10,10,10));
            Text text1 = new Text(RelationalDatabaseTools.KeySet.get(i));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
            hbox.getChildren().add(text1);
            if (i%16 == 0) j++;
            flow.add(hbox,j+1,(i)%16);
        }

        stage.show();
    }

    public void SuperKeys(){

        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();

        setStyle(stage,borderpane,scene,flow,"Super Keys");

        int size = RelationalDatabaseTools.SuperKeySet.size();
        for(int i = 0, j = 0; i < size; i++){
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2,10,10,10));
            Text text1 = new Text(RelationalDatabaseTools.SuperKeySet.get(i));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
            hbox.getChildren().add(text1);
            if (i%14 == 0) j++;
            flow.add(hbox,j+1,(i)%14);
        }
        stage.show();
    }

    public void MinimalCover(){
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();

        setStyle(stage,borderpane,scene,flow,"Minimal Cover");
        int size = RelationalDatabaseTools.MinCoverSet.size();

        flow.add(getHeadings("Functional Dependencies in the minimal cover set:",20),0+1,(0));

        for(int i = 0, j = 0; i < size; i++){
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2,10,10,10));
            hbox.setAlignment(Pos.BASELINE_CENTER);
            Text text1 = new Text(RelationalDatabaseTools.MinCoverSet.get(i));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
            hbox.getChildren().add(text1);
            if (i%16 == 0) j++;
            flow.add(hbox,j,(i+1)%16);
        }
        stage.show();
    }

    public void CheckNormalForm(){
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();


        for (int i = 0; i < ButtonArray.length; i++) {
            ButtonArray[i] = new Button();
            ButtonArray[i].setPadding(new Insets(9));
            ButtonArray[i].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");
            ButtonArray[i].setAlignment(Pos.CENTER);
            ButtonArray[i].setOnAction(this);
        }

        ButtonArray[0].setText("DETAILS");
        ButtonArray[1].setText("DETAILS");
        ButtonArray[2].setText("DETAILS");
        ButtonArray[3].setText("DETAILS");


        for(int events =0; events<4; events++) {
            int k = events;
            ButtonArray[k].addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ButtonArray[k].setEffect(shadow);
                            ButtonArray[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: white; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                        }
                    });

            ButtonArray[k].addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ButtonArray[k].setEffect(null);
                            ButtonArray[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                        }
                    });
        }

        setStyle(stage,borderpane,scene,flow,"Highest Normal Form Satisfied");
        javafx.scene.image.Image image1 = new javafx.scene.image.Image("/tick.jpg");
        javafx.scene.image.Image image2 = new javafx.scene.image.Image("/cross.jpg");
        ImageView imageView2 = new ImageView(image2);

        ImageView images [] = new ImageView[8];
        for (int i = 0 ; i < 8; i++){
            if (i%2 == 0)
                images[i] = new ImageView(image1);
            else
                images[i] = new ImageView(image2);
        }

        int j = 2;

        flow.add(getHeadings("1NF          ",50),0,j+=2);
        if (RelationalDatabaseTools.CheckNormalForms.get(0).contains("be in 1NF"))
            flow.add(images[0],1,j);
        else
            flow.add(images[1],1,j);
        flow.add(ButtonArray[0],3,j);


        flow.add(getHeadings("2NF          ",50),0,j+=2);
        if (RelationalDatabaseTools.CheckNormalForms.get(1).contains("is in 1NF"))
            flow.add(images[2],1,j);
        else
            flow.add(images[3],1,j);
        flow.add(ButtonArray[1],3,j);


        flow.add(getHeadings("3NF          ",50),0,j+=2);
        if (RelationalDatabaseTools.CheckNormalForms.get(2).contains("is in 3NF"))
            flow.add(images[4],1,j);
        else
            flow.add(images[5],1,j);
        flow.add(ButtonArray[2],3,j);


        flow.add(getHeadings("BCNF         ",50),0,j+=2);
        if (RelationalDatabaseTools.CheckNormalForms.get(3).contains("is in BCNF"))
            flow.add(images[6],1,j);
        else
            flow.add(images[7],1,j);
        flow.add(ButtonArray[3],3,j);

        stage.show();
    }

    public void handle(ActionEvent event) {

        if (event.getSource() == SQL[0]){
            try {
                String temp = XMLParser.parse(sql2.getText());
                sql.setText(temp);
                return;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return;
        }

        if(event.getSource() == SQL[1]){
            try{
                String filename = sql2.getText().substring(0,sql2.getText().length()-4)+ ".sql";
                PrintWriter writer = new PrintWriter(filename, "UTF-8");
                writer.println(sql.getText());
                writer.close();
            } catch (IOException e) {
                // do something
            }
            return;
        }

        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 1200, 400);
        GridPane flow = new GridPane();

        flow.setStyle("-fx-background-color: edebe5  ;");
        flow.setHgap(15);
        flow.setVgap(5);
        flow.setPadding(new Insets(10,10,10,10));
        flow.setAlignment(Pos.TOP_CENTER);

        if ((event.getSource() == ButtonArray[0])) {
            stage.setTitle("Details for Relation to be in 1NF");
            flow.add(getHeadings(RelationalDatabaseTools.CheckNormalForms.get(0),16),0,0);
        }

        if ((event.getSource() == ButtonArray[1])) {
            stage.setTitle("Details for Relation to be in 2NF");
            flow.add(getHeadings(RelationalDatabaseTools.CheckNormalForms.get(1),16),0,0);
        }

        if ((event.getSource() == ButtonArray[2])){
            stage.setTitle("Details for Relation to be in 3NF");
            flow.add(getHeadings(RelationalDatabaseTools.CheckNormalForms.get(2),16),0,0);
        }

        if ((event.getSource() == ButtonArray[3])){
            stage.setTitle("Details for Relation to be in BCNF");
            flow.add(getHeadings(RelationalDatabaseTools.CheckNormalForms.get(3),16),0,0);
        }

        if ((event.getSource() == ThreeNFDetails)){
            stage.setTitle("Details for Decomposition in 3NF");
            flow.add(getHeadings(RelationalDatabaseTools.threeNFDetails,16),0,0);
        }

        if ((event.getSource() == BCNFDetails)){
            stage.setTitle("Details for Decomposition in BCNNF");
            flow.add(getHeadings(RelationalDatabaseTools.BCNFDetails,16),0,0);
        }


        borderpane.setCenter(flow);
        stage.setScene(scene);
        stage.show();
    }

    public void ThreeNF() {
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();

        setStyle(stage, borderpane, scene, flow, "Decomposition to 3NF");

        int size = RelationalDatabaseTools.ThreeNFRelations.size();

        int i = 0, j = 0;
        for (i = 0, j = 0; i < size; i++) {
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2, 10, 10, 10));
            Text text1 = new Text(("Relation " + RelationalDatabaseTools.ThreeNFRelations.get(i).substring(1)));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
            hbox.getChildren().add(text1);
            if (i % 16 == 0) j++;
            flow.add(hbox, j, (i + 1) % 16);
        }

        ThreeNFDetails.setPadding(new Insets(9));
        ThreeNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");
        ThreeNFDetails.setAlignment(Pos.CENTER);
        ThreeNFDetails.setOnAction(this);
        flow.add(ThreeNFDetails, j, i + 5);

        ThreeNFDetails.setText("DETAILS");
        ThreeNFDetails.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        ThreeNFDetails.setEffect(shadow);
                        ThreeNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: white; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                    }
                });

        ThreeNFDetails.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        ThreeNFDetails.setEffect(null);
                        ThreeNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                    }
                });

        stage.show();
    }

    public void BCNF(){
        Stage stage = new Stage();
        BorderPane borderpane = new BorderPane();
        Scene scene = new Scene(borderpane, 900, 650);
        GridPane flow = new GridPane();

        setStyle(stage,borderpane,scene,flow,"Decomposition to BCNF");

        int size = RelationalDatabaseTools.BCNFRelations.size();

        int i,j;
        for(i = 0, j = 0; i < size; i++){
            HBox hbox = new HBox();
            hbox.setStyle("-fx-background-color: db3c2d");
            hbox.setPadding(new Insets(2,10,10,10));
            Text text1 = new Text(("Relation " + RelationalDatabaseTools.BCNFRelations.get(i).substring(1)));
            text1.setFill(Color.WHITE);
            text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 25));
            hbox.getChildren().add(text1);
            if (i%16 == 0) j++;
            flow.add(hbox,j,(i+1)%16);
        }

        BCNFDetails.setPadding(new Insets(9));
        BCNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");
        BCNFDetails.setAlignment(Pos.CENTER);
        BCNFDetails.setOnAction(this);
        flow.add(BCNFDetails, j, i + 5);

        BCNFDetails.setText("DETAILS");
        BCNFDetails.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        BCNFDetails.setEffect(shadow);
                        BCNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: white; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                    }
                });

        BCNFDetails.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        BCNFDetails.setEffect(null);
                        BCNFDetails.setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 20 Calibri ; -fx-text-fill: white; ");

                    }
                });
        stage.show();
    }

}
