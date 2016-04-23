package communication;

import android.os.AsyncTask;
import communication.request.*;

/**
 * @author Simon Danner, 19.04.2016.
 */
public class BackgroundTask<T> extends AsyncTask<Object, Void, T>
{
  private AbstractWebserviceRequest request;
  private boolean serverUnavailable = false;

  public BackgroundTask(AbstractWebserviceRequest pRequest)
  {
    request = pRequest;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected T doInBackground(Object... pObjects)
  {
    if (!request.execute(pObjects[0]))
      serverUnavailable = true;

    if (!serverUnavailable && request instanceof AbstractResponseWebservice)
      return ((AbstractResponseWebservice<T>) request).getObject();

    return null;
  }

  public boolean isServerUnavailable()
  {
    return serverUnavailable;
  }
}
