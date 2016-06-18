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
      //TODO
      throw new RuntimeException(pE);
    }
  }

  private void _sendNotification(INotification pNotification)
  {
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
    notificationManager.notify(0, notificationBuilder.build());
  }
}
