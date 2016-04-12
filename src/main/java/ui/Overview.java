package ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import communication.ServerInterface;
import notification.*;
import ui.util.AndroidUtil;

import java.util.List;

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
    server = new ServerInterface();
    phoneNumber = AndroidUtil.getOwnNumber(getApplicationContext());
    _initNewButton();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
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

  private class _ListAdapter extends ArrayAdapter<BaseNotification>
  {
    private List<BaseNotification> notifications;
    private Context context;

    _ListAdapter(Context pContext, int pListId, List<BaseNotification> pNotifications)
    {
      super(pContext, pListId, pNotifications);
      notifications = pNotifications;
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final BaseNotification notification = notifications.get(position);
      View rowView = NotificationUtil.createListRow(context, parent, notification.getTitle(context), notification.getIconId());

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

    void setListContent(List<BaseNotification> pNotifications)
    {
      notifications = pNotifications;
    }
  }
}