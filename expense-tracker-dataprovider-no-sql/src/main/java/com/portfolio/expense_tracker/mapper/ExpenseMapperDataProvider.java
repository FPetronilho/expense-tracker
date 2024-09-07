package com.portfolio.expense_tracker.mapper;

import com.portfolio.expense_tracker.document.ExpenseCategoryDocument;
import com.portfolio.expense_tracker.document.ExpenseDocument;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.util.ExpenseConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {UUID.class}
)
public interface ExpenseMapperDataProvider {

    @Mapping(target = "date", source = ExpenseConstants.CREATED_AT_DB_FIELD)
    Expense toExpense(ExpenseDocument expenseDocument);

    List<Expense> toExpenseList(List<ExpenseDocument> expenseDocuments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dbId", ignore = true)
    void updateExpenseDocument(
            @MappingTarget ExpenseDocument expenseDocument,
            ExpenseUpdate expenseUpdate
    );

    @Mapping(
            target = "id",
            expression = "java(java.util.UUID.randomUUID().toString())"
    )
    @Mapping(target = "dbId", ignore = true)
    ExpenseDocument toExpenseDocument(ExpenseCreate expenseCreate);

    ExpenseCategory toExpenseCategory(ExpenseCategoryDocument expenseCategoryDocument);

    List<ExpenseCategory> toExpenseCategoryList(List<ExpenseCategoryDocument> expenseCategoryDocuments);

    @Mapping(target = "dbId", ignore = true)
    ExpenseCategoryDocument toExpenseCategoryDocument(ExpenseCategory expenseCategory);

    List<ExpenseCategoryDocument> toExpenseCategoryDocumentList(List<ExpenseCategory> expenseCategories);
}
