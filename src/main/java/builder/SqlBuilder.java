package builder;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class SqlBuilder {

    private final List<String> columns = new ArrayList<>();
    private final List<String> whereConditions = new ArrayList<>();
    private final List<String> joinedTables = new ArrayList<>();
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

    public SqlBuilder leftJoin(String table, String condition) {
        joinedTables.add(MessageFormat.format("left join {0} on {1}", table, condition));
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
        String query = MessageFormat.format("select {0} from {1}",
                String.join(", ", columns), table);

        if (!joinedTables.isEmpty()) {
            String condition = queryTime != null
                ? " and start_date <= :qt and (end_date > :qt or end_date IS NULL)"
                : "";

            query += " " + joinedTables.stream()
                    .map(each -> each + condition)
                    .collect(Collectors.joining(" "));
        }

        ArrayList<String> whereConditions = new ArrayList<>(this.whereConditions);
        if (queryTime != null) {
            whereConditions.add("start_date <= :qt");
            whereConditions.add("(end_date > :qt or end_date IS NULL)");
        }

        if (!whereConditions.isEmpty()) {
            query += " where " + String.join(" and ", whereConditions);
        }

        if (queryTime != null) {
            parameters.put("qt", queryTime);
        }

        return query;
    }

    private String nextLabel() {
        return "p" + labelCounter++;
    }

    public static Parameter param(String name, Object value) {
        return new Parameter(name, value);
    }

    public record Parameter(String name, Object value) {}

}