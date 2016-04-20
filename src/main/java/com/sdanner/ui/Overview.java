package com.sdanner.ui;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.AndroidUtil;
import communication.ServerInterface;
import notification.*;

import java.util.List;
import java.util.regex.*;

/**
 * @author Simon Danner, 22.05.2015
 */
public class Overview extends Activity
{
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
    _initNewButton();
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

    if (adapter == null)
      _createList();
    else
      adapter.setListContent(server.getNotifications(phoneNumber));
  }

  private void _createList()
  {
    ListView list = (ListView) findViewById(R.id.listView);
    adapter = new _ListAdapter(list.getContext(), R.id.listView, server.getNotifications(phoneNumber));
    list.setAdapter(adapter);
  }

  private void _initNewButton()
  {
    ImageButton newButton = (ImageButton) findViewById(R.id.newNotificationButton);
    newButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(Overview.this, CreateNotification.class);
        startActivity(intent);
      }
    });
  }

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

          _createList();
          dialog.dismiss();
        }
        else
          Toast.makeText(getApplicationContext(), getString(R.string.invalid_number_message), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private boolean _checkValidNumber(String pPhoneNumber)
  {
    String regex = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(pPhoneNumber);
    return m.matches();
  }

  private class _ListAdapter extends ArrayAdapter<INotification>
  {
    private List<INotification> notifications;
    private Context context;

    _ListAdapter(Context pContext, int pListId, List<INotification> pNotifications)
    {
      super(pContext, pListId, pNotifications);
      notifications = pNotifications;
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final BaseNotification notification = (BaseNotification) notifications.get(position);
      View rowView = NotificationUtil.createListRow(context, parent, notification.getTitle(context), notification.getIconID());

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(Overview.this, NotificationView.class);
          intent.putExtra("notification", notification);
          startActivity(intent);
        }
      });

      return rowView;
    }

    void setListContent(List<INotification> pNotifications)
    {
      notifications = pNotifications;
    }
  }
}