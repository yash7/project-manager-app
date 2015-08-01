package ateamcomp354.projectmanagerapp.testing;

import ateamcomp354.projectmanagerapp.model.Pojos;
import org.junit.Test;

import java.util.Calendar;

import static ateamcomp354.projectmanagerapp.testing.util.PojoMaker.makeDate;
import static org.junit.Assert.assertEquals;

public class PojosTest {

    @Test
    public void testDateToInt_1digitMonth() {

        Calendar date = makeDate(1992, 8, 22);

        int i = Pojos.dateToInt( date );

        assertEquals( 19920822, i );
    }

    @Test
    public void testDateToInt_1digitDay() {

        Calendar date = makeDate(2015, 12, 1);

        int i = Pojos.dateToInt( date );

        assertEquals( 20151201, i );
    }

    @Test
    public void testDateToInt_1digitMonthAndDay() {

        Calendar date = makeDate(2015, 1, 1);

        int i = Pojos.dateToInt( date );

        assertEquals( 20150101, i );
    }

    @Test
    public void testDateToInt_2digitMonthAndDay() {

        Calendar date = makeDate(1992, 12, 19);

        int i = Pojos.dateToInt( date );

        assertEquals( 19921219, i );
    }

    @Test
    public void testIntToDate_1digitMonth() {

        int i = 19940623;

        Calendar date = Pojos.intToDate(i);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);;
        int day = date.get(Calendar.DAY_OF_MONTH);

        assertEquals(1994, year);
        assertEquals(6 - 1, month);
        assertEquals(23, day);
    }

    @Test
    public void testIntToDate_1digitDay() {

        int i = 20151201;

        Calendar date = Pojos.intToDate(i);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);;
        int day = date.get(Calendar.DAY_OF_MONTH);

        assertEquals(2015, year);
        assertEquals(12 - 1, month);
        assertEquals(1, day);
    }

    @Test
    public void testIntToDate_1digitMonthAndDay() {

        int i = 20150101;

        Calendar date = Pojos.intToDate(i);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);;
        int day = date.get(Calendar.DAY_OF_MONTH);

        assertEquals(2015, year);
        assertEquals(1 - 1, month);
        assertEquals(1, day);
    }

    @Test
    public void testIntToDate_2digitMonthAndDay() {

        int i = 19921219;

        Calendar date = Pojos.intToDate(i);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);;
        int day = date.get(Calendar.DAY_OF_MONTH);

        assertEquals(1992, year);
        assertEquals(12 - 1, month);
        assertEquals(19, day);
    }
}
