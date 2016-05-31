package util;

/**
 * Beschreibt das Update einer Position eines User (Telefon-Nummer)
 *
 * @author Simon Danner, 09.04.2016.
 */
public class PositionUpdate
{
  private String phoneNumber;
  private double longitude;
  private double latitude;

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
    return new Object[] {phoneNumber, longitude, latitude, System.currentTimeMillis()};
  }
}
