package CCH.business;

public enum TipoUtilizador {
	ADMIN(0),
	FABRICA(1),
	STAND(2);

	private final int value;

	private TipoUtilizador(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}