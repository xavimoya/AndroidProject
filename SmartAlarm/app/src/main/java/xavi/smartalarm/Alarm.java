package xavi.smartalarm;

import java.util.Calendar;

/*
 * Created by Xavi and Reylin on 14/03/2017.
 */

class Alarm {

    private Calendar date;
    private String title;
    private String destiny;
    private int hashcode;

    Alarm(Calendar date, String title, String destiny){
        this.date = date;
        this.title = title;
        this.destiny = destiny;
        this.hashcode = this.hashCode();
    }

    /** Setters and Getters */
    Calendar getDate() {
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

    public void setHour(int hour){


        Calendar cal = Calendar.getInstance();
        cal.setTime(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        this.date.setTime(cal.getTime());
    }

    public int getHour(){
         return date.get(Calendar.HOUR_OF_DAY);
    }

    public void setMinute(int minute){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date.getTime());
        cal.set(Calendar.MINUTE, minute);
        this.date.setTime(cal.getTime());

    }

    public int getMinute(){
        return date.get(Calendar.MINUTE);
    }

    public int getHashCode(){
        return hashcode;
    }

    public void setHashcode(int code){
        this.hashcode = code;
    }


}
