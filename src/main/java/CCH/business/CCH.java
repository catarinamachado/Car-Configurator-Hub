package CCH.business;

import CCH.dataaccess.ComponenteDAO;
import CCH.dataaccess.PacoteDAO;
import CCH.dataaccess.UtilizadorDAO;
import CCH.exception.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe principal da aplicação Car Configurator Hub.
 *
 * @version 20181229
 */

public class CCH {
	private GestaoDeConfiguracao gestaoDeConfiguracao;
	private OperacaoFabril operacaoFabril;
	private UtilizadorDAO utilizadorDAO;
	private PacoteDAO pacoteDAO;
	private ComponenteDAO componenteDAO;

	/**
	 * Construtor por omissão de CCH.
	 */
	public CCH() {
		this.operacaoFabril = new OperacaoFabril();
		this.gestaoDeConfiguracao = new GestaoDeConfiguracao();
		this.utilizadorDAO = new UtilizadorDAO();
		this.pacoteDAO = new PacoteDAO();
		this.componenteDAO = new ComponenteDAO();
	}

	/**
	 * Devolve a GestaoDeConfiguracao, que possui as configurações e encomendas
	 * do sistema.
	 *
	 * @return GestaoDeConfiguracao
	 */
	public GestaoDeConfiguracao getGestaoDeConfiguracao() {
		return gestaoDeConfiguracao;
	}

	/**
	 * Atualiza a GestaoDeConfiguracao do sistema.
	 *
	 * @param gestaoDeConfiguracao GestaoDeConfiguracao com as informações
	 * das configurações e encomendas
	 */
	public void setGestaoDeConfiguracao(GestaoDeConfiguracao gestaoDeConfiguracao) {
		this.gestaoDeConfiguracao = gestaoDeConfiguracao;
	}

	/**
	 * Devolve a OperacaoFabril, que possui as informações dos componentes e das
	 * encomendas.
	 *
	 * @return OperacaoFabril
	 */
	public OperacaoFabril getOperacaoFabril() {
		return operacaoFabril;
	}

	/**
	 * Atualiza a OperacaoFabril do sistema.
	 *
	 * @param operacaoFabril OperacaoFabril com as informações dos componentes e encomendas
	 */
	public void setOperacaoFabril(OperacaoFabril operacaoFabril) {
		this.operacaoFabril = operacaoFabril;
	}

	/**
	 * Devolve o UtilizadorDAO, que permite aceder às
	 * informações dos utilizadores na base de dados.
	 *
	 * @return UtilizadorDAO
	 */
	public UtilizadorDAO getUtilizadorDAO() {
		return utilizadorDAO;
	}

	/**
	 * Atualiza o UtilizadorDAO do sistema.
	 *
	 * @param utilizadorDAO UtilizadorDAO com os devidos métodos para aceder à base de dados.
	 */
	public void setUtilizadorDAO(UtilizadorDAO utilizadorDAO) {
		this.utilizadorDAO = utilizadorDAO;
	}

	/**
	 * Devolve o PacoteDAO, que permite aceder às
	 * informações dos pacotes na base de dados.
	 *
	 * @return PacoteDAO
	 */
	public PacoteDAO getPacoteDAO() {
		return pacoteDAO;
	}

	/**
	 * Atualiza o PacoteDAO do sistema.
	 *
	 * @param pacoteDAO PacoteDAO com os devidos métodos para aceder à base de dados.
	 */
	public void setPacoteDAO(PacoteDAO pacoteDAO) {
		this.pacoteDAO = pacoteDAO;
	}

	/**
	 * Devolve o ComponenteDAO, que permite aceder às
	 * informações dos componentes na base de dados.
	 *
	 * @return ComponenteDAO
	 */
	public ComponenteDAO getComponenteDAO() {
		return componenteDAO;
	}

	/**
	 * Atualiza o ComponenteDAO do sistema.
	 *
	 * @param componenteDAO ComponenteDAO com os devidos métodos para aceder à base de dados.
	 */
	public void setComponenteDAO(ComponenteDAO componenteDAO) {
		this.componenteDAO = componenteDAO;
	}

	/**
	 * Método que permite que o utilizador aceda à aplicação.
	 *
	 * @param id Id do utilizador
	 * @param password Password do utilizador
	 * @return Utilizador que iniciou sessão
	 * @throws WrongCredentialsException Caso o par das credenciais inseridas
	 * não corresponda a nenhum utilizador registado na aplicação
	 */
	public Utilizador iniciarSessao(int id, String password) throws WrongCredentialsException {
		Utilizador utilizador = utilizadorDAO.get(id);
		if (utilizador == null)
			throw new WrongCredentialsException();

		boolean loggedIn = utilizador.validarCredenciais(id, password);
		if (!loggedIn)
			throw new WrongCredentialsException();

		return utilizador;
	}

