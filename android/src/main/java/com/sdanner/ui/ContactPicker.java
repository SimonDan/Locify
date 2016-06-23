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

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.contactpicker);
    new _FetchContactsTask().execute();
  }

  /**
   * Adapter, welcher List-Items liefert, die die Namen der Kontakte anzeigt und bei Klick das Result festlegt
   */
  private class _ListAdapter extends ArrayAdapter<NotificationTarget>
  {
    public _ListAdapter(Context pContext, int pResourceID, List<NotificationTarget> pTargets)
    {
      super(pContext, pResourceID);
      addAll(pTargets);
      notifyDataSetChanged();
    }

    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pParent)
    {
      final NotificationTarget target = getItem(pPosition);
      View rowView = AndroidUtil.createContactListRow(getApplicationContext(), pParent, target.getName());
      rowView.setOnClickListener(new View.OnClickListener()
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

      return rowView;
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
        AndroidUtil.showErrorOnUIThread(ContactPicker.this, pE);
        return null;
      }

      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          ListView list = (ListView) findViewById(R.id.contactList);
          list.setAdapter(new _ListAdapter(list.getContext(), R.id.contactList, targets));

          if (targets.size() == 0)
            Toast.makeText(getApplicationContext(), getString(R.string.nocontact), Toast.LENGTH_SHORT).show();
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
      findViewById(R.id.progressContactPicker).setVisibility(View.INVISIBLE);
    }
  }
}
