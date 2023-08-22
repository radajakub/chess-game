package pjv.sp.chess.model;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;

/**
 * Datum class gets current datum of the game and provides methods to get it.
 * @author Jakub Rada
 * @version 1.0
 */
public class Datum {

    private final int day;

    private final int month;

    private final int year;

    /**
     * Creates new Date object and obtains current date
     */
    public Datum() {
        Date date = new Date();
        LocalDate local = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.day = local.getDayOfMonth();
        this.month = local.getMonthValue();
        this.year = local.getYear();
    }

    /**
     * Gets day when the game was played
     * @return value of the day property
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Gets month when the game was played
     * @return value of the month property
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Gets year when the game was played
     * @return value of the year property
     */
    public int getYear() {
        return this.year;
    }
}
