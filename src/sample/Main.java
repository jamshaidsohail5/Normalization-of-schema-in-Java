package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


public class  Main extends Application implements EventHandler <ActionEvent> {

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    BorderPane borderpane = new BorderPane();
    Scene scene = new Scene(borderpane, 1366, 700);

    static Button[] ButtonArray = new Button[10];
    static TextArea relation = new TextArea("(A,B,C)");
    static TextField functionalDependencies = new TextField();
    static GridPane flow = new GridPane();
    static DynamicWindows dynamicWindows = new DynamicWindows();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Database Normalization");
        VBox buttons = new VBox(5);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setStyle("-fx-background-color: DARKGRAY");
        DropShadow shadow = new DropShadow();


        for (int i = 0; i < ButtonArray.length; i++) {
            ButtonArray[i] = new Button();
            ButtonArray[i].setPadding(new Insets(9));
            ButtonArray[i].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 15 Calibri ; -fx-text-fill: white; ");
            ButtonArray[i].setAlignment(Pos.CENTER);
            if (i!=0)
                ButtonArray[i].setOnAction(this);

        }

        ButtonArray[0].setText("HOME");
        ButtonArray[1].setText("GENERATE SQL FROM ERWIN");
        ButtonArray[2].setText("FIND ATTRIBUTES CLOSURE");
        ButtonArray[3].setText("FIND ALL CANDIDATE KEYS");
        ButtonArray[4].setText("FIND ALL SUPER KEYS");
        ButtonArray[5].setText("FIND MINIMAL COVER");
        ButtonArray[6].setText("CHECK NORMAL FORM");
        ButtonArray[7].setText("NORMALIZE TO 3NF");
        ButtonArray[8].setText("NORMALIZE TO BCNF");
        ButtonArray[9].setText("NORMALIZE THIS RELATION");


        buttons.setPadding(new Insets(10,35,93,15));

        for (int j = 0; j < ButtonArray.length; j++) {
            if (j == 2) {
                Text text1 = new Text("Functions            ");
                text1.setFill(Color.CORNSILK);
                text1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 32));
                buttons.getChildren().add(text1);
            }
            buttons.getChildren().add(ButtonArray[j]);
        }


        borderpane.setLeft(buttons);  //LEFT SIDE

        //-----------------------------------------------------------------------

        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color: DARKGRAY");

        Image image = new Image("/fast2.png");
        Image image2 = new Image("/dbn.png");
        ImageView imageView = new ImageView(image);
        ImageView imageView2 = new ImageView(image2);

        Region extra = new Region();
        extra.setPrefWidth(144);

        imageView.setFitHeight(125);
        imageView.setFitWidth(127);

        imageView2.setFitHeight(125);
        imageView2.setFitWidth(710);

        hbox.getChildren().add(imageView);
        hbox.getChildren().add(extra);
        hbox.getChildren().add(imageView2);
        hbox.setPadding(new Insets(10));
        borderpane.setTop(hbox);

        for(int events =0; events<10; events++) {
            int k = events;
            ButtonArray[k].addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ButtonArray[k].setEffect(shadow);
                            ButtonArray[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: white; -fx-text-alignment: center;  -fx-font: 16 Calibri ; -fx-text-fill: white; ");

                        }
                    });

            ButtonArray[k].addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {
                            ButtonArray[k].setEffect(null);
                            ButtonArray[k].setStyle("   -fx-base: db3c2d ; -fx-border-color: black; -fx-text-alignment: center;  -fx-font: 15 Calibri ; -fx-text-fill: white; ");

                        }
                    });
        }


        flow.setStyle("-fx-background-color: edebe5  ;");
        flow.setHgap(15);
        flow.setVgap(5);
        flow.setPadding(new Insets(10,10,10,10));
        flow.setAlignment(Pos.TOP_LEFT);

        relation.setPadding(new Insets(1));
        relation.setStyle("-fx-Arc-Width: 10; -fx-Arc-Height: 10; -fx-background-color: aqua;");

        flow.add(getText("Attributes in Table\n",35),0,0);
        flow.add( relation,0,1);
        flow.add(getText("Functional Dependencies\n",35),0,3);
        flow.add(functionalDependencies,0,4);

        flow.add(ButtonArray[9],0,7);

        flow.setHalignment(Main.ButtonArray[9], HPos.LEFT);

        borderpane.setCenter(flow);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/fast2.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event) {

        if ((event.getSource() == ButtonArray[9]))
            RelationalDatabaseTools.Calculate();

        if (event.getSource() == ButtonArray[1])
            dynamicWindows.SQLGenerator();

        if ((event.getSource() == ButtonArray[2]))
            dynamicWindows.AttributeClosure();

        if ((event.getSource() == ButtonArray[3]))
            dynamicWindows.CandidateKeys();

        if ((event.getSource() == ButtonArray[4]))
            dynamicWindows.SuperKeys();

        if ((event.getSource() == ButtonArray[5]))
            dynamicWindows.MinimalCover();

        if((event.getSource() == ButtonArray[6]))
            dynamicWindows.CheckNormalForm();

        if((event.getSource() == ButtonArray[7]))
            dynamicWindows.ThreeNF();

        if((event.getSource() == ButtonArray[8]))
            dynamicWindows.BCNF();
    }

    private static Text getText(String text, int size){
        Text text1 = new Text(text);
        text1.setUnderline(true);
        text1.setFill(Color.GRAY);
        text1.setFont(Font.font("Adobe Caslon Pro Bold", FontWeight.BOLD, FontPosture.REGULAR, size));
        return text1;
    }
}
