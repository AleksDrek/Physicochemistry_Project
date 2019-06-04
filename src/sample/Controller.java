package sample;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    private Stage stage;
    private static Calculations calculations;

    public void chooseDataOnAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z danymi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki txt", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if(file != null)
        {
            System.out.println("Plik " + file.getAbsolutePath());
            try {
                loadData(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        fileNameLabel.setText(file.getName());
    }

    private void loadData(File file) throws FileNotFoundException {
        Path pathToFile = Paths.get(file.getAbsolutePath());
        ArrayList<String> loadedData = new ArrayList();
        calculations = new Calculations();
        double [] temperatures;
        double [] specific_heat;


        try{
            loadedData = (ArrayList) Files.readAllLines(pathToFile);

        }
        catch (IOException ex){
            System.out.println("Nie można otworzyć pliku");
        }

        int lineNumber=0;
        int size = loadedData.size() - 5;
        temperatures = new double[size];
        specific_heat = new double[size];
        int tabIterator = 0;
        for(String line : loadedData)
        {
            if(lineNumber >=5 ) {
                String[] dateLine = line.split(" ");
                temperatures[tabIterator] = Double.parseDouble(dateLine[0]);
                specific_heat[tabIterator] = Double.parseDouble(dateLine[1]);
                tabIterator++;

            }

            lineNumber++;
        }

        calculations.init(temperatures, specific_heat, size);
    }

    public void draw1OnAction(ActionEvent actionEvent) {

        enthalpyChart.setTitle("Wykres entalpii - 1. wariant obliczeń");

        XYChart.Series seria = new XYChart.Series();
        seria.setName("seria");
        seria.getData().add(new XYChart.Data(String.valueOf(1970), 15));
        seria.getData().add(new XYChart.Data(String.valueOf(1980), 25));
        seria.getData().add(new XYChart.Data(String.valueOf(1990), 0));
        seria.getData().add(new XYChart.Data(String.valueOf(2000), 88));
        seria.getData().add(new XYChart.Data(String.valueOf(2012), 40));

        XYChart.Series series = calculations.setEnthalpySeries(1);
        series.setName("Entalpia");
        seria.setName("Entalpia");
        enthalpyChart.getData().add(series);
    }

    public void draw2OnAction(ActionEvent actionEvent) {
        enthalpyChart.setTitle("Wykres entalpii - 2. wariant obliczeń");

    }

    public void addTransformsOnAction(ActionEvent actionEvent) {
    }

    public void displayResultsOnAction(ActionEvent actionEvent) {
    }

    
}
