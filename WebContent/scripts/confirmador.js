/**
 * Conforma a exclusão de um contato
 * @author Diego Sena
 */

function confirmar(idcon){
	let resposta = confirm("Confirma a exclusão?");
	
	if(resposta === true){
		//alert(idcon);
		window.location.href = "delete?idcon=" + idcon;
	}
}