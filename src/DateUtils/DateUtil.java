package DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

//发送的时候获得时间
public class DateUtil {
    public static String getnow(){
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return time.format(nowTime);
    }

}
