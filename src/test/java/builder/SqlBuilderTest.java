package builder;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static builder.SqlBuilder.param;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class SqlBuilderTest {

    @Test
    public void singleColumnFromSingleTable() {
        SqlBuilder b = new SqlBuilder()
                .selectColumn("a")
                .from("t");

        assertThat(b.getSql(), is("select a from t"));
    }

    @Test
    public void singleMultipleColumnsFromSingleTable() {
        SqlBuilder b = new SqlBuilder()
                .selectColumns("a", "b")
                .from("t");

        assertThat(b.getSql(), is("select a, b from t"));
    }

    @Test
    public void whereConditionsTakeParameters() {
        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from("t")
            .where("id = :id", param("id", 1));

        assertThat(b.getSql(), is("select a from t where id = :id"));
        assertThat(b.getParameters().get("id"), is(1));

    }

    @Test
    public void whereConditionsAreJoinedWithAnd() {
        SqlBuilder b = new SqlBuilder()
                .selectColumn("a")
                .from("t")
                .where("is_hidden = 1")
                .where("deleted_on is null");

        assertThat(b.getSql(), is("select a from t where is_hidden = 1 and deleted_on is null"));
    }

    @Test
    public void whereConditionIsAddedWhenValueIsNotNull() {
        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from("t")
            .eqIfNotNull("a", 1)
            .eqIfNotNull("b", null)
            .eqIfNotNull("c", 3);

        assertThat(b.getSql(), is("select a from t where a = :p1 and c = :p2"));
        assertThat(b.getParameters().get("p1"), is(1));
        assertThat(b.getParameters().get("p2"), is(3));
    }

    @Test
    public void supportsInQueries() {
        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from("t")
            .in("id", List.of(1, 2));

        assertThat(b.getSql(), is("select a from t where id in (:p1, :p2)"));
        assertThat(b.getParameters().get("p1"), is(1));
        assertThat(b.getParameters().get("p2"), is(2));
    }

    @Test
    public void supportsLeftJoins() {
        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from("t")
            .leftJoin("u", "u.id = t.u_id");

        assertThat(b.getSql(), is("select a from t left join u on u.id = t.u_id"));
    }

    @Test
    public void supportsSubQueries() {
        SqlBuilder sub = new SqlBuilder()
            .selectColumn("b")
            .from("t");

        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from(sub);

        assertThat(b.getSql(), is("select a from (select b from t)"));
    }

    @Test
    public void supportsHistoryQueries() {
        SqlBuilder b = new SqlBuilder()
                .selectColumn("a")
                .from("t")
                .withQueryTime(LocalDateTime.now());

        String expected = "select a from t " +
                "where t.start_date <= :qt " +
                "and (t.end_date > :qt or t.end_date IS NULL)";

        assertThat(b.getSql(), is(expected));
        assertThat(b.getParameters().get("qt"), is(notNullValue()));
    }

    @Test
    public void supportsHistoryQueriesInJoins() {
        SqlBuilder b = new SqlBuilder()
                .selectColumn("a")
                .from("t")
                .leftJoin("u", "u.id = t.u_id")
                .withQueryTime(LocalDateTime.now());

        String expected = "select a from t left " +
                "join u on u.id = t.u_id and u.start_date <= :qt and (u.end_date > :qt or u.end_date IS NULL) " +
                "where t.start_date <= :qt and (t.end_date > :qt or t.end_date IS NULL)";

        assertThat(b.getSql(), is(expected));
        assertThat(b.getParameters().get("qt"), is(notNullValue()));
    }
}

