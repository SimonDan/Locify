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
public class BackgroundTask<T> extends AsyncTask<Object, Void, T>
{
  private Context context;
  private AbstractWebserviceRequest request;
  private int loadingMessageID;
  private boolean shouldFinishActivity;
  @Nullable
  private ITaskCallback<T> callback;

  private boolean serverUnavailable = false;
  private ProgressDialog progressDialog;

  /**
   * Erzeugt einen neuen Background-Task
   *
   * @param pContext          der aktuelle Kontext
   * @param pRequest          der Request, welcher durchgeführt werden soll
   * @param pLoadingMessageID die ID der Nachricht, die während der Durchführung gezeigt werden soll
   */
  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID)
  {
    this(pContext, pRequest, pLoadingMessageID, false, null);
  }

  /**
   * Erzeugt einen neuen Background-Task
   *
   * @param pContext          der aktuelle Kontext
   * @param pRequest          der Request, welcher durchgeführt werden soll
   * @param pLoadingMessageID die ID der Nachricht, die während der Durchführung gezeigt werden soll
   * @param pCallback         ein Callback, welches nach der Task durchgeführt wird
   */
  public BackgroundTask(Context pContext, AbstractWebserviceRequest pRequest, int pLoadingMessageID,
                        @Nullable ITaskCallback<T> pCallback)
  {
    this(pContext, pRequest, pLoadingMessageID, false, pCallback);
  }

  /**
   * Erzeugt einen neuen Background-Task
   *
   * @param pContext              der aktuelle Kontext
   * @param pRequest              der Request, welcher durchgeführt werden soll
   * @param pLoadingMessageID     die ID der Nachricht, die während der Durchführung gezeigt werden soll
   * @param pShouldFinishActivity <tt>true</tt> wenn die Activity nach dieser Task beendet werden soll
   * @param pCallback             ein Callback, welches nach der Task durchgeführt wird
   */
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
  protected T doInBackground(Object... pObjects)
  {
    if (!request.execute(pObjects[0]))
      serverUnavailable = true;

    //noinspection unchecked
    return request instanceof AbstractResponseWebservice ? ((AbstractResponseWebservice<T>) request).getObject() : null;
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
  protected void onPostExecute(T pResult)
  {
    super.onPostExecute(pResult);

    if (progressDialog.isShowing())
      progressDialog.dismiss();

    if (callback != null)
      callback.onFinish(pResult, serverUnavailable);

    if (shouldFinishActivity)
      if (context instanceof Activity)
        ((Activity) context).finish();
      else
        throw new RuntimeException();
  }
}
