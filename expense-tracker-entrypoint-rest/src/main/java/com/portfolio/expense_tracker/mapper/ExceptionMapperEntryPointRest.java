package com.portfolio.expense_tracker.mapper;

import com.portfolio.expense_tracker.exception.BusinessException;
import com.portfolio.expense_tracker.exception.ExceptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExceptionMapperEntryPointRest {

    ExceptionDto toExceptionDto(BusinessException e);
}
