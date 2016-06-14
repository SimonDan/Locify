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

  /**
   * Liefert eine Fehlermeldung abhängig vom Typ der Anfrage
   *
   * @param pContext der Kontext
   * @return die Fehler-Nachricht
   */
  public String getErrorMessage(Context pContext)
  {
    int errorMessageID;

    switch (operation)
    {
      case FETCH_NOTIFICATIONS:
        errorMessageID = R.string.error_fetch_notifications;
        break;
      case FETCH_SINGLE_NOTIFICATION:
        errorMessageID = R.string.error_fetch_single_notification;
        break;
      case UPDATE_NOTIFICATION:
        errorMessageID = R.string.error_update_notification;
        break;
      case DELETE_NOTIFICATION:
        errorMessageID = R.string.error_delete_notification;
        break;
      case POSSIBLE_TARGETS:
        errorMessageID = R.string.error_possible_targets;
      default:
        errorMessageID = R.string.error_unknown;
    }

    return pContext.getString(errorMessageID);
  }

  /**
   * Definiert die möglichen Server-Operationen
   */
  public enum EServerOperation
  {
    FETCH_NOTIFICATIONS, FETCH_SINGLE_NOTIFICATION, UPDATE_NOTIFICATION, DELETE_NOTIFICATION, POSSIBLE_TARGETS
  }
}
