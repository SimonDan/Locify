package position.gcm;

import android.app.Activity;
import android.content.*;
import android.preference.PreferenceManager;
import com.google.android.gms.common.*;
import com.sdanner.ui.Overview;

/**
 * Utility-Klasse für GCM
 *
 * @author Simon Danner, 16.05.2016.
 */
public final class GCMUtil
{
  private final static int GOOGLE_PLAY_REQUEST = 9000;

  private GCMUtil()
  {
  }

  public static void checkPlayServices(Activity pContext)
  {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(pContext);
    if (resultCode != ConnectionResult.SUCCESS)
      if (apiAvailability.isUserResolvableError(resultCode))
        apiAvailability.getErrorDialog(pContext, resultCode, GOOGLE_PLAY_REQUEST).show();
      else
        pContext.finish();
  }

  public static boolean isGCMRegistered(Context pContext)
  {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
    return sharedPreferences.getBoolean(RegistrationService.SENT_TOKEN_TO_SERVER, false);
  }

  public static void register(Context pContext, String pPhoneNumber, boolean pForce)
  {
    Intent intent = new Intent(pContext, RegistrationService.class);
    intent.putExtra(Overview.PHONE_NUMBER, pPhoneNumber);
    intent.putExtra(RegistrationService.FORCE_SENT, pForce);
    pContext.startService(intent);
  }
}
