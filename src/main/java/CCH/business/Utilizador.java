package CCH.business;

import CCH.dataaccess.UtilizadorDAO;
import CCH.exception.TipoUtilizadorInexistenteException;

public class Utilizador {

	private TipoUtilizador tipoUtilizador;
	private int id;
	private String nome;
	private String password;

	private UtilizadorDAO utilizadorDAO = new UtilizadorDAO();

	/**
	 *
	 * @param nome
	 * @param password
	 */
	public Utilizador(String nome, String password) {
		this.id = utilizadorDAO.getNextId();
		this.nome = nome;
		this.password = password;
		this.tipoUtilizador = TipoUtilizador.STAND; //default
	}

	public Utilizador(int id, String nome, String password, TipoUtilizador tipoUtilizador) {
		this.id = id;
		this.nome = nome;
		this.password = password;
		this.tipoUtilizador = tipoUtilizador;
	}

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

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public TipoUtilizador getTipoUtilizador() {
		return tipoUtilizador;
	}

	public void setTipoUtilizador(TipoUtilizador tipoUtilizador) {
		this.tipoUtilizador = tipoUtilizador;
	}

	public void setTipoUtilizadorValue(int value) {
		TipoUtilizador tipoUtilizador = TipoUtilizador.values()[value];

		this.tipoUtilizador = tipoUtilizador;
	}

	/**
	 *
	 * @param id
	 * @param password
	 */
	public boolean validarCredenciais(int id, String password) {
		return this.id == id && this.password.equals(password);
	}

	public String getNomeUtilizador() {
		return "Utilizador " + id;
	}

	public String getNomeTipoUtilizador() {
		if (tipoUtilizador == TipoUtilizador.ADMIN)
			return "Admin";
		else if (tipoUtilizador == TipoUtilizador.FABRICA)
			return "Fabrica";
		else
			return "Stand";
	}

	public int parseNomeTipoToValue(String nomeTipo) throws TipoUtilizadorInexistenteException {
		if (nomeTipo.equals("Admin"))
			return 0;
		else if (nomeTipo.equals("Fabrica"))
			return 1;
		else if (nomeTipo.equals("Stand"))
			return 2;
		else
			throw new TipoUtilizadorInexistenteException();
	}

	public void atualizarUser(Utilizador utilizador) {
		utilizadorDAO.updateUser(utilizador);
	}

	public void atualizarPassword(Utilizador utilizador) {
		utilizadorDAO.updatePassword(utilizador);
	}

	public void atualizarTipo(Utilizador utilizador) {
		utilizadorDAO.updateTipo(utilizador);
	}

}