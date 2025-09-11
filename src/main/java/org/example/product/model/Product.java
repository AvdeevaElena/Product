package org.example.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "products"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_is_active")
    private Integer productIsActive;

    @Column(name = "product_is_del")
    private Integer productIsDel;

    @Column(name = "product_creation_date")
    private String productCreationDate;

    @Column(name = "product_creation_user")
    private String productCreationUser;

    @Column(name = "product_modified_date")
    private String productModifiedDate;

    @Column(name = "product_modified_user")
    private String productModifiedUser;

    public Product() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductIsActive() {
        return productIsActive;
    }

    public void setProductIsActive(Integer productIsActive) {
        this.productIsActive = productIsActive;
    }

    public Integer getProductIsDel() {
        return productIsDel;
    }

    public void setProductIsDel(Integer productIsDel) {
        this.productIsDel = productIsDel;
    }

    public String getProductCreationDate() {
        return productCreationDate;
    }

    public void setProductCreationDate(String productCreationDate) {
        this.productCreationDate = productCreationDate;
    }

    public String getProductCreationUser() {
        return productCreationUser;
    }

    public void setProductCreationUser(String productCreationUser) {
        this.productCreationUser = productCreationUser;
    }

    public String getProductModifiedDate() {
        return productModifiedDate;
    }

    public void setProductModifiedDate(String productModifiedDate) {
        this.productModifiedDate = productModifiedDate;
    }

    public String getProductModifiedUser() {
        return productModifiedUser;
    }

    public void setProductModifiedUser(String productModifiedUser) {
        this.productModifiedUser = productModifiedUser;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId) && Objects.equals(productName, product.productName) && Objects.equals(productDescription, product.productDescription) && Objects.equals(productImageUrl, product.productImageUrl) && Objects.equals(category, product.category) && Objects.equals(productPrice, product.productPrice) && Objects.equals(productIsActive, product.productIsActive) && Objects.equals(productIsDel, product.productIsDel) && Objects.equals(productCreationDate, product.productCreationDate) && Objects.equals(productCreationUser, product.productCreationUser) && Objects.equals(productModifiedDate, product.productModifiedDate) && Objects.equals(productModifiedUser, product.productModifiedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, productDescription, productImageUrl, category, productPrice, productIsActive, productIsDel, productCreationDate, productCreationUser, productModifiedDate, productModifiedUser);
    }
}
