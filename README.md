# 📝 **Api REST ForoHub**

## ✨ **Descripción**  
Este proyecto implementa una **API REST** para la gestión de **tópicos** dentro de un foro.  
Permite a los usuarios:  
- Consultar ✅  
- Listar 📋  
- Filtrar tópicos 🛠️  

Con información detallada como:  
🎯 **Título**, 📜 **Mensaje**, 📅 **Fecha de Creación**, 🚦 **Estado**, 👤 **Autor** y 📘 **Curso Asociado**.

---

## ⚙️ **Tecnologías Utilizadas**
<div style="background-color: #f9f9f9; padding: 10px; border-radius: 8px;">
🚀 **Lenguaje:**  
- <span style="color: #007396;">Java</span>  

🌱 **Frameworks y Librerías:**  
- <span style="color: #6db33f;">Spring Boot</span>  
- 🏗️ Hibernate  

📂 **Base de Datos:**  
- <span style="color: #4479A1;">MySQL</span>  

🛠️ **Herramientas de Desarrollo:**  
- 🖥️ IntelliJ IDEA  
- 🧪 Insomnia/Postman  
</div>

---

## 🌟 **Características**
-1️⃣ **Listar todos los tópicos**  
   - 🔄 Retorna un listado paginado de todos los tópicos.

-2️⃣ **Detalles de un tópico**  
   - 🔍 Muestra información detallada de un tópico específico mediante su ID.

-3️⃣ **Filtrar tópicos**  
   - 📚 Por **nombre de curso**.  
   - 🗓️ Por **año de creación**.  
   - 🔀 Por combinación de **nombre de curso y año**.  

-4️⃣ **Formatos personalizados**  
   - 📅 Devuelve las fechas en el formato `dd/MM/yyyy`.

---

# ⚙️ **Instalación y Configuración**

## 📝 **Prerrequisitos**
Antes de comenzar, asegúrate de tener instalados los siguientes programas:
- ☕ **Java 11 o superior**
- 🔧 **Maven**
- 🗄️ **MySQL**
- 🖥️ **IntelliJ IDEA** o cualquier otro IDE similar.

## 🗃️ **Configuración de Base de Datos**
Sigue estos pasos para configurar la base de datos:
- 1️⃣ **Crear una base de datos** llamada `foro`.
- 2️⃣ **Configurar las credenciales** en el archivo `application.properties` o `application.yml`:

`spring.datasource.url="jdbc:mysql://localhost:3306/foro"`



🚀 Instrucciones de Ejecución
Para poner en marcha la aplicación, sigue estos pasos: 
- 1️⃣ Clonar el repositorio a tu máquina local.
- 2️⃣ Importar el proyecto en tu IDE. 
- 3️⃣ Ejecuta el siguiente comando en la terminal: mvn clean install
- 4️⃣ Iniciar la aplicación desde la clase principal.

📡 Endpoints
- 1️⃣ Listar Tópicos
- 💻 Método: GET
- 📍 Endpoint: /topicos
- 🔧 Parámetros opcionales:
- nombreCurso: Filtra por el nombre del curso.
- fechaCreacion: Filtra por el año de creación.
- 📝 Ejemplo de Solicitud: curl -X GET "http://localhost:8081/topicos?nombreCurso=Java&fechaCreacion=2025"



### 2. Detalles de un Tópico
**🔍 GET** `/topicos/{id}`

- **Parámetro obligatorio**:
    - `id`: Identificador del tópico.

**📋 Ejemplo de solicitud**:
`bash
curl -X GET "http://localhost:8081/topicos/1"`



<h2>💬Respuesta:</h2>
{
    <strong>
<br>
  "id": 1,
  <br>
  "titulo": "Problemas con Java",
  <br>
  "mensaje": "Tengo problemas al iniciar mi aplicación Java.",
  <br>
  "fechaCreacion": "26/12/2024",
  <br>
  "estado": "abierto",
  <br>
  "nombreAutor": "Juan Pérez",
  <br>
  "nombreCurso": "Java Avanzado"
  <br>
}
</strong>


<h1>📜Regla de Negocio</h1>
Los ID para realizar consultas son obligatorios.
<br>
Se deben manejar errores en caso de que un tópico no exista o se envíe un ID incorrecto.
<br>
<h2>🗂️ Ejemplo de Datos Iniciales</h2>
<br>
Usar el siguiente JSON para pruebas con Insomnia o Postman:

[<br>
{
    <br>
  "titulo": "Problemas con Java",
  <br>
  "mensaje": "Tengo problemas al iniciar mi aplicación Java.",
  "autorId": 1,
  <br>
  "cursoId": 1
  <br>
},
<br>
{
<br>
  "titulo": "Error en Spring Boot",
  <br>
  "mensaje": "No logro conectar con la base de datos.",
  <br>
  "autorId": 2,
  <br>
  "cursoId": 2
  <br>
}
<br>]


<h2>Pruebas</h2>
<ul>
    <li>Usar herramientas como <strong>Postman</strong> para realizar las solicitudes HTTP.</li>
    <li>Probar con filtros combinados y verificando respuestas JSON correctas.</li>
    <li>Verificar la paginación en el endpoint principal.</li>
</ul>

<hr>

<h2>Documentacion</h2>

<ul>
<br>
  <il>Swagger Doc<il>
  <br>
  <il>Disponible en http://localhost:8081/swagger-ui.html</il>
  <br>
  <il>Debes iniciar el servidor para poder acceder</il>
</ul>

<h2>Autor</h2>
<ul>
    <li>José Pino Araya</li>
    <li>Contacto: <a href="j.pino2610@gmail.com">j.pino2610@gmail.com</a></li>
</ul>