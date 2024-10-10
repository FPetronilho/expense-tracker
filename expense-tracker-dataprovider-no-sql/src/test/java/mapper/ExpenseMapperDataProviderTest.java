package mapper;

import com.portfolio.expensetracker.document.ExpenseDocument;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.mapper.ExpenseMapperDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import util.TestDataUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseMapperDataProviderTest {

    private ExpenseMapperDataProvider mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(ExpenseMapperDataProvider.class);
    }

    @Test
    void testToExpense() {
        // Arrange
        ExpenseDocument expenseDocument = TestDataUtil.createTestExpenseDocumentA();

        // Act
        Expense expense = mapper.toExpense(expenseDocument);

        // Assert
        assertNotNull(expense);
        assertEquals("Expense Test ID A", expense.getId());
        assertEquals("Category Test ID A", expense.getCategory().getId());
        assertEquals("Food", expense.getCategory().getName());
        assertEquals("Food related expenses.", expense.getCategory().getDescription());
        assertTrue(expense.getCategory().getIsDefault());
        assertEquals("Lunch", expense.getDescription());
        assertEquals(23.59f, expense.getAmount());
        assertNotNull(expense.getDate());
    }

    @Test
    void testToExpenseList() {
        // Arrange
        ExpenseDocument expenseDocumentA = TestDataUtil.createTestExpenseDocumentA();
        ExpenseDocument expenseDocumentB = TestDataUtil.createTestExpenseDocumentB();
        List<ExpenseDocument> expenseDocuments = List.of(expenseDocumentA, expenseDocumentB);

        // Act
        List<Expense> expenses = mapper.toExpenseList(expenseDocuments);

        // Assert
        assertEquals(2, expenses.size());

        assertEquals("Expense Test ID A", expenses.get(0).getId());
        assertEquals("Category Test ID A", expenses.get(0).getCategory().getId());
        assertEquals("Food", expenses.get(0).getCategory().getName());
        assertEquals("Food related expenses.", expenses.get(0).getCategory().getDescription());
        assertTrue(expenses.get(0).getCategory().getIsDefault());
        assertEquals("Lunch", expenses.get(0).getDescription());
        assertEquals(23.59f, expenses.get(0).getAmount());
        assertNotNull(expenses.get(0).getDate());

        assertNotNull(expenses);
        assertEquals("Expense Test ID B", expenses.get(1).getId());
        assertEquals("Category Test ID B", expenses.get(1).getCategory().getId());
        assertEquals("Travel", expenses.get(1).getCategory().getName());
        assertEquals("Travel related expenses.", expenses.get(1).getCategory().getDescription());
        assertFalse(expenses.get(1).getCategory().getIsDefault());
        assertEquals("Hotel", expenses.get(1).getDescription());
        assertEquals(64.78f, expenses.get(1).getAmount());
        assertNotNull(expenses.get(1).getDate());
    }

    @Test
    void testToExpenseDocument() {
        // Arrange
        ExpenseCreate expenseCreate = TestDataUtil.createTestExpenseCreate();

        // Act
        ExpenseDocument expenseDocument = mapper.toExpenseDocument(expenseCreate);

        // Assert
        assertNotNull(expenseDocument);
        assertNotNull(expenseDocument.getId());
        assertEquals("Rent", expenseDocument.getDescription());
        assertEquals(700f, expenseDocument.getAmount());
    }
}
