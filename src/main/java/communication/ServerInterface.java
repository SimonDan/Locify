package communication;

import com.sdanner.ui.util.ServerUnavailableException;
import communication.request.*;
import notification.INotification;

import java.util.List;

/**
 * Beschreibt das Webservice-Interface zum Server
 *
 * @author Simon Danner, 10.04.2016.
 */
public class ServerInterface
{
  public void updatePosition(PositionUpdate pUpdate) throws ServerUnavailableException
  {
    BackgroundTask<Void> task = new BackgroundTask<Void>(new PUTRequest("updatePosition"));
    task.execute(pUpdate);
    if (task.isServerUnavailable())
      throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.UPDATE_POSITION);
  }

  @SuppressWarnings("unchecked")
  public List<INotification> getNotifications(String pPhoneNumber) throws ServerUnavailableException
  {
    GETRequest<List> getRequest = new GETRequest<>("getNotifications", List.class, pPhoneNumber);
    if (getRequest.execute(pPhoneNumber))
      return getRequest.getObject();

    throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.FETCH_NOTIFICATIONS);
  }

  public void updateNotification(INotification pNotification) throws ServerUnavailableException
  {
    BackgroundTask<Void> task = new BackgroundTask<>(new PUTRequest("updateNotification", true));
    task.execute(pNotification);
    if (task.isServerUnavailable())
      throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.UPDATE_NOTIFICATION);
  }

  public void deleteNotification(String pNotificationID) throws ServerUnavailableException
  {
    BackgroundTask<Void> task = new BackgroundTask<>(new PUTRequest("deleteNotification"));
    task.execute(pNotificationID);
    if (task.isServerUnavailable())
      throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.DELETE_NOTIFICATION);
  }
}
