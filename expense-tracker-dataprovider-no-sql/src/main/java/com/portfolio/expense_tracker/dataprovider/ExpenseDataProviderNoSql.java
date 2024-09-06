package com.portfolio.expense_tracker.dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expense_tracker.document.ExpenseDocument;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.exception.BusinessException;
import com.portfolio.expense_tracker.exception.ExceptionCode;
import com.portfolio.expense_tracker.mapper.ExpenseMapperDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseDataProviderNoSql implements ExpenseDataProvider {

    private final MongoTemplate mongoTemplate;
    private final ExpenseMapperDataProvider mapper;

    @Override
    public Expense create(ExpenseCreate expenseCreate) {
        ExpenseDocument expenseDocument = mapper.toExpenseDocument(expenseCreate);
        expenseDocument = mongoTemplate.save(expenseDocument);
        return mapper.toExpense(expenseDocument);
    }

    @Override
    public Expense findById(String id) {
        ExpenseDocument expenseDocument = findDocumentById(id);
        return mapper.toExpense(expenseDocument);
    }

    @Override
    public List<Expense> findAll(Integer offset, Integer limit) {
        return null;
    }

    @Override
    public Expense update(String id, ExpenseUpdate expenseUpdate) {
        ExpenseDocument expenseDocument = findDocumentById(id);
        mapper.updateExpenseDocument(expenseDocument, expenseUpdate);
        expenseDocument = mongoTemplate.save(expenseDocument);
        return mapper.toExpense(expenseDocument);
    }

    @Override
    public void delete(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        DeleteResult deleteResult = mongoTemplate.remove(query, ExpenseDocument.class);
        if (deleteResult.getDeletedCount() == 0) {
            throw new BusinessException(
                    ExceptionCode.RESOURCE_NOT_FOUND,
                    String.format("expense %s not found", id)
            );
        }
    }

    private ExpenseDocument findDocumentById(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        ExpenseDocument expenseDocument = mongoTemplate.findOne(query, ExpenseDocument.class);
        expenseDocument = Optional.ofNullable(expenseDocument).orElseThrow(
                () -> new BusinessException(
                        ExceptionCode.RESOURCE_NOT_FOUND,
                        String.format("expense %s not found", id)
                )
        );

        return expenseDocument;
    }
}
