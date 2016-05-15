package position;

import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import communication.*;

/**
 * Hintergrund-Service, welcher dem Server Positions-Änderungen mitteilt
 *
 * @author Simon Danner, 15.05.2016.
 */
public class PositionService
{
  private Activity context;
  private String phoneNumber;
  private ServerInterface server;

  public PositionService(Activity pContext, String pPhoneNumber)
  {
    context = pContext;
    phoneNumber = pPhoneNumber;
    server = new ServerInterface(context);
  }

  public void start()
  {
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

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

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
  }
}
