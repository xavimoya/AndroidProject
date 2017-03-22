package xavi.smartalarm;

import java.util.Calendar;

/**
 * Created by Reylin on 14/03/2017.
 */

public class Alarm {

    public Calendar date;
    public String title;
    public String destiny;

    public Alarm(Calendar date, String title, String destiny){
        this.date = date;
        this.title = title;
        this.destiny = destiny;
    }

    /** Setters and Getters */
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

}
