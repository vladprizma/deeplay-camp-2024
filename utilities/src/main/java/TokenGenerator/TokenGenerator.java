package TokenGenerator;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int generateID() {
        return counter.incrementAndGet();
    }
}
