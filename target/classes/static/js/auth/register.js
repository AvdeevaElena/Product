document.getElementById("registerForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch("/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });
    const result = await response.json();
    if (result.success) {
        alert("Успешная регистрация!");
        window.location.href = "/login";
    }
    else {
        throw new Error("Ошибка при регистрации: " + (result.message || response.status));
    }
});