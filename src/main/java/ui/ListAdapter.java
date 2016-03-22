package ui;

import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;
import notification.BaseNotification;

import java.util.List;

/**
 * @author simon, 11.06.2015
 */
public class ListAdapter extends ArrayAdapter<BaseNotification>
{
  private List<BaseNotification> notifications;
  private Context context;
  private Activity activity;

  public ListAdapter(Context pContext, int pListId, List<BaseNotification> pNotifications, Activity pActivity)
  {
    super(pContext, pListId, pNotifications);
    notifications = pNotifications;
    context = pContext;
    activity = pActivity;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.listrow, parent, false);
    ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
    TextView titleView = (TextView) rowView.findViewById(R.id.item_title);

    final BaseNotification notification = notifications.get(position);
    imgView.setImageResource(notification.getIconId());
    titleView.setText(notification.getTitle(context));

    rowView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        Intent intent = new Intent(activity, NotificationView.class);
        intent.putExtra("notification", notification); //TODO
        activity.startActivity(intent);
      }
    });

    return rowView;
  }
}
