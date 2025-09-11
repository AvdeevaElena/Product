import {safeGetItem, fetchWithAuth } from '../auth/authStorage.js';

const tableBody = document.getElementById("categories-body");
const createModal = document.getElementById('create-category-modal');
const creationForm = document.getElementById('create-category-form');
const culumnWithButtons = document.getElementById('culumn-with-buttons');

const role = safeGetItem("role");

if (role !== "ADMIN") {
    if (creationForm) creationForm.style.display = "none";
    if (createModal) createModal.style.display = "none";
    if (culumnWithButtons) culumnWithButtons.style.display = "none";
}

if (tableBody && creationForm) {
    document.addEventListener("DOMContentLoaded", async function () {
        try {
            const data = await fetchCategories();
            renderCategories(data, tableBody);
            editCreateCategory(creationForm, createModal, tableBody);
        } catch (error) {
            console.error(error);
        }
    });
}

async function createCategory(categoryName, categoryDescription) {
    try {
        const createCategory = {
            categoryName: categoryName,
            categoryDescription: categoryDescription
        };
        const result = await fetchWithAuth(`/api/category/create`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(createCategory)
        });
        if (!result.success) {
            throw new Error("Ошибка при создании категории: " + (result.message || response.status));
        }
    } catch (error) {
        console.error(error);
        throw error;
    }
}

function editCreateCategory(creationForm, createModal, tableBody) {
    if (!creationForm) return;

    creationForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const categoryName = creationForm.querySelector("input[name='categoryName']").value.trim();
        const categoryDescription = creationForm.querySelector("textarea[name='categoryDescription']").value.trim();
        if (!validateCategoryFields(categoryName, categoryDescription)) return;
        try {
            await createCategory(categoryName, categoryDescription);
            creationForm.reset();
            const data = await fetchCategories();
            renderCategories(data, tableBody);
        } catch (error) {
            console.error(error);
        }
    });
}


function validateCategoryFields(categoryName, categoryDescription) {
    if (!categoryName || !categoryDescription) {
        alert("Поля название и описание должны быть заполнены.");
        return false;
    }
    return true;
}

export async function fetchCategories() {
    const result = await fetchWithAuth('/api/category');
    if (!result.success) {
        throw new Error('Ошибка сети: ' + response.status);
    }
    if (!result.success || !Array.isArray(result.category)) {
        throw new Error('Неверный формат данных категорий');
    }
    return result.category;
}

function renderCategories(data, tableBody) {
    if (!tableBody) return;

    tableBody.innerHTML = "";
    if (data && Array.isArray(data)) {
        data.forEach((cat, index) => {
            const row = document.createElement('tr');
            if (role === "ADMIN") {
                row.innerHTML = `
                <td>
                    <button data-id="${cat.categoryId}" class="delete-btn">Удалить</button>
                    <button data-id="${cat.categoryId}" class="edit-btn">Редактировать</button>
                </td>
                <td>${index + 1}</td>
                <td>${cat.categoryId}</td>
                <td>${cat.categoryName}</td>
                <td>${cat.categoryDescription}</td>`;
            } else {
                row.innerHTML = `
                <td>${index + 1}</td>
                <td>${cat.categoryId}</td>
                <td>${cat.categoryName}</td>
                <td>${cat.categoryDescription}</td>`;
            }
            tableBody.appendChild(row);
        });

        tableBody.querySelectorAll(".delete-btn").forEach(btn => {
            btn.addEventListener("click", async function () {
                const categoryId = this.dataset.id;
                await removeCategory(categoryId, tableBody);
            });
        });

        tableBody.querySelectorAll(".edit-btn").forEach((btn) => {
            btn.addEventListener("click", function () {
                const row = this.closest("tr");
                const categoryId = this.dataset.id;
                const category = data.find((c) => c.categoryId == categoryId);
                editUpdateRowCategory(row, category, tableBody);
            });
        });
    } else {
        tableBody.innerHTML = '<tr><td colspan="5">Нет данных</td></tr>';
    }
}

async function removeCategory(categoryId, tableBody) {
    if (!confirm("Вы точно хотите удалить запись?")) return;

    try {
        const result = await fetchWithAuth(`/api/category/remove?categoryId=${categoryId}`, {
            method: "DELETE"
        });
        if (!result.success) {
            throw new Error("Ошибка при удалении категории: " + response.status);
        }
        const data = await fetchCategories();
        renderCategories(data, tableBody);
    } catch (error) {
        console.error(error);
    }
}

async function editUpdateRowCategory(row, cat, tableBody) {
    if (!row || !cat) return;

    row.innerHTML = `
        <td>
          <button class="save-btn">Сохранить</button>
          <button class="cancel-btn">Отмена</button>
        </td>
        <td>${cat.categoryId}</td>
        <td> </td>
        <td><input type="text" value="${cat.categoryName}" class="edit-name" /></td>
        <td><input type="text" value="${cat.categoryDescription}" class="edit-desc" /></td>`;

    row.querySelector(".save-btn").addEventListener("click", async () => {
        const categoryName = row.querySelector(".edit-name").value.trim();
        const categoryDescription = row.querySelector(".edit-desc").value.trim();
        if (!validateCategoryFields(categoryName, categoryDescription)) return;
        await updateCategory(cat.categoryId, categoryName, categoryDescription, tableBody);
    });

    const data = await fetchCategories();
    row.querySelector(".cancel-btn").addEventListener("click", () => {
        renderCategories(data, tableBody);
    });

    async function updateCategory(categoryId, categoryName, categoryDescription, tableBody) {
        try {
            const updateCategory = {
                categoryId: categoryId,
                categoryName: categoryName,
                categoryDescription: categoryDescription
            };
            const result = await fetchWithAuth(`/api/category/update`, {
                method: 'PUT',
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updateCategory)
            });
            if (result.success) {
                const data = await fetchCategories();
                renderCategories(data, tableBody);
            } else {
                throw new Error("Ошибка при изменении категории: " + (result.message || response.status));
            }
        } catch (error) {
            console.error("Ошибка:", error);
        }
    }
}