package notification.templates.util;

/**
 * Beschreibt eine Button-Aktion für ein ValueFromActionTemplate
 *
 * @author Simon Danner, 12.05.2016.
 */
public interface IButtonAction<T>
{
  void executeButtonAction(ValueContainer<T> pValueContainer);
}
