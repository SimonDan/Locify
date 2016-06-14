package com.sdanner.ui;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.*;
import communication.ServerInterface;
import notification.definition.NotificationTarget;

import java.util.List;

/**
 * Custom-Contact-Picker, welcher nur Kontakte anzeigt, die Locify nutzen
 *
 * @author Simon Danner, 14.06.2016.
 */
public class ContactPicker extends Activity
{
  public static final String TARGET_RESULT = "targetResult";

  private _ListAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.overview);
    _initList();
  }

  private void _initList()
  {
    adapter = new _ListAdapter(findViewById(R.id.contactList).getContext(), R.id.contactList);
    new _FetchContactsTask().execute();
  }

  /**
   * Adapter, welcher List-Items liefert, die die Namen der Kontakte anzeigt und bei Klick das Result festlegt
   */
  private class _ListAdapter extends ArrayAdapter<String>
  {
    private List<NotificationTarget> targets;

    public _ListAdapter(Context pContext, int pResourceID)
    {
      super(pContext, pResourceID);
    }

    @Override
    public View getView(int pPosition, View convertView, ViewGroup parent)
    {
      final NotificationTarget target = targets.get(pPosition);
      TextView view = new TextView(getApplicationContext());
      view.setText(target.getName());
      view.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View pView)
        {
          Intent intent = new Intent();
          intent.putExtra(TARGET_RESULT, target);
          setResult(Activity.RESULT_OK, intent);
          finish();
        }
      });

      return view;
    }

    public void setContent(List<NotificationTarget> pTargets)
    {
      targets = pTargets;
    }
  }

  /**
   * Async-Task, um die möglichen Kontakte zu laden
   */
  private class _FetchContactsTask extends AsyncTask<Void, Void, Void>
  {
    @Override
    protected Void doInBackground(Void... pVoids)
    {
      final List<NotificationTarget> targets;

      try
      {
        ServerInterface server = new ServerInterface(getApplicationContext());
        targets = server.getPossibleTargets(AndroidUtil.getAllContactNumbers(getApplicationContext()));
      }
      catch (ServerUnavailableException pE)
      {
        AndroidUtil.showErrorOnUIThread(getApplicationContext(), pE);
        return null;
      }

      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          adapter.setContent(targets);
        }
      });

      return null;
    }

    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
      findViewById(R.id.progressContactPicker).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void pVoid)
    {
      super.onPostExecute(pVoid);
      findViewById(R.id.progressContactPicker).setVisibility(View.VISIBLE);
    }
  }
}
