package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class TransformationViewController {



    public Button applyButton;
    public Button cancelButton;
    public AnchorPane transformationPane;
    public HBox buttonsHBox;
    Stage stage;

    List<TextField> listOfTF;

    public void initTransformationPane(int size)
    {
        VBox allVbox = new VBox(10);
        listOfTF = new ArrayList<>();



        for(int i = 0 ; i<size; i++) {

            Main.goToTransformation();

            Label firstTemperatureLabel = new Label("Początkowa temperatura przemiany");
            TextField firstTemperatureTextField = new TextField();
            listOfTF.add(firstTemperatureTextField);
            Label secondTemperatureLabel = new Label("Końcowa temperatura przemiany");
            TextField secondTemperatureTextField = new TextField();
            listOfTF.add(secondTemperatureTextField);
            Label heatLabel = new Label("Wartość przemiany");
            TextField heatTextField = new TextField();
            listOfTF.add(heatTextField);

            VBox vbox1 = new VBox(2);
            vbox1.getChildren().addAll(firstTemperatureLabel, firstTemperatureTextField);
            VBox vbox2 = new VBox(2);
            vbox2.getChildren().addAll(secondTemperatureLabel, secondTemperatureTextField);
            VBox vbox3 = new VBox(2);
            vbox3.getChildren().addAll(heatLabel, heatTextField);

            HBox hbox = new HBox(20);
            hbox.getChildren().addAll(vbox1, vbox2, vbox3);

            allVbox.getChildren().addAll(hbox);


           // transformationPane.setTopAnchor(hbox, 10.0);

        }

        transformationPane.getChildren().addAll(allVbox);
        transformationPane.setBottomAnchor(buttonsHBox,10.0);



    }


    public void applyButtonOnAction(ActionEvent actionEvent) {
        double transformationArray[] = new double[listOfTF.size()];

        for(int i =0; i<listOfTF.size();i++)
        {
            transformationArray[i] = Double.parseDouble(listOfTF.get(i).getText());
        }

        Controller.applyTransformationData(transformationArray);

        Main.goToFirstScene();
    }

    public void cancelButtonOnAction(ActionEvent actionEvent) {
        Main.goToFirstScene();
    }
}
