package builder;

import java.time.LocalDateTime;
import java.util.*;


public class SqlBuilder {

    private final List<String> columns = new ArrayList<>();
    private final List<String> whereConditions = new ArrayList<>();
    private final List<JoinedTable> joinedTables = new ArrayList<>();
    private final Map<String, Object> parameters = new HashMap<>();

    private String table;
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
        throw new RuntimeException("not implemented yet");
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public SqlBuilder eqIfNotNull(String column, Object parameter) {
        if (parameter != null) {
            String label = nextLabel();
            whereConditions.add(String.format("%s = :%s", column, label));
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

        whereConditions.add(String.format("%s in (%s)",
                column, String.join(", ", labels)));

        return this;
    }

    public SqlBuilder from(SqlBuilder sub) {
        table = String.format("(%s)", sub.getSql());
        return this;
    }

    public String getSql() {
        String query = String.format("select %s from %s",
                String.join(", ", columns), table);

        ArrayList<String> joinClauses = getJoinClauses();

        if (!joinClauses.isEmpty()) {
            query += " " + String.join(" ", joinClauses);
        }

        if (!whereConditions.isEmpty()) {
            query += " where " + String.join(" and ", whereConditions);
        }

        return query;
    }

    private ArrayList<String> getJoinClauses() {
        ArrayList<String> joinClauses = new ArrayList<>();
        for (JoinedTable each : joinedTables) {
            joinClauses.add(String.format("left join %s on %s",
                    each.name, each.condition));
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