package position.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.sdanner.ui.util.AndroidUtil;

/**
 * @author Simon Danner, 15.05.2016.
 */
public class InstanceIDListener extends InstanceIDListenerService
{
  @Override
  public void onTokenRefresh()
  {
    GCMUtil.register(this, AndroidUtil.getOwnNumberFromPrefs(this), true);
  }
}
