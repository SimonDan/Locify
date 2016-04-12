package ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import notification.*;
import notification.definition.NotificationStartDate;
import notification.notificationtypes.*;

import java.util.*;

/**
 * @author simon, 17.06.2015
 */
public class CreateNotification extends Activity
{
  private List<BaseNotification> TYPES = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.createnotification);
    NotificationStartDate date = new NotificationStartDate(new Date());
    TYPES.add(new DebtsNotification(getApplicationContext(), "-1", date, null, false, 0));
    TYPES.add(new TextNotification(getApplicationContext(), "-1", date, null, false, "", ""));
    _initList();
    _initBackButton();
  }

  private void _initList()
  {
    ListView list = (ListView) findViewById(R.id.notTypesList);
    list.setAdapter(new _ListAdapter(list.getContext(), R.id.notTypesList));
  }

  private void _initBackButton()
  {
    ImageButton backButton = (ImageButton) findViewById(R.id.returnButton);
    backButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(CreateNotification.this, Overview.class);
        startActivity(intent);
      }
    });
  }

  private class _ListAdapter extends ArrayAdapter<BaseNotification>
  {
    private Context context;

    public _ListAdapter(Context pContext, int pListId)
    {
      super(pContext, pListId, TYPES);
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final BaseNotification notification = TYPES.get(position);
      View rowView = NotificationUtil.createListRow(context, parent, notification.getTypeName(context), notification.getIconID());

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(CreateNotification.this, NotificationView.class);
          intent.putExtra("notification", notification);
          intent.putExtra("newNotification", true);
          startActivity(intent);
        }
      });

      return rowView;
    }
  }
}