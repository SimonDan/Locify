package util;

import objects.*;
import config.Config;
import registry.BoxRegistry;

import java.io.OutputStream;
import java.net.*;
import java.util.Objects;

/**
 * Überprüft, ob nach einem Positions-Update eine Push-Benachrichtigung an einen Nutzer geschickt werden müssen
 *
 * @author Simon Danner, 09.04.2016.
 */
public final class PositionChecker
{
  private final static String API_KEY = "AIzaSyAw6Iue7GUF4C-bVmMW8GH4cJe9eZunJW0";
  //SENDER ID: 770719471762

  private PositionChecker()
  {
  }

  public static void checkAfterPositionUpdate(PositionUpdate pUpdate)
  {
    for (BaseNotification notification : BoxRegistry.NOTIFICATIONS.find(BaseNotification.target.asSearch(pUpdate.getPhoneNumber())))
    {
      String creator = notification.getCreator();
      UserPosition creatorPosition = BoxRegistry.POSITIONS.findOne(UserPosition.phoneNumber.asSearch(creator));
      if (inRange(pUpdate.getLongitude(), pUpdate.getLatitude(), creatorPosition))
      {
        String token = BoxRegistry.USERTOKEN.findOne(UserToken.phoneNumber.asSearch(creator)).getToken();
        sendPushNotification(notification.getID(), token);
      }
    }
  }

  public static void sendPushNotification(Object pNotificationID, String pGCMToken)
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

  private static boolean inRange(double pLongitude, double pLatitude, UserPosition pPosition)
  {
    double earthRadius = 6371000; //In Meter
    double dLat = Math.toRadians(pPosition.getLatitude() - pLatitude);
    double dLng = Math.toRadians(pPosition.getLongitude() - pLongitude);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(Math.toRadians(pLatitude)) * Math.cos(Math.toRadians(pPosition.getLatitude())) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    float dist = (float) (earthRadius * c);

    String border = Config.get("MEETING_BORDER");
    Objects.requireNonNull(border);
    return dist <= Float.parseFloat(border);
  }
}
