package Client.Controller;

import Client.Layouts.Layout;
import javafx.event.ActionEvent;

public class HomescreenController {
    private Object NutzerInstanz;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzerInstanz(layout.getNutzer());
    }

    public Object getNutzerInstanz() {
        return NutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        NutzerInstanz = nutzerInstanz;
    }
    
}
