package communication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import communication.request.*;
import org.jetbrains.annotations.Nullable;

/**
 * @author Simon Danner, 19.04.2016.
 */
public class BackgroundTask<T> extends AsyncTask<Object, Void, T>
{
  private Context context;
  private AbstractWebserviceRequest request;
  private int loadingMessageID;
  private boolean serverUnavailable = false;
  private ProgressDialog progressDialog;
  @Nullable
  private ITaskCallback callback;

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID)
  {
    this(pContext, pRequest, pLoadingMessageID, null);
  }

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID,
                        @Nullable ITaskCallback pCallback)
  {
    context = pContext;
    request = pRequest;
    loadingMessageID = pLoadingMessageID;
    callback = pCallback;
    progressDialog = new ProgressDialog(context);
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

  @Override
  protected void onPreExecute()
  {
    super.onPreExecute();
    if (loadingMessageID == -1)
      return;

    progressDialog.setMessage(context.getString(loadingMessageID));
    progressDialog.show();
  }

  @Override
  protected void onPostExecute(T pT)
  {
    super.onPostExecute(pT);
    if (progressDialog.isShowing())
      progressDialog.dismiss();

    if (callback != null)
      callback.onFinish(this);
  }

  public boolean isServerUnavailable()
  {
    return serverUnavailable;
  }
}
