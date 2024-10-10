package util;

import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.document.ExpenseDocument;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.dto.ExpenseCreate;

import java.time.LocalDateTime;

public class TestDataUtil {

    public static ExpenseDocument createTestExpenseDocumentA() {
        return ExpenseDocument.builder()
                .id("Expense Test ID A")
                .category(createTestExpenseCategoryDocumentA())
                .description("Lunch")
                .amount(23.59f)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ExpenseDocument createTestExpenseDocumentB() {
        return ExpenseDocument.builder()
                .id("Expense Test ID B")
                .category(createTestExpenseCategoryDocumentB())
                .description("Hotel")
                .amount(64.78f)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ExpenseCreate createTestExpenseCreate() {
        return ExpenseCreate.builder()
                .categoryId("House")
                .description("Rent")
                .amount(700f)
                .build();
    }

    public static ExpenseCategoryDocument createTestExpenseCategoryDocumentA() {
        return ExpenseCategoryDocument.builder()
                .id("Category Test ID A")
                .name("Food")
                .description("Food related expenses.")
                .isDefault(true)
                .build();
    }

    public static ExpenseCategoryDocument createTestExpenseCategoryDocumentB() {
        return ExpenseCategoryDocument.builder()
                .id("Category Test ID B")
                .name("Travel")
                .description("Travel related expenses.")
                .isDefault(false)
                .build();
    }

    public static ExpenseCategoryCreate createTestExpenseCategoryCreate() {
        return ExpenseCategoryCreate.builder()
                .name("House")
                .description("House related expenses.")
                .build();
    }

    public static ExpenseCategory createTestExpenseCategory() {
        return ExpenseCategory.builder()
                .id("Test ID 789")
                .name("Pets")
                .description("Pets related expenses.")
                .isDefault(false)
                .build();
    }
}
