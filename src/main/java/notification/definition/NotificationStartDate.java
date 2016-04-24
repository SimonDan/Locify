package notification.definition;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Simon Danner, 23.06.2015
 */
public class NotificationStartDate implements Serializable
{
  private Date date;

  public NotificationStartDate(Date pDate)
  {
    setDate(pDate);
  }

  public Date getDate()
  {
    return date == null ? new Date() : date;
  }

  public void setDate(Date pDate)
  {
    date = pDate;
  }

  @Override
  public String toString()
  {
    SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    return simpleDateformat.format(getDate());
  }
}
