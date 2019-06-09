package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.io.*;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculations {

    private double[] temperatures;
    private double[] specific_heat;

    private double[] interpolated_temperatures;
    private double[] interpolated_specific_heat;
    private double[] enthalpy1;
    private double[] enthalpy2;

    private double[] transformsData;

    public void init(double[] temp, double[] heat, int length) {
        temperatures = new double[length];
        specific_heat = new double[length];
        temperatures = Arrays.copyOf(temp, length);
        specific_heat = Arrays.copyOf(heat, length);

        for (int i = 0; i < length; i++) {
            System.out.println("Temperatures: ");
            System.out.println(temperatures[i]);
        }

        for (int i = 0; i < length; i++) {
            System.out.println("Ciepło właściwe: ");
            System.out.println(specific_heat[i]);
        }

        interpolate(temperatures[0], temperatures[length - 1]);
    }

    public void interpolate(double firstTemp, double lastTemp) {
        int length = (int) (lastTemp - firstTemp);
        interpolated_specific_heat = new double[length];
        interpolated_temperatures = new double[length];
        double[] interpolationCoefficients = new double[temperatures.length - 1];

        for (int i = 0; i < interpolationCoefficients.length; i++) {
            interpolationCoefficients[i] = (specific_heat[i + 1] - specific_heat[i]) / (temperatures[i + 1] - temperatures[i]);
        }

        interpolated_temperatures[0] = temperatures[0];
        interpolated_specific_heat[0] = specific_heat[0];
        int coeffIterator = 0;


        File fileTemp = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\interpolate_temp.txt");
        File fileHeat = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\interpo_heat.txt");
        FileWriter saveTemp = null, saveHeat = null;

        try {
            saveTemp = new FileWriter(fileTemp);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nie otworzylo pliku do zapisania TEMPERATURY, a nie chce mi sie tego wszystkiego wypisywac");

        }

        try {
            saveHeat = new FileWriter(fileHeat);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nie otworzylo pliku do zapisania ciepla, a nie chce mi sie tego wszystkiego wypisywac");
        }

        String zapis;

        for (int i = 1; i < length; i++) {
            interpolated_temperatures[i] = interpolated_temperatures[i - 1] + 1;
            //saveTemp.write((int)interpolated_temperatures[i-1]);
            zapis = Double.toString(interpolated_temperatures[i - 1]);

            try {
                saveTemp.write(zapis);
                saveTemp.write(System.getProperty("line.separator"));


            } catch (IOException e) {
                e.printStackTrace();
            }

            if (interpolated_temperatures[i] == temperatures[coeffIterator + 1]) {
                coeffIterator++;
            }

            interpolated_specific_heat[i] = interpolated_specific_heat[i - 1] + interpolationCoefficients[coeffIterator];
            zapis = Double.toString(interpolated_specific_heat[i - 1]);
            try {
                saveHeat.write(zapis);
                saveHeat.write(System.getProperty("line.separator"));

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        try {
            saveTemp.close();
            saveHeat.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        calculateEnthalpy(length);

    }

    public void calculateEnthalpy(int length) {
        enthalpy1 = new double[length];
        enthalpy2 = new double[length];

        File fileEnth1 = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\enthalpy1.txt");
        File fileEnth2 = new File("E:\\Studia\\Semestr VIII\\Fizykochemia Procesów\\src\\sample\\enthalpy2.txt");
        FileWriter saveE1 = null, saveE2 = null;
        String zapis1, zapis2;

        try {
            saveE1 = new FileWriter(fileEnth1);
            saveE2 = new FileWriter(fileEnth2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        enthalpy1[0] = (interpolated_temperatures[1] - interpolated_temperatures[0]) * interpolated_specific_heat[0];
        zapis1 = Double.toString(enthalpy1[0]);
        enthalpy2[0] = 0.0;
        zapis2 = Double.toString(enthalpy2[0]);

        try {
            saveE1.write(zapis1);
            saveE1.write(System.getProperty("line.separator"));

            saveE2.write(zapis2);
            saveE2.write(System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < length; i++) {
            enthalpy1[i] = enthalpyEquation(interpolated_temperatures[i - 1], interpolated_temperatures[i], interpolated_specific_heat[i - 1], interpolated_specific_heat[i], enthalpy1[i - 1]);
            enthalpy2[i] = enthalpyEquation(interpolated_temperatures[i - 1], interpolated_temperatures[i], interpolated_specific_heat[i - 1], interpolated_specific_heat[i], enthalpy2[i - 1]);
            zapis1 = Double.toString(enthalpy1[i]);
            zapis2 = Double.toString(enthalpy2[i]);

            try {
                saveE1.write(zapis1);
                saveE1.write(System.getProperty("line.separator"));

                saveE2.write(zapis2);
                saveE2.write(System.getProperty("line.separator"));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            saveE1.close();
            saveE2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private double enthalpyEquation(double temp1, double temp2, double heat1, double heat2, double prevEnthalpy) {

        double heatAverage = (heat1 + heat2) / 2;
        return (heatAverage * (temp2 - temp1) + prevEnthalpy);
    }

    public XYChart.Series setEnthalpySeries(int enthNumber) {
        int length = enthalpy1.length;
        Double y, x;
        XYChart.Series enthalpySeries = new XYChart.Series<>();

        if (enthNumber == 1) {
            for (int i = 0; i < length; i++) {
                y = new Double(enthalpy1[i]);
                x = new Double(interpolated_temperatures[i]);

                enthalpySeries.getData().add(new XYChart.Data(x, y));
            }
        } else {
            for (int i = 0; i < length; i++) {
                y = new Double(enthalpy2[i]);
                x = new Double(interpolated_temperatures[i]);

                enthalpySeries.getData().add(new XYChart.Data(x, y));
            }
        }


        return enthalpySeries;
    }

    private void calculateMethod1(int numberOfMethods) {
        int index1 = (int) (transformsData[0] - interpolated_temperatures[0]);

        if (transformsData[0] == interpolated_temperatures[index1]) {
            interpolated_specific_heat[index1] += transformsData[2];
        }


    }

    private void calculateMethod2() {

    }

    private void calculateMethod3() {

    }

    private void addTransformsChanges(int numberOfChanges) {
        int indexArray[] = new int[numberOfChanges];

        for (int i = 0; i < numberOfChanges; i++) {
            indexArray[i] = (int)(transformsData[i*3] - interpolated_temperatures[0]);
        }

    }


    public void setTransformsData(double[] transformsData) {
        this.transformsData = transformsData;
    }
}