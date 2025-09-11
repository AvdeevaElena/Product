import { safeSetItem, safeGetItem, fetchWithAuth } from './authStorage.js';

document.getElementById("loginForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch("/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, password})
    });
    console.log('QWERTY THERE')
    const result = await response.json();
    if (result.success) {
        safeSetItem("token", result.token);
        safeSetItem("role", result.role);

        alert("Успешный вход!", result.role);
        window.location.href = "/categories";
    } else {
        console.log('QWERTY THERE')
        alert("Ошибка при авторизации ", result.message);
        throw new Error("Ошибка при авторизации: " + (result.message || response.status));
    }
});