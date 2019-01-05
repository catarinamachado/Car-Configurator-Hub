package CCH.business;

import CCH.dataaccess.ComponenteDAO;
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
	private ComponenteDAO componenteDAO = new ComponenteDAO();

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
		this.id = id;
		this.desconto = desconto;
	}

	public Pacote(List<String> rs){
		this.id = Integer.valueOf(rs.get(0));
		this.desconto = Double.valueOf(rs.get(1));
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
		return componenteDAO.getComponentesPacote(id);
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

	/*fora*********************************************************************/
	public void adicionaComponente(int componenteId) throws ComponenteJaExisteNoPacoteException,
															ComponenteIncompativelNoPacoteException {

		Map<Integer,Componente> mComp =  componenteDAO.getComponentesPacote(this.id);

		boolean alreadyHas = mComp.containsKey(componenteId);

		if (alreadyHas)
			throw new ComponenteJaExisteNoPacoteException();

		for (Componente c : mComp.values()) {
			if (c.getIncompativeis() != null && c.getIncompativeis().containsKey(componenteId))
				throw new ComponenteIncompativelNoPacoteException(c.getFullName());

		}

		componenteDAO.adicionaComponentePacote(this.id, componenteId);
	}

	/**
	 * Método que remove um determinado componente presente no pacote.
	 *
	 * @param componenteId Id do componente que se pretende remover
	 */

	/*fora*********************************************************************/
	public void removeComponente(int componenteId) {
		componenteDAO.removeComponentePacote(this.id, componenteId);
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


	@Override
	public String toString() {
		return "Pacote{" +
				"id=" + id +
				", desconto=" + desconto +
				'}';
	}
}
