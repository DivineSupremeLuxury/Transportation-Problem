package sample.ChartFrame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.MainFrame.MainController;
import java.util.ArrayList;

public class ChartController extends Application{

    private static int startX = 50;
    private static double startY = 0.0;

    @FXML
    private static LineChart<?, ?> lineChart;

    @FXML
    private static NumberAxis xAxis;

    @FXML
    private static NumberAxis yAxis;

    @FXML
    void initialize() {

    }
    //Построение графика
    @Override
    public void start(Stage stage) throws Exception {
        ArrayList<Double> reliability = MainController.getReliabilityFunction();

        xAxis = new NumberAxis(0,100,10);
        xAxis.setLabel("Уровень потребностей в процентах от максимального");

        yAxis = new NumberAxis(0,1.0,0.1);
        yAxis.setLabel("Надежность функции цели");

        lineChart = new LineChart<>(xAxis,yAxis);

        XYChart.Series series = new XYChart.Series();
        series.setName("Надежность потребностей");
        for (int i = 0; i < 6 ;i++) {
            series.getData().add(new XYChart.Data<>(startX, startY));
            startX += 10;
            startY += 0.2;
        }
        startX = 50;
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Степень уверенности в том, что план эффективен по затратам");
        for (int i = 0; i < reliability.size(); i++){
            series2.getData().add(new XYChart.Data<>(startX, reliability.get(i)));
            if (startX == 90)
                startX += 5;
            else
                startX += 10;
        }
        lineChart.getData().addAll(series,series2);

        StackPane root = new StackPane(lineChart);
        Scene scene = new Scene(root,600,400);
        stage.setTitle("График");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            series.getData().clear();
            series2.getData().clear();
            startX = 50;
            startY = 0.0;
        });
    }
}
