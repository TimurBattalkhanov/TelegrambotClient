package kz.timur;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Currency {

    public static String SetCurrency(double currency, long timestamp){

        long newTimestamp = timestamp*1000;
        Date date = new Date(newTimestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+6"));
        String formattedDate = sdf.format(date);

        String formattedCurrency = String.format("%.2f", currency);
        return formattedCurrency + " по состоянию на " + formattedDate;
    }
}