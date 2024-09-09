package com.portfolio.expense_tracker.dataprovider;

import com.mongodb.client.result.DeleteResult;
import com.portfolio.expense_tracker.document.ExpenseDocument;
import com.portfolio.expense_tracker.domain.Expense;
import com.portfolio.expense_tracker.domain.OrderBy;
import com.portfolio.expense_tracker.domain.OrderDirection;
import com.portfolio.expense_tracker.dto.ExpenseCreate;
import com.portfolio.expense_tracker.dto.ExpenseUpdate;
import com.portfolio.expense_tracker.exception.BusinessException;
import com.portfolio.expense_tracker.exception.ExceptionCode;
import com.portfolio.expense_tracker.mapper.ExpenseMapperDataProvider;
import com.portfolio.expense_tracker.usecases.expense.ListByCriteriaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<Expense> listByCriteria(ListByCriteriaUseCase.Input input) {
        Query query = new Query();

        // Add pagination
        query.with(PageRequest.of(
                input.getOffset(),
                input.getLimit()
        ));

        // Filtering by category if provided
        if (input.getCategoryName() != null) {
            query.addCriteria(Criteria.where("category.name").is(input.getCategoryName()));
        }

        // Date filtering
        if (input.getDate() != null) {
            query.addCriteria(Criteria.where("date").is(input.getDate()));
        } else {
            if (input.getFrom() != null) {
                query.addCriteria(Criteria.where("date").gte(input.getFrom()));
            }
            if (input.getTo() != null) {
                query.addCriteria(Criteria.where("date").lte(input.getTo()));
            }
        }

        // Amount filtering
        if (input.getAmount() != null) {
            query.addCriteria(Criteria.where("amount").is(input.getAmount()));
        } else {
            if (input.getAmountGte() != null) {
                query.addCriteria(Criteria.where("amount").gte(input.getAmountGte()));
            }
            if (input.getAmountLte() != null) {
                query.addCriteria(Criteria.where("amount").lte(input.getAmountLte()));
            }
        }

        // Sorting logic
        if (input.getOrderByList() != null && !input.getOrderByList().isEmpty()) {
            for (int i = 0; i < input.getOrderByList().size(); i++) {

                OrderBy orderBy = input.getOrderByList().get(i);
                OrderDirection direction = (input.getOrderDirectionList() != null &&
                        input.getOrderDirectionList().size() > i) ? input.getOrderDirectionList().get(i) : OrderDirection.ASC;

                Sort.Direction sortDirection = (direction == OrderDirection.DESC) ? Sort.Direction.DESC : Sort.Direction.ASC;
                query.with(Sort.by(sortDirection, orderBy.getValue()));
            }
        }

        // Execute the query and return results
        return mongoTemplate.find(query, Expense.class);
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
