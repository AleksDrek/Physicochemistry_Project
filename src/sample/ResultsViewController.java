package sample;

import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultsViewController {
    public LineChart firstMethodLineChart;
    public LineChart secondMethodLineChart;
    public LineChart thirdMethodLineChart;

    private Calculations calculations;

    public void saveResultsOnAction(ActionEvent actionEvent) {
    //tumturum tum, zapisywanie do pliku, potem wyswietl info o tym, ze sie udalo
        File fileMeth1 = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\method1.txt");
        File fileMeth2 = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\method2.txt");
        File fileMeth3 = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\method3.txt");
        FileWriter saveE1 = null, saveE2 = null, save3 = null;
        String zapis1, zapis2, zapis3;

        try {
            saveE1 = new FileWriter(fileMeth1);
            saveE2 = new FileWriter(fileMeth2);
            save3 = new FileWriter(fileMeth3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for (int i = 1; i < length; i++) {

            zapis1 = Double.toString([i]);
            zapis2 = Double.toString(enthalpy2[i]);

            try {
                saveE1.write(zapis1);
                saveE1.write(System.getProperty("line.separator"));

                saveE2.write(zapis2);
                saveE2.write(System.getProperty("line.separator"));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/

        try {
            saveE1.close();
            saveE2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void closeTheWindowOnAction(ActionEvent actionEvent) {
        Main.goToFirstScene();
    }

    public void init()
    {
        Main.goToResults();

        clearAllLineCharts();
        setNamesOFAllLineCharts();

        //zmienic funkcje dotyczaca ustawiania series. wrecz stworzyc funkcje, ktora bedzie dzialac na Controllerze i z niego przekazywać do reszty, z niego operować na pozostalych

        calculations = Controller.getCalculations();
        calculations.calculateMethod1(calculations.getTransformsData().length/3);
        calculations.calculateMethod2(calculations.getTransformsData().length/3);
        calculations.calculateMethod3(calculations.getTransformsData().length/3);

        addSeriesToChart();


    }

    private void addSeriesToChart() {
        firstMethodLineChart.getData().addAll(calculations.method11Series, calculations.method12Series, calculations.method13Series);
        secondMethodLineChart.getData().add(calculations.method12Series);
        thirdMethodLineChart.getData().add(calculations.method13Series);

    }

    private void setNamesOFAllLineCharts() {
        firstMethodLineChart.setTitle("Pierwsza metoda");
        secondMethodLineChart.setTitle("Druga metoda");
        thirdMethodLineChart.setTitle("Trzecia metoda");
    }

    private void clearAllLineCharts() {
        firstMethodLineChart.getData().clear();
        secondMethodLineChart.getData().clear();
        thirdMethodLineChart.getData().clear();

        firstMethodLineChart.setCreateSymbols(false);
        secondMethodLineChart.setCreateSymbols(false);
        thirdMethodLineChart.setCreateSymbols(false);
    }
}
