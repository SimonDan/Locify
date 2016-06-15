package communication;

import android.content.Context;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sdanner.ui.R;
import com.sdanner.ui.util.*;
import communication.request.*;
import storable.StorableBaseNotification;
import notification.*;
import notification.definition.NotificationTarget;
import wrapper.*;

import java.util.*;

/**
 * Beschreibt das Webservice-Interface zum Server
 *
 * @author Simon Danner, 10.04.2016.
 */
public class ServerInterface
{
  private Context context;

  public ServerInterface(Context pContext)
  {
    context = pContext;
  }

  /**
   * Aktualisiert die Position der Benutzers
   *
   * @param pUpdate die neuen Positions-Daten
   */
  public void updatePosition(PositionUpdate pUpdate)
  {
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("updatePosition"), -1);
    task.execute(pUpdate);
  }

  /**
   * Legt das User-Token für die GCM-Kommunikation fest
   *
   * @param pPhoneNumber die Telefon-Nummer des Users
   * @param pToken       das GCM-Token
   */
  public void setUserToken(String pPhoneNumber, String pToken)
  {
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("setUserToken"), -1);
    task.execute(new TokenWrapper(pPhoneNumber, pToken));
  }

  /**
   * Liefert die Erinnerungen für den User
   * Entweder die von ihm erstellten oder die die ihn betreffen
   *
   * @param pPhoneNumber die Telefon-Nummer des Users
   * @param pCreatedByMe gibt an, ob es seine oder die die ihn betreffen sein sollen
   * @return eine Liste von Erinnerungen
   * @throws ServerUnavailableException wenn der Server nicht erreichbar ist
   */
  public List<INotification> getNotifications(String pPhoneNumber, boolean pCreatedByMe) throws ServerUnavailableException
  {
    TypeReference<List<StorableBaseNotification>> type = new TypeReference<List<StorableBaseNotification>>()
    {
    };

    String serviceName = pCreatedByMe ? "getNotifications" : "getTargetNotifications";
    GETRequest<List<StorableBaseNotification>> request = new GETRequest<>(serviceName, type, pPhoneNumber);
    if (request.execute(null))
    {
      List<StorableBaseNotification> storables = request.getObject();
      if (storables == null)
        return Collections.emptyList();

      List<INotification> notifications = new ArrayList<>();
      for (StorableBaseNotification storable : storables)
        notifications.add(NotificationUtil.createNotificationFromStorable(storable));
      return notifications;
    }

    throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.FETCH_NOTIFICATIONS);
  }

  /**
   * Liefert eine bestimmte Erinnerung über die ID
   *
   * @param pNotificationID die ID der Erinnerung
   * @return eine einzelne Erinnerung
   * @throws ServerUnavailableException wenn der Server nicht erreichbar ist
   */
  public INotification getNotification(String pNotificationID) throws ServerUnavailableException
  {
    GETRequest<StorableBaseNotification> request = new GETRequest<>("getNotification", StorableBaseNotification.class,
                                                                    pNotificationID);
    if (request.execute(null))
      return NotificationUtil.createNotificationFromStorable(request.getObject());

    throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.FETCH_SINGLE_NOTIFICATION);
  }

  /**
   * Speichert eine Erinnerung am Server
   *
   * @param pNotification die zu speichernde Erinnerung
   */
  public void updateNotification(final INotification pNotification)
  {
    ITaskCallback<String> callback = new ITaskCallback<String>()
    {
      @Override
      public void onFinish(String pResult, boolean pIsServerUnavailable)
      {
        if (pIsServerUnavailable)
          AndroidUtil.showErrorOnUIThread(context, new ServerUnavailableException(
              ServerUnavailableException.EServerOperation.UPDATE_NOTIFICATION));
        else
          pNotification.setID(pResult);
      }
    };
    BackgroundTask<String> task = new BackgroundTask<>(context, new POSTRequest<>("updateNotification", String.class),
                                                       R.string.loading_update_notification, callback);
    task.execute(pNotification.getStorableNotification());
  }

  /**
   * Löscht eine Erinnerung
   *
   * @param pNotificationID die ID der Erinnerung
   */
  public void deleteNotification(String pNotificationID)
  {
    ITaskCallback<Void> callback = new ITaskCallback<Void>()
    {
      @Override
      public void onFinish(Void pResult, boolean pIsServerUnavailable)
      {
        if (pIsServerUnavailable)
          AndroidUtil.showErrorOnUIThread(context, new ServerUnavailableException(
              ServerUnavailableException.EServerOperation.DELETE_NOTIFICATION));
      }
    };
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("deleteNotification"),
                                                     R.string.loading_delete_notification, true, callback);
    task.execute(pNotificationID);
  }

  /**
   * Liefert alle Kontakte, die Locify nutzen
   *
   * @param pAllContactNumbers alle Nummern aus dem Kontaktbuch des Users
   * @return eine Liste von Notification-Targets
   * @throws ServerUnavailableException wenn der Server nicht errichbar ist
   */
  public List<NotificationTarget> getPossibleTargets(List<String> pAllContactNumbers) throws ServerUnavailableException
  {
    TypeReference<List<String>> type = new TypeReference<List<String>>()
    {
    };

    POSTRequest<List<String>> request = new POSTRequest<>("getPossibleTargets", type);
    if (!request.execute(pAllContactNumbers))
      throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.POSSIBLE_TARGETS);

    List<String> possibleNumbers = request.getObject();
    Objects.requireNonNull(possibleNumbers);
    List<NotificationTarget> targets = new ArrayList<>();
    for (String number : possibleNumbers)
      targets.add(new NotificationTarget(AndroidUtil.getContactNameFromNumber(context, number), number));
    return targets;
  }
}
