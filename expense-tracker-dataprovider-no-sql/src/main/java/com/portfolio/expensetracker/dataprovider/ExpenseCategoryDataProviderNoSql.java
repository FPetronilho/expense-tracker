package com.portfolio.expensetracker.dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expensetracker.document.ExpenseCategoryDocument;
import com.portfolio.expensetracker.domain.ExpenseCategory;
import com.portfolio.expensetracker.dto.ExpenseCategoryCreate;
import com.portfolio.expensetracker.exception.ResourceAlreadyExistsException;
import com.portfolio.expensetracker.exception.ResourceNotFoundException;
import com.portfolio.expensetracker.mapper.ExpenseCategoryMapperDataProvider;
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
    public List<ExpenseCategory> list(Integer offset, Integer limit, String ids) {
        Query query = new Query();

        // If a list of IDs is provided and not null, filter for those specific IDs
        if (ids != null && !ids.isEmpty()) {
            query.addCriteria(Criteria.where("id").in(ids));
        }

        query.with(PageRequest.of(offset, limit));
        List<ExpenseCategoryDocument> expenseCategoryDocuments = mongoTemplate.find(query, ExpenseCategoryDocument.class);
        return mapper.toExpenseCategoryList(expenseCategoryDocuments);
    }

    @Override
    public void delete(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        DeleteResult deleteResult = mongoTemplate.remove(query, ExpenseCategoryDocument.class);

        if (deleteResult.getDeletedCount() == 0) {
            throw new ResourceNotFoundException(ExpenseCategoryDocument.class, id);
        }
    }

    @Override
    public ExpenseCategory findById(String id) {
        Query query = new Query().addCriteria(Criteria.where("id").is(id));
        ExpenseCategoryDocument expenseCategoryDocument = mongoTemplate.findOne(query, ExpenseCategoryDocument.class);

        expenseCategoryDocument = Optional.ofNullable(expenseCategoryDocument).orElseThrow(
                () -> new ResourceNotFoundException(ExpenseCategoryDocument.class, id)
        );

        return mapper.toExpenseCategory(expenseCategoryDocument);
    }

    private boolean existsByName(String name) {
        Query query = new Query().addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.exists(query, ExpenseCategoryDocument.class);
    }
}
