package mx.com.caminandog;

public class Elemento_Agenda {


    private String mName;
    private long timest;


    // Constructor that is used to create an instance of the Movie object
    /*public Elemento_Agenda(String mName) {
        this.mName = mName;
    }*/

    public Elemento_Agenda(String mName, long timest) {
        this.mName = mName;
        this.timest = timest;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getTimest() {
        return timest;
    }

    public void setTimest(long timest) {
        this.timest = timest;
    }
}
