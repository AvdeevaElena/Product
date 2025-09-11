let inMemoryStorage = {};

export function safeSetItem(key, value) {
    try {
        localStorage.setItem(key, value);
    } catch (e1) {
        try {
            sessionStorage.setItem(key, value);
        } catch (e2) {
            console.warn("Storage недоступен, сохраняем в памяти:", key, value);
            inMemoryStorage[key] = value;
        }
    }
}

export function safeGetItem(key) {
    try {
        return localStorage.getItem(key);
    } catch (e1) {
        try {
            return sessionStorage.getItem(key);
        } catch (e2) {
            return inMemoryStorage[key] || null;
        }
    }
}

export async function fetchWithAuth(url, options = {}) {
    const token = safeGetItem("token");
    if (!token) throw new Error("Нет токена авторизации. Пожалуйста, войдите.");
    options.headers = {
        ...options.headers,
        "Authorization": `Bearer ${token}`
    };
    if (!options.headers["Content-Type"]) {
        options.headers["Content-Type"] = "application/json";
    }
    const response = await fetch(url, options);

    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Ошибка запроса: ${response.status} ${text}`);
    }
    const data = await response.json();
    if (typeof data.success !== "undefined" && !data.success) {
        throw new Error("Ошибка запроса: " + (data.message || "Неизвестная ошибка"));
    }
    return data;
}


export function initLogout(buttonId = "logout-btn") {
    const logoutBtn = document.getElementById(buttonId);
    if (!logoutBtn) return;
    logoutBtn.addEventListener("click", () => {
        try {
            safeSetItem("token", "");
            safeSetItem("role", "");
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            sessionStorage.removeItem("token");
            sessionStorage.removeItem("role");
        } catch (e) {
            console.warn("Ошибка при очистке хранилища:", e);
        }
        window.location.href = "/login";
    });
}




