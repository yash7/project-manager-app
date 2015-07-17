package ateamcomp354.projectmanagerapp.jooq.generator;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;
import org.jooq.util.TableDefinition;

/**
 * Name generated objects that extend Table with a prefix.
 * Otherwise pojos will have the same names as the objects that extend Table.
 *
 * see jooq-code.xml for usage
 *
 * see jooq's site for more information.
 * http://www.jooq.org/doc/3.6/manual/code-generation/codegen-generatorstrategy/
 */
public class GeneratorNamingStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {

        // Usually if mode is default then we are generating an object that extends table.
        // Dao generation calls this with ColumnDefinition instance for its fetchByY methods
        // where Y is the column of a table. In those cases we don't want the suffix.

        String suffix = mode == Mode.DEFAULT && definition instanceof TableDefinition ? "Table" : "";

        return super.getJavaClassName(definition, mode) + suffix;
    }
}
