# Referencia Rápida de Thymeleaf

Thymeleaf es un motor de templates Java que permite SSR (Server-Side Rendering).

## Declaración Namespace

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
```

## Variables y Expresiones

### Mostrar una variable
```html
<!-- De objeto en Model -->
<p th:text="${usuario.nombre}">Nombre</p>

<!-- Shorthand -->
<p th:text="${nombre}">Nombre</p>

<!-- Con texto por defecto si no existe -->
<p th:text="${nombre} ?: 'Sin nombre'">Nombre</p>
```

### Método concatenado
```html
<p th:text="${user.firstName} + ' ' + ${user.lastName}"></p>
<p>[[${user.firstName} + ' ' + ${user.lastName}]]</p>
```

## Atributos

### Cambiar atributos
```html
<!-- href -->
<a th:href="@{/user/}${id}">Link</a>
<a th:href="@{/user/{id}(id=${id})}">Link</a>

<!-- src -->
<img th:src="@{/images/logo.png}" alt="Logo">

<!-- value en formularios -->
<input type="text" th:value="${usuario.nombre}">

<!-- class -->
<div th:class="${activo} ? 'active' : 'inactive'">Estado</div>

<!-- id -->
<div th:id="${id}">Contenido</div>
```

## Iteración (Loops)

```html
<!-- Listar todos los items -->
<ul>
    <li th:each="item : ${items}" th:text="${item.nombre}"></li>
</ul>

<!-- Con índice -->
<ul>
    <li th:each="item, stat : ${items}">
        <span th:text="${stat.index}">0</span>
        <span th:text="${item.nombre}">Nombre</span>
    </li>
</ul>

<!-- Estadísticas del loop -->
<ul>
    <li th:each="item, stat : ${items}">
        Número: <span th:text="${stat.count}"></span>
        Índice: <span th:text="${stat.index}"></span>
        Tamaño: <span th:text="${stat.size}"></span>
        Es primero: <span th:text="${stat.first}"></span>
        Es último: <span th:text="${stat.last}"></span>
        Es par: <span th:text="${stat.even}"></span>
        Es impar: <span th:text="${stat.odd}"></span>
    </li>
</ul>
```

## Condicionales

```html
<!-- if -->
<div th:if="${usuario.activo}">
    Usuario está activo
</div>

<!-- unless (negación) -->
<div th:unless="${usuario.activo}">
    Usuario está inactivo
</div>

<!-- switch-case -->
<div th:switch="${usuario.rol}">
    <span th:case="'admin'">Es administrador</span>
    <span th:case="'user'">Es usuario</span>
    <span th:case="*">Rol desconocido</span>
</div>
```

## Formularios

### Formulario básico
```html
<form th:action="@{/guardar}" method="post">
    <button type="submit">Guardar</button>
</form>
```

### Con object binding
```html
<form th:action="@{/guardar}" th:object="${usuario}" method="post">
    <input type="text" th:field="*{nombre}" required>
    <input type="email" th:field="*{email}" required>
    <input type="password" th:field="*{password}" required>
    <button type="submit">Guardar</button>
</form>
```

### Mostrar errores de validación
```html
<form th:action="@{/guardar}" th:object="${usuario}" method="post">
    <div class="form-group">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" th:field="*{nombre}">
        <span th:if="${#fields.hasErrors('nombre')}" 
              th:errors="*{nombre}" 
              class="error">Error</span>
    </div>
</form>
```

### Errores globales
```html
<div th:if="${#fields.hasErrors('*')}">
    <div class="alert alert-danger">
        <ul>
            <li th:each="err : ${#fields.errors('*')}" th:text="${err}">Error</li>
        </ul>
    </div>
</div>
```

## Links y URLs

```html
<!-- Link simple -->
<a th:href="@{/}">Inicio</a>

<!-- Con parámetro -->
<a th:href="@{/usuario/{id}(id=${user.id})}">Ver Usuario</a>

<!-- Con múltiples parámetros -->
<a th:href="@{/buscar(q=${query},page=${page})}">Buscar</a>

<!-- Con contexto -->
<a th:href="@{~/admin/}">Admin</a>
```

## Objetos Útiles

### Variables globales
```html
<!-- Objeto #application (ServletContext) -->
<p th:text="${#application.getAttribute('appName')}"></p>

<!-- Objeto #session (HttpSession) -->
<p th:text="${#session.getAttribute('user.name')}"></p>

<!-- Objeto #request (HttpServletRequest) -->
<p th:text="${#request.getAttribute('error')}"></p>

<!-- Objeto #param (request parameters) -->
<p th:text="${#param.pageNum}"></p>
```

### Métodos útiles

```html
<!-- Funciones de strings -->
<p th:text="${#strings.toUpperCase(user.name)}">NAME</p>
<p th:text="${#strings.toLowerCase(user.name)}">name</p>
<p th:text="${#strings.abbreviate(user.name, 10)}">name</p>
<p th:text="${#strings.length(user.name)}">4</p>
<p th:text="${#strings.startsWith(user.name, 'J')}"></p>
<p th:text="${#strings.contains(user.name, 'oh')}"></p>

