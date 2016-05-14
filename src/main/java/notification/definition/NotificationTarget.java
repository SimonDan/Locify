package notification.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Beschreibt das Ziel einer Erinnerung
 * Beinhaltet Nummer und Name
 *
 * @author Simon Danner, 22.06.2015
 */
public class NotificationTarget implements Serializable
{
  @JsonIgnore
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

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber(String pNumber)
  {
    phoneNumber = pNumber;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
