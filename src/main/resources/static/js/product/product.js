import {fetchCategories } from '../category/category.js';
import {safeGetItem, fetchWithAuth } from '../auth/authStorage.js';

const updateButtonDiv = document.getElementById('update-product-buttons-div');
const culumnWithButtons = document.getElementById('culumn-with-buttons');

const role = safeGetItem("role");
if (role !== "ADMIN") {
    if (updateButtonDiv) updateButtonDiv.style.display = "none";
    if (culumnWithButtons) culumnWithButtons.style.display = "none";
}

let editedProducts = [];
let originalProducts = [];
let categoriesCache = [];
let changedProductsMap = {};

document.addEventListener("DOMContentLoaded", async () => {
    const tableBody = document.getElementById("product-body");
    try {
        const [products, categories] = await Promise.all([
            fetchProducts(),
            fetchCategories()
        ]);
        originalProducts = [...products];
        editedProducts = [...products];
        categoriesCache = [...categories];

        renderProducts(products, categoriesCache, tableBody);
        renderFilterCategories(categoriesCache);
        renderNewProductCategories(categoriesCache);
        setupEventListeners();
    } catch (error) {
        console.error(error);
    }
});

async function fetchProducts() {
    const status  = await fetchWithAuth('/api/product');

    if (!status.success || !Array.isArray(status.product)) {
        throw new Error('Неверный формат данных продуктов');
    }
    return status.product;
}

function renderProducts(products, categories, tableBody) {
    tableBody.innerHTML = "";
    const statusOptions = [
        { value: 1, label: 'активен' },
        { value: 0, label: 'не активен' }
    ];

    products.forEach((product, indexInView) => {
        const row = document.createElement("tr");
        const globalIndex = editedProducts.findIndex(p => p.productId === product.productId);

        const selectCategory = categories.map(cat => {
            const selected = product.category?.categoryId === cat.categoryId ? "selected" : "";
            return `<option value="${cat.categoryId}" ${selected}>${cat.categoryName}</option>`;
        }).join("");

        const selectStatus = statusOptions.map(opt => {
            const selected = product.productIsActive === opt.value ? "selected" : "";
            return `<option value="${opt.value}" ${selected}>${opt.label}</option>`;
        }).join("");
        row.innerHTML = `
            <td>${indexInView + 1}</td>
            <td>${product.productId}</td>
            <td contenteditable="true" class="editable" oninput="updateLocalProduct(${globalIndex}, 'productName', this)">${product.productName}</td>
            <td contenteditable="true" class="editable" oninput="updateLocalProduct(${globalIndex}, 'productDescription', this)">${product.productDescription}</td>
            <td contenteditable="true" class="editable" oninput="updateLocalProduct(${globalIndex}, 'productPrice', this)">${product.productPrice}</td>
            <td><img src="${product.productImageUrl}" alt="Картинка" class="product-image"></td>
            <td>
                <select class="category-select" data-index="${globalIndex}" onchange="updateLocalProduct(${globalIndex}, 'categoryId', this)">
                    ${selectCategory}
                </select>
            </td>
            <td>${product.productCreationDate}</td>
            <td>
                <select class="status-select" data-index="${globalIndex}" onchange="updateLocalProduct(${globalIndex}, 'productIsActive', this)">
                    ${selectStatus}
                </select>
            </td>`;
        if (role == "ADMIN") {
            row.innerHTML += `
                <td>
                    <button data-id="${product.productId}" class="delete-btn">Удалить</button>
                </td>`;
        }

        tableBody.appendChild(row);
        tableBody.querySelectorAll(".delete-btn").forEach(btn => {
            btn.addEventListener("click", async function () {
                const productId = this.dataset.id;
                await removeProduct(productId, tableBody);
            });
        });
    });
}

function renderFilterCategories(categories) {
    const filterCategorySelect = document.getElementById("filterCategory");
    filterCategorySelect.innerHTML = `<option value="">Все категории</option>`;
    categories.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat.categoryId;
        option.textContent = cat.categoryName;
        filterCategorySelect.appendChild(option);
    });
}

function renderNewProductCategories(categories) {
    const newProductCategory = document.getElementById("newProductCategory");
    if (!newProductCategory) return;

    newProductCategory.innerHTML = "";
    categories.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat.categoryId;
        option.textContent = cat.categoryName;
        newProductCategory.appendChild(option);
    });
}

function setupEventListeners() {
    document.getElementById("apply-filters-btn").addEventListener("click", applyFilters);
    document.getElementById("clear-filters-btn").addEventListener("click", clearFilters);
    document.getElementById("save-products").addEventListener("click", async () => {
        await saveProducts();
    });
    document.getElementById("undo-products").addEventListener("click", undoProducts);
    initCreateProductModal();
}

async function applyFilters() {
    const filterName = document.getElementById("filterName").value.toLowerCase();
    const filterCategory = document.getElementById("filterCategory").value;
    const filterPriceMin = parseFloat(document.getElementById("filterPriceMin").value);
    const filterPriceMax = parseFloat(document.getElementById("filterPriceMax").value);

    let filtered = [...originalProducts];

    if (filterName) filtered = filtered.filter(p => p.productName.toLowerCase().includes(filterName));
    if (filterCategory) filtered = filtered.filter(p => p.category?.categoryId == filterCategory);
    if (!isNaN(filterPriceMin)) filtered = filtered.filter(p => p.productPrice >= filterPriceMin);
    if (!isNaN(filterPriceMax)) filtered = filtered.filter(p => p.productPrice <= filterPriceMax);

    renderProducts(filtered, categoriesCache, document.getElementById("product-body"));
}

