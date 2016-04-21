package sample.Model;

/**
 * Created by Wojciech on 2015-10-31.
 */

/**
 * Design pattern: factory method
 */
public class VehicleFactory {
    public enum VehicleType{
        CAR,
        MOTOR,
        QUAD
    }
    public static Vehicle createVehicle(VehicleType type){
        switch (type){
            case CAR:return new Car();
            case QUAD:return new Quad();
            case MOTOR:return new Motor();
            default: throw new IllegalArgumentException("Type: " + type + " is not recognized");
        }
    }

}
