package model;

public class JavaBeans {

	private String idcon;
	private String fone;
	private String nome;
	private String email;
	
	public JavaBeans() {
		super();
	}
	public JavaBeans(String idcon, String fone, String nome, String email) {
		super();
		this.idcon = idcon;
		this.fone = fone;
		this.nome = nome;
		this.email = email;
	}
	public String getIdcon() {
		return idcon;
	}
	public void setIdcon(String idcon) {
		this.idcon = idcon;
	}
	public String getFone() {
		return fone;
	}
	public void setFone(String fone) {
		this.fone = fone;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
