package Client.Controller;





public class Lernkarten{

        private String vorderseite;
        private String rueckseite;

        public Lernkarten (String vorderseite, String rueckseite){
            this.vorderseite = vorderseite;
            this.rueckseite = rueckseite;

        }

        public String getVorderseite(){
            return vorderseite;
        }

        public String getRueckseite(){
            return rueckseite;
        }
    }
}

