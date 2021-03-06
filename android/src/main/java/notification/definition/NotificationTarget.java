package notification.definition;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Beschreibt das Ziel einer Erinnerung
 * Beinhaltet Nummer und Name
 *
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

  public void setName(String pName)
  {
    name = pName;
  }

  @NotNull
  public String getPhoneNumber()
  {
    return phoneNumber != null ? phoneNumber : "";
  }

  @Override
  public String toString()
  {
    return name;
  }
}
