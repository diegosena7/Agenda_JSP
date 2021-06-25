package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })//Caminho de acesso as requisi��es
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
	}

	/*
	 * Nesta classe fazemos o redirecionamento de acordo com o tipo de requisi��o feita ao servlet
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Testando a conex�o dao.testaConexao();
		String action = request.getServletPath();//Atributo que recebe as requisi��es
		System.out.println("Tipo de requisi��o do client: " + action);//Imprime o tipo de requisi��o da anota��o @WebServlet

		if (action.equals("/main")) {//redireciona a requisi��o feita atrav�s do main para o m�todo interno contatos
			contatos(request, response);
		} else if (action.equals("/insert")) {
			novoContato(request, response);
		} else if (action.equals("/select")) {
			listarContatos(request, response);
		}else if (action.equals("/update")) {
			editarContato(request, response);
		}else if (action.equals("/delete")) {
			removerContato(request, response);
		}else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		}else {
			response.sendRedirect("index.html");
		} 
	}

	/*
	 * Nesta classe estamos listando os contatos e criando din�micamente os dados da agenda, dispachando a requisi��o
	 * para a classe Agenda.jsp.
	 */
	protected void contatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Criando um objeto que recebe os dados JavaBenas
		ArrayList<JavaBeans> lista = dao.listarContatos();
		
		//Enviando a lista ao documento Agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("Agenda.jsp");//informa ao dispacher qual o documento vai receber os valores
		rd.forward(request, response);//dispachando a requisi��o e a resposta a camada view (Agenda.jsp)
	}

	/*
	 * Nesta classe estamos inserindo um novo contato em nossa lista de contatos
	 */
	protected void novoContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Teste recebimento dos par�metros
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("email"));
		System.out.println(request.getParameter("fone"));
		
		//Setando os atributos na entidade, camada model da aplica��o
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		
		//Invocando o m�todo inserirContato da clase DAO.java, passando o objeto contato como par�metro
		dao.inserirContato(contato);
		
		//Redirecionar para o doc Agenda.jsp
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos selecionando um contato em nossa lista de contatos atrav�s do id
	 */
	protected void listarContatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recebe o id do contato a ser editado via par�metro da classe Agenda.jsp
		String idcon = request.getParameter("idcon");
		System.out.println("Id: " + idcon);
		
		//Seta o atributo idcon recebido da view na vari�vel da classe JavaBens
		contato.setIdcon(idcon);
		
		//Executa a sele��o do contato
		dao.selecionarContato(contato);
		
		//Setando os atributos do form com o conte�do da Model (JavaBeans)
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		
		//Encaminhando ao documento Editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("Editar.jsp");
		rd.forward(request, response);
	}
	
	
	/*
	 * Nesta classe estamos editando um contato em nossa lista de contatos atrav�s do idcon
	 */
	protected void editarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Testa os par�metros proveniente da requisi��o de update de dados na view
		System.out.println(request.getParameter("idcon"));
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("fone"));
		System.out.println(request.getParameter("email"));
		
		//Seta os atributos recebidos da view nas vari�veis da classe JavaBens
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		
		//Executa o m�todo alterarContato da classe DAO.java
		dao.alterarContato(contato);
		
		//Redireciona para o documento Agenda.jsp com as altera��es efetuadas e retorna a lista atualizada
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos excluindo um contato em nossa lista de contatos atrav�s do idcon
	 */
	protected void removerContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idcon = request.getParameter("idcon");//Recebe o id do contato que ser� exclu�do
		System.out.println(idcon);
		
		//Seta o atributo recebido da view na vari�vel da classe JavaBens
		contato.setIdcon(idcon);
		
		//Executa o m�todo deletarContato da classe DAO.java
		dao.deletarContato(contato);
		
		//Redireciona para o documento Agenda.jsp com as altera��es efetuadas e retorna a lista atualizada
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos gerando o relat�rio em PDF com os contatos
	 */
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Document documento = new Document();
		try {
			//Tipo de conte�do
			response.setContentType("apllication/pdf");
			
			//Nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			
			//Criando o PDF
			PdfWriter.getInstance(documento, response.getOutputStream());
			
			//Abrindo o doc
			documento.open();
			
			//Adicionando os valores ao doc
			documento.add(new Paragraph("Lista de contatos: "));
			documento.add(new Paragraph(" "));
			
			//Criando uma tabela no doc
			PdfPTable tabela = new PdfPTable(3);//n�m de colunas na tabela
			//Cabe�alho
			PdfPCell coluna1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell coluna2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell coluna3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(coluna1);
			tabela.addCell(coluna2);
			tabela.addCell(coluna3);
			
			//Populando a tabela a ser exibida no doc
			ArrayList<JavaBeans> lista = dao.listarContatos();//Faz a chamada do m�todo que busca os contatos no BD
			
			for (int i = 0; i < lista.size(); i++) {//percorre a lista de contatos e adiciona na tabela
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
			
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			documento.close();
		}
	}
}
