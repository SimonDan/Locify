package com.sdanner.ui;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.AndroidUtil;
import communication.ServerInterface;
import notification.*;
import notification.definition.NotificationTarget;

/**
 * View, um eine Erinnerung anzuzeigen und zu bearbeiten.
 * Sie besitzt verschiedene Stati (siehe _EState)
 *
 * @author Simon Danner, 04.06.2015
 */
public class NotificationView extends Activity
{
  public final static int CONTACT_PICKER_RESULT = 3689;
  public final static String FROM_PUSH_NOTIFICATION = "fromPushNotification";

  //Status-Informationen der View
  private _EState currentState;
  private INotification notification;
  private boolean isNewNotification;

  //Helper
  private LayoutInflater inflater;
  private ServerInterface server;

  //Button-Panel
  private ImageButton edit, delete, save, cancel, back, accept, keep;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.notificationview);
    server = new ServerInterface(this);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    notification = (INotification) getIntent().getSerializableExtra(Overview.NOTIFICATION);
    isNewNotification = getIntent().getBooleanExtra(CreateNotification.NEW_NOTIFICATION, false);
    boolean fromPush = getIntent().getBooleanExtra(FROM_PUSH_NOTIFICATION, false);
    _initLayout();
    _EState initState = fromPush ? _EState.NOTIFICATION : isNewNotification ? _EState.EDITING : _EState.DEFAULT;
    _switchState(initState);
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
    //Die Parents von den Komponenten des Templates entfernen
    for (ITemplateComponent template : notification.getFields(this))
    {
      View comp = template.getGraphicComponent(this);
      ViewGroup parent = (ViewGroup) comp.getParent();
      if (parent != null)
        parent.removeView(comp);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode == RESULT_OK)
    {
      switch (requestCode)
      {
        case CONTACT_PICKER_RESULT:
          Uri uri = data.getData();
          Cursor cursor = getContentResolver().query(uri, null, null, null, null);
          cursor.moveToFirst();
          int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
          int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
          String number = cursor.getString(phoneIndex);
          String name = cursor.getString(nameIndex);
          ((BaseNotification) notification).setTargetInContainer(new NotificationTarget(name, number));
          cursor.close();
          break;
      }
    }
  }

  /**
   * Wechselt den Zustand der Erinnerung-Ansicht
   *
   * @param pState der neue Zustand
   */
  private void _switchState(_EState pState)
  {
    currentState = pState;
    _toggleEditMode();
    _setButtonPanel();
    _setTitle();
  }

  /**
   * Initialisiert das Layout der View
   * Dabei wird der Titel gesetzt und die Key-Value-Paare der Erinnerung hinzugefügt
   */
  private void _initLayout()
  {
    _setTitle();

    //Notification-Templates setzen
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
    inflater = (LayoutInflater) fieldsPanel.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    for (ITemplateComponent comp : notification.getFields(this))
      fieldsPanel.addView(_createTemplateRow(comp, fieldsPanel));
  }

  /**
   * Lädt den Titel neu (wird von der INotification festgelegt)
   */
  private void _setTitle()
  {
    TextView title = (TextView) findViewById(R.id.notificationTitle);
    title.setText(notification.getNotificationTitle(getApplicationContext()));
  }

  /**
   * Erzeugt eine grafische Zeile der Erinnerung (Key-Value)
   *
   * @param pComponent die Komponente (Key-Value)
   * @param pParent    der Parent dieser Zeile (grafische Komponente konnte vorher schon Teil einer Zeile sein)
   * @return die erzeugte View
   */
  private View _createTemplateRow(ITemplateComponent pComponent, ViewGroup pParent)
  {
    View templateRow = inflater.inflate(R.layout.templaterow, pParent, false);
    TextView key = (TextView) templateRow.findViewById(R.id.key);
    LinearLayout viewContainer = (LinearLayout) templateRow.findViewById(R.id.viewContainer);
    key.setText(getString(R.string.key_text, pComponent.getKey()));
    viewContainer.addView(pComponent.getGraphicComponent(this));
    return templateRow;
  }

  /**
   * Wechselt den Bearbeitungs-Zustand der grafischen Komponenten abhängig vom Zustand der Notification-View
   */
  private void _toggleEditMode()
  {
    for (ITemplateComponent component : notification.getFields(this))
      component.setEditable(currentState == _EState.EDITING);
  }

  /**
   * Konfiguriert das Button-Panel abhängig vom Zustand
   */
  private void _setButtonPanel()
  {
    LinearLayout buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel);
    buttonPanel.removeAllViews();

    switch (currentState)
    {
      case DEFAULT:
        if (edit == null)
          edit = _createButton(buttonPanel.getContext(), R.drawable.change, _getEditAction());
        if (delete == null)
          delete = _createButton(buttonPanel.getContext(), R.drawable.delete, _getDeleteAction());
        if (back == null)
          back = _createButton(buttonPanel.getContext(), R.drawable.back, _getBackAction());
        buttonPanel.addView(back);
        buttonPanel.addView(edit);
        buttonPanel.addView(delete);
        break;
      case EDITING:
        if (save == null)
          save = _createButton(buttonPanel.getContext(), R.drawable.save, _getSaveAction());
        if (cancel == null)
          cancel = _createButton(buttonPanel.getContext(), R.drawable.back, _getCancelAction());
        buttonPanel.addView(save);
        buttonPanel.addView(cancel);
        break;
      case NOTIFICATION:
        if (accept == null)
          accept = _createButton(buttonPanel.getContext(), R.drawable.ok, _getDeleteAction());
        if (keep == null)
          keep = _createButton(buttonPanel.getContext(), R.drawable.back, _getBackAction());
        break;
    }
  }

  /**
   * Erzeugt einen Button für das Button-Panel. Legt auch die Action fest
   *
   * @param pContext der Context des Panels
   * @param pIconId  die ID des Icons
   * @param pAction  die Aktion, die ausgeführt werden soll
   * @return ein neu erzeugter Image-Button
   */
  private ImageButton _createButton(Context pContext, int pIconId, final Runnable pAction)
  {
    ImageButton button = new ImageButton(pContext);
    button.setImageResource(pIconId);
    button.setBackgroundResource(R.color.transparent_back);
    button.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        pAction.run();
      }
    });
    return button;
  }

  /**
   * Definiert eine Aktion, um zur Erinnerungs-Übersicht zurückzukehren
   */
  private Runnable _getBackAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        finish();
      }
    };
  }

  /**
   * Definiert eine Aktion, um in den Editier-Modus zu wechseln
   */
  private Runnable _getEditAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        _switchState(_EState.EDITING);
      }
    };
  }

  /**
   * Definiert eine Aktion, um eine Erinnerung zu löschen
   */
  private Runnable _getDeleteAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        Runnable callback = new Runnable()
        {
          @Override
          public void run()
          {
            server.deleteNotification(notification.getID());
          }
        };
        AndroidUtil.showConfirmDialog(NotificationView.this, getString(R.string.delete_dialog_title), callback);
      }
    };
  }

  /**
   * Definiert eine Aktion, um eine Erinnerung zu speichern
   */
  private Runnable _getSaveAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        if (!notification.isValid())
          Toast.makeText(NotificationView.this, getString(R.string.not_all_filled), Toast.LENGTH_SHORT).show();
        else
        {
          _setValuesFromGraphicComponents(); //Values endgültig setzen vorm Speichern
          server.updateNotification(notification);
          _switchState(_EState.DEFAULT);
        }
      }
    };
  }

  private void _setValuesFromGraphicComponents()
  {
    for (ITemplateComponent template : notification.getFields(this))
      template.setValueFromGraphicComponent();
  }

  /**
   * Definiert eine Aktion, um das Editieren einer Erinnerung abzubrechen (Revert)
   */
  private Runnable _getCancelAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        if (isNewNotification)
        {
          finish();
          return;
        }
        _shiftValuesToGraphicComponents(); //Alte Werte wieder in den grafischen Komponenten setzen
        _switchState(_EState.DEFAULT);
      }
    };
  }

  private void _shiftValuesToGraphicComponents()
  {
    for (ITemplateComponent template : notification.getFields(this))
      template.shiftValueToGraphicComponent();
  }

  /**
   * Legt die möglichen Zustände dieser View fest
   */
  private enum _EState
  {
    DEFAULT, EDITING, NOTIFICATION
  }
}