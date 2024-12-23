package com.portfolio.expensetracker.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Document(collection = "expense-category")
public class ExpenseCategoryDocument extends BaseDocument {

    private String id;
    private String name;
    private String description;
    private boolean isDefault;

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
