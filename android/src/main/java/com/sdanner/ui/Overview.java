package com.sdanner.ui;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.graphics.Color;
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
  public static final String PHONE_NUMBER = "phoneNumber";
  public static final String NOTIFICATION = "notification";
  public static final String STORABLE_NOTIFICATION = "storableNotification";

  private _ListAdapter adapter;
  private ServerInterface server;
  private String phoneNumber;
  private boolean targetNotificationsLoaded = false;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.overview);

    //Runtime-Permissions zu Beginn anfordern
    AndroidUtil.requestRuntimePermission(this, android.Manifest.permission.READ_CONTACTS);
    AndroidUtil.requestRuntimePermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

    //Benötigte Hilfsmittel und Informationen
    GCMUtil.checkPlayServices(this);
    server = new ServerInterface(this);
    _resolvePhoneNumber();

    //Layout aufbauen
    _initNewButton();
    _initCheckBoxListener();
    _createList();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    adapter.reset();
    new _FillListTask(true).execute();
    if (((CheckBox) findViewById(R.id.showMeAsTargetCheckbox)).isChecked())
      new _FillListTask(false).execute();
  }

  /**
   * Ermittelt die Telefon-Nummer des Users
   * Zeigt einen Input-Dialog, wenn die Nummer nicht über die SIM-Karte ermittelt werden kann
   */
  private void _resolvePhoneNumber()
  {
    phoneNumber = AndroidUtil.getOwnNumber(this);

    if (phoneNumber == null) //Konnte nicht ermittelt werden -> manuell eingeben
    {
      _showNumberInputDialog();
      return;
    }

    _doAfterNumberResolve();
  }

  /**
   * Bestimmt, was nach dem Ermitteln der Telefon-Nummer getan werden muss
   * Die Nummer wird bei GCM registriert (falls nicht geschehen) und der Position-Service wird gestartet
   */
  private void _doAfterNumberResolve()
  {
    GCMUtil.register(this, phoneNumber, true);
    new PositionService(this, phoneNumber).start();
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
   * Initialisiert den Checkbox-Listener
   * Dieser lädt die mich betreffenden Erinnerung nach, wenn noch nicht geschehen, um diese anzuzeigen
   */
  private void _initCheckBoxListener()
  {
    CheckBox checkbox = (CheckBox) findViewById(R.id.showMeAsTargetCheckbox);
    checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton pCompoundButton, boolean pChecked)
      {
        if (pChecked && !targetNotificationsLoaded)
          new _FillListTask(false).execute();
        else
          adapter.setShowTargetNotifications(pChecked);
      }
    });
  }

  /**
   * Initialisiert die Liste mit den Erinnerungen
   */
  private void _createList()
  {
    ListView list = (ListView) findViewById(R.id.listView);
    adapter = new _ListAdapter(list.getContext(), R.id.listView);
    list.setAdapter(adapter);
  }

  /**
   * Zeigt einen Dialog, um die Telefon-Nummer einzutippen
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
    private List<INotification> myNotifications;
    private boolean showTargetNotifications = false;
    private Context context;

    public _ListAdapter(Context pContext, int pListId)
    {
      super(pContext, pListId);
      notifications = new ArrayList<>();
      myNotifications = new ArrayList<>();
      showTargetNotifications = false;
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final INotification<?> notification;

      if (showTargetNotifications || !targetNotificationsLoaded)
        notification = (INotification) notifications.get(position);
      else
        notification = (INotification) myNotifications.get(position);

      boolean isMyNotification = notification.getCreator().equals(phoneNumber);
      //Bevor die Erinnerung angezeigt werden kann, muss noch der Name des Betreffenden ermittelt werden
      NotificationTarget target = notification.getNotificationTarget();
      target.setName(_getTargetName(isMyNotification, target));

      String title = notification.getNotificationTitle(context, isMyNotification);
      View rowView = NotificationUtil.createListRow(context, parent, title, notification.getIconID());
      if (!isMyNotification)
        rowView.setBackgroundColor(Color.GREEN);

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(Overview.this, NotificationView.class);
          startActivity(NotificationUtil.createNotificationIntent(intent, notification, phoneNumber));
        }
      });

      return rowView;
    }

    /**
     * Ermittelt den Namen des Targets
     *
     * @param pMyNotification gibt an, ob diese Erinnerung vom User erstellt wurde
     * @param pTarget         das Ziel der Erinnerung
     * @return der Name des Ziels oder "Du", wenn der User das Target ist
     */
    private String _getTargetName(boolean pMyNotification, NotificationTarget pTarget)
    {
      if (pMyNotification)
        return AndroidUtil.getContactNameFromNumber(getApplicationContext(), pTarget.getPhoneNumber());
      return getString(R.string.you_text);
    }

    /**
     * Fügt dem Adapter eine Menge von Erinnerungen hinzu, welche angezeigt werden sollen
     *
     * @param pNotifications die neuen Erinnerungen
     */
    public void addNotifications(List<INotification> pNotifications)
    {
      boolean showTargetNotifications = notifications.size() > 0;
      if (showTargetNotifications)
        myNotifications.addAll(notifications);
      if (!targetNotificationsLoaded && showTargetNotifications)
        targetNotificationsLoaded = true;
      notifications.addAll(pNotifications);
      setShowTargetNotifications(showTargetNotifications);
    }

    /**
     * Gibt an, ob bei dem Adapter auch Erinnerungen gezeigt werden sollen, die den User selbst betreffen
     *
     * @param pShowTargetNotifications <tt>true</tt> wenn auch die mich Betreffenden angezeigt werden sollen
     */
    public void setShowTargetNotifications(boolean pShowTargetNotifications)
    {
      clear();
      showTargetNotifications = pShowTargetNotifications;
      List<INotification> l = pShowTargetNotifications || !targetNotificationsLoaded ? notifications : myNotifications;
      //Sortieren nach Start-Datum
      Collections.sort(l, new Comparator<INotification>()
      {
        @Override
        public int compare(INotification pNotification, INotification pOtherNotification)
        {
          return (int) (pNotification.getStorableNotification().getStartDate() -
              pOtherNotification.getStorableNotification().getStartDate());
        }
      });

      addAll(l);
    }

    /**
     * Setzt den Adapter zurück
     */
    public void reset()
    {
      clear();
      showTargetNotifications = false;
      targetNotificationsLoaded = false;
      notifications.clear();
      myNotifications.clear();
    }
  }

  /**
   * Eine asynchrone Task, um den Inhalt der Liste vom Server zu holen
   * Hier kann unterschieden werden, ob die eigenen oder die mich betreffenden Erinnerungen geladen werden sollen
   */
  private class _FillListTask extends AsyncTask<Void, Void, Void>
  {
    private boolean loadMyNotifications;

    public _FillListTask(boolean pLoadMyNotifications)
    {
      loadMyNotifications = pLoadMyNotifications;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
      final List<INotification> notifications;

      try
      {
        notifications = server.getNotifications(phoneNumber, loadMyNotifications);
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
          adapter.addNotifications(notifications);
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