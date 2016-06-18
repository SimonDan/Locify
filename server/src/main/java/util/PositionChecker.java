package util;

import definition.SearchCondition;
import registry.BoxRegistry;
import storable.*;
import wrapper.PositionUpdate;

import java.io.OutputStream;
import java.net.*;

/**
 * Überprüft, ob nach einem Positions-Update eine Push-Benachrichtigung an einen Nutzer geschickt werden müssen
 *
 * @author Simon Danner, 09.04.2016.
 */
public final class PositionChecker
{
  private static final String API_KEY = "AIzaSyAw6Iue7GUF4C-bVmMW8GH4cJe9eZunJW0";
  private static final float MEETING_BORDER = 100; //In Meter

  private PositionChecker()
  {
  }

  public static void checkAfterPositionUpdate(PositionUpdate pUpdate)
  {
    SearchCondition<String> create = StorableBaseNotification.creator.asSearch(pUpdate.getPhoneNumber());
    SearchCondition<String> target = StorableBaseNotification.target.asSearch(pUpdate.getPhoneNumber());

    for (StorableBaseNotification notification : BoxRegistry.NOTIFICATIONS.find(create))
      _check(pUpdate, notification, true);
    for (StorableBaseNotification notification : BoxRegistry.NOTIFICATIONS.find(target))
      _check(pUpdate, notification, false);
  }

  private static void _check(PositionUpdate pUpdate, StorableBaseNotification pNotification, boolean pCreate)
  {
    String phoneNumber = pCreate ? pNotification.getTarget() : pNotification.getCreator();
    UserPosition position = BoxRegistry.POSITIONS.findOne(UserPosition.phoneNumber.asSearch(phoneNumber));
    if (_inRange(pUpdate.getLongitude(), pUpdate.getLatitude(), position))
    {
      String token = BoxRegistry.USERTOKEN.findOne(UserToken.phoneNumber.asSearch(pNotification.getCreator())).getToken();
      _sendPushNotification(pNotification.getID(), token);
    }
  }

  private static void _sendPushNotification(Object pNotificationID, String pGCMToken)
  {
    try
    {
      URL url = new URL("https://android.googleapis.com/gcm/send");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Authorization", "key=" + API_KEY);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);

      OutputStream outputStream = conn.getOutputStream();
      outputStream.write(CommonUtil.toJSON(new PushContent(pNotificationID.toString(), pGCMToken)).getBytes("UTF-8"));
      conn.getInputStream();
    }
    catch (Exception pE)
    {
      throw new RuntimeException(pE);
    }
  }

  private static boolean _inRange(double pLongitude, double pLatitude, UserPosition pPosition)
  {
    double earthRadius = 6371000; //In Meter
    double dLat = Math.toRadians(pPosition.getLatitude() - pLatitude);
    double dLng = Math.toRadians(pPosition.getLongitude() - pLongitude);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(pLatitude)) * Math.cos(Math.toRadians(pPosition.getLatitude())) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    float dist = (float) (earthRadius * c);
    return dist <= (MEETING_BORDER);
  }
}
