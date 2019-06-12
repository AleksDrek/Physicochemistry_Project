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
    //private double[] enthalpyMethod1;
    //private double[] specific_heat_after_transforms;

    public double[] getTransformsData() {
        return transformsData;
    }

    private double[] transformsData;


    //serie do wykresow wynikowych

    public XYChart.Series method11Series, method12Series, method13Series;

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

                enthalpySeries.getData().add(new XYChart.Data(new Double(interpolated_temperatures[i]), new Double(enthalpy1[i])));
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

    public void calculateMethod1(int numberOfMethods) {
        method11Series = new XYChart.Series<>();

        double[] enthalpyMethod1 = new double[enthalpy1.length]; //czy method1, czy zawsze method, a zapisuję gdzieś indziej <do jakiegoś chart series czy cos>
        enthalpyMethod1 = enthalpy1.clone();
        double[] specific_heat_after_transforms = new double[interpolated_specific_heat.length]; // tutaj mozna pomyslec czy na pewno to tutaj inicjalizowac, czy jakos inaczej, zeby nie bylo bledu
        specific_heat_after_transforms = interpolated_specific_heat.clone();

        int index[] = findTempIndex(numberOfMethods);
        int start = index[0];
        int licznik = 0, licznik_heat = 2;

        //specific_heat_after_transforms[index[0]] += transformsData[2];

        for (int i = start; i < specific_heat_after_transforms.length; i++) {
            if (i == index[licznik]) {
                specific_heat_after_transforms[i] += transformsData[licznik_heat];

                if (licznik < index.length - 2) {
                    licznik += 2;
                    licznik_heat += 3;
                }
            }

            enthalpyMethod1[i] = enthalpyEquation(interpolated_temperatures[i - 1], interpolated_temperatures[i], specific_heat_after_transforms[i - 1], specific_heat_after_transforms[i], enthalpyMethod1[i - 1]);

        }

        for (int i = 0; i < specific_heat_after_transforms.length; i++) {
            method11Series.getData().add(new XYChart.Data(new Double(interpolated_temperatures[i]), new Double(enthalpyMethod1[i])));
        }

    }


    public void calculateMethod2(int numberOfMethods) {
        method12Series = new XYChart.Series<>();

        double[] enthalpyMethod1 = new double[enthalpy1.length];
        enthalpyMethod1 = enthalpy1.clone();
        double[] specific_heat_after_transforms = new double[interpolated_specific_heat.length];
        specific_heat_after_transforms = interpolated_specific_heat.clone();

        int index[] = findTempIndex(numberOfMethods); // tablica zawierajaca indeksy temperatur przemian w glownej tablicy
        double diff = (interpolated_temperatures[index[1]] - interpolated_temperatures[index[0]]) / 2;

        int tx = (int) (index[0] + diff); //indeks srodkowej temperatury
        specific_heat_after_transforms[tx] += transformsData[2];
        int licznik = 2, licznik_heat = 5;


        for (int i = tx; i < enthalpyMethod1.length; i++) {

            enthalpyMethod1[i] = enthalpyEquation(interpolated_temperatures[i - 1], interpolated_temperatures[i], specific_heat_after_transforms[i], specific_heat_after_transforms[i - 1], enthalpyMethod1[i - 1]);

            if (i == index[licznik]) {
                diff = (interpolated_temperatures[index[licznik + 1]] - interpolated_temperatures[index[licznik]]) / 2;
                tx = (int) (index[licznik] + diff);
                specific_heat_after_transforms[tx] += transformsData[licznik_heat];
                if (licznik < index.length - 2) {
                    licznik += 2;
                    licznik_heat += 3;
                }
            }
        }

        for (int i = 0; i < specific_heat_after_transforms.length; i++) {
            method12Series.getData().add(new XYChart.Data(new Double(interpolated_temperatures[i]), new Double(enthalpyMethod1[i])));
        }

    }

    public void calculateMethod3(int numberOfMethods) {

        method13Series = new XYChart.Series<>();

        double[] enthalpyMethod1 = new double[enthalpy1.length]; //czy method1, czy zawsze method, a zapisuję gdzieś indziej <do jakiegoś chart series czy cos>
        enthalpyMethod1 = enthalpy1.clone();
        double[] specific_heat_after_transforms = new double[interpolated_specific_heat.length]; // tutaj mozna pomyslec czy na pewno to tutaj inicjalizowac, czy jakos inaczej, zeby nie bylo bledu
        specific_heat_after_transforms = interpolated_specific_heat.clone();

        int index[] = findTempIndex(numberOfMethods);
        int start = index[1];
        int licznik = 3, licznik_heat = 5;

        specific_heat_after_transforms[index[1]] += transformsData[2];

        for (int i = start; i < specific_heat_after_transforms.length; i++) {
            enthalpyMethod1[i] = enthalpyEquation(interpolated_temperatures[i - 1], interpolated_temperatures[i], specific_heat_after_transforms[i - 1], specific_heat_after_transforms[i], enthalpyMethod1[i - 1]);
            if (i == index[licznik]) {
                specific_heat_after_transforms[i] += transformsData[licznik_heat];

                if (licznik < index.length - 2) {
                    licznik += 2;
                    licznik_heat += 3;
                }
            }

        }

        for (int i = 0; i < specific_heat_after_transforms.length; i++) {
            method13Series.getData().add(new XYChart.Data(new Double(interpolated_temperatures[i]), new Double(enthalpyMethod1[i])));
        }


    }

    private int[] findTempIndex(int numberOfChanges) {
        int indexArray[] = new int[numberOfChanges * 2];
        int licznik = 0;

        for (int i = 0; i < indexArray.length; i++) {
            if (i % 2 == 0 && i != 0)
                licznik++;
            indexArray[i] = (int) (transformsData[licznik] - interpolated_temperatures[0]);
            licznik++;
        }

        return indexArray;
    }


    public void setTransformsData(double[] transformsData) {
        this.transformsData = transformsData;
    }
}