package ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import communication.NotificationFetcher;
import notification.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author simon, 22.05.2015
 */
public class Overview extends Activity
{
  private _ListAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.overview);
    _initNewButton();
  }

  @Override
  protected void onStart()
  {
    super.onStart();
    if (adapter == null)
      _createList();
    else
      adapter.setListContent(_fetchNotifications());
  }

  private void _createList()
  {
    ListView list = (ListView) findViewById(R.id.listView);
    adapter = new _ListAdapter(list.getContext(), R.id.listView, _fetchNotifications());
    list.setAdapter(adapter);
  }

  private List<BaseNotification> _fetchNotifications()
  {
    try
    {
      return new NotificationFetcher(getApplicationContext()).execute().get();
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new RuntimeException(e); //TODO
    }
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