package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "category_product")
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_description", nullable = false)
    private String categoryDescription;

    @Column(name = "category_is_del")
    private Integer categoryIsDel;

    @Column(name = "category_creation_date")
    private String categoryCreationDate;

    @Column(name = "category_creation_user")
    private String categoryCreationUser;

    @Column(name = "category_modified_date")
    private String categoryModifiedDate;

    @Column(name = "category_modified_user")
    private String categoryModifiedUser;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Integer getCategoryIsDel() {
        return categoryIsDel;
    }

    public void setCategoryIsDel(Integer categoryIsDel) {
        this.categoryIsDel = categoryIsDel;
    }

    public String getCategoryCreationDate() {
        return categoryCreationDate;
    }

    public void setCategoryCreationDate(String categoryCreationDate) {
        this.categoryCreationDate = categoryCreationDate;
    }

    public String getCategoryCreationUser() {
        return categoryCreationUser;
    }

    public void setCategoryCreationUser(String categoryCreationUser) {
        this.categoryCreationUser = categoryCreationUser;
    }

    public String getCategoryModifiedDate() {
        return categoryModifiedDate;
    }

    public void setCategoryModifiedDate(String categoryModifiedDate) {
        this.categoryModifiedDate = categoryModifiedDate;
    }

    public String getCategoryModifiedUser() {
        return categoryModifiedUser;
    }

    public void setCategoryModifiedUser(String categoryModifiedUser) {
        this.categoryModifiedUser = categoryModifiedUser;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Category() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryId, category.categoryId) && Objects.equals(categoryName, category.categoryName) && Objects.equals(categoryDescription, category.categoryDescription) && Objects.equals(categoryIsDel, category.categoryIsDel) && Objects.equals(categoryCreationDate, category.categoryCreationDate) && Objects.equals(categoryCreationUser, category.categoryCreationUser) && Objects.equals(categoryModifiedDate, category.categoryModifiedDate) && Objects.equals(categoryModifiedUser, category.categoryModifiedUser) && Objects.equals(products, category.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName, categoryDescription, categoryIsDel, categoryCreationDate, categoryCreationUser, categoryModifiedDate, categoryModifiedUser, products);
    }
}
