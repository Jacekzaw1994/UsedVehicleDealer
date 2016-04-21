package sample.Model;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wojciech on 2015-10-31.
 */

/**
 * Singleton class
 * Model class for MVC
 */
public class SingletonVehicles {
    private static SingletonVehicles instance = null;
    private List<Vehicle> vehicleList;
    private SingletonVehicles(){
        vehicleList = new ArrayList<>();
    }
    public static SingletonVehicles getInstance(){
        synchronized (SingletonVehicles.class) {
            if (instance == null) {
                instance = new SingletonVehicles();
            }
        }
        return instance;
    }

    /**
     * method load data from Vehicles.sqlite to field vehicleList
     */
    public void loadDataFromDB(String type){
        try{
            ConnectorDB db = new ConnectorDB();
            Statement statement = db.getConn().createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from Vehicle WHERE type = '" + type + "';");
            while (resultSet.next()){
                Vehicle vehicle;
                if(resultSet.getString("Type").equals("Car")) {
                    vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.CAR);
                }
                else if(resultSet.getString("Type").equals("Motor")){
                    vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.MOTOR);
                }
                else if(resultSet.getString("Type").equals("Quad")){
                    vehicle = VehicleFactory.createVehicle(VehicleFactory.VehicleType.QUAD);
                }
                else{
                    vehicle = null;
                }

                vehicle.setID(resultSet.getInt("ID"));
                vehicle.setName(resultSet.getString("NAME"));
                vehicle.setMark(resultSet.getString("MARK"));
                vehicle.setPrice(resultSet.getDouble("PRICE"));
                vehicle.setAvailability(resultSet.getBoolean("AVAILABILITY"));
                vehicleList.add(vehicle);
            }
            resultSet.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }
}
