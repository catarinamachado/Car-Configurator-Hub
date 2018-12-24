package CCH.business;

public class ClasseComponente {

	private TipoComponente tipoComponente;
	private int id;
	private boolean eObrigatorio;
	private String nome;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoComponente getTipoComponente() {
		return tipoComponente;
	}

	public void setTipoComponente(TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	public boolean getEObrigatorio() {
		return eObrigatorio;
	}

	public void setEObrigatorio(boolean eObrigatorio) {
		this.eObrigatorio = eObrigatorio;
	}

	public ClasseComponente(int id, boolean eObrigatorio, String nome, TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
		this.id = id;
		this.eObrigatorio = eObrigatorio;
		this.nome = nome;
	}
}