package com.splot.atm.service.mapper;

public interface ResponseDtoMapper<T, K> {
    T mapToDto(K k);
}
