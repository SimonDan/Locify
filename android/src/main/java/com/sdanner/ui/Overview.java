package com.sdanner.ui;

import android.Manifest;
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
import position.PositionService;
import position.gcm.GCMUtil;

import java.util.List;
import java.util.regex.*;

/**
 * View, um eine Übersicht aller Erinnerungen zu haben
 * Von hier kann man in die Create-View gelangen
 *
 * @author Simon Danner, 22.05.2015
 */
public class Overview extends Activity
{
  public static final String PHONE_NUMBER = "phoneNumber";
  public static final String NOTIFICATION = "notification";
  public static final String STORABLE_NOTIFICATION = "storableNotification";


  private _ListAdapter adapter;
  private ServerInterface server;
  private String phoneNumber;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.overview);
    GCMUtil.checkPlayServices(this); //Google-Play-Services müssen installiert sein
    server = new ServerInterface(this);
    //Runtime-Permissions
    AndroidUtil.requestRuntimePermission(this, android.Manifest.permission.READ_CONTACTS);
    AndroidUtil.requestRuntimePermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    _initNewButton();
    _createList();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    phoneNumber = AndroidUtil.getOwnNumber(this);

    if (phoneNumber == null) //Konnte nicht ermittelt werden -> manuell eingeben
    {
      _showNumberInputDialog();
      return;
    }

    _doAfterNumberResolve();
  }

  private void _doAfterNumberResolve()
  {
    GCMUtil.register(this, phoneNumber, true);
    new PositionService(this, phoneNumber).start();
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
          AndroidUtil.storeOwnNumberInPrefs(Overview.this, phoneNumber);
          _doAfterNumberResolve();
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
      final INotification<?> notification = (INotification) notifications.get(position);

      //Bevor die Erinnerung angezeigt werden kann, muss noch der Name des Betreffenden ermittelt werden
      NotificationTarget target = notification.getNotificationTarget();
      target.setName(AndroidUtil.getContactNameFromNumber(getApplicationContext(), target.getPhoneNumber()));

      View rowView = NotificationUtil.createListRow(context, parent, notification.getNotificationTitle(context),
                                                    notification.getIconID());

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(Overview.this, NotificationView.class);
          //Die Storable-Erinnerung während der Serialisierung von der INotification trennen

          startActivity(NotificationUtil.createNotificationIntent(intent, notification));
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