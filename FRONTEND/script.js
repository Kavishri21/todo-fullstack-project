// Shared script for login, register, and todos pages
const SERVER_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

// --- Helper to parse response safely ---
async function parseResponse(res) {
  const ct = res.headers.get("content-type") || "";
  if (ct.includes("application/json")) {
    return await res.json();
  } else {
    // fallback to text
    return { message: await res.text() };
  }
}

// --- Login page logic ---
async function login() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const res = await fetch(`${SERVER_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await parseResponse(res);

    if (!res.ok) {
      const msg = data.error || data.message || JSON.stringify(data);
      throw new Error(msg);
    }

    const jwt = data.token;
    if (!jwt) throw new Error("No token provided by server");

    localStorage.setItem("token", jwt);
    // redirect to todos
    window.location.href = "todos.html";

  } catch (err) {
    alert(err.message);
  }
}

// --- Register page logic ---
function register() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  fetch(`${SERVER_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  })
  .then(async response => {
    const data = await parseResponse(response);
    if (!response.ok) throw new Error(data.error || data.message || "Registration failed");
    alert("Registration successful, please login!!");
    window.location.href = "login.html";
  })
  .catch(err => {
    alert(err.message);
  });
}

// --- Todos UI helpers ---
function createTodoCard(todo) {
  const card = document.createElement("div");
  card.className = "todo-card";

  const checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.checked = todo.isComplete;
  checkbox.addEventListener("change", function () {
    const updatedTodo = { ...todo, isComplete: checkbox.checked };
    updateTodoStatus(updatedTodo);
  });

  const span = document.createElement("span");
  span.textContent = todo.title;

  if (todo.isComplete) {
    span.style.textDecoration = "line-through";
    span.style.color = "#aaa";
  }

  const deletebtn = document.createElement("button");
  deletebtn.textContent = "X";
  deletebtn.onclick = function () { deleteTodo(todo.id); };

  card.appendChild(checkbox);
  card.appendChild(span);
  card.appendChild(deletebtn);

  return card;
}

// --- Load todo list ---
function loadTodos() {
  // token loaded at top from localStorage
  if (!token) {
    alert("Please login first");
    window.location.href = "login.html";
    return;
  }

  fetch(`${SERVER_URL}/todo`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` }
  })
  .then(async response => {
    const data = await parseResponse(response);
    if (!response.ok) {
      throw new Error(data.error || data.message || "Failed to get todos");
    }
    return data;
  })
  .then(todos => {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = "";

    if (!todos || todos.length === 0) {
      todoList.innerHTML = `<p id="empty-message">No todos yet. Add one below.</p>`;
    } else {
      todos.forEach(todo => {
        todoList.appendChild(createTodoCard(todo));
      });
    }
  })
  .catch(error => {
    document.getElementById("todo-list").innerHTML = `<p style="color:red">Failed to load todos</p>`;
    console.error("loadTodos error:", error);
  });
}

// --- Add todo ---
function addTodo() {
  const input = document.getElementById("new-todo");
  const todoText = input.value.trim();
  if (!todoText) return;

  fetch(`${SERVER_URL}/todo/create`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify({ title: todoText, isComplete: false })
  })
  .then(async response => {
    const data = await parseResponse(response);
    if (!response.ok) {
      throw new Error(data.error || data.message || "Failed to add todo");
    }
    return data;
  })
  .then(newTodo => {
    input.value = "";
    loadTodos(); // reload list
  })
  .catch(error => {
    alert(error.message);
    console.error("addTodo error:", error);
  });
}

// --- Update todo (PUT) ---
function updateTodoStatus(todo) {
  fetch(`${SERVER_URL}/todo`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    },
    body: JSON.stringify(todo)
  })
  .then(async response => {
    const data = await parseResponse(response);
    if (!response.ok) {
      throw new Error(data.error || data.message || "Failed to update todo");
    }
    return data;
  })
  .then(() => loadTodos())
  .catch(error => {
    alert(error.message);
    console.error("updateTodoStatus error:", error);
  });
}

// --- Delete todo ---
function deleteTodo(id) {
  fetch(`${SERVER_URL}/todo/${id}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` }
  })
  .then(async response => {
    // server might return empty 200/204 or json
    if (response.status === 204) return {};
    const data = await parseResponse(response);
    if (!response.ok) throw new Error(data.error || data.message || "Failed to delete todo");
    return data;
  })
  .then(() => loadTodos())
  .catch(error => {
    alert(error.message);
    console.error("deleteTodo error:", error);
  });
}

// Page-specific initializations
document.addEventListener("DOMContentLoaded", function () {
  if (document.getElementById("todo-list")) {
    loadTodos();
  }
});
