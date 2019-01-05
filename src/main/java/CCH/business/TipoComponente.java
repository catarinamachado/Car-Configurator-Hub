package CCH.business;

/**
 * Classe que armazena os diferentes tipos de componentes.
 *
 * @version 20181229
 */

public enum TipoComponente {
	Pintura(0),
	JantesEPneus(1),
	Motor(2),
	DetalhesExteriores(3),
	DetalhesInteriores(4);

	private final int value;

	/**
	 * Construtor parametrizado de TipoComponente.
	 *
	 * @param value Valor do tipo de componente
	 */
	private TipoComponente(int value) {
		this.value = value;
	}

	/**
	 * Devolve o valor correspondente ao tipo de componente.
	 *
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Devolve o nome do tipo de componente.
	 *
	 * @param value Valor do tipo de componente
	 * @return nome do tipo de componente
	 */
	public static TipoComponente withValue(int value) {
		return TipoComponente.values()[value];
	}
}
