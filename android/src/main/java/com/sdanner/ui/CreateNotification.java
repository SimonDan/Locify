package com.sdanner.ui;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.google.common.collect.*;
import definition.*;
import notification.*;
import notification.notificationtypes.*;

import java.util.*;

/**
 * View, um neue Erinnerungen anzulegen
 *
 * @author Simon Danner, 17.06.2015
 */
public class CreateNotification extends Activity
{
  protected final static String NEW_NOTIFICATION = "newNotification";

  //Typen von Erinnerungen
  public static BiMap<Class<? extends INotification>, Class<? extends StorableBaseNotification>> TYPES = HashBiMap.create();

  //Hier müssen Typen von Erinnerungen eingetragen werden, die neu erstellt werden können
  static
  {
    TYPES.put(DebtsNotification.class, StorableDebtsNotification.class);
    TYPES.put(TextNotification.class, StorableTextNotification.class);
  }

  private List<INotification> typeList;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.createnotification);
    _initList();
    _initBackButton();
  }

  /**
   * Initialsiert die Liste
   * Hier werden abhängig von den statischen möglichen Typen für Erinnerungen neue Instanzen erzeugt,
   * welche dann bei Auswahl als neue Erinnerungen verwendet werden
   */
  private void _initList()
  {
    ListView list = (ListView) findViewById(R.id.notTypesList);
    Context context = list.getContext();
    String phoneNumber = getIntent().getStringExtra(Overview.PHONE_NUMBER);

    typeList = new ArrayList<>();
    for (Class<? extends INotification> type : TYPES.keySet())
    {
      try
      {
        StorableBaseNotification storable = TYPES.get(type).newInstance();
        storable.setValue(StorableBaseNotification.creator, phoneNumber);
        typeList.add(NotificationUtil.createNotificationFromStorable(storable));
      }
      catch (InstantiationException | IllegalAccessException pE)
      {
        throw new RuntimeException(pE);
      }
    }


    list.setAdapter(new _ListAdapter(context, R.id.notTypesList));
  }

  /**
   * Initialisiert den Zurück-Button
   */
  private void _initBackButton()
  {
    ImageButton backButton = (ImageButton) findViewById(R.id.returnButton);
    backButton.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        finish();
      }
    });
  }

  /**
   * Der List-Adapter für die Liste mit den möglichen Typen von Erinnerungen
   */
  private class _ListAdapter extends ArrayAdapter<INotification>
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
      final INotification notification = typeList.get(position);
      View rowView = NotificationUtil.createListRow(context, parent, notification.getTypeName(context), notification.getIconID());

      rowView.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          Intent intent = new Intent(CreateNotification.this, NotificationView.class);
          intent.putExtra(Overview.NOTIFICATION, notification);
          intent.putExtra(NEW_NOTIFICATION, true);
          startActivity(intent);
        }
      });

      return rowView;
    }
  }
}