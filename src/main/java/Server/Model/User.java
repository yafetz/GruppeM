package Server.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField
    private int id;
    @DatabaseField
    private int matrikelnummer;
    @DatabaseField
    private String password;

    public User() {
        // ORMLite needs a no-arg constructor
    }
    public User(int matrikelnummer, String password) {
        this.matrikelnummer = matrikelnummer;
        this.password = password;
    }
    public int getMatrikelnummer() {
        return matrikelnummer;
    }
    public void setMatrikelnummer(int name) {
        this.matrikelnummer = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
