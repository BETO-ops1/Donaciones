<%-- 
    Document   : exito
    Created on : 14/11/2025, 3:08:26 p. m.
    Author     : KATIA GRATEROL
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Donación Procesada</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f2f2f2;
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
            border: 1px solid #ddd;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 15px;
            border: none;
            border-radius: 5px;
            background: #4CAF50;
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="card">
    <h2>✔ Donación registrada con éxito</h2>
    <p>¡Gracias por su aporte!</p>
    <a href="donacion.jsp" class="btn">Registrar otra donación</a>
</div>

</body>
</html>
