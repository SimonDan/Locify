package communication;

import android.content.Context;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sdanner.ui.R;
import com.sdanner.ui.util.*;
import communication.request.*;
import communication.wrapper.*;
import notification.INotification;

import java.util.List;


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
    BackgroundTask<PUTRequest> task = new BackgroundTask<>(context, new PUTRequest("updatePosition"), -1);
    task.execute(pUpdate);
  }

  public void setUserToken(String pPhoneNumber, String pToken)
  {
    BackgroundTask<PUTRequest> task = new BackgroundTask<>(context, new PUTRequest("setUserToken"), -1);
    task.execute(new TokenWrapper(pPhoneNumber, pToken));
  }

  @SuppressWarnings("unchecked")
  public List<INotification> getNotifications(String pPhoneNumber) throws ServerUnavailableException
  {
    TypeReference<List<INotification>> type = new TypeReference<List<INotification>>()
    {
    };

    GETRequest<List<INotification>> request = new GETRequest<>("getNotifications", true, type, pPhoneNumber);
    if (request.execute(null))
      return request.getObject();

    throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.FETCH_NOTIFICATIONS);
  }

  public INotification getNotification(String pNotificationID) throws ServerUnavailableException
  {
    GETRequest<INotification> request = new GETRequest<>("getNotification", true, INotification.class, pNotificationID);
    if (request.execute(null))
      return request.getObject();

    throw new ServerUnavailableException(ServerUnavailableException.EServerOperation.FETCH_SINGLE_NOTIFICATION);
  }

  public void updateNotification(final INotification pNotification)
  {
    ITaskCallback<String> callback = new ITaskCallback<String>()
    {
      @Override
      public void onFinish(String pResult, boolean pIsServerUnavailable)
      {
        if (pIsServerUnavailable)
          AndroidUtil.showErrorOnUIThread(context, new ServerUnavailableException(ServerUnavailableException.EServerOperation.UPDATE_NOTIFICATION));
        else
          pNotification.setID(pResult);
      }
    };
    BackgroundTask<String> task = new BackgroundTask<>(context, new POSTRequest<>("updateNotification", true, String.class),
                                                       R.string.loading_update_notification, callback);
    task.execute(pNotification);
  }

  public void deleteNotification(String pNotificationID)
  {
    ITaskCallback<Void> callback = new ITaskCallback<Void>()
    {
      @Override
      public void onFinish(Void pResult, boolean pIsServerUnavailable)
      {
        if (pIsServerUnavailable)
          AndroidUtil.showErrorOnUIThread(context, new ServerUnavailableException(ServerUnavailableException.EServerOperation.DELETE_NOTIFICATION));
      }
    };
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("deleteNotification"),
                                                     R.string.loading_delete_notification, true, callback);
    task.execute(pNotificationID);
  }
}
