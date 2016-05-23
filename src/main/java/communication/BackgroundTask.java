package communication;

import android.app.*;
import android.content.Context;
import android.os.AsyncTask;
import communication.request.*;
import org.jetbrains.annotations.Nullable;

/**
 * Beschreibt einen Task, welcher im Hintergrund eine Webservice-Anfrage ausführt.
 * Über ein Callback kann optional auf die Beendigung dieser reagiert werden.
 * Es kann außerdem optional die aufrufende Activity beendet werden nach Abschluss der Task.
 *
 * @author Simon Danner, 19.04.2016.
 */
public class BackgroundTask<T> extends AsyncTask<Object, Void, Void>
{
  private Context context;
  private AbstractWebserviceRequest request;
  private int loadingMessageID;
  private boolean shouldFinishActivity;
  @Nullable
  private ITaskCallback<T> callback;

  private boolean serverUnavailable = false;
  private ProgressDialog progressDialog;

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID)
  {
    this(pContext, pRequest, pLoadingMessageID, false, null);
  }

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID,
                        @Nullable ITaskCallback<T> pCallback)
  {
    this(pContext, pRequest, pLoadingMessageID, false, pCallback);
  }

  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID,
                        boolean pShouldFinishActivity, @Nullable ITaskCallback<T> pCallback)
  {
    context = pContext;
    request = pRequest;
    loadingMessageID = pLoadingMessageID;
    callback = pCallback;
    shouldFinishActivity = pShouldFinishActivity;
    progressDialog = new ProgressDialog(context);
  }

  @Override
  protected Void doInBackground(Object... pObjects)
  {
    if (!request.execute(pObjects[0]))
      serverUnavailable = true;

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
  protected void onPostExecute(Void pResult)
  {
    super.onPostExecute(pResult);

    if (progressDialog.isShowing())
      progressDialog.dismiss();

    if (callback != null)
    {
      T result = request instanceof AbstractResponseWebservice ? ((AbstractResponseWebservice<T>) request).getObject() : null;
      callback.onFinish(result, serverUnavailable);
    }

    if (shouldFinishActivity)
      if (context instanceof Activity)
        ((Activity) context).finish();
      else
        throw new RuntimeException(); //TODO
  }
}
