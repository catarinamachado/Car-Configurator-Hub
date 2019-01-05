package CCH.business;

import CCH.dataaccess.PacoteDAO;
import CCH.dataaccess.RemoteClass;
import CCH.exception.ComponenteIncompativelNoPacoteException;
import CCH.exception.ComponenteJaExisteNoPacoteException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe que simboliza o pacote, que agrega vários componentes e confere um desconto.
 *
 * @version 20181229
 */
public class Pacote implements RemoteClass<Integer> {
	private int id;
	private double desconto;
	private PacoteDAO pacoteDAO;

	/**
	 * Construtor por omissão do Pacote.
	 */
   
	public Pacote() {
		this.id = 0;
		this.desconto = 0.0;
	}


	/**
	 * Construtor parametrizado do Pacote.
	 *
	 * @param id Id do Pacote
	 * @param desconto Desconto associado ao pacote
	 */
	public Pacote(int id, double desconto) {
		this.pacoteDAO = new PacoteDAO();
		this.id = id;
		this.desconto = desconto;
	}
  
	/**
	 * Devolve o id do pacote.
	 *
	 * @return id
	 */
	public int getId() {
		return this.id;
	}


	public Integer key(){return this.id; }

    public Integer key(String k) {
        return Integer.valueOf(k);
    }

	@Override
	public Pacote fromRow(List<String> row) {
		return new Pacote(row);
	}

	@Override
	public List<String> toRow() {
		List<String> l = new LinkedList<>();
		l.add(String.valueOf(this.id));
		l.add(String.valueOf(this.desconto));
		return l;
	}

	/**
	 * Atualiza o id do pacote.
	 *
	 * @param id Id do pacote
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devolve o valor do desconto do pacote.
	 *
	 * @return desconto
	 */
	public double getDesconto() {
		return this.desconto;
	}

	/**
	 * Atualiza o desconto do pacote.
	 *
	 * @param desconto Id do desconto
	 */
	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	/**
	 * Devolve todos os componentes presentes no pacote.
	 *
	 * @return Map<Integer, Componente> par chave valor de todos os componentes
	 * no pacote
	 */
	public Map<Integer, Componente> getComponentes() {
		return pacoteDAO.getComponentes(id);
	}


	public Pacote(List<String> rs){
		this.id = Integer.valueOf(rs.get(0));
		this.desconto = Double.valueOf(rs.get(1));
		this.pacoteDAO = new PacoteDAO();
	}

	/**
	 * Método que adiciona um novo componente ao pacote.
	 *
	 * @param componenteId Id do componente que se pretende adicionar ao pacote
	 * @throws ComponenteJaExisteNoPacoteException Caso o componente já faça
	 * parte do pacote
	 * @throws ComponenteIncompativelNoPacoteException Caso exista algum componente
	 * no pacote que seja incompatível com o componente que se pretende adicionar
	 */

	public void adicionaComponente(int componenteId) throws ComponenteJaExisteNoPacoteException,
															ComponenteIncompativelNoPacoteException {
		boolean alreadyHas = pacoteDAO.getAllIdsComponentesNoPacote(this.id).contains(componenteId);
		if (alreadyHas)
			throw new ComponenteJaExisteNoPacoteException();

		for (Componente c : pacoteDAO.getAllComponentesNoPacote(this.id)) {
			if (c.getIncompativeis() != null && c.getIncompativeis().containsKey(componenteId))
				throw new ComponenteIncompativelNoPacoteException(c.getFullName());

		}

		pacoteDAO.adicionaComponente(this.id, componenteId);
	}

	/**
	 * Método que remove um determinado componente presente no pacote.
	 *
	 * @param componenteId Id do componente que se pretende remover
	 */
	public void removeComponente(int componenteId) {
		pacoteDAO.removeComponente(this.id, componenteId);
	}

	/**
	 * Devolve o nome do Pacote (string "Pacote" + o id do pacote).
	 *
	 * @return string
	 */
	public String getNome() {
		return "Pacote " + id;
	}

	/**
	 * Devolve o desconto associado ao pacote em formato string.
	 *
	 * @return string
	 */
	public String getDescontoString() {
		return Double.toString(this.desconto);
	}

	/**
	 * Atualiza o desconto associado ao pacote (incluindo na base de dados).
	 *
	 * @param pacote Objeto pacote já com as informações novas
	 */
	public void atualizarDesconto(Pacote pacote) {
		pacoteDAO.updateDesconto(pacote);
	}

	@Override
	public String toString() {
		return "Pacote{" +
				"id=" + id +
				", desconto=" + desconto +
				'}';
	}
}
