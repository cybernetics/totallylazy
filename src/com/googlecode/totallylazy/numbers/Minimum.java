package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Identity;

public class Minimum extends Function2<Number, Number, Number> implements Identity<Number> {
    @Override
    public Number call(Number a, Number b) throws Exception {
        return Numbers.compare(a, b) > 0 ? b : a;
    }

    @Override
    public Number identity() {
        return Numbers.POSITIVE_INFINITY;
    }
}