<!-- Funciones de números -->
<p th:text="${#numbers.formatDecimal(totalPrice, 2, 'COMMA')}">1,234.56</p>
<p th:text="${#numbers.formatInteger(totalItems, 3)}">001</p>

<!-- Funciones de fechas -->
<p th:text="${#dates.format(fecha, 'dd/MM/yyyy')}">12/01/2024</p>
<p th:text="${#dates.year(fecha)}">2024</p>

<!-- Funciones de listas -->
<p th:text="${#lists.size(items)}">5</p>
<p th:text="${#lists.contains(items, item)}"></p>
```

## Fragmentos y Includes

### Definir fragmento
```html
<!-- En archivo base.html -->
<div th:fragment="header">
    <h1>Mi Sitio</h1>
</div>
```

### Incluir fragmento
```html
<!-- En página principal -->
<div th:insert="base :: header"></div>
<!-- O mejor: -->
<div th:insert="~{base :: header}"></div>
```

### Con parámetros
```html
<!-- Definición -->
<div th:fragment="titulo(text)">
    <h1 th:text="${text}">Título</h1>
</div>

<!-- Uso -->
<div th:insert="~{base :: titulo('Bienvenido')}"></div>
```

## Atributos Data

```html
<!-- Para pasar datos a JavaScript sin exposición -->
<div th:data-user-id="${user.id}" th:data-user-role="${user.role}">
    Contenido
</div>

<!-- O -->
<button th:attr="data-id=${user.id}, data-action='edit'">Editar</button>
```

## Clases y Estilos Condicionales

```html
<!-- Una clase -->
<div th:classappend="${user.premium} ? 'premium' : ''">
    Contenido
</div>

<!-- Múltiples clases -->
<div th:class="'card ' + (${active} ? 'active' : '') + (${highlighted} ? 'highlight' : '')">
    Contenido
</div>

<!-- Styles -->
<div th:style="${user.color != null} ? 'color: ' + ${user.color} : ''">
    Contenido
</div>
```

## Seguridad (Spring Security)

```html
<!-- Verificar autenticación -->
<div th:if="${#authentication.principal}">
    <p>Usuario autenticado</p>
</div>

<!-- Verificar rol -->
<div sec:authorize="hasRole('ADMIN')">
    Solo administradores ven esto
</div>

<!-- Obtener usuario -->
<p th:text="${#authentication.principal.username}">Usuario</p>
```

## Comentarios

```html
<!-- Comentario HTML normal (visible en source) -->
<p>Contenido</p>

<!-- Comentario Thymeleaf (no visible en source) -->
<!--/* Comentario que no aparece en HTML */-->

<!-- Bloque comentado -->
<!--/*
<p>Esta línea no se procesa</p>
*/-->
```

## Ejemplos Completos

### Tabla con items
```html
<table>
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="usuario : ${usuarios}" th:if="${not #lists.isEmpty(usuarios)}">
            <td th:text="${usuario.nombre}">Nombre</td>
            <td th:text="${usuario.email}">Email</td>
            <td>
                <a th:href="@{/usuario/{id}(id=${usuario.id})}">Ver</a>
                <a th:href="@{/usuario/{id}/edit(id=${usuario.id})}">Editar</a>
            </td>
        </tr>
        <tr th:if="${#lists.isEmpty(usuarios)}">
            <td colspan="3">No hay usuarios</td>
        </tr>
    </tbody>
</table>
```

### Formulario con validación
```html
<form th:action="@{/usuarios/guardar}" th:object="${usuarioDTO}" method="post">
    
    <div class="form-group" th:classappend="${#fields.hasErrors('nombre')} ? 'has-error' : ''">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" th:field="*{nombre}" class="form-control">
        <span th:if="${#fields.hasErrors('nombre')}" 
              th:errors="*{nombre}" 
              class="help-block">Error en nombre</span>
    </div>

    <div class="form-group" th:classappend="${#fields.hasErrors('email')} ? 'has-error' : ''">
        <label for="email">Email:</label>
        <input type="email" id="email" th:field="*{email}" class="form-control">
        <span th:if="${#fields.hasErrors('email')}" 
              th:errors="*{email}" 
              class="help-block">Error en email</span>
    </div>

    <button type="submit" class="btn btn-primary">Guardar</button>
</form>
```

## Debug en Thymeleaf

```html
<!-- Ver objeto completo -->
<div th:text="${#objects.isEmpty(usuario) ? 'vacío' : usuario}"></div>

<!-- Ver contenido de lista -->
<div th:text="${usuarios.toString()}"></div>

<!-- Log -->
<!-- No hay logger nativo, usa JavaScript o Spring logs -->
```

## Tips

1. Usar `th:field="*{}"` en formularios (más seguro que `th:name`)
2. Siempre validar en el servidor, no confiar en HTML
3. Usar fragmentos para reutilizar componentes
4. Usar `#strings.abbreviate()` para textos largos
5. Proteger URLs sensibles con `th:href` en vez de href literal
6. Usar `@{}` para URLs dinámicas siempre

---

Para más info: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
