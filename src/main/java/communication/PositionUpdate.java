package communication;

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

  public PositionUpdate(String pPhoneNumber, double pLongitude, double pLatitude)
  {
    phoneNumber = pPhoneNumber;
    longitude = pLongitude;
    latitude = pLatitude;
  }
}
