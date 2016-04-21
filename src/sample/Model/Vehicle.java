package sample.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Wojciech on 2015-10-31.
 */

public abstract class Vehicle implements IVehicle {
    private int ID;
    private String mark;
    private String name;
    private double price;
    private boolean availability;

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Mark: " + mark  +
                ", name: " + name  +
                ", price: " + price +
                ", availability: " + availability ;
    }

    @Override
    public boolean buy(String type) {
        ConnectorDB connectorDB = new ConnectorDB();
        /// insert to DB
        try {
            PreparedStatement preparedStatement = connectorDB.getConn().prepareStatement("insert into Vehicle values (null, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, mark);
            preparedStatement.setDouble(4, price);
            preparedStatement.setBoolean(5, availability);
            preparedStatement.execute();

            ///set ID
            Statement statement = connectorDB.getConn().createStatement();
            ResultSet rs = statement.executeQuery("Select max(ID) FROM  Vehicle ");
            if (rs.next())
                this.ID = rs.getInt("max(ID)");
            rs.close();

            ///add to address book
            SingletonVehicles ksiazkaAdresowa = SingletonVehicles.getInstance();
            ksiazkaAdresowa.getVehicleList().add(this);
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean sell() {
        try {
            ConnectorDB connectorDB = new ConnectorDB();
            PreparedStatement preparedStatement = connectorDB.getConn().prepareStatement("DELETE from Vehicle where ID = ?");
            preparedStatement.setInt(1,ID);
            preparedStatement.execute();

            //remove from vehicleList
            SingletonVehicles ksiazkaAdresowa = SingletonVehicles.getInstance();
            ksiazkaAdresowa.getVehicleList().remove(this);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean lend() {
        if(availability){
            try{
                ConnectorDB connectorDB = new ConnectorDB();
                Statement statement = connectorDB.getConn().createStatement();
                statement.execute("UPDATE Vehicle SET Availability='0' WHERE ID="+ID);
                availability = false;
            }catch(SQLException e){
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean returnVehicle() {
        if(!availability){
            try{
                ConnectorDB connectorDB = new ConnectorDB();
                Statement statement = connectorDB.getConn().createStatement();
                statement.execute("UPDATE Vehicle SET Availability='1' WHERE ID="+ID);
                availability = true;
            }catch(SQLException e){
                return false;
            }
            return true;
        }
        return false;
    }
}
