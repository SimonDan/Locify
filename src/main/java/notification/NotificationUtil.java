package notification;

import android.content.Context;
import android.view.*;
import android.widget.*;
import ui.R;

/**
 * @author simon, 20.06.2015
 */
public class NotificationUtil
{
  private NotificationUtil()
  {
  }

  public static View createListRow(Context pContext, ViewGroup pParent, String pTitle, int pIconId)
  {
    LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.listrow, pParent, false);
    ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
    TextView titleView = (TextView) rowView.findViewById(R.id.item_title);

    imgView.setImageResource(pIconId);
    titleView.setText(pTitle);

    return rowView;
  }
}
