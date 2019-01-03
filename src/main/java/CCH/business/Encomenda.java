package CCH.business;

import java.util.Map;

/**
 * Classe que representa uma encomenda.
 *
 * @version 20181229
 */

public class Encomenda {
	private Map<Integer, Componente> componentes;
	private int id;
	private double preco;
	private String nomeCliente;
	private String numeroDeIdentificacaoCliente;
	private String moradaCliente;
	private String paisCliente;
	private String emailCliente;

	/**
	 * Construtor parametrizado da Encomenda.
	 *
	 * @param id Id da encomenda
	 * @param nomeCliente Nome do cliente a que a encomenda corresponde
	 * @param numeroDeIdentificacaoCliente Número de Identificação do cliente
	 * @param moradaCliente Morada do cliente
	 * @param paisCliente País do cliente
	 * @param emailCliente E-mail do cliente
	 */
	public Encomenda(int id, String nomeCliente, String numeroDeIdentificacaoCliente, String moradaCliente, String paisCliente, String emailCliente) {
		this.id = id;
		this.nomeCliente = nomeCliente;
		this.numeroDeIdentificacaoCliente = numeroDeIdentificacaoCliente;
		this.moradaCliente = moradaCliente;
		this.paisCliente = paisCliente;
		this.emailCliente = emailCliente;
	}

	/**
	 * Construtor parametrizado mais completo da Encomenda.
	 *
	 * @param componentes Componentes que fazem parte da encomenda
	 * @param id Id da encomenda
	 * @param preco Preco da encomenda
	 * @param nomeCliente Nome do cliente a que a encomenda corresponde
	 * @param numeroDeIdentificacaoCliente Número de Identificação do cliente
	 * @param moradaCliente Morada do cliente
	 * @param paisCliente País do cliente
	 * @param emailCliente E-mail do cliente
	 */
	public Encomenda(Map<Integer, Componente> componentes, int id, double preco, String nomeCliente, String numeroDeIdentificacaoCliente, String moradaCliente, String paisCliente, String emailCliente) {
		this.componentes = componentes;
		this.id = id;
		this.preco = preco;
		this.nomeCliente = nomeCliente;
		this.numeroDeIdentificacaoCliente = numeroDeIdentificacaoCliente;
		this.moradaCliente = moradaCliente;
		this.paisCliente = paisCliente;
		this.emailCliente = emailCliente;
	}

	/**
	 * Devolve os componentes que constituem a encomenda.
	 *
	 * @return Map<Integer, Componente> com o par chave valor dos componentes
	 * da encomenda
	 */
	public Map<Integer, Componente> getComponentes() {
		return componentes;
	}

	/**
	 * Atualiza os componentes que constituem a encomenda.
	 *
	 * @param componentes Componentes que constituem a encomenda
	 */
	public void setComponentes(Map<Integer, Componente> componentes) {
		this.componentes = componentes;
	}

	/**
	 * Devolve o id da encomenda.
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Atualiza o id da encomenda.
	 *
	 * @param id Id da encomenda
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devolve o preço da encomenda.
	 *
	 * @return preço
	 */
	public double getPreco() {
		return preco;
	}

	/**
	 * Atualiza o preço da encomenda.
	 *
	 * @param preco Preço da encomenda
	 */
	public void setPreco(double preco) {
		this.preco = preco;
	}

	/**
	 * Devolve o nome do cliente a que a encomenda corresponde.
	 *
	 * @return nome do cliente
	 */
	public String getNomeCliente() {
		return this.nomeCliente;
	}

	/**
	 * Atualiza o nome do cliente.
	 *
	 * @param nomeCliente Nome do cliente da encomenda
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	/**
	 * Devolve o número de identificação do cliente a que a encomenda corresponde.
	 *
	 * @return número de identificação do cliente
	 */
	public String getNumeroDeIdentificacaoCliente() {
		return this.numeroDeIdentificacaoCliente;
	}

	/**
	 * Atualiza o número de identificação do cliente.
	 *
	 * @param numeroDeIdentificacaoCliente Número de Identificação do cliente
	 */
	public void setNumeroDeIdentificacaoCliente(String numeroDeIdentificacaoCliente) {
		this.numeroDeIdentificacaoCliente = numeroDeIdentificacaoCliente;
	}

	/**
	 * Devolve a morada do cliente.
	 *
	 * @return morada
	 */
	public String getMoradaCliente() {
		return this.moradaCliente;
	}

	/**
	 * Atualiza a morada do cliente.
	 *
	 * @param moradaCliente Morada do cliente
	 */
	public void setMoradaCliente(String moradaCliente) {
		this.moradaCliente = moradaCliente;
	}

	/**
	 * Devolve o país do cliente.
	 *
	 * @return país
	 */
	public String getPaisCliente() {
		return this.paisCliente;
	}

	/**
	 * Atualiza o país do cliente.
	 *
	 * @param paisCliente País do cliente
	 */
	public void setPaisCliente(String paisCliente) {
		this.paisCliente = paisCliente;
	}

	/**
	 * Devolve o e-mail do cliente.
	 *
	 * @return e-mail
	 */
	public String getEmailCliente() {
		return this.emailCliente;
	}

	/**
	 * Atualiza o e-mail do cliente.
	 *
	 * @param emailCliente E-mail do cliente
	 */
	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}
}