package builder;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
            .where("id = ?", 1);

        assertThat(b.getSql(), is("select a from t where id = ?"));
        assertThat(b.getParameters(), contains(1));

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

        assertThat(b.getSql(), is("select a from t where a = ? and c = ?"));
        assertThat(b.getParameters(), is(Arrays.asList(1, 3)));
    }

    @Test
    public void supportsInQueries() {
        SqlBuilder b = new SqlBuilder()
            .selectColumn("a")
            .from("t")
            .in("id", List.of(1, 2));

        assertThat(b.getSql(), is("select a from t where id in (?, ?)"));
        assertThat(b.getParameters(), contains(1, 2));
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
}

