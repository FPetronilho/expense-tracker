package dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expensetracker.dataprovider.ExpenseCategoryDataProvider;
import com.portfolio.expensetracker.dataprovider.ExpenseDataProviderNoSql;
import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.document.ExpenseDocument;
import com.portfolio.expensetracker.domain.Expense;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.domain.OrderBy;
import com.portfolio.expensetracker.domain.OrderDirection;
import com.portfolio.expensetracker.dto.ExpenseCreate;
import com.portfolio.expensetracker.dto.ExpenseUpdate;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.mapper.ExpenseCategoryMapperDataProvider;
import com.portfolio.expensetracker.mapper.ExpenseMapperDataProvider;
import com.portfolio.expensetracker.usecases.expense.ListByCriteriaUseCase;
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

public class ExpenseDataProviderNoSqlTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ExpenseMapperDataProvider expenseMapper;

    @Mock
    private ExpenseCategoryMapperDataProvider expenseCategoryMapper;

    @Mock
    private ExpenseCategoryDataProvider expenseCategoryDataProvider;

    @InjectMocks
    private ExpenseDataProviderNoSql expenseDataProvider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate_shouldReturnExpense() {
        // Given
        ExpenseCreate expenseCreate = new ExpenseCreate();
        ExpenseCategory expenseCategory = new ExpenseCategory();

        ExpenseDocument expenseDocument = new ExpenseDocument();
        ExpenseCategoryDocument expenseCategoryDocument = new ExpenseCategoryDocument();
        expenseDocument.setCategory(expenseCategoryDocument);

        Expense expectedExpense = new Expense();

        when(expenseMapper.toExpenseDocument(expenseCreate)).thenReturn(expenseDocument);
        when(expenseCategoryMapper.toExpenseCategoryDocument(expenseCategory)).thenReturn(expenseCategoryDocument);
        when(mongoTemplate.save(expenseDocument)).thenReturn(expenseDocument);
        when(expenseMapper.toExpense(expenseDocument)).thenReturn(expectedExpense);

        // When
        Expense result = expenseDataProvider.create(expenseCreate, expenseCategory);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result);
        verify(mongoTemplate).save(expenseDocument);
        verify(expenseMapper).toExpenseDocument(expenseCreate);
        verify(expenseCategoryMapper).toExpenseCategoryDocument(expenseCategory);
        verify(expenseMapper).toExpense(expenseDocument);
    }

    @Test
    void testFindById_shouldReturnExpenseWhenExpenseExists() {
        // Given
        String id = "test-id";

        ExpenseDocument expenseDocument = new ExpenseDocument();
        Expense expectedExpense = new Expense();

        when(mongoTemplate.findOne(any(Query.class), eq(ExpenseDocument.class))).thenReturn(expenseDocument);
        when(expenseMapper.toExpense(expenseDocument)).thenReturn(expectedExpense);

        // When
        Expense result = expenseDataProvider.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result);
        verify(mongoTemplate).findOne(any(Query.class), eq(ExpenseDocument.class));
        verify(expenseMapper).toExpense(expenseDocument);
    }

    @Test
    void testFindById_shouldReturnResourceNotFoundExceptionWhenExpenseDoesNotExists() {
        // Given
        String id = "test-id";

        when(mongoTemplate.findOne(any(Query.class), eq(ExpenseDocument.class))).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> expenseDataProvider.findById(id));
    }

    @Test
    void testListByCriteria_shouldReturnExpenseList() {
        // Given
        ListByCriteriaUseCase.Input input = ListByCriteriaUseCase.Input.builder()
                .jwt("test-jwt")
                .offset(0)
                .limit(10)
                .categoryId("test-categoryId")
                .date(null)
                .from(null)
                .to(null)
                .amountLte(null)
                .amount(null)
                .amountGte(null)
                .orderByList(List.of(OrderBy.AMOUNT))
                .orderDirectionList(List.of(OrderDirection.ASC))
                .ids(List.of("test-ids"))
                .build();

        ExpenseDocument expenseDocument = new ExpenseDocument();
        List<ExpenseDocument> expenseDocuments = new ArrayList<>(List.of(expenseDocument));

        Expense expense = new Expense();
        List<Expense> expectedExpenses = new ArrayList<>(List.of(expense));

        when(mongoTemplate.find(any(Query.class), eq(ExpenseDocument.class))).thenReturn(expenseDocuments);
        when(expenseMapper.toExpenseList(expenseDocuments)).thenReturn(expectedExpenses);

        // When
        List<Expense> results = expenseDataProvider.listByCriteria(input);

        // Then
        assertNotNull(results);
        assertEquals(expectedExpenses, results);
        verify(mongoTemplate).find(any(Query.class), eq(ExpenseDocument.class));
        verify(expenseMapper).toExpenseList(expenseDocuments);
    }

    @Test
    void testUpdate_shouldReturnExpense() {
        // Given
        String id = "test-id";
        ExpenseUpdate expenseUpdate = new ExpenseUpdate();

        ExpenseDocument expenseDocument = new ExpenseDocument();
        Expense expectedExpense = new Expense();

        when(mongoTemplate.findOne(any(Query.class), eq(ExpenseDocument.class))).thenReturn(expenseDocument);
        when(mongoTemplate.save(expenseDocument)).thenReturn(expenseDocument);
        when(expenseMapper.toExpense(expenseDocument)).thenReturn(expectedExpense);

        // When
        Expense result = expenseDataProvider.update(id, expenseUpdate);

        // Then
        assertNotNull(result);
        assertEquals(expectedExpense, result);
        verify(expenseMapper).updateExpenseDocument(expenseDocument, expenseUpdate, expenseCategoryDataProvider);
        verify(mongoTemplate).save(expenseDocument);
        verify(expenseMapper).toExpense(expenseDocument);
    }

    @Test
    void testDelete_shouldDeleteExpenseWhenExpenseExists() {
        // Given
        String id = "test-id";

        DeleteResult deleteResult = mock(DeleteResult.class);
        when(mongoTemplate.remove(any(Query.class), eq(ExpenseDocument.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(1L);

        // When
        expenseDataProvider.delete(id);

        // Then
        assertEquals(1, deleteResult.getDeletedCount());
        verify(mongoTemplate).remove(any(Query.class), eq(ExpenseDocument.class));
    }

    @Test
    void testDelete_shouldThrowResourceNotFoundExceptionWhenExpenseDoesNotExist() {
        // Given
        String id = "test-id";

        DeleteResult deleteResult = mock(DeleteResult.class);
        when(mongoTemplate.remove(any(Query.class), eq(ExpenseDocument.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(0L);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> expenseDataProvider.delete(id));
    }
}
