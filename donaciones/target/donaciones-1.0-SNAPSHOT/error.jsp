<%-- 
    Document   : error
    Created on : 14/11/2025, 3:08:41 p. m.
    Author     : KATIA GRATEROL
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error en la Donación</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8d7da;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .card {
            background: white;
            width: 380px;
            padding: 25px;
            border-radius: 10px;
            text-align: center;
            border: 1px solid #dc3545;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 15px;
            border: none;
            border-radius: 5px;
            background: #dc3545;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="card">
    <h2>❌ Error al procesar la donación</h2>
    <p>Ocurrió un problema. Por favor, intenta nuevamente.</p>
    <a href="donacion.jsp" class="btn">Volver al formulario</a>
</div>

</body>
</html>
