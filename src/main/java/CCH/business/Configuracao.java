package CCH.business;

import java.util.List;

public class Configuracao {

	private int id;
	private double preco;
	private double desconto;
	private List<Componente> componentes;
	private List<Pacote> pacotes;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPreco() {
		return this.preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public double getDesconto() {
		return this.desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public List<Componente> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<Componente> componentes) {
		this.componentes = componentes;
	}

	public List<Pacote> getPacotes() {
		return pacotes;
	}

	public void setPacotes(List<Pacote> pacotes) {
		this.pacotes = pacotes;
	}

	public Configuracao() {
		// TODO - implement Configuracao.Configuracao
		throw new UnsupportedOperationException();
	}

	public Configuracao(int id, double preco, double desconto) {
		this.id = id;
		this.preco = preco;
		this.desconto = desconto;
	}

	/**
	 *
	 * @param precoMaximo
	 */
	public List<Componente> gerarConfiguracaoOtima(double precoMaximo) {
		// TODO - implement Configuracao.gerarConfiguracaoOtima
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param componenteId
	 */
	public Componente adiconarComponente(int componenteId) {
		// TODO - implement Configuracao.adiconarComponente
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param componenteId
	 */
	public void removerComponente(int componenteId) {
		// TODO - implement Configuracao.removerComponente
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param pacoteId
	 */
	public void adicionarPacote(int pacoteId) {
		// TODO - implement Configuracao.adicionarPacote
		throw new UnsupportedOperationException();
	}

	public List<Pacote> consultarPacotes() {
		// TODO - implement Configuracao.consultarPacotes
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param tipoComponente
	 */
	public List<Componente> consultarComponentes(TipoComponente tipoComponente) {
		// TODO - implement Configuracao.consultarComponentes
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param componentes
	 */
	public void aceitarConfiguracaoOtima(List<Componente> componentes) {
		// TODO - implement Configuracao.aceitarConfiguracaoOtima
		throw new UnsupportedOperationException();
	}

}