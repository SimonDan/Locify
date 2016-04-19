package communication;

import android.content.Context;
import communication.request.*;
import notification.INotification;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

  public void updatePosition(PositionUpdate pUpdate)
  {
    new BackgroundTask<Void>(context, new PUTRequest("updatePosition")).execute(pUpdate);
  }

  @SuppressWarnings("unchecked")
  public List<INotification> getNotifications(String pPhoneNumber)
  {
    GETRequest<List> getRequest = new GETRequest<>("getNotifications", List.class, pPhoneNumber);
    try
    {
      return new BackgroundTask<List<INotification>>(context, getRequest).execute(pPhoneNumber).get();
    }
    catch (InterruptedException | ExecutionException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  public void updateNotification(INotification pNotification)
  {
    new BackgroundTask<Void>(context, new PUTRequest("updateNotification")).execute(pNotification);
  }

  public void deleteNotification(String pNotificationID)
  {
    new BackgroundTask<Void>(context, new PUTRequest("deleteNotification")).execute(pNotificationID);
  }
}
