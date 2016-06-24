package wrapper;

/**
 * Beschreibt das Update einer Position
 *
 * @author Simon Danner, 10.04.2016.
 */
public class PositionUpdate
{
  private String phoneNumber;
  private double longitude;
  private double latitude;

  public PositionUpdate()
  {
    //Benötigt für Serialisierung
  }

  public PositionUpdate(String pPhoneNumber, double pLongitude, double pLatitude)
  {
    phoneNumber = pPhoneNumber;
    longitude = pLongitude;
    latitude = pLatitude;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public Object[] getAsArray()
  {
    return new Object[]{phoneNumber, longitude, latitude, System.currentTimeMillis()};
  }
}
