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
  private static LocationListener listener = null;

  private static final long UPDATE_INTERVAL = 10 * 1000; //Alle 10 Sek updaten
  private static final float MIN_DISTANCE = 5; //Oder alle 5 Meter

  private LocationManager locationManager;
  private String phoneNumber;
  private ServerInterface server;

  /**
   * Erzeugt einen neuen Service, dabei wird ein bereits existierender gestoppt
   *
   * @param pContext     der aktuelle Kontext
   * @param pPhoneNumber die Telefon-Nummer
   */
  public PositionService(Activity pContext, String pPhoneNumber)
  {
    locationManager = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
    phoneNumber = pPhoneNumber;
    server = new ServerInterface(pContext);

    if (listener != null)
      locationManager.removeUpdates(listener);
  }

  /**
   * Startet den Service
   */
  public void start()
  {
    listener = new LocationListener()
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

    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, listener);
  }
}
