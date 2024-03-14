package aimeter.arion.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeFormatConstants {

    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

}
