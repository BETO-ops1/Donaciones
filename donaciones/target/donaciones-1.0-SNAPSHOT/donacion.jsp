<%-- 
    Document   : donacion
    Created on : 14/11/2025, 3:07:54 p. m.
    Author     : KATIA GRATEROL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Registrar Donación</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f4f4;
            padding: 30px;
        }
        .card {
            background: white;
            width: 420px;
            margin: auto;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        input, select, textarea {
            width: 100%;
            padding: 8px;
            margin-top: 8px;
            border-radius: 6px;
            border: 1px solid #ccc;
        }
        button {
            width: 100%;
            padding: 10px;
            margin-top: 15px;
            border: none;
            border-radius: 6px;
            background: #2a6df4;
            color: white;
            cursor: pointer;
            font-size: 15px;
        }
        button:hover {
            background: #1f54c0;
        }
        .hidden {
            display: none;
        }
    </style>

    <script>
        function cambiarCampos() {
            const tipo = document.getElementById("TipoDonacion").value;

            const camposDinero = document.getElementById("dineroFields");
            const entidad = document.getElementById("EntidadBancaria");
            const monto = document.getElementById("Monto");

            if (tipo === "Dinero") {
                camposDinero.classList.remove("hidden");
                entidad.required = true;
                monto.required = true;
            } else {
                camposDinero.classList.add("hidden");
                entidad.required = false;
                monto.required = false;
            }
        }
    </script>
</head>

<body>

<div class="card">
    <h2>Registrar Donación</h2>

    <form action="DonacionControl" method="POST">

       
        <label>Tipo de Donación:</label>
        <select name="TipoDonacion" id="TipoDonacion" onchange="cambiarCampos()" required>
            <option value="">Seleccione...</option>
            <option value="Dinero">Dinero</option>
            <option value="Especie">Especie</option>
        </select>

       
        <label>Nombre:</label>
        <input type="text" name="Nombre" required>

       
        <label>Correo:</label>
        <input type="email" name="Correo" required>

        
        <label>Número de contacto:</label>
        <input type="text" name="NumeroContacto" required>

      
        <label>Tipo Identificación:</label>
        <select name="TipoIdentificacion" required>
            <option value="CC">Cédula</option>
            <option value="TI">Tarjeta Identidad</option>
            <option value="CE">Cédula Extranjería</option>
            <option value="PAS">Pasaporte</option>
        </select>

        
        <label>Número Identificación:</label>
        <input type="text" name="Identificacion" required>

       
        <div id="dineroFields" class="hidden">
            <label>Entidad Bancaria:</label>
            <select name="EntidadBancaria" id="EntidadBancaria">
                <option value="">Seleccione...</option>
                <option value="Bancolombia">Bancolombia</option>
                <option value="Davivienda">Davivienda</option>
                <option value="BBVA">BBVA</option>
                <option value="Banco de Bogotá">Banco de Bogotá</option>
            </select>

            <label>Monto:</label>
            <input type="number" step="0.01" name="Monto" id="Monto">
        </div>

      
        <label>Mensaje:</label>
        <textarea name="Mensaje" rows="3"></textarea>
        <button type="submit">Enviar Donación</button>
    </form>
</div>

</body>
</html>
