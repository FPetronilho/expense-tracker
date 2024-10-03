package com.portfolio.expensetracker.mapper;

import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.document.ExpenseDocument;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UUID.class}
)
public interface ExpenseMapperDataProvider {

    @Mapping(target = "date", source = "createdAt")
    Expense toExpense(ExpenseDocument expenseDocument);

    List<Expense> toExpenseList(List<ExpenseDocument> expenseDocuments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dbId", ignore = true)
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "resolveCategoryId")
    void updateExpenseDocument(
            @MappingTarget ExpenseDocument expenseDocument,
            ExpenseUpdate expenseUpdate,
            @Context ExpenseCategoryDataProvider expenseCategoryDataProvider
    );

    @Named("resolveCategoryId")
    default ExpenseCategory resolveCategoryId(
            String categoryId,
            @Context ExpenseCategoryDataProvider expenseCategoryDataProvider
    ) {
        return categoryId != null ? expenseCategoryDataProvider.findById(categoryId) : null;
    }

    @Mapping(target = "dbId", ignore = true)
    ExpenseCategoryDocument toExpenseCategoryDocument(ExpenseCategory expenseCategory);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "dbId", ignore = true)
    ExpenseDocument toExpenseDocument(ExpenseCreate expenseCreate);
}
