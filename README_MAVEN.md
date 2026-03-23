# Bolsa de Empleo - Spring Boot 3.2.3

Sistema de gestión de bolsa de empleo construido con Spring Boot 3.2.3, Java 17 y Maven.

## Especificaciones del Proyecto

- **Spring Boot:** 3.2.3
- **Java:** 17
- **Build Tool:** Maven
- **GroupId:** cr.una.bolsaempleo
- **ArtifactId:** bolsa-empleo
- **Base de Datos:** MySQL

## Dependencias Principales

- Spring Web
- Thymeleaf (Server-Side Rendering)
- Spring Data JPA
- MySQL Driver 8.0.33
- Spring Security
- Validation (Jakarta Bean Validation)
- Lombok
- Spring Boot Test & Security Test

## Requisitos Previos

- Java 17 JDK instalado
- Maven 3.6+ instalado
- MySQL Server 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## Instalación y Configuración

### 1. Clonar o Descargar el Proyecto

```bash
git clone <repository-url>
cd bolsa-empleo
```

### 2. Crear la Base de Datos

```sql
CREATE DATABASE bolsa_empleo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'bolsa_user'@'localhost' IDENTIFIED BY 'bolsa_password';
GRANT ALL PRIVILEGES ON bolsa_empleo.* TO 'bolsa_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configurar application.properties

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bolsa_empleo?useSSL=false&serverTimezone=UTC
spring.datasource.username=bolsa_user
spring.datasource.password=bolsa_password
```

### 4. Compilar el Proyecto

```bash
mvn clean install
```

### 5. Ejecutar la Aplicación

#### Opción A: Con Maven
```bash
mvn spring-boot:run
```

#### Opción B: Con Java
```bash
java -jar target/bolsa-empleo-1.0.0.jar
```

#### Opción C: Desde el IDE
- Click derecho en `BolsaEmpleoApplication.java`
- Run as → Java Application

## Acceder a la Aplicación

- URL: http://localhost:8080
- Puerto: 8080

## Estructura del Proyecto

```
bolsa-empleo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cr/una/bolsaempleo/
│   │   │       ├── config/          # Configuraciones (Security, Database, etc)
│   │   │       ├── controller/      # Controladores REST/MVC
│   │   │       ├── model/           # Entidades JPA
│   │   │       ├── repository/      # Repositorios Spring Data JPA
│   │   │       ├── service/         # Lógica de negocio
│   │   │       ├── util/            # Utilidades
│   │   │       └── BolsaEmpleoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/           # Templates Thymeleaf
│   │       │   ├── index.html
│   │       │   ├── about.html
│   │       │   ├── contact.html
│   │       │   ├── login/
│   │       │   ├── empresa/
│   │       │   └── oferente/
│   │       └── static/
│   │           ├── css/
│   │           ├── js/             # (Sin JavaScript frontend)
│   │           └── images/
│   └── test/
│       └── java/                   # Tests unitarios
├── pom.xml                         # Configuración Maven
└── README.md
```

## Características

✅ **Autenticación y Autorización** con Spring Security
✅ **Validación de datos** con Jakarta Bean Validation
✅ **Servidor-Side Rendering** con Thymeleaf (sin JavaScript)
✅ **ORM** con Spring Data JPA
✅ **Base de datos MySQL** con Hibernate
✅ **Lombok** para reducir código boilerplate
✅ **Responsive Design** con CSS moderno

## Configuración de Seguridad

La aplicación utiliza Spring Security con:
- Autenticación basada en formularios
- Contraseñas encriptadas con BCrypt
- CSRF protection habilitada
- Rutas públicas y protegidas

### Rutas Públicas por Defecto

- `/` - Página de inicio
- `/about` - Acerca de
- `/contact` - Contacto
- `/css/**` - Archivos CSS
- `/images/**` - Imágenes

## Desarrollo

### Crear una Nueva Entidad

```java
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String nombre;
}
```

### Crear un Repositorio

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
```

### Crear un Servicio

```java
@Service
@Transactional
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
```

### Crear un Controlador

```java
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuarios/lista";
    }
}
```

## Archivos de Configuración Disponibles

### Perfiles de Ejecución

Crear `application-{profile}.properties`:
- `application-dev.properties` - Desarrollo
- `application-prod.properties` - Producción
- `application-test.properties` - Testing

Ejecutar con perfil:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## Logging

Los niveles de log están configurados en `application.properties`:
- Root: INFO
- Aplicación (cr.una.bolsaempleo): DEBUG
- Spring Web: DEBUG
- Spring Security: DEBUG
- Hibernate SQL: DEBUG

## Tests

Ejecutar todos los tests:
```bash
mvn test
```

Ejecutar test específico:
```bash
mvn test -Dtest=NombreTest
```

## Plugins Maven Utilizados

- spring-boot-maven-plugin: Para compilar y ejecutar la aplicación
- maven-compiler-plugin: Para compilar con Java 17

## Troubleshooting

### Error de conexión a MySQL
- Verificar que MySQL está en ejecución
- Revisar credenciales en application.properties
- Validar que la base de datos existe

### Puerto 8080 en uso
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Limpiar y reconstruir
```bash
mvn clean install -U
```

## Documentación Adicional

- [Spring Boot 3.2.3 Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Lombok](https://projectlombok.org/)

## Licencia

Este proyecto es de uso educativo.

## Autor

Creado para el curso de Programación IV - Universidad Nacional de Costa Rica
