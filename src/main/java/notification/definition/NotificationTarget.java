package notification.definition;

import java.io.Serializable;

/**
 * @author Simon Danner, 22.06.2015
 */
public class NotificationTarget implements Serializable
{
  private String name;
  private String phoneNumber;

  public NotificationTarget(String pName, String pPhoneNumber)
  {
    name = pName;
    phoneNumber = pPhoneNumber;
  }

  public String getName()
  {
    return name;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
