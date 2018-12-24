package CCH.business;

import CCH.dataaccess.*;

import java.util.List;

public class CCH {

	private GestaoDeConfiguracao gestao_de_configuracao;
	private UtilizadorDAO utilizadores;
	private OperacaoFabril operacao_fabril;

	/**
	 * 
	 * @param id
	 * @param password
	 */
	public void iniciarSessao(int id, String password) {
		// TODO - implement CCH.iniciarSessao
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param pacote
	 */
	public void criarPacote(Pacote pacote) {
		// TODO - implement CCH.criarPacote
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param pacoteId
	 */
	public void removerPacote(int pacoteId) {
		// TODO - implement CCH.removerPacote
		throw new UnsupportedOperationException();
	}

	public List<Utilizador> consultarFuncionarios() {
		// TODO - implement CCH.consultarFuncionarios
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param nome
	 * @param password
	 * @param tipo
	 */
	public Utilizador criarFuncionario(String nome, String password, TipoUtilizador tipo) {
		// TODO - implement CCH.criarFuncionario
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param utilizadorId
	 */
	public void removerFuncionario(int utilizadorId) {
		// TODO - implement CCH.removerFuncionario
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param componenteId
	 * @param nrComponentes
	 */
	public void atualizarStock(int componenteId, int nrComponentes) {
		// TODO - implement CCH.atualizarStock
		throw new UnsupportedOperationException();
	}

}