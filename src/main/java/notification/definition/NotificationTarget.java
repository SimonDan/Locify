package notification.definition;

import java.io.Serializable;

/**
 * @author simon, 22.06.2015
 */
public class NotificationTarget implements Serializable
{
  private String name;
  private int phoneNumber;

  public NotificationTarget(String pName, int pPhoneNumber)
  {
    name = pName;
    phoneNumber = pPhoneNumber;
  }

  public String getName()
  {
    return name;
  }

  public int getPhoneNumber()
  {
    return phoneNumber;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
