package com.googlecode.totallylazy.functions;

public class And implements CurriedMonoid<Boolean> {
    @Override
    public Boolean call(Boolean a, Boolean b) throws Exception {
        return a && b;
    }

    @Override
    public Boolean identity() {
        return true;
    }
}