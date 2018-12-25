package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.EncomendaDAO;
import CCH.exception.SemEncomendasDisponiveisException;

import java.util.*;

public class OperacaoFabril {

	private EncomendaDAO encomendaDAO;
	private ComponenteDAO componenteDAO;

	public OperacaoFabril() {
		this.encomendaDAO = new EncomendaDAO();
		this.componenteDAO = new ComponenteDAO();
	}

	public List<Componente> consultarComponentes() {
		return new ArrayList<>(componenteDAO.values());
	}

	public Encomenda consultarProximaEncomenda() throws SemEncomendasDisponiveisException {
		Collection<Encomenda> sorted = new TreeMap<>(encomendaDAO.getAll()).values();

		for (Encomenda encomenda : sorted) {
			boolean available = true;
			Collection<Componente> componentes = encomendaDAO.getComponentes(encomenda.getId()).values();

			for (Componente componente : componentes) {
				if (componente.getStock() <= 0) {
					available = false;
					break;
				}
			}

			if (available) {
				return encomenda;
			}
		}

		throw new SemEncomendasDisponiveisException();
	}

	/**
	 *
	 * @param id
	 */
	public void removerEncomenda(Integer id) {
		encomendaDAO.remove(id);
	}

	public Encomenda atualizarStock(Componente componente) throws SemEncomendasDisponiveisException  {
		componenteDAO.updateStock(componente);
		return consultarProximaEncomenda();
	}
}