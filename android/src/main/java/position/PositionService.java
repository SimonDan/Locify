package position;

import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import communication.ServerInterface;
import wrapper.PositionUpdate;

/**
 * Hintergrund-Service, welcher dem Server Positions-Änderungen mitteilt
 *
 * @author Simon Danner, 15.05.2016.
 */
public class PositionService
{
  private static final long UPDATE_INTERVAL = 10 * 1000;
  private static final float MIN_DISTANCE = 5;

  private LocationManager locationManager;
  private String phoneNumber;
  private ServerInterface server;

  public PositionService(Activity pContext, String pPhoneNumber)
  {
    locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
    phoneNumber = pPhoneNumber;
    server = new ServerInterface(pContext);
  }

  public void start()
  {
    LocationListener locationListener = new LocationListener()
    {
      public void onLocationChanged(Location pLocation)
      {
        server.updatePosition(new PositionUpdate(phoneNumber, pLocation.getLongitude(), pLocation.getLatitude()));
      }

      public void onStatusChanged(String provider, int status, Bundle extras)
      {
      }

      public void onProviderEnabled(String provider)
      {
      }

      public void onProviderDisabled(String provider)
      {
      }
    };

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, locationListener);
  }
}
