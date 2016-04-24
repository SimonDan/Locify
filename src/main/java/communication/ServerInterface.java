package communication;

import android.app.Activity;
import com.sdanner.ui.R;
import com.sdanner.ui.util.*;
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
  private Activity context;

  public ServerInterface(Activity pContext)
  {
    context = pContext;
  }

  public void updatePosition(PositionUpdate pUpdate) throws ServerUnavailableException
  {
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("updatePosition"), -1);
    task.execute(pUpdate);
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
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("updateNotification", true),
                                                     R.string.loading_update_notification,
                                                     new _Callback(ServerUnavailableException.EServerOperation.UPDATE_NOTIFICATION));
    task.execute(pNotification);
  }

  public void deleteNotification(String pNotificationID) throws ServerUnavailableException
  {
    BackgroundTask<Void> task = new BackgroundTask<>(context, new PUTRequest("deleteNotification"),
                                                     R.string.loading_delete_notification,
                                                     new _Callback(ServerUnavailableException.EServerOperation.DELETE_NOTIFICATION));
    task.execute(pNotificationID);
  }

  private class _Callback implements ITaskCallback
  {
    private ServerUnavailableException.EServerOperation serverOperation;

    private _Callback(ServerUnavailableException.EServerOperation pServerOperation)
    {
      serverOperation = pServerOperation;
    }

    @Override
    public void onFinish(BackgroundTask pTask)
    {
      if (pTask.isServerUnavailable())
        AndroidUtil.showErrorOnUIThread(context, new ServerUnavailableException(serverOperation));
    }
  }
}
