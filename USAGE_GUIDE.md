# Guía de Uso - Bolsa de Empleo

## 🎬 Escenarios de Uso

### Escenario 1: Una Empresa Publica un Empleo

#### Paso 1: Registro de la Empresa
1. Acceder a `http://localhost:8080/empresa/registro`
2. Llenar el formulario:
   - **Nombre de la Empresa:** Tech Innovations S.A.
   - **Email:** rh@techinnovations.com
   - **Contraseña:** MiPassword123!
   - **Razón Social:** Tecnología e Innovación para el Futuro
   - **RFC:** TIN123456ABC
   - **Teléfono:** (555) 123-4567
   - **Ciudad:** México
   - **Estado:** Distrito Federal
   - **Sitio Web:** www.techinnovations.com
   - **Descripción:** Empresa líder en soluciones tecnológicas

3. Hacer clic en "Registrarse"
4. Será redirigido a la página de login

#### Paso 2: Login de la Empresa
1. Acceder a `http://localhost:8080/empresa/login`
2. Ingresar credenciales:
   - Email: rh@techinnovations.com
   - Contraseña: MiPassword123!
3. Hacer clic en "Iniciar Sesión"
4. Será dirigido al dashboard

#### Paso 3: Crear un Puesto de Trabajo
1. En el dashboard, hacer clic en "Crear Puesto"
2. Llenar el formulario:
   - **Título del Puesto:** Desarrollador Java Senior
   - **Descripción:** 
     ```
     Buscamos un desarrollador Java con experiencia en:
     - Spring Boot
     - Microservicios
     - Bases de datos relacionales
     - API REST
     ```
   - **Requisitos:**
     ```
     - Licenciatura en Informática o afín
     - 5+ años de experiencia
     - Disponibilidad para trabajo remoto
     - Inglés avanzado
     ```
   - **Salario Mínimo:** 50000
   - **Salario Máximo:** 70000
   - **Moneda:** MXN
   - **Tipo de Contrato:** Tiempo Completo
   - **Experiencia Requerida:** 5-7 años
   - **Nivel de Educación:** Licenciatura
   - **Ciudad:** Ciudad de México
   - **Estado:** CDMX

3. Hacer clic en "Crear Puesto"

#### Paso 4: Agregar Características (Habilidades) Requeridas
1. En la vista de detalle del puesto, hacer clic en "Agregar Característica"
2. Agregar varias habilidades:
   
   **Habilidad 1:**
   - Nombre: Java
   - Tipo: Técnica
   - Nivel: Avanzado
   
   **Habilidad 2:**
   - Nombre: Spring Boot
   - Tipo: Herramienta
   - Nivel: Avanzado
   
   **Habilidad 3:**
   - Nombre: SQL
   - Tipo: Técnica
   - Nivel: Avanzado
   
   **Habilidad 4:**
   - Nombre: Inglés
   - Tipo: Idioma
   - Nivel: Avanzado

---

### Escenario 2: Un Candidato Se Registra y Aplica a un Empleo

#### Paso 1: Registro del Candidato
1. Acceder a `http://localhost:8080/oferente/registro`
2. Llenar el formulario:
   - **Nombre:** Carlos
   - **Apellido:** Martínez López
   - **Email:** carlos.martinez@email.com
   - **Contraseña:** MiPassword456!
   - **Teléfono:** (555) 987-6543
   - **Profesión:** Desarrollador Java
   - **Años de Experiencia:** 6
   - **Ciudad:** Ciudad de México
   - **Resumen Profesional:**
     ```
     Desarrollador Java con 6 años de experiencia en desarrollo
     de aplicaciones empresariales. Especializado en Spring Boot,
     microservicios y arquitectura REST.
     ```

3. Hacer clic en "Registrarse"

#### Paso 2: Login del Candidato
1. Acceder a `http://localhost:8080/oferente/login`
2. Ingresar:
   - Email: carlos.martinez@email.com
   - Contraseña: MiPassword456!
3. Acceder al dashboard

#### Paso 3: Agregar Habilidades
1. En el dashboard, hacer clic en "Ver Perfil"
2. En la sección "Habilidades y Características", hacer clic en "Agregar Habilidad"
3. Agregar habilidades propias:
   
   **Habilidad 1:**
   - Nombre: Java
   - Tipo: Técnica
   - Nivel: Avanzado
   - Descripción: 10+ años programando en Java
   
   **Habilidad 2:**
   - Nombre: Spring Boot
   - Tipo: Herramienta
   - Nivel: Avanzado
   - Descripción: Experiencia con versiones 2.0 a 3.x
   
   **Habilidad 3:**
   - Nombre: SQL
   - Tipo: Técnica
   - Nivel: Avanzado
   
   **Habilidad 4:**
   - Nombre: Inglés
   - Tipo: Idioma
   - Nivel: Avanzado
   - Descripción: TOEFL 110/120

#### Paso 4: Subir Currículum Vitae
1. En el perfil, hacer clic en "Subir CV"
2. Seleccionar un archivo (PDF, DOC o DOCX)
3. Ejemplo de archivo: `Carlos_Martinez_CV.pdf`
4. Hacer clic en "Subir CV"

#### Paso 5: Establecer CV Principal
1. En el perfil, sección "Currículum Vitae"
2. Hacer clic en "Establecer Principal" para el CV recién cargado
3. El CV será marcado como principal (✓)

