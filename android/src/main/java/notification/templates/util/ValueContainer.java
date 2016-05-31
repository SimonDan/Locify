package notification.templates.util;

import android.widget.TextView;
import org.jetbrains.annotations.NotNull;

/**
 * Beinhaltet einen Wert, wessen String-Repräsentation in eine Text-View geschrieben wird
 *
 * @author Simon Danner, 12.05.2016.
 */
public class ValueContainer<T>
{
  private TextView textView;
  private T value;

  public ValueContainer(@NotNull TextView pTextView)
  {
    textView = pTextView;
  }

  public T getValue()
  {
    return value;
  }

  public void setValue(T pValue)
  {
    value = pValue;
    textView.setText(value.toString());
  }
}
