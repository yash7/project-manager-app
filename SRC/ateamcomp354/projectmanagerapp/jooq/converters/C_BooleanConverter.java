package ateamcomp354.projectmanagerapp.jooq.converters;

import org.jooq.Converter;

/**
 * For converting columns that are meant to be flags.
 * Null or zero is false, any other value is true.
 *
 * See jooq-code.xml for its usage.
 */
public class C_BooleanConverter implements Converter<Integer,Boolean> {

    @Override
    public Boolean from(Integer databaseObject) {
        return databaseObject == null || databaseObject == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Integer to(Boolean userObject) {
        return Boolean.TRUE.equals( userObject ) ? 1 : 0;
    }

    @Override
    public Class<Integer> fromType() {
        return Integer.class;
    }

    @Override
    public Class<Boolean> toType() {
        return Boolean.class;
    }
}
