package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.PacoteDAO;
import CCH.exception.ComponenteIncompativelNoPacoteException;
import CCH.exception.ComponenteJaExisteNoPacoteException;

import java.util.Map;

public class Pacote {
	private int id;
	private double desconto;
	private PacoteDAO pacoteDAO = new PacoteDAO();


	public Pacote(int id, double desconto) {
		this.id = id;
		this.desconto = desconto;
	}

	public Pacote() {
		this.desconto = 0.0;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDesconto() {
		return this.desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public Map<Integer, Componente> getComponentes() {
		return pacoteDAO.getComponentes(id);
	}

	/**
	 *
	 * @param componenteId
	 */
	public void adicionaComponente(int componenteId) throws ComponenteJaExisteNoPacoteException, ComponenteIncompativelNoPacoteException {
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
	 *
	 * @param componenteId
	 */
	public void removeComponente(int componenteId) {
		pacoteDAO.removeComponente(this.id, componenteId);
	}

	public String getNome() {
		return "Pacote " + id;
	}

	public String getDescontoString() {
		return Double.toString(this.desconto);
	}

	public void atualizarDesconto(Pacote pacote) {
		pacoteDAO.updateDesconto(pacote);
	}
}
