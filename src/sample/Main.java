package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene firstScene, transformationScene, resultScene;

    private static Controller controller;
    private TransformationViewController transformationViewController;
    private static ResultsViewController resultsViewController;

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent first_scene_layout = mainLoader.load();
        controller = mainLoader.getController();
        firstScene = new Scene(first_scene_layout);

        FXMLLoader transformationLoader = new FXMLLoader(getClass().getResource("transformationView.fxml"));
        Parent transformation_scene_layout = transformationLoader.load();
        transformationViewController = transformationLoader.getController();
        transformationScene = new Scene(transformation_scene_layout);

        /*FXMLLoader resultsLoader = new FXMLLoader(getClass().getResource("resultsView.fxml"));
        Parent results_scene_layout = resultsLoader.load();
        resultsViewController = resultsLoader.getController();
        resultScene = new Scene(results_scene_layout);*/

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        goToFirstScene();

        controller.setTransformationViewController(transformationViewController);

    }

    public static void goToFirstScene() {
        primaryStage.setTitle("Zadanko Fizykochemia Procesów");
        primaryStage.setScene(firstScene);
        primaryStage.show();
    }

    public static void goToTransformation()
    {
        primaryStage.setTitle("Podaj przemiany");
        primaryStage.setScene(transformationScene);
        primaryStage.show();
    }

    public static void goToResults()
    {
        primaryStage.setTitle("Wyniczki");
        primaryStage.setScene(resultScene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
