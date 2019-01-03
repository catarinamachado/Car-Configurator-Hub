package CCH.business;

/**
 * Classe que armazena os diferentes tipos de utilizadores.
 *
 * @version 20181229
 */

public enum TipoUtilizador {
	ADMIN(0),
	FABRICA(1),
	STAND(2);

	private final int value;

	/**
	 * Construtor parametrizado de TipoUtilizador.
	 *
	 * @param value Valor do tipo de utilizador
	 */
	private TipoUtilizador(int value) {
		this.value = value;
	}

	/**
	 * Devolve o valor correspondente ao tipo de utilizador.
	 *
	 * @return value
	 */
	public int getValue() {
		return value;
	}
}