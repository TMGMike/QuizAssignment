import javax.swing.*;

public class DeselectButtonGroup extends ButtonGroup {
    // This class overrides the default behaviour from ButtonGroup which always keeps one button selected at all times.
    // Overriding it to clear selection will mean the answers get deselected after each question.
    @Override
    public void setSelected(ButtonModel model, boolean selected){
        if(selected) {
            super.setSelected(model, selected);
        }else {
            clearSelection();
        }
    }
}
