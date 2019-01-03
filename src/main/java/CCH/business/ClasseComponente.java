package CCH.business;

/**
 * ClasseComponente simboliza as características básicas do componente, como
 * por exemplo o seu tipo e se se trata de um componente obrigatório ou não.
 *
 * @version 20181229
 */

public class ClasseComponente {
	private TipoComponente tipoComponente;
	private int id;
	private boolean eObrigatorio;
	private String nome;

	/**
	 * Construtor parametrizado da ClasseComponente.
	 *
	 * @param id Id da Classe Componente
	 * @param eObrigatorio Indica se se trata de um componente obrigatório ou não
	 * @param nome Nome da Classe a que o Componente pertence
	 * @param tipoComponente Tipo em que se insere o componente
	 */
	public ClasseComponente(int id, boolean eObrigatorio, String nome, TipoComponente tipoComponente) {
		this.id = id;
		this.eObrigatorio = eObrigatorio;
		this.nome = nome;
		this.tipoComponente = tipoComponente;
	}

	/**
	 * Devolve o id da classe do componente.
	 *
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Atualiza o id da classe do componente.
	 *
	 * @param id Id da ClasseComponente
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devolve o nome da classe do componente.
	 *
	 * @return nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Atualiza o nome da classe do componente.
	 *
	 * @param nome Nome da ClasseComponente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Devolve o tipo do componente.
	 *
	 * @return TipoComponente
	 */
	public TipoComponente getTipoComponente() {
		return tipoComponente;
	}

	/**
	 * Atualiza o tipo do componente.
	 *
	 * @param tipoComponente Tipo do componente
	 */
	public void setTipoComponente(TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	/**
	 * Devolve true se os componentes que sejam desta classe são obrigatórios, false caso contrário.
	 *
	 * @return boolean
	 */
	public boolean getEObrigatorio() {
		return eObrigatorio;
	}

	/**
	 * Atualiza o campo que guarda a informação se os componentes que forem desta
	 * classe serão componentes obrigatórios ou não.
	 *
	 * @param eObrigatorio True se os componentes forem obrigatórios, false caso contrário
	 */
	public void setEObrigatorio(boolean eObrigatorio) {
		this.eObrigatorio = eObrigatorio;
	}
}