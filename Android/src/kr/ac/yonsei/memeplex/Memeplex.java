package kr.ac.yonsei.memeplex;

import android.app.Application;
import android.location.Location;

public class Memeplex extends Application {
    Location location;
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public double getLatitude() {
        if (location != null) {
            return location.getLatitude();
        }
        
        return 0;
    }
    
    public double getLongitude() {
        if (location != null) {
            return location.getLongitude();
        }
        
        return 0;
    }
}
