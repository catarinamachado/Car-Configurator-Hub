package CCH.business;

public class Encomenda {

	private Componente componentes;
	private int id;
	private double preco;
	private String nomeCliente;
	private String numeroDeIdentificacaoCliente;
	private String moradaCliente;
	private String paisCliente;
	private String emailCliente;

	public Componente getComponentes() {
		return componentes;
	}

	public void setComponentes(Componente componentes) {
		this.componentes = componentes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getNomeCliente() {
		return this.nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNumeroDeIdentificacaoCliente() {
		return this.numeroDeIdentificacaoCliente;
	}

	public void setNumeroDeIdentificacaoCliente(String numeroDeIdentificacaoCliente) {
		this.numeroDeIdentificacaoCliente = numeroDeIdentificacaoCliente;
	}

	public String getMoradaCliente() {
		return this.moradaCliente;
	}

	public void setMoradaCliente(String moradaCliente) {
		this.moradaCliente = moradaCliente;
	}

	public String getPaisCliente() {
		return this.paisCliente;
	}

	public void setPaisCliente(String paisCliente) {
		this.paisCliente = paisCliente;
	}

	public String getEmailCliente() {
		return this.emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	/**
	 *
	 * @param id
	 * @param nomeCliente
	 * @param numeroDeIdentificacaoCliente
	 * @param moradaCliente
	 * @param paisCliente
	 * @param emailCliente
	 */
	public Encomenda(int id, String nomeCliente, String numeroDeIdentificacaoCliente, String moradaCliente, String paisCliente, String emailCliente) {
		// TODO - implement Encomenda.Encomenda
		throw new UnsupportedOperationException();
	}

}