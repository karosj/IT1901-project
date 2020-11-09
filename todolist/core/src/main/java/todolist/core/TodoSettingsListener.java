package todolist.core;

import java.util.Collection;

public interface TodoSettingsListener {
  public void todoSettingsChanged(TodoSettings settings, Collection<String> changedProperties);
}