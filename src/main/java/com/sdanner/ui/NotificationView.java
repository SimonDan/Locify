package com.sdanner.ui;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import communication.ServerInterface;
import notification.*;

import java.util.*;

/**
 * View, um eine Erinnerung anzuzeigen und zu bearbeiten.
 * Sie kann verschieden Stati
 *
 * @author Simon Danner, 04.06.2015
 */
public class NotificationView extends Activity
{
  private _NotificationState currentState;
  private INotification notification;
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
    server = new ServerInterface(this);
    notification = (INotification) getIntent().getSerializableExtra("notification");
    _initLayout();
    _switchState(_NotificationState.DEFAULT);
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
  }

  private void _switchState(_NotificationState pState)
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
    inflater = (LayoutInflater) fieldsPanel.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    for (ITemplateComponent comp : notification.getFields(this))
      fieldsPanel.addView(_createTemplateRow(comp, fieldsPanel));
  }

  private View _createTemplateRow(ITemplateComponent pComponent, ViewGroup pParent)
  {
    View templateRow = inflater.inflate(R.layout.templaterow, pParent, false);
    TextView key = (TextView) templateRow.findViewById(R.id.key);
    LinearLayout viewContainer = (LinearLayout) templateRow.findViewById(R.id.viewContainer);
    key.setText(pComponent.getKey() + ":");
    viewContainer.addView(pComponent.getGraphicComponent(getApplicationContext(), null));
    return templateRow;
  }

  private void _toggleEditMode()
  {
    if (currentState == _NotificationState.EDITING && !editing)
      editing = true;
    else if (currentState != _NotificationState.EDITING && editing)
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
          cancel = _createButton(buttonPanel.getContext(), R.drawable.cancel, _getCancelAction());
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
        _switchState(_NotificationState.EDITING);
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
        server.deleteNotification(notification.getID());
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
        server.updateNotification(notification);
        _switchState(_NotificationState.DEFAULT);
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
        //TODO undo
        _switchState(_NotificationState.DEFAULT);
      }
    };
  }

  private Runnable _getDatePickerAction()
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(notification.getStartDate().getDate());

        new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener()
        {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
          {

          }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
      }
    };
  }

  private enum _NotificationState
  {
    DEFAULT, EDITING, NOTIFICATION
  }
}