package notification.templates;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.R;
import notification.ITemplateComponent;
import notification.templates.util.*;

import java.io.Serializable;

/**
 * @author simon, 20.06.2015
 */
public class ValueFromActionTemplate<T> implements ITemplateComponent<T>, Serializable
{
  private String key;
  private T value;
  private ValueContainer<T> valueContainer;
  private IButtonAction<T> buttonAction;

  //Layout
  private RelativeLayout container;
  private ImageButton button;

  public ValueFromActionTemplate(T pValue)
  {
    value = pValue;
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public void setKey(String pKey)
  {
    key = pKey;
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (container == null)
    {
      LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      container = (RelativeLayout) inflater.inflate(R.layout.textfromaction, null, false);
      valueContainer = new ValueContainer<>((TextView) container.findViewById(R.id.textFromActionValue));
      button = (ImageButton) container.findViewById(R.id.textFromActionButton);
      button.setOnClickListener(new View.OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          buttonAction.executeButtonAction(valueContainer);
        }
      });

      setEditable(false);
      shiftValueToGraphicComponent();
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
  public void shiftValueToGraphicComponent()
  {
    valueContainer.setValue(getValue());
  }

  @Override
  public void setValueFromGraphicComponent()
  {
    value = valueContainer.getValue();
  }

  public void setButtonAction(IButtonAction<T> pAction)
  {
    buttonAction = pAction;
  }

  public ValueContainer<T> getValueContainer()
  {
    return valueContainer;
  }
}
