package util;

/**
 * Speichert die Telfon-Nummer zusammen mit einem GCM-Token
 *
 * @author Simon Danner, 16.05.2016.
 */
public class TokenWrapper
{
  private String phoneNumber;
  private String token;

  public TokenWrapper()
  {
  }

  public TokenWrapper(String pPhoneNumber, String pToken)
  {
    phoneNumber = pPhoneNumber;
    token = pToken;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getToken()
  {
    return token;
  }

  public Object[] getAsArray()
  {
    return new Object[] {phoneNumber, token};
  }
}
