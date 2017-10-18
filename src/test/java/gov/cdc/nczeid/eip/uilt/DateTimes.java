package gov.cdc.nczeid.eip.uilt;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DateTimes {

    @Test
    public void testDateISO8601() {
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    }

    @Test
    public void testGUID () {
        System.out.println(UUID.randomUUID());
    }
}
