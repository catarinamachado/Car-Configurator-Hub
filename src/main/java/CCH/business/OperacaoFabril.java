package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.EncomendaDAO;
import CCH.exception.SemEncomendasDisponiveisException;
import CCH.exception.StockInvalidoException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Operação Fabril representa as informações que os
 * funcionários da fábrica necessitam de conhecer.
 *
 * @version 20181229
 */

public class OperacaoFabril {
	private EncomendaDAO encomendaDAO;
	private ComponenteDAO componenteDAO;

	/**
	 * Construtor por omissão da OperacaoFabril.
	 */
	public OperacaoFabril() {
		this.encomendaDAO = new EncomendaDAO();
		this.componenteDAO = new ComponenteDAO();
	}

	/**
	 * Devolve todos os componentes do sistema.
	 *
	 * @return List<Componente> lista com todos os componentes presentes no sistema
	 */
	public List<Componente> consultarComponentes() {
		return new ArrayList<>(componenteDAO.values());
	}

	/**
	 * Devolve a próxima encomenda que está pronta a ser produzida (existindo
	 * em stock todos os componentes necessários para a produção da encomenda).
	 *
	 * @return Encomenda pronta a produzir
	 * @throws SemEncomendasDisponiveisException Caso não exista nenhuma encomenda disponível,
	 * por exemplo, se simplesmente não existir nenhuma encomenda para produzir ou
	 * se não houver em stock algum dos componentes necessários para a encomenda
	 */
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
	 * Método que remove uma encomenda (incluindo da base de dados).
	 *
	 * @param id Id da encomenda que se pretende remover
	 */
	public void removerEncomenda(Integer id) {
		for (Componente c : encomendaDAO.getComponentes(id).values())
			c.decrementaStock();

		encomendaDAO.remove(id);
	}

	/**
	 * Atualiza o stock de um determinado componente (incluindo na base de dados),
	 * por exemplo, caso algum componente se parta ou esteja esquecido na Fábrica.
	 *
	 * @param componente Objeto componente já com as informações novas
	 * @throws SemEncomendasDisponiveisException Caso esta atualização não altere
	 * as encomendas disponíveis a produzir.
	 */
	public Encomenda atualizarStock(Componente componente) throws SemEncomendasDisponiveisException, StockInvalidoException {
		if (componente.getStock() < 0)
			throw new StockInvalidoException();
    
		componenteDAO.updateStock(componente);

		return consultarProximaEncomenda();
	}
}