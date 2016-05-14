package com.sdanner.ui;

import android.app.*;
import android.content.*;
import android.os.*;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.*;
import communication.ServerInterface;
import notification.*;
import notification.definition.NotificationTarget;

import java.util.*;
import java.util.regex.*;

/**
 * View, um eine Übersicht aller Erinnerungen zu haben
 * Von hier kann man in die Create-View gelangen
 *
 * @author Simon Danner, 22.05.2015
 */
public class Overview extends Activity
{
  protected final static String PHONE_NUMBER = "phoneNumber";
  protected final static String NOTIFICATION = "notification";

  private _ListAdapter adapter;
  private ServerInterface server;
  private String phoneNumber;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.overview);
    server = new ServerInterface(this);
    phoneNumber = AndroidUtil.getOwnNumber(this);
    AndroidUtil.requestReadContactsPermission(this);
    _initNewButton();
    _createList();
  }

  @Override
  protected void onStart()
  {
    super.onStart();

    if (phoneNumber == null) //Konnte nicht ermittelt werden -> manuell eingeben
    {
      _showNumberInputDialog();
      return;
    }

    //Liste mit Erinnerung füllen
    adapter.clear();
    new _FillListTask().execute();
  }

  /**
   * Initialisiert den Neu-Button. Bei Ausführung muss die eigene Handy-Nummer übergeben werden
   */
  private void _initNewButton()
  {
    ImageButton newButton = (ImageButton) findViewById(R.id.newNotificationButton);
    newButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(Overview.this, CreateNotification.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        startActivity(intent);
      }
    });
  }

  /**
   * Initialsiert die Liste mit den Erinnerungen
   */
  private void _createList()
  {
    ListView list = (ListView) findViewById(R.id.listView);
    adapter = new _ListAdapter(list.getContext(), R.id.listView);
    list.setAdapter(adapter);
  }

  /**
   * Zeigt einen Dialog, um seine Handy-Nummer einzutippen.
   * Dieser muss gezeigt werden, wenn diese nicht automatisch ermittelt werden konnte
   */
  private void _showNumberInputDialog()
  {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(getString(R.string.phone_dialog_message));
    builder.setCancelable(false);

    final EditText input = new EditText(this);
    input.setInputType(InputType.TYPE_CLASS_PHONE);
    builder.setView(input);
    builder.setPositiveButton(getString(R.string.key_ok), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int which)
      {
        //Hier nichts machen, wird später überschrieben
      }
    });

    final AlertDialog dialog = builder.create();
    dialog.show();

    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        phoneNumber = input.getText().toString();

        if (_checkValidNumber(phoneNumber))
        {
          //In Shared-Prefs speichern
          SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPrefs.edit();
          editor.putString(getString(R.string.key_phoneNumber), phoneNumber);
          editor.apply();

          new _FillListTask().execute();
          dialog.dismiss();
        }
        else
          Toast.makeText(getApplicationContext(), getString(R.string.invalid_number_message), Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * Überprüft, ob es sich um eine gültige Nummer handelt
   */
  private boolean _checkValidNumber(String pPhoneNumber)
  {
    String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(pPhoneNumber);
    return m.matches();
  }

  /**
   * Der List-Adapter für die Liste. (Verwaltet INotifications)
   */
  private class _ListAdapter extends ArrayAdapter<INotification>
  {
    private List<INotification> notifications;
    private Context context;

    public _ListAdapter(Context pContext, int pListId)
    {
      super(pContext, pListId);
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final BaseNotification notification = (BaseNotification) notifications.get(position);

      //Bevor die Erinnerung angezeigt werden kann, muss noch der Name des Betreffenden ermittelt werden
      NotificationTarget target = notification.getNotificationTarget();
      target.setName(AndroidUtil.getContactNameFromNumber(getApplicationContext(), target.getPhoneNumber()));

      View rowView = NotificationUtil.createListRow(context, parent, notification.getNotificationTitle(context), notification.getIconID());

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(Overview.this, NotificationView.class);
          intent.putExtra(NOTIFICATION, notification);
          startActivity(intent);
        }
      });

      return rowView;
    }

    /**
     * Liegt den Inhalt der Liste neu fest
     *
     * @param pNotifications die neuen Erinnerungen
     */
    public void setListContent(List<INotification> pNotifications)
    {
      notifications = pNotifications;
      clear();
      addAll(notifications);
    }
  }

  /**
   * Eine asynchrone Task, um den Inhalt der Liste vom Server zu holen
   */
  private class _FillListTask extends AsyncTask<Void, Void, Void>
  {
    @Override
    protected Void doInBackground(Void... params)
    {
      final List<INotification> notifications;

      try
      {
        notifications = server.getNotifications(phoneNumber);
      }
      catch (final ServerUnavailableException pE)
      {
        AndroidUtil.showErrorOnUIThread(Overview.this, pE);
        return null;
      }

      runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          adapter.setListContent(notifications);
        }
      });
      return null;
    }

    @Override
    protected void onPreExecute()
    {
      super.onPreExecute();
      findViewById(R.id.progressOverview).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void pVoid)
    {
      super.onPostExecute(pVoid);
      findViewById(R.id.progressOverview).setVisibility(View.INVISIBLE);
    }
  }
}