package sample.MainFrame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import sample.TransportationProblemSimple;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class  MainController {

    private static int numRows = 3;
    private static int numCols = 3;


    private static ArrayList<Integer> supply;
    private static ArrayList<Integer> demand;
    private static ArrayList<Integer> newDemand;

    private static int [][] demandToPrint;
    private static double[] costPrice;
    private static double[][] costs;
    private static double[][] newCosts;
    private static int[][] supplySet;
    private static ArrayList<Double> reliability;

    //поле для ввода количества заводов
    @FXML
    private TextField factoriesValueField;

    //поле для ввода количества клиентов
    @FXML
    private TextField clientsValueField;

    //ячейки для ввода транспортных издержек
    @FXML
    private GridPane tariffMatrixGrid;

    //ячейки для ввода потребностей клиентов
    @FXML
    private GridPane supplyGrid;

    //ячейки для ввода запасов завода
    @FXML
    private GridPane demandGrid;

    //ячейки для ввода собестоимостей деталей
    @FXML
    private GridPane costPriceGrid;

    @FXML
    void initialize() {
        Pattern p = Pattern.compile("(\\d+?\\d*)?");

        //Условие на возможность вводить в поля "количество заводов" и "количество клиентов" ТОЛЬКО цифры от 0 до 9
        factoriesValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches() || newValue.length() > 1) factoriesValueField.setText(oldValue);
        });

        clientsValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches() || newValue.length() > 1) clientsValueField.setText(oldValue);
        });
        createMatrix();
    }

    //Функция создания матрицы условий, исходя из введенного значения в поля "количество заводов" и "количество клиентов"
    private void createMatrix(){
        //Паттерн на возможность вводить в поля ТОЛЬКО как целые числа, так и дробные
        Pattern d = Pattern.compile("\\d*|\\d+\\.\\d*");

        //Построение матрицы "транспортные издержки"
        tariffMatrixGrid.getChildren().clear();
        tariffMatrixGrid.getColumnConstraints().clear();
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            tariffMatrixGrid.getColumnConstraints().add(colConst);
        }
        tariffMatrixGrid.getRowConstraints().clear();
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints(30, 50, 50);
            tariffMatrixGrid.getRowConstraints().add(rowConst);
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#E0FFFF; -fx-border-color:#000000");
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!d.matcher(newValue).matches()) textField.setText(oldValue);
                });
                tariffMatrixGrid.add(textField, j, i);
                tariffMatrixGrid.setMargin(textField, new Insets(1, 3, 1, 3));
            }
        }
        tariffMatrixGrid.setDisable(true);

        //Построение полей "потребности клиентов"
        supplyGrid.getChildren().clear();
        supplyGrid.getColumnConstraints().clear();
        for (int i = 0; i < numRows; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            supplyGrid.getColumnConstraints().add(colConst);
        }
        supplyGrid.getRowConstraints().clear();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < 1; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#FFEFD5; -fx-border-color:#000000");
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!d.matcher(newValue).matches()) textField.setText(oldValue);
                });
                supplyGrid.add(textField, j, i);
                supplyGrid.setMargin(textField, new Insets(2, 3, 2, 3));
            }
        }
        supplyGrid.setDisable(true);

        //Построение полей "запасы заводов"
        demandGrid.getChildren().clear();
        demandGrid.getColumnConstraints().clear();
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints(55, 55, 55);
            demandGrid.getColumnConstraints().add(colConst);
        }
        demandGrid.getRowConstraints().clear();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < numCols; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#98FB98; -fx-border-color:#000000");
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!d.matcher(newValue).matches()) textField.setText(oldValue);
                });
                demandGrid.add(textField, j, i);
                demandGrid.setMargin(textField, new Insets(1, 3, 1, 3));
            }
        }
        demandGrid.setDisable(true);

        //Построение полей "себестоимости деталей"
        costPriceGrid.getChildren().clear();
        costPriceGrid.getColumnConstraints().clear();
        for (int i = 0; i < numRows; i++) {
            ColumnConstraints colConst = new ColumnConstraints(85, 55, 55);
            costPriceGrid.getColumnConstraints().add(colConst);
        }
        costPriceGrid.getRowConstraints().clear();
        int k = 1;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < numRows; j++) {
                TextField textField = new TextField();
                textField.alignmentProperty().setValue(Pos.CENTER);
                textField.setStyle("-fx-background-color:#E6E6FA; -fx-border-color:#000000; -fx-prompt-text-fill:#708090");
                textField.setPromptText("Завод №" + (k));
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!d.matcher(newValue).matches()) textField.setText(oldValue);
                });
                costPriceGrid.add(textField, j, i);
                costPriceGrid.setMargin(textField, new Insets(1, 3, 1, 3));
                k++;
            }
        }
        costPriceGrid.setDisable(true);
    }

    @FXML
    void enterValues(ActionEvent event) {
        numRows = 0;
        numCols = 0;

        //проверка на заполненность первоначальных полей
        try {
            numRows = Integer.parseInt(factoriesValueField.getText());
            numCols = Integer.parseInt(clientsValueField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, введите значения в необходимые поля!", ButtonType.OK);
            alert.show();
            return;
        }
        createMatrix();

        tariffMatrixGrid.setDisable(false);
        supplyGrid.setDisable(false);
        demandGrid.setDisable(false);
        costPriceGrid.setDisable(false);
    }

    @FXML
    void result(ActionEvent event) {

        supply = new ArrayList();
        demand = new ArrayList();

        //проверка на заполненность ячеек потребностей клиентов
        for (int i = 0; i < numRows; i++){
            try {
                supply.add(Integer.parseInt(((TextField) supplyGrid.getChildren().get(i)).getText()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, заполните значение потребности клиентов!", ButtonType.OK);
                alert.show();
                return;
            }
        }

        //проверка на заполненность ячеек запасов заводов
        for (int i = 0; i < numCols; i++){
            try {
                demand.add(Integer.parseInt(((TextField) demandGrid.getChildren().get(i)).getText()));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, заполните значения запасов заводов!", ButtonType.OK);
                alert.show();
                return;
            }
        }
        //проверка на заполненность ячеек себестоимостей деталей
        costPrice = new double[supply.size()];
        for (int i = 0; i < numRows; i++){
            try {
                costPrice[i] = Double.parseDouble(((TextField) costPriceGrid.getChildren().get(i)).getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, заполните значения себестоимостей деталей!", ButtonType.OK);
                alert.show();
                return;
            }
        }

        costs = new double[supply.size()][demand.size()];
        newCosts = new double[supply.size()][demand.size()];

        //проверка на заполненность ячеек транспортных издержек
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                try {
                    costs[i][j] = Double.parseDouble(((TextField) tariffMatrixGrid.getChildren().get(numCols * i + j)).getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, заполните матрицу транспортных издержек!", ButtonType.OK);
                    alert.show();
                    return;
                }
            }
        }
        printTable1();
        calculationsupplySet();
        calculationReliabilityFunction();

        //Переход на окно результатов
        Node node=(Node) event.getSource();
        Stage stage=(Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../ResultFrame/resultFrame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Считаем новые транспортные издержки и новые потребности клиентов
    private void printTable1() {
        for (int i = 0; i < supply.size(); i++) {
            for (int j = 0; j < demand.size(); j++) {
                newCosts[i][j] = costs[i][j] + costPrice[i];
            }
        }

        newDemand = new ArrayList<Integer>();
        demandToPrint = new int[6][demand.size()];

        double bk = 0.0;
        for (int k = 0; k <= 5; k++) {
            for (int i = 0; i < demand.size(); i++) {
                newDemand.add((int) ((bk*(demand.get(i) - (demand.get(i) * 0.5))) + (demand.get(i) * 0.5)));
                demandToPrint[k][i] = (int) ((bk*(demand.get(i) - (demand.get(i) * 0.5))) + (demand.get(i) * 0.5));
            }
            if(bk==0.8)
                bk+=0.1;
            else bk+=0.2;
            TransportationProblemSimple.start();
            newDemand = new ArrayList<Integer>();
        }
    }

    //Считаем множество потребностей
    private void calculationsupplySet(){
        int[] supplySetLow = new int[demand.size()];
        int[] supplySetMiddle = new int[demand.size()];
        int[] supplySetHigh = new int[demand.size()];

        supplySet = new int[demand.size()][3];

        for (int i = 0; i < demand.size(); i ++){
            supplySetLow[i] = demand.get(i)/2;
            supplySetMiddle[i] = demand.get(i);
            supplySetHigh[i] = findMaxSupply(supply) + (findMinDemand(demand)/6);
        }
        for (int i = 0; i < demand.size(); i ++){
            supplySet[i][0] = supplySetLow[i];
            supplySet[i][1] = supplySetMiddle[i];
            supplySet[i][2] = supplySetHigh[i];
        }
    }
    //Считаем надежность функции цели
    private void calculationReliabilityFunction(){
        double scale = Math.pow(10,2);
        reliability = new ArrayList<>();
        for (int i = 0 ; i < TransportationProblemSimple.getCost().size(); i ++){

            reliability.add(Math.ceil(((TransportationProblemSimple.getCost().get(5) - TransportationProblemSimple.getCost().get(i))/(TransportationProblemSimple.getCost().get(5) - TransportationProblemSimple.getCost().get(0))) * scale) / scale);
        }
    }
    //Находим максимыльный запас заводов
    private static int findMaxSupply(ArrayList<Integer> inputArray){
        int maxValue = inputArray.get(0);
        for(int i=1;i < inputArray.size();i++){
            if(inputArray.get(i) > maxValue){
                maxValue = inputArray.get(i);
            }
        }
        return maxValue;
    }
    //Находим минимальные потребности клиентов
    private static int findMinDemand(ArrayList<Integer> inputArray){
        int minValue = inputArray.get(0);
        for(int i=1;i<inputArray.size();i++){
            if(inputArray.get(0) < minValue){
                minValue = inputArray.get(0);
            }
        }
        return minValue;
    }

    public static ArrayList<Integer> getNewDemand(){
        return newDemand;
    }

    public static ArrayList<Integer> getDemand(){
        return demand;
    }

    public static ArrayList<Integer> getSupply(){
        return supply;
    }

    public static double getNewCosts(int i , int j){
        return newCosts[i][j];
    }

    public static int getSupplySet(int i , int j){
        return supplySet[i][j];
    }

    public static int getDemandToPrint(int i , int j){
        return demandToPrint[i][j];
    }

    public static ArrayList<Double> getReliabilityFunction(){
        return reliability;
    }

    public static int getNumRows(){
        return numRows;
    }

    public static int getNumCols(){
        return numCols;
    }

}

