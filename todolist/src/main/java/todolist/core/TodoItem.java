package todolist.core;

public class TodoItem {

    private String text;
    private boolean checked;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void toggleChecked() {
        setChecked(! checked);
    }
}
