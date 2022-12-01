package com.ufcg.psoft.scrumboard.interfaces;

import java.util.Collection;

public interface Crud<T, S> {

    String create(S object);
    String change(T id, S object);
    String delete(S object);
    Collection<S> listAll();

}
