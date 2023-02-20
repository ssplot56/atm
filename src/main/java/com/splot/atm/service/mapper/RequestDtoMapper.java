package com.splot.atm.service.mapper;

public interface RequestDtoMapper <T, K> {
    K mapToModel(T t);
}
