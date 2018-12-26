package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.PacoteDAO;
import CCH.exception.ComponenteIncompativelNoPacoteException;
import CCH.exception.ComponenteJaExisteNoPacoteException;

import java.util.List;

public class Pacote {
	private ComponenteDAO componentes;
	private int id;
	private double desconto;

	private PacoteDAO pacoteDAO = new PacoteDAO();

	public Pacote(int id, double desconto) {
		this.id = id;
		this.desconto = desconto;
		this.componentes = new ComponenteDAO();
	}

	public Pacote() {
		this.id = pacoteDAO.getNextId();
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

	public ComponenteDAO getComponentes() {
		return componentes;
	}

	public void setComponentes(ComponenteDAO componentes) {
		this.componentes = componentes;
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

	public List<Integer> idsComponentes() {
		return null;
	}

}