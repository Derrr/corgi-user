import com.corgi.support.UserQuerySupporter;
import com.corgi.user.entity.UserQuery;

import java.util.Base64;

public class CorgiTest {
    public static void main(String[] args) {
        System.out.println(new String(Base64.getDecoder().decode("eyJpc3MiOiJXbnI2UFFPNCIsImlhdCI6MTU4Nzg3ODM5NywiZXhwIjoxNTg3ODg1NTk3LCJhdWQiOiJnbzUzOG1yZWp4Iiwib3Blbl9pZCI6InJ6cXllbzF2ZDJvcGR3djgiLCJ1c2VyX2lkIjoiZzE1ODI2MjE0MTRhZDExZWE5YmRjNTdmZWY4Y2E0MjEiLCJuYW1lIjoiXHU3MzhiXHU5ODk2XHU2Mzc3IiwiYXZhdGFyIjoiaHR0cHM6Ly91c2h1Lm9zcy1jbi1iZWlqaW5nLmFsaXl1bmNzLmNvbS9XbnI2UFFPNC9hdmF0YXIvLzNkNzExZGU0NzU5OTY1ODZmNzg4ZWNhYTM2YmFjY2FlLmpwZyIsInZlcnNpb24iOiJ2MSJ9")));
    }

}
