package position.gcm;

import android.app.*;
import android.content.*;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.sdanner.ui.*;
import com.sdanner.ui.util.ServerUnavailableException;
import communication.ServerInterface;
import notification.INotification;

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
    Intent intent = new Intent(this, NotificationView.class);
    intent.putExtra(Overview.NOTIFICATION, pNotification);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

    String title = getString(R.string.push_title, pNotification.getNotificationTarget().getName());
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(title)
        .setContentText(pNotification.getNotificationTitle(this, true))
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }
}
