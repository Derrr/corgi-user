import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserQuery;

import java.util.Base64;

public class CorgiTest {
    public static void main(String[] args) {
        System.out.println(new String(Base64.getDecoder().decode("eyJleHAiOjE1OTMxNzk1NDUsInVzZXJJZCI6IjQiLCJpYXQiOjE1OTMxNzIzNDV9")));
        System.out.println("%".replaceAll("%","\\\\%"));
    }

}
