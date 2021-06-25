package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.cj.xdevapi.Result;

/*
 * Classe responsável por coonectar com o BD
 */
public class DAO {
	
	//Parâmetros de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "dsena7";
	private String password = "Santosfc@1912";
	
	//Método de Conexão
	private Connection conectar() {
		Connection conn = null;
		try {
			Class.forName(driver);//Obtem os parâmetros de conexão
			conn = DriverManager.getConnection(url, user, password);//Estabele a conexão com o BD
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	//Testa conexão
	public void testaConexao() {
		try {
			Connection conn = conectar();//executando a conexão
			System.out.println(conn);
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	//CRUD CREATE
	public void inserirContato(JavaBeans contato) {
		String create = "insert into contatos (nome, fone, email) values(?, ?, ?)";
		try {
			//Abrindo a conexão
			Connection con = conectar();
			//Preparando a execução da query no BD
			PreparedStatement pst = con.prepareStatement(create);
			//Setando os valores dos atributos na query
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			//Executando a query
			pst.executeUpdate();
			//Encerrando a conexão com o BD
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	//CRUD READ (usado ArrayList para criar um vetor dinâmico)
	public ArrayList<JavaBeans> listarContatos(){
		//Objeto de acesso a classe JavaBeans
		ArrayList<JavaBeans> contatos = new ArrayList<>();
		//Select no BD para retornar os contatos
		String read = "select * from contatos order by idcon";
		try {
			//Abrindo a conexão
			Connection con = conectar();
			//Preparando a execução da query no BD
			PreparedStatement pst = con.prepareStatement(read);
			//Executando a query
			ResultSet rs = pst.executeQuery();
			
			//Retornando a lista de contatos
			while (rs.next()) {
				//Receptor de valores do BD
				String idcon = rs.getString(1);
				String fone = rs.getString(2);
				String nome = rs.getString(3);
				String email = rs.getString(4);
				//Populando a lista
				contatos.add(new JavaBeans(idcon, fone, nome, email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	//CRUD UPDATE - Selecionando um contato
	public void selecionarContato(JavaBeans contato) {
		String update = "select * from contatos where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(update);
			pst.setString(1, contato.getIdcon());
			ResultSet rs = pst.executeQuery();
			
			//Setando as variáveis na JavaBeans
			while (rs.next()) {
				contato.setIdcon(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	//Editar contato
	public void alterarContato(JavaBeans contato) {
		String create = "update contatos set nome = ?, fone = ?, email = ? where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(create);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setString(4, contato.getIdcon());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	//CRUD DELETE
	public void deletarContato(JavaBeans contato) {
		String delete = "delete from contatos where idcon = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(delete);
			pst.setString(1, contato.getIdcon());//seta o atributo idcon na interrogação da query
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
