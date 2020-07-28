import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.TimeZone;

public class CorgiTest {
    public static void main(String[] args) {
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            System.out.println(sdf.parse("2020-07-28 12:50:59").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
