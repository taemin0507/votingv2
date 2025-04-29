document.getElementById("login-form").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })
        .then(response => {
            if (!response.ok) throw new Error("로그인 실패");
            return response.json();
        })
        .then(data => {
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("username", data.username);
            localStorage.setItem("role", data.role);
            window.location.href = "vote-list.html";
        })
        .catch(error => {
            document.getElementById("result").innerText = "❌ 로그인 실패: " + error.message;
        });
});
