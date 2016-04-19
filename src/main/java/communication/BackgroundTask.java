package communication;

import android.content.Context;
import android.os.AsyncTask;
import communication.request.*;

/**
 * @author Simon Danner, 19.04.2016.
 */
public class BackgroundTask<T> extends AsyncTask<Object, Void, T>
{
  private Context context;
  private AbstractWebserviceRequest request;

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest)
  {
    context = pContext;
    request = pRequest;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected T doInBackground(Object... pObjects)
  {
    if (!request.execute(pObjects[0]))
    {
      //Toast toast = Toast.makeText(context, "Error while executing Webserice-Request", 20); //TODO
      //toast.show();
    }

    if (request instanceof AbstractResponseWebservice)
      return ((AbstractResponseWebservice<T>) request).getObject();

    return null;
  }
}
