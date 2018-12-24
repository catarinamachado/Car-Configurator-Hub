package CCH.business;

public enum TipoComponente {
	Pintura(0),
	JantesEPneus(1),
	Motor(2),
	DetalhesExteriores(3),
	DetalhesInteriores(4);

	private final int value;

	private TipoComponente(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static TipoComponente withValue(int value) {
		return TipoComponente.values()[value];
	}
}