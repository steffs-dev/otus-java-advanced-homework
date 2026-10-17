package lesson_26_di_ioc.services;

import lesson_26_di_ioc.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
