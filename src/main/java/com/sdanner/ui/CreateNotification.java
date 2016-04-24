package com.sdanner.ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.AndroidUtil;
import notification.*;
import notification.notificationtypes.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author simon, 17.06.2015
 */
public class CreateNotification extends Activity
{
  //Typen von Erinnerungen
  private static List<Class<? extends BaseNotification>> TYPES = new ArrayList<>();

  //Hier müssen Typen von Erinnerungen eingetragen werden, die neu erstellt werden sollen können
  static
  {
    TYPES.add(DebtsNotification.class);
    TYPES.add(TextNotification.class);
  }

  private List<BaseNotification> typeList;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.createnotification);
    _initList();
    _initBackButton();
  }

  private void _initList()
  {
    ListView list = (ListView) findViewById(R.id.notTypesList);
    Context context = list.getContext();

    typeList = new ArrayList<>();
    for (Class<? extends BaseNotification> type : TYPES)
      try
      {
        typeList.add(type.getDeclaredConstructor(Context.class, String.class).newInstance(context, AndroidUtil.getOwnNumber(this)));
      }
      catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException pE)
      {
        throw new RuntimeException(pE);
      }

    list.setAdapter(new _ListAdapter(context, R.id.notTypesList));
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
      super(pContext, pListId, typeList);
      context = pContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final BaseNotification notification = typeList.get(position);
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