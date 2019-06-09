package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    public Button chooseDataButton;
    public Label fileNameLabel;
    public Button draw1Button;
    public Button draw2Button;
    public Button addTransformsButton;
    public LineChart enthalpyChart;
    public NumberAxis xAxis;
    public NumberAxis yAxis;
    public TextField amountOfTransfers;
    private Stage stage;
    private static Calculations calculations;
    private  TransformationViewController transformationViewController;
    private boolean dataFileIsLoaded = false;

    public TransformationViewController getTransformationViewController() {
        return transformationViewController;
    }

    public void setTransformationViewController(TransformationViewController transformationViewController) {
        this.transformationViewController = transformationViewController;
    }

    public void chooseDataOnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki txt", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            System.out.println("Plik " + file.getAbsolutePath());
            try {
                loadData(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        fileNameLabel.setText(file.getName());
        dataFileIsLoaded = true;
    }

    private void loadData(File file) throws FileNotFoundException {
        Path pathToFile = Paths.get(file.getAbsolutePath());
        ArrayList<String> loadedData = new ArrayList();
        calculations = new Calculations();
        double[] temperatures;
        double[] specific_heat;


        try {
            loadedData = (ArrayList) Files.readAllLines(pathToFile);

        } catch (IOException ex) {
            System.out.println("Nie można otworzyć pliku");
        }

        int lineNumber = 0;
        int size = loadedData.size() - 5;
        temperatures = new double[size];
        specific_heat = new double[size];
        int tabIterator = 0;
        for (String line : loadedData) {
            if (lineNumber >= 5) {
                String[] dateLine = line.split(" ");
                temperatures[tabIterator] = Double.parseDouble(dateLine[0]);
                specific_heat[tabIterator] = Double.parseDouble(dateLine[1]);
                tabIterator++;

            }

            lineNumber++;
        }

        calculations.init(temperatures, specific_heat, size);
        enthalpyChartInit();
    }

    private void enthalpyChartInit() {
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(1600);
        xAxis.setLowerBound(50);
        xAxis.setTickUnit(100);


        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1000.0);
        yAxis.setTickUnit(50);

    }

    public void draw1OnAction(ActionEvent actionEvent) {
        if (dataFileIsLoaded) {
            enthalpyChart.getData().clear();

            enthalpyChart.setTitle("Wykres entalpii - 1. wariant obliczeń");

            XYChart.Series series = calculations.setEnthalpySeries(1);
            series.setName("Entalpia");
            enthalpyChart.getData().add(series);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie wybrano pliku z danymi!");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }


    }

    public void draw2OnAction(ActionEvent actionEvent) {
        if (dataFileIsLoaded) {
            enthalpyChart.getData().clear();

            enthalpyChart.setTitle("Wykres entalpii - 2. wariant obliczeń");
            XYChart.Series series = calculations.setEnthalpySeries(2);
            series.setName("Entalpia");
            enthalpyChart.getData().add(series);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nie wybrano pliku z danymi!");
            alert.showAndWait().filter(response -> response == ButtonType.OK);
        }

    }

    public void addTransformsOnAction(ActionEvent actionEvent) {
        int amount = Integer.parseInt(amountOfTransfers.getText().toString());
      //  transformationViewController = new TransformationViewController();
        transformationViewController.initTransformationPane(amount);

        //moveToTransformationScene();



    }

    private void moveToTransformationScene() {
        Main.goToTransformation();
    }

    public void displayResultsOnAction(ActionEvent actionEvent) {
    }

    public static void applyTransformationData(double temp[]){
        calculations.setTransformsData(temp);
    }


}
