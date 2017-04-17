package GUI;

import Client.Visitor.Begin_Visit_Command;
import Client.Visitor.End_Visit_Command;
import Client.Visitor.LBMS_VisitorKeeper;
import Time.Advance_Time_Command;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by DemonicVampire on 4/17/17.
 */
public class Connect_View extends Application{

    private String clientMessage;
    private Integer clientID;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Stage stage = primaryStage;
        LBMS_VisitorKeeper visitorKeeper = LBMS_VisitorKeeper.getInstance();
        BorderPane root = new BorderPane();
        primaryStage.setTitle("LBMS");
        Button btn = new Button();
        btn.setText("Connect");
        Button btn2 = new Button();
        btn2.setText("Disconnect");
        Button btn3 = new Button();
        btn3.setText("SearchToBuy");
        Button btn4 = new Button();
        btn4.setText("Arrive");
        Button btn5 = new Button();
        btn5.setText("Advance");
        VBox client = new VBox();
        Label clientText = new Label();
        Label visitorText = new Label();
        TextField visitorTextField = new TextField();
        client.maxHeight(Double.MAX_VALUE);
        client.maxWidth(Double.MAX_VALUE);
        btn3.setDisable(true);
        btn4.setDisable(true);
        btn5.setDisable(true);
        visitorTextField.setDisable(true);
        Button depart = new Button("Depart");

        VBox visitorBox = new VBox();
        VBox clientBox = client;

        Scene scene1 = new Scene(visitorBox);
//        Scene scene2 = new Scene(visitorBox);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btn.setDisable(true);
                clientMessage = visitorKeeper.startConnection();
                clientText.setText(clientMessage);
                clientID = Integer.parseInt(clientMessage.split(",|\\;")[1]);
                btn3.setDisable(false);
                btn4.setDisable(false);
                btn5.setDisable(false);
                visitorTextField.setDisable(false);
                client.getChildren().addAll(clientText);
            }
        });
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    visitorKeeper.disconnectConnection(clientID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    restart(primaryStage);
                }
            }
        });
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Book_Search_View bsv = new Book_Search_View();
                GridPane gp = bsv.order(clientID);
                gp.add(clientBox,0,1);
                visitorBox.getChildren().addAll(gp);
                stage.setScene(scene1);
            }
        });
        btn4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long visitor = Long.parseLong(visitorTextField.getText());
                Begin_Visit_Command bvc = new Begin_Visit_Command(visitor,false);
                String result = bvc.execute();
                String visitorID = result.split(",")[1];
                visitorText.setText("Visitor: " + visitorID);
                visitorText.setDisable(true);
                depart.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        depart.setDisable(false);
                        End_Visit_Command evc = new End_Visit_Command(Long.parseLong(visitorID), false);
                        evc.execute();
                        depart.setDisable(true);
                    }
                });
                client.getChildren().add(depart);
            }
        });
        Label advance = new Label("Advance Time");
        Label days = new Label("Days:");
        ComboBox daysToAdvance = new ComboBox();
        for(int i = 0; i < 7; i++)
            daysToAdvance.getItems().add(i);
        daysToAdvance.setValue(0);
        Label hours = new Label("Hours:");
        final ComboBox hoursToAdd = new ComboBox();
        for(int i = 0; i < 24; i++)
            hoursToAdd.getItems().add(i);
        hoursToAdd.setValue(0);
        btn5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Advance_Time_Command atc = new Advance_Time_Command(Integer.parseInt(daysToAdvance.getValue().toString()),Integer.parseInt(hoursToAdd.getValue().toString()));
                atc.execute();
            }
        });
        client.getChildren().addAll(btn,btn2,btn3,btn4,visitorTextField,visitorText,clientText,advance,days,daysToAdvance,hours,hoursToAdd,btn5);
        root.setLeft(clientBox);
        timeGUI timer = new timeGUI();
        HBox currentTime = timer.start();
        root.setBottom(currentTime);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

    }
    void cleanup(){

    }
    void restart(Stage stage) {
        cleanup();
        start(stage);
    }
}