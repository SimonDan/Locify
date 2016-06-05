package notification.definition;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Beschreibt das Start-Datum einer Erinnerung
 * Besonderheit: toString: Schönes Format
 *
 * @author Simon Danner, 23.06.2015
 */
public class NotificationStartDate implements Serializable
{
  private long date;

  public NotificationStartDate(long pDate)
  {
    setDate(pDate);
  }

  public long getDate()
  {
    return date;
  }

  public void setDate(long pDate)
  {
    if (pDate > 0)
      date = pDate;
  }

  @Override
  public String toString()
  {
    SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    return simpleDateformat.format(getDate());
  }
}
