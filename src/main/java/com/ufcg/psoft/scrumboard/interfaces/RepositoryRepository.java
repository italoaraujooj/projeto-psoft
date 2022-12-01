package com.ufcg.psoft.scrumboard.interfaces;

import java.util.Collection;

public interface RepositoryRepository<S,T>{

    String create(S object1, T object2);
    String change(S object1, T object2);
    String delete(S object);
    T findOne(S object);
    Collection<T> listAll();

}
