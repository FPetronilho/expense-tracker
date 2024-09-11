package com.portfolio.expense_tracker.dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expense_tracker.document.ExpenseCategoryDocument;
import com.portfolio.expense_tracker.document.ExpenseDocument;
import com.portfolio.expense_tracker.domain.ExpenseCategory;
import com.portfolio.expense_tracker.dto.ExpenseCategoryCreate;
import com.portfolio.expense_tracker.exception.BusinessException;
import com.portfolio.expense_tracker.exception.ExceptionCode;
import com.portfolio.expense_tracker.exception.ResourceAlreadyExistsException;
import com.portfolio.expense_tracker.exception.ResourceNotFoundException;
import com.portfolio.expense_tracker.mapper.ExpenseCategoryMapperDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryDataProviderNoSql implements ExpenseCategoryDataProvider{

    private final MongoTemplate mongoTemplate;
    private final ExpenseCategoryMapperDataProvider mapper;

    @Override
    public ExpenseCategory create(ExpenseCategoryCreate expenseCategoryCreate) {
        if (existsByName(expenseCategoryCreate.getName())) {
            throw new ResourceAlreadyExistsException(ExpenseCategoryDocument.class, expenseCategoryCreate.getName());
        }

        ExpenseCategoryDocument expenseCategoryDocument = mapper.toExpenseCategoryDocument(expenseCategoryCreate);
        expenseCategoryDocument = mongoTemplate.save(expenseCategoryDocument);
        return mapper.toExpenseCategory(expenseCategoryDocument);
    }

    @Override
    public List<ExpenseCategory> list(Integer offset, Integer limit) {
        Query query = new Query();
        query.with(PageRequest.of(offset, limit));
        List<ExpenseCategoryDocument> expenseCategoryDocuments = mongoTemplate.find(query, ExpenseCategoryDocument.class);
        return mapper.toExpenseCategoryList(expenseCategoryDocuments);
    }

    @Override
    public void delete(String name) {
        Query query = new Query().addCriteria(Criteria.where("name").is(name));
        DeleteResult deleteResult = mongoTemplate.remove(query, ExpenseCategoryDocument.class);

        if (deleteResult.getDeletedCount() == 0) {
            throw new ResourceNotFoundException(ExpenseCategoryDocument.class, name);
        }
    }

    @Override
    public ExpenseCategory findByName(String name) {
        Query query = new Query().addCriteria(Criteria.where("name").is(name));
        ExpenseCategoryDocument expenseCategoryDocument = mongoTemplate.findOne(query, ExpenseCategoryDocument.class);

        expenseCategoryDocument = Optional.ofNullable(expenseCategoryDocument).orElseThrow(
                () -> new ResourceNotFoundException(ExpenseCategoryDocument.class, name)
        );

        return mapper.toExpenseCategory(expenseCategoryDocument);
    }

    private boolean existsByName(String name) {
        Query query = new Query().addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.exists(query, ExpenseCategoryDocument.class);
    }
}
