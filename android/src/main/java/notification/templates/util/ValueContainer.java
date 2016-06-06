package notification.templates.util;

import android.widget.TextView;

import java.io.Serializable;

/**
 * Beinhaltet einen Wert, wessen String-Repräsentation in eine Text-View geschrieben wird
 *
 * @author Simon Danner, 12.05.2016.
 */
public class ValueContainer<T> implements Serializable
{
  private TextView textView;
  private T value;

  public T getValue()
  {
    return value;
  }

  public void setValue(T pValue)
  {
    value = pValue;
    _setText();
  }

  public void setTextView(TextView pTextView)
  {
    textView = pTextView;
    _setText();
  }

  private void _setText()
  {
    if (textView != null && value != null)
      textView.setText(value.toString());
  }
}
