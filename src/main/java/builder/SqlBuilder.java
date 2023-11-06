package builder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlBuilder {

    private List<String> columns = new ArrayList<>();
    private List<String> whereConditions = new ArrayList<>();
    private List<String> joinedTables = new ArrayList<>();
    private String table;
    private List<Object> parameters = new ArrayList<>();

    public SqlBuilder selectColumn(String column) {
        columns.add(column);
        return this;
    }

    public SqlBuilder from(String table) {
        this.table = table;
        return this;
    }

    public SqlBuilder selectColumns(String ... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public SqlBuilder where(String condition, Object parameter) {
        whereConditions.add(condition);
        parameters.add(parameter);
        return this;
    }

    public SqlBuilder where(String condition) {
        whereConditions.add(condition);
        return this;
    }

    public List<Object> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public SqlBuilder eqIfNotNull(String column, Object parameter) {
        if (parameter != null) {
            whereConditions.add(MessageFormat.format("{0} = ?", column));
            parameters.add(parameter);
        }
        return this;
    }

    public SqlBuilder leftJoin(String table, String condition) {
        joinedTables.add(MessageFormat.format(" left join {0} on {1}", table, condition));
        return this;
    }

    public SqlBuilder in(String column, List<Object> parameters) {
        if (parameters.isEmpty()) {
            return this;
        }

        String questionMarks = Arrays.stream(new Integer[parameters.size()])
                .map(each -> "?")
                .collect(Collectors.joining(", "));

        whereConditions.add(MessageFormat.format("{0} in ({1})", column, questionMarks));
        this.parameters.addAll(parameters);

        return this;
    }

    public SqlBuilder from(SqlBuilder sub) {
        table = MessageFormat.format("({0})", sub.getSql());
        return this;
    }

    public String getSql() {
        String query = MessageFormat.format("select {0} from {1}",
                String.join(", ", columns), table);

        if (!whereConditions.isEmpty()) {
            query += " where " + String.join(" and ", whereConditions);
        }

        if (!joinedTables.isEmpty()) {
            query += String.join(" ", joinedTables);
        }

        return query;
    }

}