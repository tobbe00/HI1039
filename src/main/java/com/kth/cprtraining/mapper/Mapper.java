package com.kth.cprtraining.mapper;

public interface Mapper<A, B>  {
    B mapToDTO(A a);
    A mapToEntity(B b);
}
