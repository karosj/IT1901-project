package todolist.core;

import java.util.Collection;

/**
 * Listener for changes to TodoSettings.
 */
public interface TodoSettingsListener {

  /**
   * Called when settings are changed.
   * Changes may be vetoed by throwing an exception.
   *
   * @param settings the TodoSettings that changed
   * @param changedProperties the properties (names) that changed 
   */
  public void todoSettingsChanged(TodoSettings settings, Collection<String> changedProperties);
}