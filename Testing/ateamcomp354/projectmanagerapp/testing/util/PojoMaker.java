package ateamcomp354.projectmanagerapp.testing.util;

import ateamcomp354.projectmanagerapp.model.Pojos;
import ateamcomp354.projectmanagerapp.model.Status;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.util.Calendar;

/**
 * Functions to help make objects more easily,
 *
 * suggested to static import * this class for even more ease.
 */
public class PojoMaker {

    public static int activityIdSeq = 0;

    /**
     * Util to make a Calendar object
     *
     * @param year YYYY
     * @param month 1-based, 1 = January and 12 = December
     * @param day 1-based day of the month
     * @return
     */
    public static Calendar makeDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        c.set(year, month - 1, day);
        return c;
    }

    public static Project makeProject(int id, String name) {
        return new Project(id, name, "", false, 10000, 10000);
    }

    public static Activity makeActivity(int projectId, String name, int cost) {
        Activity a = new Activity();
        a.setId( activityIdSeq++ );
        a.setProjectId( projectId );
        a.setLabel( name );
        a.setDescription("");
        a.setStatus(Status.NEW);
        a.setPlannedValue(0);
        a.setActualCost(cost);
        return a;
    }

    public static Activity makeActivity(int projectId, String name, Calendar start, Calendar end) {
        Activity a = new Activity();
        a.setId( activityIdSeq++ );
        a.setProjectId( projectId );
        a.setLabel( name );
        a.setEarliestStart(Pojos.dateToInt( start ) );
        a.setEarliestFinish(Pojos.dateToInt( end ) );
        a.setDescription("");
        a.setStatus(Status.NEW);
        a.setPlannedValue(0);
        return a;
    }
}
