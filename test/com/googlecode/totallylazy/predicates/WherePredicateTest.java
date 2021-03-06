package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.functions.Callables.toString;
import static com.googlecode.totallylazy.predicates.Predicates.always;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.never;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class WherePredicateTest {
    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(where(toString, is("13")).equals(where(toString, is("13"))), Matchers.is(true));
        assertThat(where(toString, is("13")).equals(where(toString, is("14"))), Matchers.is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(where(toString, is("13")).toString(), Matchers.is("where toString is 13"));
    }

    @Test
    public void combiningAWhereWithAlwaysReturnsAnAlways() throws Exception {
        assertThat(where(toString, always()), instanceOf(always().getClass()));
    }

    @Test
    public void combiningAWhereWithNeverReturnsANever() throws Exception {
        assertThat(where(toString, never()), instanceOf(never().getClass()));
    }

    @Test
    public void supportsDeMorganLawByMovingNotToOutside() throws Exception {
        assertThat(where(toString, not(is("Dan"))), Matchers.is(not(where(toString, is("Dan")))));
    }
}
