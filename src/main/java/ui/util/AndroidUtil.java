package ui.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author simon, 11.04.2016.
 */
public class AndroidUtil
{
  private AndroidUtil()
  {
  }

  public static String getOwnNumber(Context pContext)
  {
    TelephonyManager tMgr = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
    return tMgr.getLine1Number();
  }
}
