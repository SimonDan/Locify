package position.gcm;

import android.app.IntentService;
import android.content.*;
import android.preference.PreferenceManager;
import com.google.android.gms.gcm.*;
import com.google.android.gms.iid.InstanceID;
import com.sdanner.ui.Overview;
import communication.ServerInterface;

/**
 * @author simon, 16.05.2016.
 */
public class RegistrationService extends IntentService
{
  public final static String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
  public final static String FORCE_SENT = "forceSent";
  private final static String SENDER_ID = "770719471762";
  private final static String TAG = "RegIntentService";

  public RegistrationService()
  {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent pIntent)
  {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    boolean force = pIntent.getBooleanExtra(FORCE_SENT, false);

    if (!force && GCMUtil.isGCMRegistered(this))
      return;

    try
    {
      InstanceID instanceID = InstanceID.getInstance(this);
      String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      String phoneNumber = pIntent.getStringExtra(Overview.PHONE_NUMBER);
      if (phoneNumber == null || phoneNumber.isEmpty())
        return;

      new ServerInterface(this).setUserToken(phoneNumber, token);
      sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    }
    catch (Exception e)
    {
      sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
    }
  }
}
