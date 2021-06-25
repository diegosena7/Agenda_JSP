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

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })//Caminho de acesso as requisições
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
	}

	/*
	 * Nesta classe fazemos o redirecionamento de acordo com o tipo de requisição feita ao servlet
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Testando a conexão dao.testaConexao();
		String action = request.getServletPath();//Atributo que recebe as requisições
		System.out.println("Tipo de requisição do client: " + action);//Imprime o tipo de requisição da anotação @WebServlet

		if (action.equals("/main")) {//redireciona a requisição feita através do main para o método interno contatos
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
	 * Nesta classe estamos listando os contatos e criando dinâmicamente os dados da agenda, dispachando a requisição
	 * para a classe Agenda.jsp.
	 */
	protected void contatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Criando um objeto que recebe os dados JavaBenas
		ArrayList<JavaBeans> lista = dao.listarContatos();
		
		//Enviando a lista ao documento Agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("Agenda.jsp");//informa ao dispacher qual o documento vai receber os valores
		rd.forward(request, response);//dispachando a requisição e a resposta a camada view (Agenda.jsp)
	}

	/*
	 * Nesta classe estamos inserindo um novo contato em nossa lista de contatos
	 */
	protected void novoContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Teste recebimento dos parâmetros
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("email"));
		System.out.println(request.getParameter("fone"));
		
		//Setando os atributos na entidade, camada model da aplicação
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		
		//Invocando o método inserirContato da clase DAO.java, passando o objeto contato como parâmetro
		dao.inserirContato(contato);
		
		//Redirecionar para o doc Agenda.jsp
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos selecionando um contato em nossa lista de contatos através do id
	 */
	protected void listarContatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Recebe o id do contato a ser editado via parâmetro da classe Agenda.jsp
		String idcon = request.getParameter("idcon");
		System.out.println("Id: " + idcon);
		
		//Seta o atributo idcon recebido da view na variável da classe JavaBens
		contato.setIdcon(idcon);
		
		//Executa a seleção do contato
		dao.selecionarContato(contato);
		
		//Setando os atributos do form com o conteúdo da Model (JavaBeans)
		request.setAttribute("idcon", contato.getIdcon());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		
		//Encaminhando ao documento Editar.jsp
		RequestDispatcher rd = request.getRequestDispatcher("Editar.jsp");
		rd.forward(request, response);
	}
	
	
	/*
	 * Nesta classe estamos editando um contato em nossa lista de contatos através do idcon
	 */
	protected void editarContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Testa os parâmetros proveniente da requisição de update de dados na view
		System.out.println(request.getParameter("idcon"));
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("fone"));
		System.out.println(request.getParameter("email"));
		
		//Seta os atributos recebidos da view nas variáveis da classe JavaBens
		contato.setIdcon(request.getParameter("idcon"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		
		//Executa o método alterarContato da classe DAO.java
		dao.alterarContato(contato);
		
		//Redireciona para o documento Agenda.jsp com as alterações efetuadas e retorna a lista atualizada
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos excluindo um contato em nossa lista de contatos através do idcon
	 */
	protected void removerContato(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idcon = request.getParameter("idcon");//Recebe o id do contato que será excluído
		System.out.println(idcon);
		
		//Seta o atributo recebido da view na variável da classe JavaBens
		contato.setIdcon(idcon);
		
		//Executa o método deletarContato da classe DAO.java
		dao.deletarContato(contato);
		
		//Redireciona para o documento Agenda.jsp com as alterações efetuadas e retorna a lista atualizada
		response.sendRedirect("main");
	}
	
	/*
	 * Nesta classe estamos gerando o relatório em PDF com os contatos
	 */
	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Document documento = new Document();
		try {
			//Tipo de conteúdo
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
			PdfPTable tabela = new PdfPTable(3);//núm de colunas na tabela
			//Cabeçalho
			PdfPCell coluna1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell coluna2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell coluna3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(coluna1);
			tabela.addCell(coluna2);
			tabela.addCell(coluna3);
			
			//Populando a tabela a ser exibida no doc
			ArrayList<JavaBeans> lista = dao.listarContatos();//Faz a chamada do método que busca os contatos no BD
			
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
