package config;

import java.util.StringTokenizer;

public class ValidateServerProperties {
    public static boolean validateIpAddress(String ipAddress) {
        boolean b1 = false;
        StringTokenizer t = new StringTokenizer(ipAddress, ".");
        int a = Integer.parseInt(t.nextToken());
        int b = Integer.parseInt(t.nextToken());
        int c = Integer.parseInt(t.nextToken());
        int d = Integer.parseInt(t.nextToken());
        if ((a >= 0 && a <= 255) && (b >= 0 && b <= 255)
                && (c >= 0 && c <= 255) && (d >= 0 && d <= 255))
            b1 = true;
        return b1;
    }

    public static boolean isValidPort(int portNumber) {
        return portNumber >= 0 && portNumber <= 65535;
    }
}
