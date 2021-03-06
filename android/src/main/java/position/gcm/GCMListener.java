package position.gcm;

import android.app.*;
import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.sdanner.ui.*;
import com.sdanner.ui.util.*;
import communication.ServerInterface;
import notification.*;
import notification.definition.NotificationTarget;

/**
 * Empfängt Nachrichten vom GCM
 *
 * @author Simon Danner, 15.05.2016.
 */
public class GCMListener extends GcmListenerService
{
  @Override
  public void onMessageReceived(String pFrom, Bundle pData)
  {
    _sendNotification(_getNotification(pData.getString("message")));
  }

  private INotification _getNotification(String pNotificationID)
  {
    try
    {
      return new ServerInterface(this).getNotification(pNotificationID);
    }
    catch (ServerUnavailableException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  /**
   * Sendet eine neue Push-Benachrichtigung, dass sich ein Ziel einer Erinnerung in der Nähe befindet
   *
   * @param pNotification die betreffende Erinnerung
   */
  private void _sendNotification(INotification pNotification)
  {
    String notificationID = pNotification.getID();
    NotificationTarget target = pNotification.getNotificationTarget();
    target.setName(AndroidUtil.getContactNameFromNumber(getApplicationContext(), target.getPhoneNumber()));
    String content = pNotification.getNotificationTitle(this, true);
    Intent intent = new Intent(this, NotificationView.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent = NotificationUtil.createNotificationIntent(intent, pNotification, pNotification.getCreator());
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

    String title = getString(R.string.push_title, target.getName());
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(title)
        .setContentText(content)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(notificationID.hashCode(), notificationBuilder.build());
  }
}
