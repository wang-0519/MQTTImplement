package helperClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Created by admin on 2020/3/25.
 */
public class TimeGetter {
    static private SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static public String getTime(){
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
