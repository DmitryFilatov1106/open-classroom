package ru.fildv.openclassroomdb.mapper;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
