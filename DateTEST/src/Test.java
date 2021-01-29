import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String date1 = format.format(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24"));
        String date2 = format.format(new Date());

        java.sql.Date sqlDate1 = java.sql.Date.valueOf(date1);
        java.sql.Date sqlDate2 = java.sql.Date.valueOf(date2);
        LocalDate ld1 = sqlDate1.toLocalDate();
        LocalDate ld2 = sqlDate2.toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(ld1, ld2);
        System.out.println(daysBetween);
    }
}
