/**
 * Conforma a exclusão de um contato
 * Criamos a função confirmar, que recebe um idcon como parâmetro para confirmar a exclusão de um contato através do id
 * @param idcon
 * @author Diego Sena
 */

function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão?");
	
	if(resposta === true){
		window.location.href = "delete?idcon=" + idcon;
	}
}