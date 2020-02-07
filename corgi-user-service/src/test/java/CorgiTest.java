import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserQuery;

public class CorgiTest {
    public static void main(String[] args) {
        UserQuery userQuery = new UserQuery();
        userQuery.setRange(100.0);
        userQuery.setLng(121.4539450000);
        userQuery.setLat(31.4220410000);
        UserQuerySupporter supporter = new UserQuerySupporter(userQuery);
        System.out.println(supporter);

        System.out.println(Math.cos(Math.toRadians(userQuery.getLat())));
    }

}
