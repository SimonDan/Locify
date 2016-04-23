package notification.templates;

import android.content.Context;
import android.view.*;
import android.widget.*;
import notification.ITemplateComponent;
import com.sdanner.ui.R;

import java.io.Serializable;

/**
 * @author simon, 20.06.2015
 */
public class ValueFromActionTemplate<T> implements ITemplateComponent<T>, Serializable
{
  private String key;
  private T value;
  private Runnable buttonAction;

  //Layout
  private RelativeLayout container;
  private TextView text;
  private ImageButton button;

  public ValueFromActionTemplate(String pKey)
  {
    this(pKey, null);
  }

  public ValueFromActionTemplate(String pKey, T pValue)
  {
    key = pKey;
    value = pValue;
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (container == null)
    {
      LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      container = (RelativeLayout) inflater.inflate(R.layout.textfromaction, null, false);
      text = (TextView) container.findViewById(R.id.textFromActionValue);
      button = (ImageButton) container.findViewById(R.id.textFromActionButton);
      button.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          buttonAction.run();
        }
      });

      setEditable(false);
      setValue(value);
    }

    return container;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    button.setVisibility(pEditable ? View.VISIBLE : View.INVISIBLE);
  }

  @Override
  public T getValue()
  {
    return value;
  }

  @Override
  public void setValue(T pValue)
  {
    value = pValue;
    text.setText(value == null ? "" : value.toString());
  }

  public void setButtonAction(Runnable pAction)
  {
    buttonAction = pAction;
  }
}