	/**
	 * Método que cria um novo pacote no sistema.
	 *
	 * @return Pacote criado
	 */
	public Pacote criarPacote() {
		Pacote pacote = new Pacote();
		pacote = pacoteDAO.put(pacote.getId(), pacote);
		return pacote;
	}

	/**
	 * Método que remove um pacote do sistema.
	 *
	 * @param pacoteId Id do pacote que se pretende eliminar
	 */
	public void removerPacote(int pacoteId) {
		gestaoDeConfiguracao.removePacote(pacoteId, pacoteDAO.getDescontoPacote(pacoteId));

		pacoteDAO.removeAllComponentes(pacoteId);
		pacoteDAO.remove(pacoteId);
	}

	/**
	 * Método que cria um novo utilizador no sistema.
	 *
	 * @return Utilizador criado
	 */
	public Utilizador criarUtilizador() {
		Utilizador utilizador = new Utilizador("empty", "empty");
		utilizador = utilizadorDAO.put(utilizador.getId(), utilizador);
		return utilizador;
	}

	/**
	 * Método que remove um utilizador do sistema.
	 *
	 * @param utilizadorId Id do utilizador que se pretende eliminar
	 */
	public void removerUtilizador(int utilizadorId) {
		utilizadorDAO.remove(utilizadorId);
	}

	/**
	 * Método que devolve todos os utilizadores no sistema.
	 *
	 * @return List<Utilizador> Lista de todos os utilizadores no sistema
	 */
	public List<Utilizador> consultarFuncionarios() {
		return new ArrayList<>(utilizadorDAO.values());
	}

	/**
	 * Método que devolve todos os pacotes no sistema.
	 *
	 * @return List<Pacote> Lista de todos os pacotes no sistema
	 */
	public List<Pacote> consultarPacotes() {
		return new ArrayList<>(pacoteDAO.values());
	}

	/**
	 * Método que devolve todos os componentes no sistema.
	 *
	 * @return List<Componente> Lista de todos os componentes no sistema
	 */
	public List<Componente> consultarComponentes() {
		return new ArrayList<>(componenteDAO.values());
	}

	/**
	 * Método que gera uma configuração ótima, ou seja, uma configuração que tenta
	 * maximizar a utilização do dinheiro previsto.
	 *
	 * @param configuracao Configuração com o ponto de partida para se gerar a
	 * configuração ótima
	 * @param valor Valor máximo que o cliente está disposto a gastar
	 * @return Configuracao ótima gerada
	 * @throws NoOptimalConfigurationException Caso não exista nenhuma configuração
	 * ótima tendo em consideração os parâmetros fornecidos
	 * @throws ConfiguracaoNaoTemObrigatoriosException Caso a configuração não
	 * contenha os componentes básicos (obrigatórios)
	 */
	public Configuracao ConfiguracaoOtima(Configuracao configuracao, double valor) throws NoOptimalConfigurationException, ConfiguracaoNaoTemObrigatoriosException {
		Collection<Pacote> pacs = pacoteDAO.values();
		Collection<Componente> comps = componenteDAO.values();
		return gestaoDeConfiguracao.configuracaoOtima(comps,pacs,configuracao,valor);
	}

	/**
	 * Método que devolve os componentes dentro de um determinado pacote.
	 *
	 * @param pacote_id Id do pacote em questão
	 * @return List<Componente> Lista dos componentes no pacote
	 */
	public List<Componente> consultarComponentesNoPacote(int pacote_id) {
		return new ArrayList<>(pacoteDAO.getAllComponentesNoPacote(pacote_id));
	}

	/**
	 * Método que remove um determinado componente de um determinado pacote.
	 *
	 * @param pacote Pacote em questão
	 * @param componente_id Id do componente que se pretende eliminar do pacote
	 */
	public void removerComponenteDoPacote(Pacote pacote, int componente_id) {
		pacote.removeComponente(componente_id);
	}

	/**
	 * Método que adiciona um determinado componente a um determinado pacote.
	 *
	 * @param pacote Pacote em questão
	 * @param componente_id Id do componente que se pretende adicionar ao pacote
	 * @throws ComponenteJaExisteNoPacoteException Caso o componente que se
	 * pretende adicionar já esteja presente no pacote
	 * @throws ComponenteIncompativelNoPacoteException Caso o pacote contenha um
	 * componente incompatível com o componente que se pretende adicioanr
	 */
	public void adicionarComponenteAoPacote(Pacote pacote, int componente_id) throws ComponenteJaExisteNoPacoteException, ComponenteIncompativelNoPacoteException {
		pacote.adicionaComponente(componente_id);
	}
}

