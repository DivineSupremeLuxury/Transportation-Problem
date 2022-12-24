package sample.ResultFrame;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import sample.ChartFrame.ChartController;
import sample.MainFrame.MainController;
import sample.TransportationFinal;
import sample.TransportationProblemSimple;
import java.util.ArrayList;

public class ResultController {

    private ArrayList<Integer> supply;
    private ArrayList<Integer> demand;
    private static ArrayList<Integer> demandFinal;
    private static double[][] costs;
    private static int[][] supplySet;
    private static int[][] demandToPrint;
    private static double crossing;

    @FXML
    private GridPane tariffMatrixGrid;

    @FXML
    private GridPane supplyGrid;

    @FXML
    private GridPane demandGrid;

    @FXML
    private GridPane setGrid;

    @FXML
    private GridPane bGrid;

    @FXML
    private Label setLabel;

    @FXML
    private GridPane iterationGrid;

    @FXML
    private GridPane reliabilityDemandGrid;

    @FXML
    private GridPane iterationDemandGrid;

    @FXML
    private GridPane minCostGrid;

    @FXML
    private GridPane reliabilityFunctionGrid;

    @FXML
    private GridPane demandFinalGrid;

    @FXML
    private Label labelIter0;

    @FXML
    private Label labelIter1;

    @FXML
    private Label labelIter2;

    @FXML
    private Label labelIter3;

    @FXML
    private Label labelIter4;

    @FXML
    private Label labelIter5;

    @FXML
    private Label labelCrossing;


    @FXML
    private Label textLabel;

    @FXML
    private Label matrixFinalLabel;

    @FXML
    void initialize() {

        supply = MainController.getSupply();
        demand = MainController.getDemand();
        costs = new double[supply.size()][demand.size()];
        supplySet = new int[demand.size()][3];
        demandToPrint = new int[6][demand.size()];
        for (int i = 0; i < MainController.getNumRows(); i++){
            for (int j = 0; j < MainController.getNumCols(); j++){
                costs[i][j] = MainController.getNewCosts(i,j);
            }
        }

        for (int i = 0; i < MainController.getNumCols(); i++){
            for (int j = 0; j < 3; j++){
                supplySet[i][j] = MainController.getSupplySet(i,j);
            }
        }

        for (int i = 0; i < 6; i++){
            for (int j = 0; j < demand.size(); j++){
                demandToPrint[i][j] = MainController.getDemandToPrint(i,j);
            }
        }

        //отрисовываем и заполняем матрицу транспортных издержек
        tariffMatrixGrid.getChildren().clear();
        tariffMatrixGrid.getColumnConstraints().clear();

        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            tariffMatrixGrid.getColumnConstraints().add(colConst);
        }
        tariffMatrixGrid.getRowConstraints().clear();
        for (int i = 0; i < MainController.getNumRows(); i++) {
            RowConstraints rowConst = new RowConstraints(30, 50, 50);
            tariffMatrixGrid.getRowConstraints().add(rowConst);
        }
        for (int i = 0; i < MainController.getNumRows(); i++) {
            for (int j = 0; j < MainController.getNumCols(); j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#E0FFFF; -fx-border-color:#000000");
                textField.setEditable(false);
                tariffMatrixGrid.add(textField, j, i);
                tariffMatrixGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                ((TextField) tariffMatrixGrid.getChildren().get(MainController.getNumCols() * i + j)).setText(String.valueOf(String.format("%.2f",costs[i][j])));
            }
        }

        //отрисовываем и заполняем ячейки потребностей клиентов
        supplyGrid.getChildren().clear();
        supplyGrid.getColumnConstraints().clear();
        for (int i = 0; i < MainController.getNumRows(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            supplyGrid.getColumnConstraints().add(colConst);
        }
        supplyGrid.getRowConstraints().clear();
        for (int i = 0; i < MainController.getNumRows(); i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#FFEFD5; -fx-border-color:#000000");
                textField.setEditable(false);
                supplyGrid.add(textField, j, i);
                supplyGrid.setMargin(textField, new Insets(2, 3, 2, 3));
                ((TextField) supplyGrid.getChildren().get(i)).setText(String.valueOf(supply.get(i)));
            }
        }

