package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.PacoteDAO;

import java.util.HashMap;
import java.util.Map;

public class Pacote {

	private ComponenteDAO componentes;
	private int id;
	private double desconto;

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

	public Pacote(int id, double desconto) {
		this.id = id;
		this.desconto = desconto;
		this.componentes = new ComponenteDAO();
	}

	/**
	 *
	 * @param componenteId
	 */
	public Componente adicionarComponente(int componenteId) {
		// TODO - implement Pacote.adicionarComponente
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param componenteId
	 */
	public void removerComponente(int componenteId) {
		// TODO - implement Pacote.removerComponente
		throw new UnsupportedOperationException();
	}

	public Pacote() {
		// TODO - implement Pacote.Pacote
		throw new UnsupportedOperationException();
	}

}