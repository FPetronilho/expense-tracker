package dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProviderNoSql;
import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.mapper.ExpenseCategoryMapperDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ExpenseCategoryDataProviderNoSqlTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ExpenseCategoryMapperDataProvider mapper;

    @InjectMocks
    private ExpenseCategoryDataProviderNoSql dataProvider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_shouldReturnExpenseCategory() {
        // Given
        ExpenseCategoryCreate expenseCategoryCreate = new ExpenseCategoryCreate();

        ExpenseCategoryDocument expenseCategoryDocument = new ExpenseCategoryDocument();
        ExpenseCategory expectedExpenseCategory = new ExpenseCategory();

        when(mapper.toExpenseCategoryDocument(expenseCategoryCreate)).thenReturn(expenseCategoryDocument);
        when(mongoTemplate.save(expenseCategoryDocument)).thenReturn(expenseCategoryDocument);
        when(mapper.toExpenseCategory(expenseCategoryDocument)).thenReturn(expectedExpenseCategory);

        // When
        ExpenseCategory result = dataProvider.create(expenseCategoryCreate);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpenseCategory, result);
        verify(mapper).toExpenseCategoryDocument(expenseCategoryCreate);
        verify(mongoTemplate).save(expenseCategoryDocument);
        verify(mapper).toExpenseCategory(expenseCategoryDocument);
    }

    @Test
    void testList_shouldReturnExpenseCategoryList() {
        // Given
        Integer offset = 0;
        Integer limit = 10;
        List<String> ids = new ArrayList<>(List.of("test-ids"));

        ExpenseCategoryDocument expenseCategoryDocument = new ExpenseCategoryDocument();
        List<ExpenseCategoryDocument> expenseCategoryDocuments = List.of(expenseCategoryDocument);

        ExpenseCategory expenseCategory = new ExpenseCategory();
        List<ExpenseCategory> expectedExpenseCategories = List.of(expenseCategory);

        when(mongoTemplate.find(any(Query.class), eq(ExpenseCategoryDocument.class))).thenReturn(expenseCategoryDocuments);
        when(mapper.toExpenseCategoryList(expenseCategoryDocuments)).thenReturn(expectedExpenseCategories);

        // When
        List<ExpenseCategory> results = dataProvider.list(offset, limit, ids);

        // Then
        assertNotNull(results);
        assertEquals(expectedExpenseCategories, results);
        verify(mongoTemplate).find(any(Query.class), eq(ExpenseCategoryDocument.class));
        verify(mapper).toExpenseCategoryList(expenseCategoryDocuments);
    }

    @Test
    void testDelete_shouldDeleteExpenseCategoryWhenExpenseCategoryExists() {
        // Given
        String id = "test-id";

        DeleteResult deleteResult = mock(DeleteResult.class);
        when(mongoTemplate.remove(any(Query.class), eq(ExpenseCategoryDocument.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(1L);

        // When
        dataProvider.delete(id);

        // Then
        assertEquals(1L, deleteResult.getDeletedCount());
        verify(mongoTemplate).remove(any(Query.class), eq(ExpenseCategoryDocument.class));
    }

    @Test
    void testDelete_shouldThrowResourceNotFoundExceptionWhenExpenseCategoryDoesNotExist() {
        // Given
        String id = "test-id";

        DeleteResult deleteResult = mock(DeleteResult.class);
        when(mongoTemplate.remove(any(Query.class), eq(ExpenseCategoryDocument.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(0L);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> dataProvider.delete(id));
    }

    @Test
    void testFindById_shouldReturnExpenseCategoryWhenExpenseCategoryExists() {
        // Given
        String id = "test-id";

        ExpenseCategoryDocument expenseCategoryDocument = new ExpenseCategoryDocument();
        ExpenseCategory expectedExpenseCategory = new ExpenseCategory();

        when(mongoTemplate.findOne(any(Query.class), eq(ExpenseCategoryDocument.class))).thenReturn(expenseCategoryDocument);
        when(mapper.toExpenseCategory(expenseCategoryDocument)).thenReturn(expectedExpenseCategory);

        // When
        ExpenseCategory result = dataProvider.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpenseCategory, result);
        verify(mongoTemplate).findOne(any(Query.class), eq(ExpenseCategoryDocument.class));
        verify(mapper).toExpenseCategory(expenseCategoryDocument);
    }

    @Test
    void testFindById_shouldThrowResourceNotFoundExceptionWhenExpenseCategoryDoesNotExist() {
        // Given
        String id = "test-id";

        when(mongoTemplate.findOne(any(Query.class), eq(ExpenseCategoryDocument.class))).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> dataProvider.findById(id));
    }
}
