package todolist.ui.util;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * ContextMenu that populates the list of menu items when it is about to be shown, rather than in
 * advance.
 */
public class DynamicContextMenu extends ContextMenu {

  private MenuItem dummyItem = new MenuItem("Populating ...");

  public DynamicContextMenu() {
    resetItems();
  }

  public DynamicContextMenu(Supplier<Collection<MenuItem>> menuItemsSupplier) {
    setMenuItemsSupplier(menuItemsSupplier);
  }

  private void resetItems() {
    getItems().setAll(dummyItem);
  }

  private Supplier<Collection<MenuItem>> menuItemsSupplier;

  public void setMenuItemsSupplier(Supplier<Collection<MenuItem>> menuItemsSupplier) {
    this.menuItemsSupplier = menuItemsSupplier;
  }

  protected Collection<MenuItem> getDynamicItems() {
    return menuItemsSupplier != null ? menuItemsSupplier.get() : Collections.emptyList();
  }

  protected void populateItems() {
    getItems().setAll(getDynamicItems());
  }

  @Override
  public void show(Node anchor, Side side, double dx, double dy) {
    populateItems();
    super.show(anchor, side, dx, dy);
  }

  @Override
  public void show(Node anchor, double screenX, double screenY) {
    populateItems();
    super.show(anchor, screenX, screenY);
  }

  @Override
  public void hide() {
    super.hide();
    resetItems();
  }
}
