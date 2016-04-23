package com.sdanner.ui.util;

import android.content.Context;
import com.sdanner.ui.R;

/**
 * Exception, die geworfen wird, wenn der Locify-Server nicht erreichbar ist
 *
 * @author Simon Danner, 23.04.2016.
 */
public class ServerUnavailableException extends Exception
{
  private EServerOperation operation;

  public ServerUnavailableException(EServerOperation pOperation)
  {
    operation = pOperation;
  }

  public String getErrorMessage(Context pContext)
  {
    int errorMessageID;

    switch (operation)
    {
      case FETCH_NOTIFICATIONS:
        errorMessageID = R.string.error_fetch_notifications;
        break;
      case UPDATE_NOTIFICATION:
        errorMessageID = R.string.error_update_notification;
        break;
      case DELETE_NOTIFICATION:
        errorMessageID = R.string.error_delete_notification;
        break;
      default:
        errorMessageID = R.string.error_unknown;
    }

    return pContext.getString(errorMessageID);
  }

  public enum EServerOperation
  {
    FETCH_NOTIFICATIONS, UPDATE_NOTIFICATION, DELETE_NOTIFICATION, UPDATE_POSITION
  }
}
