package com.googlecode.totallylazy.validations;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.ReducerCombiner;

import static com.googlecode.totallylazy.validations.PredicateValidator.constructors.validatePredicate;
import static com.googlecode.totallylazy.validations.ValidationResult.constructors.pass;

public interface Validator<T> extends Predicate<T> {
    ValidationResult validate(T instance);

    class functions {
        public static <T> Function<Validator<? super T>, ValidationResult> validateAgainst(final T value) {
            return new Function<Validator<? super T>, ValidationResult>() {
                @Override
                public ValidationResult call(Validator<? super T> validator) throws Exception {
                    return validator.validate(value);
                }
            };
        }

        public static <T> ReducerCombiner<T, ValidationResult> validateWith(final Validator<? super T> validator) {
            return new ReducerCombiner<T, ValidationResult>() {
                @Override
                public ValidationResult call(ValidationResult validationResult, T instance) throws Exception {
                    return validationResult.merge(validator.validate(instance));
                }

                @Override
                public ValidationResult identityElement() {
                    return pass();
                }

                @Override
                public ValidationResult combine(ValidationResult a, ValidationResult b) throws Exception {
                    return a.merge(b);
                }
            };
        }

        public static <T> Function<Validator<T>, Validator<T>> setFailureMessage(final String message) {
            return new Function<Validator<T>, Validator<T>>() {
                @Override
                public Validator<T> call(Validator<T> validator) throws Exception {
                    return validatePredicate(validator, message);
                }
            };
        }

        public static <T> Function<Validator<T>, Validator<T>> setFailureMessage(final Function<T, String> message) {
            return new Function<Validator<T>, Validator<T>>() {
                @Override
                public Validator<T> call(Validator<T> validator) throws Exception {
                    return validatePredicate(validator, message);
                }
            };
        }
    }
}
