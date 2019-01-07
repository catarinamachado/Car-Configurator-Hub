package CCH.business;

import CCH.dataaccess.RemoteClass;
import CCH.dataaccess.UtilizadorDAO;
import CCH.exception.TipoUtilizadorInexistenteException;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilizador, que representa o ator que interage com a aplicação.
 *
 * @version 20181229
 */
public class Utilizador implements RemoteClass<Integer> {
/**
 * Classe utilizador, que representa o ator que interage com a aplicação.
 *
 * @version 20181229
 */
	private int id;
	private String nome;
	private String password;
	private TipoUtilizador tipoUtilizador;

	public Utilizador() {
	}

	/**
	 * Construtor parametrizado da Utilizador.
	 *
	 * @param nome Nome do utilizador
	 * @param password Password do utilizador
	 */
	public Utilizador(Integer id, String nome, String password) {
		this.id = id;
		this.nome = nome;
		this.password = password;
		this.tipoUtilizador = TipoUtilizador.STAND; //default
	}

	/**
	 * Construtor parametrizado da Utilizador.
	 *
	 * @param nome Nome do utilizador
	 * @param password Password do utilizador
	 * @param tipoUtilizador Tipo do utilizador (poderá ser admin, funcionário da
	 * fábrica ou do stand)
	 */
	public Utilizador(int id, String nome, String password, TipoUtilizador tipoUtilizador) {
		this.id = id;
		this.nome = nome;
		this.password = password;
		this.tipoUtilizador = tipoUtilizador;

	}

	public Utilizador(List<String> rs) {
		this.id = Integer.valueOf(rs.get(0));
		this.nome = rs.get(1);
		this.password = rs.get(2);
		this.tipoUtilizador = TipoUtilizador.withValue(Integer.valueOf(rs.get(3)));

	}

	/**
	 * Devolve o id do utilizador.
	 *
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

  public Integer key(String k) {
    return Integer.valueOf(k);
  }

	public Integer key(){ return this.id; }
	

	@Override
	public List<String> toRow() {
		List<String> l = new LinkedList<>();
		l.add(String.valueOf(this.id));
		l.add(this.nome);
		l.add(this.password);
		l.add(String.valueOf(this.tipoUtilizador.getValue()));
		return l;
	}

	@Override
	public Utilizador fromRow(List<String> row) {
		return new Utilizador(row);
	}

	/**
	 * Atualiza o id do utilizador.
	 *
	 * @param id Id do utilizador
	 */
	public void setId(int id) {
		this.id = id;

	}

	/**
	 * Devolve o nome do utilizador.
	 *
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Atualiza o nome do utilizador.
	 *
	 * @param nome Nome do utilizador
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Devolve a password do utilizador.
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Atualiza a password do utilizador.
	 *
	 * @param password Password do utilizador
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Devolve o Tipo de Utilizador do utilizador.
	 *
	 * @return TipoUtilizador
	 */
	public TipoUtilizador getTipoUtilizador() {
		return tipoUtilizador;
	}

	/**
	 * Atualiza o Tipo de Utilizador do utilizador.
	 *
	 * @param tipoUtilizador Tipo de Utilizador do utilizador
	 */
	public void setTipoUtilizador(TipoUtilizador tipoUtilizador) {
		this.tipoUtilizador = tipoUtilizador;
	}

	/**
	 * Atualiza o Tipo de Utilizador através do seu valor inteiro correspondente
	 * (0, 1 ou 2).
	 *
	 * @param value Valor inteiro correspondente ao tipo de utilizador
	 */

	public void setTipoUtilizadorValue(int value) {
		TipoUtilizador tipoUtilizador = TipoUtilizador.values()[value];

		this.tipoUtilizador = tipoUtilizador;
	}

	/**
	 * Verifica se o id e a password fornecidos correspondem ao id e à
	 * password do utilizador.
	 *
	 * @param id Id do utilizador
	 * @param password Password do utilizador
	 * @return true se corresponderem, false caso contrário
	 */
	public boolean validarCredenciais(int id, String password) {
		return this.id == id && this.password.equals(password);
	}

	/**
	 * Devolve a string "Utilizador" + o seu determinado id.
	 *
	 * @return string
	 */
	public String getNomeUtilizador() {
		return "Utilizador " + id;
	}

	/**
	 * Devolve o nome do tipo de utilizador (função do utilizador na empresa:
	 * administrador, fábrica ou stand).
	 *
	 * @return string
	 */
	public String getNomeTipoUtilizador() {
		if (tipoUtilizador == TipoUtilizador.ADMIN)
			return "Admin";
		else if (tipoUtilizador == TipoUtilizador.FABRICA)
			return "Fabrica";
		else
			return "Stand";
	}

	/**
	 * Devolve o valor inteiro correspondente ao tipo de utilizador passado como
	 * parâmetro (0 -> admin, 1 -> funcionário da fábrica, 2 -> funcionário
	 * do stand).
	 *
	 * @return int Valor inteiro correspondente ao tipo de utilizador
	 * @throws TipoUtilizadorInexistenteException Caso o tipo de utilizador passado
	 * como parâmetro não existir
	 */
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

}
