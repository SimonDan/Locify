package ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import communication.WebserviceInterface;
import notification.*;
import notification.templates.*;

import java.util.List;

/**
 * @author simon, 04.06.2015
 */
public class NotificationView extends Activity
{
  private _NotificationState currentState;
  private BaseNotification notification;
  private List<ITemplateComponent> additionalFields;
  private WebserviceInterface webservice;

  //Fields-Panel
  private LayoutInflater inflater;
  private ITemplateComponent date, target, publicVisible;

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
    webservice = new WebserviceInterface();
    notification = (BaseNotification) getIntent().getSerializableExtra("notification");
    additionalFields = notification.createAdditionalFields(getApplicationContext());
    _initLayout();
    _switchState(_NotificationState.DEFAULT);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
  }

  @Override
  protected void onPause()
  {
    super.onPause();
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    LinearLayout fieldsPanel = (LinearLayout) findViewById(R.id.fieldsPanel);
    fieldsPanel.removeAllViews();
  }

  @Override
  protected void onDestroy()
  {
    super.onDestroy();
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
    //Default
    date = new TextFieldTemplate(getString(R.string.key_date), "");
    target = new TextFieldTemplate(getString(R.string.key_target), "");
    publicVisible = new CheckBoxTemplate(getString(R.string.key_public_visible), false);
    fieldsPanel.addView(_createTemplateRow(date, fieldsPanel));
    fieldsPanel.addView(_createTemplateRow(target, fieldsPanel));
    fieldsPanel.addView(_createTemplateRow(publicVisible, fieldsPanel));
    //Optional
    for (ITemplateComponent comp : additionalFields)
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

    if (date == null)
      throw new RuntimeException("not initialized"); //TODO

    date.setEditable(editing);
    target.setEditable(editing);
    publicVisible.setEditable(editing);
    for (ITemplateComponent comp : additionalFields)
      comp.setEditable(editing);
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
        webservice.deleteNotification(0); //TODO
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
        //TODO Save
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

  private enum _NotificationState
  {
    DEFAULT, EDITING, NOTIFICATION
  }
}