#### Paso 6: Ver y Aplicar a Empleos
1. Desde el perfil, hacer clic en "Ver Empleos"
2. Explorar la lista de empleos disponibles
3. Hacer clic en un empleo que coincida con el perfil
4. Ver detalles, requisitos y habilidades requeridas
5. Hacer clic en "Aplicar Ahora"

---

### Escenario 3: Búsqueda de Empleos por Ubicación

#### Paso 1: Acceder a la Búsqueda
1. Ir a `http://localhost:8080/puesto/lista`
2. Se muestra la lista de todos los empleos activos

#### Paso 2: Buscar por Ciudad
1. En la sección de búsqueda, ingresar: "Ciudad de México"
2. Hacer clic en "Buscar"
3. Se mostrarán los empleos en esa ubicación

#### Paso 3: Filtrar Resultados
- Revisar salarios, tipos de contrato
- Hacer clic en empleos de interés para ver detalles
- Comparar requisitos con habilidades propias

---

### Escenario 4: Gestión del Perfil de Empresa

#### Paso 1: Acceder al Perfil
1. Desde el dashboard, hacer clic en "Ver Perfil"
2. Se muestra toda la información de la empresa

#### Paso 2: Actualizar Información
1. Modificar campos según sea necesario
2. Ejemplo: Actualizar descripción de la empresa
   ```
   Tech Innovations es líder en soluciones digitales con
   presencia en 10 países de Latinoamérica. Nuestro equipo
   está comprometido con la innovación y excelencia.
   ```
3. Hacer clic en "Actualizar"

#### Paso 3: Gestionar Empleos Publicados
1. Desde el dashboard, hacer clic en "Ver Empleos"
2. Se muestran todos los empleos publicados por la empresa
3. Hacer clic en un empleo para:
   - Ver detalles
   - Editar información
   - Agregar más características requeridas
   - Ver candidatos que han aplicado (funcionalidad futura)

---

## 💡 Casos de Uso Avanzados

### Caso 1: Cambiar el CV Principal

**Situación:** Un candidato tiene múltiples CVs y desea cambiar cuál es el principal

**Pasos:**
1. Ir a perfil del candidato
2. En la sección "Currículum Vitae"
3. Encontrar el CV que desea establecer como principal
4. Hacer clic en "Establecer Principal"
5. El sistema automáticamente desmarca el anterior y marca el nuevo

### Caso 2: Eliminar una Habilidad

**Situación:** Un candidato desea eliminar una habilidad obsoleta de su perfil

**Pasos:**
1. Ir a perfil del candidato
2. En la sección "Habilidades"
3. Hacer clic en "Eliminar" en la habilidad deseada
4. Confirmar la eliminación

### Caso 3: Modificar una Oferta de Empleo

**Situación:** Una empresa desea modificar el salario de una oferta

**Pasos:**
1. Ir a dashboard de empresa
2. Ver los empleos publicados
3. Hacer clic en el empleo a modificar
4. Hacer clic en "Editar"
5. Cambiar el salario mínimo/máximo
6. Hacer clic en "Actualizar"

### Caso 4: Buscar Específicamente por Salario

**Nota:** Actualmente la búsqueda es por ciudad. Para mejoras futuras:
- Agregar filtros adicionales de salario
- Agregar filtros por tipo de contrato
- Agregar filtros por nivel de experiencia

---

## 📱 Datos de Prueba

### Empresa de Ejemplo (Datos precargados opcionales)
```
Email: tech@example.com
Contraseña: password123
Nombre: Tech Solutions
RFC: TSA123456789
```

### Candidato de Ejemplo
```
Email: juan@example.com
Contraseña: password123
Nombre: Juan Pérez
Profesión: Desarrollador Java
Experiencia: 5 años
```

---

## ❓ Preguntas Frecuentes

### P: ¿Cómo cambio mi contraseña?
**R:** Actualmente no existe funcionalidad de cambio de contraseña. Se recomienda una funcionalidad futura de "Recuperar Contraseña" o "Cambiar Contraseña".

### P: ¿Cuál es el tamaño máximo para un CV?
**R:** El máximo es 10 MB. Los formatos aceptados son: PDF, DOC, DOCX.

### P: ¿Puedo subir múltiples CVs?
**R:** Sí, puedes subir varios CVs. Solo uno puede ser marcado como "Principal".

### P: ¿Cómo elimino un empleo publicado?
**R:** En el detalle del empleo, hay un botón "Eliminar". Haz clic para eliminar (con confirmación).

### P: ¿Se envían notificaciones por correo?
**R:** Actualmente no. Se recomienda implementar notificaciones por email en versiones futuras.

### P: ¿Cómo veo quién ha aplicado a mis empleos?
**R:** Esta funcionalidad está planificada para versiones futuras.

---

## 🔧 Solución de Problemas

### Problema: No puedo registrarme
**Solución:** 
- Verifica que el email no esté ya registrado
- Asegúrate de llenar todos los campos requeridos

### Problema: Olvidé mi contraseña
**Solución:** 
- Actualmente no existe recuperación. Contacta al administrador.

### Problema: No puedo subir mi CV
**Solución:**
- Verifica el formato (PDF, DOC, DOCX)
- Comprueba que el archivo sea menor a 10 MB

### Problema: El CV no se guarda como principal
**Solución:**
- Intenta nuevamente haciendo clic en "Establecer Principal"
- Recarga la página y verifica

---

**Última actualización:** 18 de Marzo de 2026
