package communication;

import android.content.Context;
import android.os.AsyncTask;
import notification.BaseNotification;
import notification.definition.NotificationStartDate;
import notification.notificationtypes.DebtsNotification;

import java.util.*;

/**
 * @author simon, 10.06.2015
 */
public class NotificationFetcher extends AsyncTask<Integer, Integer, List<BaseNotification>>
{
  private Context context;

  public NotificationFetcher(Context pContext)
  {
    context = pContext;
  }

  @Override
  protected List<BaseNotification> doInBackground(Integer... params)
  {
    int userId = 0; //TODO
    //TODO CALL WEBSERVICE...

    //TEST_CASE
    if (userId == 0)
    {
      List<BaseNotification> n = new ArrayList<>();
      NotificationStartDate date = new NotificationStartDate(new Date());
      n.add(new DebtsNotification(context, 0, date, null, false, 48.50));
      n.add(new DebtsNotification(context, 0, date, null, false, 9));
      n.add(new DebtsNotification(context, 0, date, null, false, 240));
      n.add(new DebtsNotification(context, 0, date, null, false, 3));
      return n;
    }

    return new ArrayList<>();
  }

  @Override
  protected void onPostExecute(List<BaseNotification> result)
  {
    super.onPostExecute(result);
  }

  @Override
  protected void onCancelled()
  {
    super.onCancelled();
  }
}
