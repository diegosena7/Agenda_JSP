<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="utf-8">
<title>Agenda de Contatos</title>
<link rel="icon" href="imagens/favicon.png">
<link rel="stylesheet" href="style.css">
</head>
<h1>Editar contato</h1>
<form name="frmContato" action="update">
	<table>
		<tr>
			<td><input type="text" name="idcon" id="caixa3" readonly value="<%out.println(request.getAttribute("idcon"));%>"><td>
		</tr>
		<tr>
			<td><input type="text" name="nome" class="Caixa1" value="<%out.println(request.getAttribute("nome"));%>"><td>
		</tr>
		<tr>
			<td><input type="text" name="fone" class="Caixa2" value="<%out.println(request.getAttribute("fone"));%>"><td>
		</tr>
		<tr>
			<td><input type="text" name="email" class="Caixa1" value="<%out.println(request.getAttribute("email"));%>"><td>
		</tr>
	</table>
	<br>
	<input class="botao1" type="button" value="Salvar" onclick="validar()">
	<script type="text/javascript" src="scripts/validador.js"></script>
</form>
</body>
</html>