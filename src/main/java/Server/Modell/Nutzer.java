package Server.Modell;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Nutzer")
public class Nutzer {

    @DatabaseField(id = true)
    private int ID;
}
