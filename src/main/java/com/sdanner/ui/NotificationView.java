package com.sdanner.ui;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.*;
import communication.ServerInterface;
import notification.*;
import notification.definition.NotificationTarget;

/**
 * View, um eine Erinnerung anzuzeigen und zu bearbeiten.
 * Sie kann verschieden Stati
 *
 * @author Simon Danner, 04.06.2015
 */
public class NotificationView extends Activity
{
  public static final int CONTACT_PICKER_RESULT = 3689;

  private _EState currentState;
  private INotification notification;
  private boolean isNewNotification;
  private ServerInterface server;

  private LayoutInflater inflater;

  //Button-Panel
  private ImageButton edit, delete, save, cancel, back;
  private boolean editing = false;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.notificationview);
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    server = new ServerInterface();
    notification = (INotification) getIntent().getSerializableExtra("notification");
    isNewNotification = getIntent().getBooleanExtra("newNotification", false);
    _initLayout();
    _switchState(isNewNotification ? _EState.EDITING : _EState.DEFAULT);
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
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
          notification.setTarget(new NotificationTarget(name, number));
          cursor.close();
          break;
      }
    }
  }

  private void _switchState(_EState pState)
  {
    currentState = pState;
    _toggleEditMode();
    _setButtonPanel();
  }

  private void _initLayout()
  {
    //Titel setzen
    TextView title = (TextView) findViewById(R.id.notificationTitle);
    title.setText(notification.getTitle(getApplicationContext()));

    //Notification-Template setzen
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
    inflater = (LayoutInflater) fieldsPanel.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    for (ITemplateComponent comp : notification.getFields(this))
      fieldsPanel.addView(_createTemplateRow(comp, fieldsPanel));
  }

  private View _createTemplateRow(ITemplateComponent pComponent, ViewGroup pParent)
  {
    View templateRow = inflater.inflate(R.layout.templaterow, pParent, false);
    TextView key = (TextView) templateRow.findViewById(R.id.key);
    LinearLayout viewContainer = (LinearLayout) templateRow.findViewById(R.id.viewContainer);
    key.setText(getString(R.string.key_text, pComponent.getKey()));
    viewContainer.addView(pComponent.getGraphicComponent(getApplicationContext()));
    return templateRow;
  }

  private void _toggleEditMode()
  {
    if (currentState == _EState.EDITING && !editing)
      editing = true;
    else if (currentState != _EState.EDITING && editing)
      editing = false;

    for (ITemplateComponent component : notification.getFields(this))
      component.setEditable(editing);
  }

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
      case NOTIFICATION:
        if (save == null)
          save = _createButton(buttonPanel.getContext(), R.drawable.save, _getSaveAction());
        if (cancel == null)
          cancel = _createButton(buttonPanel.getContext(), R.drawable.back, _getCancelAction());
        buttonPanel.addView(save);
        buttonPanel.addView(cancel);
        break;
    }
  }

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

  private Runnable _getBackAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        Intent intent = new Intent(NotificationView.this, Overview.class);
        startActivity(intent);
      }
    };
  }

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

  private Runnable _getDeleteAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          server.deleteNotification(notification.getID());
        }
        catch (ServerUnavailableException pE)
        {
          AndroidUtil.showErrorOnUIThread(NotificationView.this, pE);
        }
      }
    };
  }

  private Runnable _getSaveAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          server.updateNotification(notification);
        }
        catch (ServerUnavailableException pE)
        {
          AndroidUtil.showErrorOnUIThread(NotificationView.this, pE);
        }

        _switchState(_EState.DEFAULT);
      }
    };
  }

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
        //TODO undo
        _switchState(_EState.DEFAULT);
      }
    };
  }

  private enum _EState
  {
    DEFAULT, EDITING, NOTIFICATION
  }
}