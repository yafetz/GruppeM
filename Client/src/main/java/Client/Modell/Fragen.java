package Client.Modell;

import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.util.ArrayList;
import java.util.Collections;

public class Fragen {
    private String frage;
    private ArrayList<String> antwortmoeglichkeit;
    private String antwort;

    public Fragen (String frage, String[] antwortmoeglichkeit, String antwort) {
        this.frage = frage;
        this.antwort = antwort;
        this.antwortmoeglichkeit = new ArrayList<String>();
        for (int i=0; i< antwortmoeglichkeit.length; i++ ) {
            this.antwortmoeglichkeit.add(antwortmoeglichkeit[i]);


        }



    }

    public String getFrage() {
        return frage;
    }

    public String getAntwort() {
        return antwort;
    }
}
