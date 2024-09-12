package com.portfolio.expense_tracker.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document(collection = "expense")
public class ExpenseDocument extends BaseDocument {

    @Indexed(unique = true)
    private String id;

    private ExpenseCategoryDocument category;
    private String description;
    private Float amount;
}
