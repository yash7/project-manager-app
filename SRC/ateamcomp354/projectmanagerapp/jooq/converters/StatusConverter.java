package ateamcomp354.projectmanagerapp.jooq.converters;

import ateamcomp354.projectmanagerapp.model.Status;
import org.jooq.impl.EnumConverter;

/**
 * For converting Activity.status column to Status enum when JOOQ generates code.
 *
 * See jooq-code.xml for its usage.
 */
public class StatusConverter extends EnumConverter<Integer,Status> {

    public StatusConverter() {
        super(Integer.class, Status.class);
    }
}
