package builder;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class SqlBuilder {

    private final List<String> columns = new ArrayList<>();
    private final List<String> whereConditions = new ArrayList<>();
    private final List<JoinedTable> joinedTables = new ArrayList<>();
    private final Map<String, Object> parameters = new HashMap<>();

    private String table;
    private Timestamp queryTime;
    private int labelCounter = 1;

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

    public SqlBuilder where(String condition, Parameter param) {
        whereConditions.add(condition);
        parameters.put(param.name, param.value);
        return this;
    }

    public SqlBuilder where(String condition) {
        whereConditions.add(condition);
        return this;
    }

    public SqlBuilder withQueryTime(LocalDateTime time) {
        queryTime = Timestamp.valueOf(time);
        return this;
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public SqlBuilder eqIfNotNull(String column, Object parameter) {
        if (parameter != null) {
            String label = nextLabel();
            whereConditions.add(MessageFormat.format("{0} = :{1}", column, label));
            parameters.put(label, parameter);
        }
        return this;
    }

    public SqlBuilder leftJoin(String tableName, String condition) {
        joinedTables.add(new JoinedTable(tableName, condition));
        return this;
    }

    public SqlBuilder in(String column, List<Object> parameters) {
        if (parameters.isEmpty()) {
            return this;
        }

        List<String> labels = new ArrayList<>();
        for (Object param : parameters) {
            String label = nextLabel();
            labels.add(":" + label);
            this.parameters.put(label, param);
        }

        whereConditions.add(MessageFormat.format("{0} in ({1})",
                column, String.join(", ", labels)));

        return this;
    }

    public SqlBuilder from(SqlBuilder sub) {
        table = MessageFormat.format("({0})", sub.getSql());
        return this;
    }

    public String getSql() {
        String query = String.format("select %s from %s",
                String.join(", ", columns), table);

        ArrayList<String> joinClauses = getJoinClauses();

        if (!joinClauses.isEmpty()) {
            query += " " + String.join(" ", joinClauses);
        }

        ArrayList<String> whereConditions = new ArrayList<>(this.whereConditions);
        if (queryTime != null) {
            whereConditions.add(String.format("%s.start_date <= :qt", table));
            whereConditions.add(String.format("(%s.end_date > :qt or %s.end_date IS NULL)",
                    table, table));
        }

        if (!whereConditions.isEmpty()) {
            query += " where " + String.join(" and ", whereConditions);
        }

        if (queryTime != null) {
            parameters.put("qt", queryTime);
        }

        return query;
    }

    private ArrayList<String> getJoinClauses() {
        ArrayList<String> joinClauses = new ArrayList<>();
        for (JoinedTable each : joinedTables) {
            joinClauses.add(String.format("left join %s on %s",
                    each.name, each.condition));

            if (queryTime != null) {
                String timeCondition = String.format(
                        "and %s.start_date <= :qt and (%s.end_date > :qt or %s.end_date IS NULL)",
                        each.name, each.name, each.name);
                joinClauses.add(timeCondition);
            }
        }
        return joinClauses;
    }

    private String nextLabel() {
        return "p" + labelCounter++;
    }

    public static Parameter param(String name, Object value) {
        return new Parameter(name, value);
    }

    public record Parameter(String name, Object value) {}

    public record JoinedTable(String name, String condition) {}

}