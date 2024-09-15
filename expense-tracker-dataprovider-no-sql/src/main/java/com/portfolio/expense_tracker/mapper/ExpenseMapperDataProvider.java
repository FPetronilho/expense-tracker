package com.portfolio.expense_tracker.mapper;

import com.portfolio.expense_tracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expense_tracker.document.ExpenseDocument;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
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
    @Mapping(target = "category", source = "categoryName", qualifiedByName = "resolveCategoryName")
    void updateExpenseDocument(
            @MappingTarget ExpenseDocument expenseDocument,
            ExpenseUpdate expenseUpdate,
            @Context ExpenseCategoryDataProvider expenseCategoryDataProvider
    );

    @Named("resolveCategoryName")
    default ExpenseCategory resolveCategoryName(
            String categoryName,
            @Context ExpenseCategoryDataProvider expenseCategoryDataProvider
    ) {
        return categoryName != null ? expenseCategoryDataProvider.findByName(categoryName) : null;
    }

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "dbId", ignore = true)
    ExpenseDocument toExpenseDocument(ExpenseCreate expenseCreate);
}
