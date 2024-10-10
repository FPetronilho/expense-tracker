package mapper;

import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.mapper.ExpenseCategoryMapperDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import util.TestDataUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseCategoryMapperDataProviderTest {

    private ExpenseCategoryMapperDataProvider mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(ExpenseCategoryMapperDataProvider.class);
    }

    @Test
    void testToExpenseCategory() {
        // Arrange
        ExpenseCategoryDocument expenseCategoryDocument = TestDataUtil.createTestExpenseCategoryDocumentA();

        // Act
        ExpenseCategory expenseCategory = mapper.toExpenseCategory(expenseCategoryDocument);

        // Assert
        assertNotNull(expenseCategory);
        assertEquals("Category Test ID A", expenseCategory.getId());
        assertEquals("Food", expenseCategory.getName());
        assertEquals("Food related expenses.", expenseCategory.getDescription());
        assertTrue(expenseCategory.getIsDefault());
    }

    @Test
    void testToExpenseCategoryList() {
        // Arrange
        ExpenseCategoryDocument expenseCategoryDocumentA = TestDataUtil.createTestExpenseCategoryDocumentA();
        ExpenseCategoryDocument expenseCategoryDocumentB = TestDataUtil.createTestExpenseCategoryDocumentB();
        List<ExpenseCategoryDocument> expenseCategoryDocuments = List.of(
                expenseCategoryDocumentA,
                expenseCategoryDocumentB
        );

        // Act
        List<ExpenseCategory> expenseCategories = mapper.toExpenseCategoryList(expenseCategoryDocuments);

        // Assert
        assertEquals(2, expenseCategories.size());

        assertEquals("Category Test ID A", expenseCategories.get(0).getId());
        assertEquals("Food", expenseCategories.get(0).getName());
        assertEquals("Food related expenses.", expenseCategories.get(0).getDescription());
        assertTrue(expenseCategories.get(0).getIsDefault());

        assertEquals("Category Test ID B", expenseCategories.get(1).getId());
        assertEquals("Travel", expenseCategories.get(1).getName());
        assertEquals("Travel related expenses.", expenseCategories.get(1).getDescription());
        assertFalse(expenseCategories.get(1).getIsDefault());
    }

    @Test
    void testToExpenseCategoryDocumentFromExpenseCategoryCreate() {
        // Arrange
        ExpenseCategoryCreate expenseCategoryCreate = TestDataUtil.createTestExpenseCategoryCreate();

        // Act
        ExpenseCategoryDocument expenseCategoryDocument = mapper.toExpenseCategoryDocument(expenseCategoryCreate);

        // Assert
        assertNotNull(expenseCategoryDocument);
        assertEquals("House", expenseCategoryDocument.getName());
        assertEquals("House related expenses.", expenseCategoryDocument.getDescription());
        assertNotNull(expenseCategoryDocument.getId());
        assertFalse(expenseCategoryDocument.getIsDefault());
    }

    @Test
    void testToExpenseCategoryDocumentFromExpenseCategory() {
        // Arrange
        ExpenseCategory expenseCategory = TestDataUtil.createTestExpenseCategory();

        // Act
        ExpenseCategoryDocument expenseCategoryDocument = mapper.toExpenseCategoryDocument(expenseCategory);

        // Assert
        assertNotNull(expenseCategoryDocument);
        assertEquals("Test ID 789", expenseCategoryDocument.getId());
        assertEquals("Pets", expenseCategoryDocument.getName());
        assertEquals("Pets related expenses.", expenseCategoryDocument.getDescription());
        assertFalse(expenseCategoryDocument.getIsDefault());
    }
}
