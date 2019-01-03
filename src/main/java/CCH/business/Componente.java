package CCH.business;

import CCH.dataaccess.ComponenteDAO;

import java.util.Map;

/**
 * Classe que representa um componente e todas as informações inerentes ao mesmo.
 *
 * @version 20181229
 */

public class Componente {
	private ClasseComponente classeComponente;
	private int id;
	private int stock;
	private double preco;
	private String nome;

	private ComponenteDAO componenteDAO = new ComponenteDAO();

	/**
	 * Construtor parametrizado do Componente.
	 *
	 * @param id Id do componente
	 * @param stock Indica o stock do componente
	 * @param preco Indica o preço do componente
	 * @param nome Indica o nome do componente
	 * @param classeComponente Classe em que se insere o componente
	 */
	public Componente(int id, int stock, double preco, String nome, ClasseComponente classeComponente) {
		this.id = id;
		this.stock = stock;
		this.preco = preco;
		this.nome = nome;
		this.classeComponente = classeComponente;
	}

	/**
	 * Devolve o id do componente.
	 *
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Atualiza o id do componente.
	 *
	 * @param id Id do componente
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devolve o stock do componente.
	 *
	 * @return stock
	 */
	public int getStock() {
		return this.stock;
	}

	/**
	 * Atualiza o stock do componente.
	 *
	 * @param stock Stock do componente
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * Devolve o preço do componente.
	 *
	 * @return preço
	 */
	public double getPreco() {
		return this.preco;
	}

	/**
	 * Atualiza o preço do componente.
	 *
	 * @param preco Preço do componente
	 */
	public void setPreco(double preco) {
		this.preco = preco;
	}

	/**
	 * Devolve o nome do componente.
	 *
	 * @return nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Atualiza o nome do componente.
	 *
	 * @param nome Nome do componente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Devolve a classe a que o componente pertence.
	 *
	 * @return ClasseComponente
	 */
	public ClasseComponente getClasseComponente() {
		return classeComponente;
	}

	/**
	 * Atualiza a classe a que o componente pertence.
	 *
	 * @param classeComponente Nova classe em que o componente se insere
	 */
	public void setClasseComponente(ClasseComponente classeComponente) {
		this.classeComponente = classeComponente;
	}

	/**
	 * Devolve os componentes que o componente requer.
	 *
	 * @return Map<Integer, Componente> com o conjunto chave valor de cada um dos
	 * componentes que o componente requer
	 */
	public Map<Integer, Componente> getRequeridos() {
		return componenteDAO.getComponentesRequeridos(id);
	}

	/**
	 * Devolve os componentes que são incompatíveis com o componente.
	 *
	 * @return Map<Integer, Componente> com o conjunto chave valor de cada um dos
	 * componentes que é incompatível o componente
	 */
	public Map<Integer, Componente> getIncompativeis() {
		return componenteDAO.getComponentesIncompativeis(id);
	}

	/**
	 * Devolve o nome completo do componente (com o nome da classe a que ele
	 * pertence inclusivé).
	 *
	 * @return nome completo do componente
	 */
	public String getFullName() {
		return classeComponente.getNome() + " " + nome;
	}

	/**
	 * Devolve o stock do componente em formato String.
	 *
	 * @return stock
	 */
	public String getStockString() {
		return Integer.toString(stock);
	}

	/**
	 * Devolve uma informação do stock do componente, no que diz respeito à
	 * quantidade de peças existentes em stock.
	 *
	 * @return string que poderá ser disponível, limitado ou indisponível
	 */
	public String getStockAvailable() {
		if (stock > 20) {
			return "Disponível";
		} else if (stock > 0) {
			return "Limitado";
		}

		return "Indisponível";
	}

  /**
	 * Decrementa o stock do componente em uma unidade.
	 */
	public void decrementaStock() {
		setStock(getStock() - 1);
		componenteDAO.updateStock(this);
	}
}