function clearFilters() {
    document.getElementById("filterName").value = "";
    document.getElementById("filterCategory").value = "";
    document.getElementById("filterPriceMin").value = "";
    document.getElementById("filterPriceMax").value = "";
    renderProducts(originalProducts, categoriesCache, document.getElementById("product-body"));
}

window.updateLocalProduct = async function (index, field, element) {
    const product = editedProducts[index];
    if (!product) return;

    let value = element.tagName === "SELECT" ? Number(element.value) : element.innerText.trim();

    const prevError = element.parentElement.querySelector('.field-error');
    if (prevError) prevError.remove();

    const errorMessage = document.createElement('span');
    errorMessage.className = 'field-error';
    errorMessage.style.color = 'red';
    errorMessage.style.fontSize = '12px';
    errorMessage.style.position = 'fixed';
    errorMessage.style.top = '2%';
    errorMessage.style.left = '50%';

    if ((field === "productName" || field === "productDescription") && !value) {
        errorMessage.innerText = 'Это поле не может быть пустым';
        element.parentElement.appendChild(errorMessage);
        element.style.backgroundColor = "#fdd";
        return;
    }
    if (field === "productPrice") {
        const numValue = parseFloat(value);
        if (isNaN(numValue) || numValue <= 0) {
            errorMessage.innerText = 'Цена должна быть больше 0';
            element.parentElement.appendChild(errorMessage);
            element.style.backgroundColor = "#fdd";
            return;
        }
        value = numValue;
    }
    if (field === "categoryId") product.category = { categoryId: value };
    else product[field] = value;

    element.style.backgroundColor = "#dfd";
    changedProductsMap[product.productId] = { ...product}
};

async function saveProducts() {
    const chengeProducts = Object.values(changedProductsMap);
    if (chengeProducts.length === 0) {
        alert("Нет изменений для сохранения")
        return;
    }
    try {
        const status = await fetchWithAuth('/api/product/update', {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(chengeProducts)
        });
        if (!status.success) throw new Error(status.message || "Ошибка обновления продукта");

        const [products, categories] = await Promise.all([fetchProducts(), fetchCategories()]);
        originalProducts = [...products];
        editedProducts = [...products];
        categoriesCache = [...categories];
        changedProductsMap = {};

        renderProducts(products, categoriesCache, document.getElementById("product-body"));
    } catch (error) {
        console.error(error);
        alert('Ошибка сохранения изменений. Попробуйте позже.');
    }
}

function deepClone(array) {
    return JSON.parse(JSON.stringify(array));
}

async function undoProducts() {
    try {
        editedProducts = deepClone(originalProducts);
        categoriesCache = await fetchCategories();
        renderProducts(editedProducts, categoriesCache, document.getElementById("product-body"));
    } catch (error) {
        console.error(error);
    }
}

function initCreateProductModal() {
    const modal = document.getElementById("productModal");
    if (!modal) return;

    const openBtn = document.getElementById("add-product-btn");
    const cancelBtn = document.getElementById("cancelModal");
    const form = document.getElementById("createProductForm");

    openBtn.addEventListener("click", () => modal.style.display = "flex");
    cancelBtn.addEventListener("click", () => modal.style.display = "none");

    form.addEventListener("submit", handleCreateProduct);
}

async function handleCreateProduct(e) {
    e.preventDefault();

    const newProduct = getNewProductData();
    const validationError = validateNewProduct(newProduct);
    if (validationError) {
        alert(validationError);
        return;
    }

    try {
        const status = await fetchWithAuth('/api/product/create', {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newProduct)
        });
        if (!status.success) throw new Error(status.message || "Ошибка при создании продукта");

        const [products, categories] = await Promise.all([fetchProducts(), fetchCategories()]);
        originalProducts = [...products];
        editedProducts = [...products];

        renderProducts(products, categories, document.getElementById("product-body"));
        renderNewProductCategories(categories);

        document.getElementById("productModal").style.display = "none";
        document.getElementById("createProductForm").reset();
    } catch (error) {
        console.error(error);
    }
}

function getNewProductData() {
    return {
        productName: document.getElementById("newProductName").value.trim(),
        productDescription: document.getElementById("newProductDescription").value.trim(),
        productPrice: parseFloat(document.getElementById("newProductPrice").value),
        productImageUrl: document.getElementById("newProductImageUrl").value.trim(),
        category: { categoryId: Number(document.getElementById("newProductCategory").value) },
        productIsActive: Number(document.getElementById("newProductStatus").value)
    };
}

function validateNewProduct(product) {
    if (!product.productName) return "Введите наименование продукта";
    if (!product.productDescription) return "Введите описание продукта";
    if (isNaN(product.productPrice) || product.productPrice <= 0) return "Цена должна быть больше 0";
    if (product.productImageUrl && !product.productImageUrl.toLowerCase().startsWith("http://"))
        return "Введите ссылку на изображение (начинается с \"http://\")";
    if (!product.category.categoryId) return "Выберите категорию";
    if (![0,1].includes(product.productIsActive)) return "Выберите корректный статус";
    return null;
}

async function removeProduct(productId, tableBody) {
    if (!confirm("Вы точно хотите удалить запись?")) return;
    try {
        const status = await fetchWithAuth(`/api/product/remove?productId=${productId}`, {
            method: "DELETE"
        });
        if (!status.success) {
            throw new Error("Ошибка при удалении продукта: " + status.message);
        }
        originalProducts = originalProducts.filter(p => p.productId != productId);
        editedProducts = editedProducts.filter(p => p.productId != productId);
        renderProducts(editedProducts, categoriesCache, tableBody);
    } catch (error) {
        console.error(error);
    }
}