package com.portfolio.expensetracker.mapper;

import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExpenseCategoryMapperDataProvider {

    ExpenseCategory toExpenseCategory(ExpenseCategoryDocument expenseCategoryDocument);

    List<ExpenseCategory> toExpenseCategoryList(List<ExpenseCategoryDocument> expenseCategoryDocumentList);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "dbId", ignore = true)
    @Mapping(target = "isDefault", ignore = true)
    ExpenseCategoryDocument toExpenseCategoryDocument(ExpenseCategoryCreate expenseCategoryCreate);

    @Mapping(target = "dbId", ignore = true)
    ExpenseCategoryDocument toExpenseCategoryDocument(ExpenseCategory expenseCategory);
}
