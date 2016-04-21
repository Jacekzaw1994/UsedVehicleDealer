package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.Model.*;
import javafx.collections.FXCollections;
import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML
    public TextField ID_MARK;
    @FXML
    public TextField ID_NAME;
    @FXML
    public TextField ID_PRICE;
    @FXML
    public ListView ID_LIST;
    @FXML
    public ComboBox ID_TYPE;

    private SingletonVehicles singletonVehicles;
    private String currentVehicleType = null;
    private ObservableList<Vehicle> listModel;
    @FXML
    public void lendAction(ActionEvent actionEvent) {
        int n = ID_LIST.getSelectionModel().getSelectedIndex();
        if(n != -1) {
            if(singletonVehicles.getVehicleList().get(n).isAvailability()) {
                singletonVehicles.getVehicleList().get(n).lend();
                refreshView(currentVehicleType);
            } else{
                JOptionPane.showMessageDialog(null, "Pojazd zosta³ ju¿ wypo¿yczony");
            }
        }
    }
    @FXML
    public void returnAction(ActionEvent actionEvent) {
        int n = ID_LIST.getSelectionModel().getSelectedIndex();
        if(n != -1) {
            if(singletonVehicles.getVehicleList().get(n).isAvailability()){
                JOptionPane.showMessageDialog(null, "Pojazd zosta³ ju¿ zwrócony");
            } else {
                singletonVehicles.getVehicleList().get(n).returnVehicle();
                refreshView(currentVehicleType);
            }
        }
    }
    @FXML
    public void buyAction(ActionEvent actionEvent) {
        Vehicle vehicle = null;
        boolean flag = false;
        try {
            String type = ID_TYPE.getSelectionModel().getSelectedItem().toString();
            if (type.equals("Car")) {
                // if previous clicked sa
                if (currentVehicleType.equals("Car"))
                    flag = true;
                vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.CAR);
            } else if (type.equals("Motor")) {
                if (currentVehicleType.equals("Motor"))
                    flag = true;
                vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.MOTOR);
            } else if (type.equals("Quad")) {
                if (currentVehicleType.equals("Quad"))
                    flag = true;
                vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.QUAD);
            }
            if (vehicle != null && !ID_PRICE.getCharacters().toString().isEmpty() && !ID_MARK.getCharacters().toString().isEmpty() &&
                    !ID_NAME.getCharacters().toString().isEmpty()) {
                vehicle.setAvailability(true);
                vehicle.setMark(ID_MARK.getCharacters().toString());
                vehicle.setPrice(Double.valueOf(ID_PRICE.getCharacters().toString()));
                vehicle.setName(ID_NAME.getCharacters().toString());
                vehicle.buy(type);
                if (flag) {
                    refreshView(type);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Wprowadz wszystkie dane!");
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Wprowadz wszystkie dane!");
        }


    }
    @FXML
    public void resetAction(ActionEvent actionEvent) {
        ID_MARK.clear();
        ID_NAME.clear();
        ID_PRICE.clear();
    }
    @FXML
    public void sellAction(ActionEvent actionEvent) {
        int n = ID_LIST.getSelectionModel().getSelectedIndex();
        if(n != -1) {
            singletonVehicles.getVehicleList().get(n).sell();
            refreshView(currentVehicleType);
        }
    }
    @FXML
    public void showCars(ActionEvent actionEvent) {

        setList("Car");
        currentVehicleType = "Car";
    }
    @FXML
    public void showQuads(ActionEvent actionEvent) {

        setList("Quad");
        currentVehicleType = "Quad";
    }
    @FXML
    public void showMotors(ActionEvent actionEvent) {

        setList("Motor");
        currentVehicleType = "Motor";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.singletonVehicles = SingletonVehicles.getInstance();
        listModel = FXCollections.observableList(singletonVehicles.getVehicleList());
        ID_LIST.setItems(listModel);
    }

    private void setList(String type){
        if(!type.equals(currentVehicleType)) {
            refreshView(type);
        }
    }
    private void refreshView(String type){
        listModel.clear();
        singletonVehicles.loadDataFromDB(type);
        listModel = FXCollections.observableList(singletonVehicles.getVehicleList());
        ID_LIST.setItems(listModel);
    }
}
