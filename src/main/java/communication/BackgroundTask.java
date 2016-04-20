package communication;

import android.app.ProgressDialog;
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
  private ProgressDialog progressDialog;

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
      throw new RuntimeException("Error");

    if (request instanceof AbstractResponseWebservice)
      return ((AbstractResponseWebservice<T>) request).getObject();

    return null;
  }

  @Override
  protected void onPreExecute()
  {
    progressDialog = ProgressDialog.show(context, "Loading...", "", true);
  }

  @Override
  protected void onPostExecute(T result)
  {
    super.onPostExecute(result);
    progressDialog.dismiss();
  }
}
