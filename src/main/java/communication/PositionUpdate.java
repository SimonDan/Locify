package communication;

/**
 * Beschreibt das Update einer Position
 *
 * @author simon, 10.04.2016.
 */
public class PositionUpdate
{
  private String phoneNumber;
  private long longitude;
  private long latitude;

  public PositionUpdate(String pPhoneNumber, long pLongitude, long pLatitude)
  {
    phoneNumber = pPhoneNumber;
    longitude = pLongitude;
    latitude = pLatitude;
  }
}