        //отрисовываем и заполняем ячейки запасов завода
        demandGrid.getChildren().clear();
        demandGrid.getColumnConstraints().clear();
        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            demandGrid.getColumnConstraints().add(colConst);
        }
        demandGrid.getRowConstraints().clear();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < MainController.getNumCols(); j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#98FB98; -fx-border-color:#000000");
                textField.setEditable(false);
                demandGrid.add(textField, j, i);
                demandGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                ((TextField) demandGrid.getChildren().get(j)).setText(String.valueOf(demand.get(j)));
            }
        }

        //отрисовываем ячейки нечетких величин, заданными на универсольном множестве [0;1]
        bGrid.getChildren().clear();
        bGrid.getColumnConstraints().clear();
        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(50, 50, 50);
            bGrid.getColumnConstraints().add(colConst);
        }
        bGrid.getRowConstraints().clear();
        for (int i = 0; i < MainController.getNumCols(); i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#D3D3D3; -fx-border-color:#000000");
                textField.setEditable(false);
                bGrid.add(textField, j, i);
                bGrid.setMargin(textField, new Insets(3, 3, 3, 3));
                ((TextField) bGrid.getChildren().get(i)).setText("b\u0302" + i + " =");
            }
        }
        //заполняем ячейки нечетких величин, заданными на универсольном множестве [0;1]
        setGrid.getChildren().clear();
        setGrid.getColumnConstraints().clear();

        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            setGrid.getColumnConstraints().add(colConst);

        }
        setGrid.getRowConstraints().clear();
        for (int i = 0; i < 3; i++) {
            RowConstraints rowConst = new RowConstraints(30, 50, 50);
            setGrid.getRowConstraints().add(rowConst);
        }
        for (int i = 0; i < MainController.getNumCols(); i++) {
            for (int j = 0; j < 3; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#D3D3D3; -fx-border-color:#000000");
                textField.setEditable(false);
                setGrid.add(textField, j, i);
                setGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                ((TextField) setGrid.getChildren().get(3 * i + j)).setText(String.valueOf(supplySet[i][j]));
            }
        }
        setLabel.textProperty().bind(Bindings.format("Потребности являются нечеткими и задаются нечеткими величинами, заданными на универсальном множестве [%d;%d]" ,0 , supplySet[demand.size()-1][2]));

        //отрисовываем и заполняем ячейки "№ шага"
        iterationGrid.getChildren().clear();
        iterationGrid.getColumnConstraints().clear();
        for (int i = 0; i < 6; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            iterationGrid.getColumnConstraints().add(colConst);
        }
        iterationGrid.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#778899; -fx-border-color:#000000");
                textField.setEditable(false);
                iterationGrid.add(textField, j, i);
                iterationGrid.setMargin(textField, new Insets(2, 3, 2, 3));
                ((TextField) iterationGrid.getChildren().get(i)).setText(String.valueOf(i));
            }
        }

        //отрисовываем и заполняем ячейки "надежность потребностей"
        double k = 0.0;
        reliabilityDemandGrid.getChildren().clear();
        reliabilityDemandGrid.getColumnConstraints().clear();
        for (int i = 0; i < 6; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            reliabilityDemandGrid.getColumnConstraints().add(colConst);
        }
        reliabilityDemandGrid.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#faebd7; -fx-border-color:#000000");
                textField.setEditable(false);
                reliabilityDemandGrid.add(textField, j, i);
                reliabilityDemandGrid.setMargin(textField, new Insets(2, 3, 2, 3));
                ((TextField) reliabilityDemandGrid.getChildren().get(i)).setText(String.valueOf(String.format("%2.1f",k)));
                if (k == 0.8)
                    k += 0.1;
                else k += 0.2;
            }
        }

        //отрисовываем и заполняем ячейки "потребности клиентов"
        iterationDemandGrid.getChildren().clear();
        iterationDemandGrid.getColumnConstraints().clear();

        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            iterationDemandGrid.getColumnConstraints().add(colConst);
        }
        iterationDemandGrid.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            RowConstraints rowConst = new RowConstraints(32, 55, 55);
            iterationDemandGrid.getRowConstraints().add(rowConst);
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < MainController.getNumCols(); j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#98FB98; -fx-border-color:#000000");
                textField.setEditable(false);
                iterationDemandGrid.add(textField, j, i);
                iterationDemandGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                ((TextField) iterationDemandGrid.getChildren().get(MainController.getNumCols() * i + j)).setText(String.valueOf(demandToPrint[i][j]));
            }
        }

        //отрисовываем и заполняем ячейки "минимальные затраты"
        minCostGrid.getChildren().clear();
        minCostGrid.getColumnConstraints().clear();
        for (int i = 0; i < 6; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            minCostGrid.getColumnConstraints().add(colConst);
        }
        minCostGrid.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#dda0dd; -fx-border-color:#000000");
                textField.setEditable(false);
                minCostGrid.add(textField, j, i);
                minCostGrid.setMargin(textField, new Insets(2, 3, 2, 3));
                ((TextField) minCostGrid.getChildren().get(i)).setText(String.valueOf(TransportationProblemSimple.getCost().get(i)));
            }
        }

        //отрисовываем и заполняем ячейки "надежность функций цели"
        reliabilityFunctionGrid.getChildren().clear();
        reliabilityFunctionGrid.getColumnConstraints().clear();
        for (int i = 0; i < 6; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            reliabilityFunctionGrid.getColumnConstraints().add(colConst);
        }
        reliabilityFunctionGrid.getRowConstraints().clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#40e0d0; -fx-border-color:#000000");
                textField.setEditable(false);
                reliabilityFunctionGrid.add(textField, j, i);
                reliabilityFunctionGrid.setMargin(textField, new Insets(2, 3, 2, 3));
                ((TextField) reliabilityFunctionGrid.getChildren().get(i)).setText(String.valueOf(MainController.getReliabilityFunction().get(i)));
            }
        }

        //отрисовываем и заполняем строчки со значениями итераций
        String iterClient0="";
        String iterClient1="";
        String iterClient2="";
        String iterClient3="";
        String iterClient4="";
        String iterClient5="";
        for (int i = 0; i < demand.size();i++) {
            iterClient0 += "Клиент " + (i+1)+ ": " + demandToPrint[0][i] + " ";
        }
        labelIter0.setText("Итерация :" + 0 + " \n Надежность потребностей :" + 0.0 + "\n Потребности клиентов : \n "+ iterClient0 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(0) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(0));

        for (int i = 0; i < demand.size();i++) {
            iterClient1 += "Клиент " + (i+1)+ ": " + demandToPrint[1][i] + " ";
        }
        labelIter1.setText("\nИтерация :" + 1 + " \n Надежность потребностей :" + 0.2 + "\n Потребности клиентов : \n "+ iterClient1 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(1) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(1));

        for (int i = 0; i < demand.size();i++) {
            iterClient2 += "Клиент " + (i+1)+ ": " + demandToPrint[2][i] + " ";
        }
        labelIter2.setText("\nИтерация :" + 2 + " \n Надежность потребностей :" + 0.4 + "\n Потребности клиентов : \n "+ iterClient2 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(2) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(2));

        for (int i = 0; i < demand.size();i++) {
            iterClient3 += "Клиент " + (i+1)+ ": " + demandToPrint[3][i] + " ";
        }
        labelIter3.setText("\nИтерация :" + 3 + " \n Надежность потребностей :" + 0.6 + "\n Потребности клиентов : \n "+ iterClient3 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(3) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(3));

        for (int i = 0; i < demand.size();i++) {
            iterClient4 += "Клиент " + (i+1)+ ": " + demandToPrint[4][i] + " ";
        }
        labelIter4.setText("\nИтерация :" + 4 + " \n Надежность потребностей :" + 0.8 + "\n Потребности клиентов : \n "+ iterClient4 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(4) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(4));

        for (int i = 0; i < demand.size();i++) {
            iterClient5 += "Клиент " + (i+1)+ ": " + demandToPrint[5][i] + " ";
        }
        labelIter5.setText("\nИтерация :" + 5 + " \n Надежность потребностей :" + 0.9 + "\n Потребности клиентов : \n "+ iterClient5 + "\n Минимальные затраты :" + TransportationProblemSimple.getCost().get(5) + " \n Надежность функции цели :" + MainController.getReliabilityFunction().get(5));

        //находим точку пересечения графиков
        Line l1 = new Line(new Point(50, 0), new Point(100, 1));
        Line l2 = new Line(new Point(60, MainController.getReliabilityFunction().get(1)), new Point(80, MainController.getReliabilityFunction().get(3)));
        String[] values = String.valueOf(findIntersection(l1, l2)).split(";");
        crossing = Math.round(Double.parseDouble(values[0]));
        crossing /= 100;

        //считаем итоговые потребности клиентов
        demandFinal = new ArrayList<>();
        for (int i =0; i < demand.size();i++){
            demandFinal.add((int) (demand.get(i)*crossing));
        }

        labelCrossing.textProperty().bind(Bindings.format("Максимальное значение уверенности в том, что план эффективен по суммарным затратам и уровню обеспечения клиентов \nисходя из пересечения графиков равно %1.2f",crossing));
        textLabel.setText("Благодаря полученному значению уровня обеспечения определяем сколько деталей нужно будет поставить каждому клиенту:");

        //отрисовываем и заполняем строчки со значениями "сколько деталей нужно будет поставить каждому клиенту"
        demandFinalGrid.getChildren().clear();
        demandFinalGrid.getColumnConstraints().clear();
        for (int i = 0; i < MainController.getNumCols(); i++) {
            ColumnConstraints colConst = new ColumnConstraints(80, 80, 80);
            demandFinalGrid.getColumnConstraints().add(colConst);
        }
        demandFinalGrid.getRowConstraints().clear();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < MainController.getNumCols(); j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#98FB98; -fx-border-color:#000000");
                textField.setEditable(false);
                demandFinalGrid.add(textField, j, i);
                demandFinalGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                ((TextField) demandFinalGrid.getChildren().get(j)).setText("b" + (j+1) + " = " + String.valueOf(demandFinal.get(j)));
            }
        }

        TransportationFinal.start();

        //Оптимальный план перевозок и минимальные затраты
        matrixFinalLabel.setText(TransportationFinal.getMatrixFinal());
    }

    public static ArrayList<Integer> getDemandFinal(){
        return demandFinal;
    }

    //класс обозначения точек графиков
    private static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("%s; %s", x, y);
        }
    }
    //класс обозначения линий графиков
    private static class Line {
        Point s, e;

        Line(Point s, Point e) {
            this.s = s;
            this.e = e;
        }
    }
    //метод поиска точки пересечения двух графиков
    private static Point findIntersection(Line l1, Line l2) {
        double a1 = l1.e.y - l1.s.y;
        double b1 = l1.s.x - l1.e.x;
        double c1 = a1 * l1.s.x + b1 * l1.s.y;

        double a2 = l2.e.y - l2.s.y;
        double b2 = l2.s.x - l2.e.x;
        double c2 = a2 * l2.s.x + b2 * l2.s.y;

        double delta = a1 * b2 - a2 * b1;
        return new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
    }

    //Переход на окно с графиком
    @FXML
    void showChart(ActionEvent event) {
        Stage stage= new Stage();
        ChartController chart = new ChartController();
        try {
            chart.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}