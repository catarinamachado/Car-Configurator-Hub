package CCH.business;

import CCH.dataaccess.ClasseComponenteDAO;
import CCH.dataaccess.ConfiguracaoDAO;
import CCH.dataaccess.EncomendaDAO;
import CCH.dataaccess.RemoteClass;
import CCH.exception.*;
import ilog.concert.IloException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe que representa as informações essenciais para que
 * uma configuração possa progredir.
 *
 * @version 20181229
 */

public class GestaoDeConfiguracao {
	private ConfiguracaoDAO configuracoes;
	private EncomendaDAO encomendas;
	private Configuracao configuracaoatual;
    /**
     * Construtor por omissão da GestaoDeConfiguracao.
     */
	public GestaoDeConfiguracao() {
		this.configuracoes = new ConfiguracaoDAO();
		this.encomendas = new EncomendaDAO();
	}


	/**
	 * Método que cria uma nova configuração, com as informações default, no
	 * sistema.
	 */

	public void criarConfiguracao() {
		Configuracao configuracao = new Configuracao(configuracoes.getNextId(), 0, 0);
		configuracoes.put(configuracao.getId(), configuracao);
	}

	/**
	 * Método que remove a configuração com o id passado como parâmetro
	 * do sistema.
	 *
	 * @param configuracaoId Id da configuração que se pretende remover
	 */
	public void removerConfiguracao(int configuracaoId) {
		configuracoes.remove(configuracaoId);
	}

	/**
	 * Método que devolve todas as configurações presentes no sistema.
	 *
	 * @return List<Configuracao> Lista com todas as configurações no sistema
	 */
	public List<Configuracao> consultarConfiguracoes() {
		return new ArrayList<>(configuracoes.getAllConfiguracao().values());
	}

	/**
	 * Método que cria uma nova encomenda no sistema.
	 *
	 * @param nomeCliente Nome do cliente a que a encomenda corresponde
	 * @param numeroDeIdentificacaoCliente Número de Identificação do cliente
	 * @param moradaCliente Morada do cliente
	 * @param paisCliente País do cliente
	 * @param emailCliente E-mail do cliente
	 * @throws EncomendaTemComponentesIncompativeis Se a configuração tem componentes
	 * incompatíveis
	 * @throws EncomendaRequerOutrosComponentes Se existem componentes na configuração
	 * que requerem outros componentes que não estão presentes na mesma
	 * @throws EncomendaRequerObrigatoriosException Se a configuração não tem todos
	 * os componentes obrigatórios
	 */
	public void criarEncomenda(
            String nomeCliente,
            String numeroDeIdentificacaoCliente,
            String moradaCliente,
            String paisCliente,
            String emailCliente
    ) throws EncomendaRequerOutrosComponentes, EncomendaTemComponentesIncompativeis, EncomendaRequerObrigatoriosException {
		Map<Integer, Componente> componentes = verificaValidade();
		int id = encomendas.getNextId();
		Encomenda encomenda = new Encomenda(componentes, id,
											configuracaoatual.getPreco(), nomeCliente,
											numeroDeIdentificacaoCliente, moradaCliente,
											paisCliente, emailCliente);
		encomendas.put(id, encomenda);
	}

	/**
	 * Método que gera uma configuração ótima, ou seja, uma configuração que tenta
	 * maximizar a utilização do dinheiro previsto.
	 *
	 * @param componentes Todos os componentes presentes no sistema
	 * @param pacotes Todos os pacotes presentes no sistema
	 * @param valor Valor máximo disponível
	 * @return Configuracao ótima gerada
	 * @throws NoOptimalConfigurationException Caso a configuração que se pretende
	 * otimizar contenha componentes incompatíveis
	 * @throws ConfiguracaoNaoTemObrigatoriosException Caso a configuração que se pretende
	 * otimizar não contenha todos os componentes obrigatórios
	 */
	public Configuracao configuracaoOtima(Collection<Componente> componentes, Collection<Pacote> pacotes, double valor) throws NoOptimalConfigurationException, ConfiguracaoNaoTemObrigatoriosException {
		if (valor < 0) {
			throw new NoOptimalConfigurationException("Negative Value");
		}

		try {
			verificaValidade();
		} catch (EncomendaTemComponentesIncompativeis encomendaTemComponentesIncompativeis) {
			throw new NoOptimalConfigurationException();
		} catch (EncomendaRequerOutrosComponentes encomendaRequerOutrosComponentes) {
		} catch (EncomendaRequerObrigatoriosException e) {
			throw new ConfiguracaoNaoTemObrigatoriosException();
		}

		ConfiguracaoOtima c = new ConfiguracaoOtima();

    
		Collection<Componente> componentesObrigatorios = configuracoes.getComponentes(configuracaoatual.getId()).values();

		try {
			return c.configuracaoOtima(componentesObrigatorios,componentes,pacotes,valor);
		} catch (IloException e) {
			throw new NoOptimalConfigurationException();
		}
	}

	/**
	 * Método que remove um determinado pacote da configuração.
	 *
	 * @param pacoteId Id do pacote que se pretende remover da configuração
	 * @param desconto Desconto associado ao pacote
	 */
	public void removePacote (int pacoteId, double desconto){
		List <Integer> configuracoesId = configuracoes.getAllIdsConfiguracoesComOPacote(pacoteId);

		configuracoes.removePacoteNasConfiguracoes(pacoteId);

		for(Integer configuracaoId : configuracoesId) {
			double descontoAtualizado =
					configuracoes.getDescontoConfiguracao(configuracaoId) - desconto;

			configuracoes.updateDesconto(configuracaoId, descontoAtualizado);
		}
	}

	public void removerComponente(int id) {

		Componente componente = configuracoes.removeComponente(configuracaoatual.getId(), id);

		configuracaoatual.setPreco(configuracaoatual.getPreco() - componente.getPreco());

		for (RemoteClass<Integer> pac : configuracoes.getPacotes(id).values()) {
			Pacote pacote = (Pacote)pac;
			if (pacote.getComponentes().containsKey(id)) {
				configuracaoatual.setDesconto(configuracaoatual.getDesconto() - pacote.getDesconto());
				configuracoes.removePacote(configuracaoatual.getId(), pacote.getId());
			}
		}

		configuracoes.put(configuracaoatual.getId(), configuracaoatual);
	}

	/**
	 * Método que remove um determinado pacote da configuração.
	 *
	 * @param id Id do pacote que se pretende remover da configuração
	 */
	public void removerPacoteConfig(int id) {
		if(configuracaoatual.updateOnPacoteRemove(id)) {
			configuracoes.removePacote(configuracaoatual.getId(),id);
			configuracoes.put(configuracaoatual.getId(), configuracaoatual);
		}
	}

	public List<Componente> componentesRequeremMeNaConfig(int id) {
		List<Componente> requeridos = new ArrayList<>();

		for (Componente componenteRequerMe : this.consultarComponentes().values()) {
			if (componenteRequerMe.getRequeridos().containsKey(id))
				requeridos.add(componenteRequerMe);
		}

		return requeridos;
	}

	public Configuracao getConfigAtual() {
		return configuracaoatual;
	}

	public List<Componente> componentesRequeridosQueNaoEstaoConfig(Map<Integer, Componente> comps) {
		List<Componente> requeridos = new ArrayList<>();

		for (Componente c : comps.values()) {
			for (Componente componenteRequerido : c.getRequeridos().values()) {
				if (!this.consultarComponentes().containsKey(componenteRequerido.getId())) {
					requeridos.add(componenteRequerido);
				}
			}
		}

		return requeridos;
	}

	public List<Componente> componentesIncompativeisNaConfig(Map<Integer, Componente> comps) {
		List<Componente> incompativeis = new ArrayList<>();

		for (Componente c : comps.values()) {
			for (Componente componenteIncomp : c.getIncompativeis().values()) {
				if (this.consultarComponentes().containsKey(componenteIncomp.getId())) {
					incompativeis.add(componenteIncomp);
				}
			}
		}
		return incompativeis;
	}

	public void loadConfigAtual(int id) {
		configuracaoatual = configuracoes.get(id);
	}

	/**
	 * Verifica se a configuração se encontra válida e pronta para ser encomendada,
	 * ou seja, se tem todos os componentes obrigatórios, se não tem componentes
	 * incompatíveis nem faltam componentes que outros requerem.
	 * Se a configuração estiver válida retorna todos os componentes da mesma,
	 * se estiver inválida lança a devida exceção.
	 *
	 * @return Map<Integer, Componente> todos os componentes que constituem a configuração
	 * @throws EncomendaTemComponentesIncompativeis Se a configuração tem componentes
	 * incompatíveis
	 * @throws EncomendaRequerOutrosComponentes Se existem componentes na configuração
	 * que requerem outros componentes que não estão presentes na mesma
	 * @throws EncomendaRequerObrigatoriosException Se a configuração não tem todos
	 * os componentes obrigatórios
	 */
	public Map<Integer, Componente> verificaValidade() throws EncomendaTemComponentesIncompativeis,
			EncomendaRequerOutrosComponentes,
			EncomendaRequerObrigatoriosException {
		Map<Integer, Componente> componentes = configuracoes.getComponentes(configuracaoatual.getId());
		temIncompativeis(componentes);
		requerOutros(componentes);

		if (!this.temComponentesObrigatorios())
			throw new EncomendaRequerObrigatoriosException();

		return componentes;
	}

	/**
	 * Verifica se a configuração tem componentes incompatíveis. Em caso afirmativo,
	 * lança a devida exceção.
	 *
	 * @param componentes Componentes presentes na configuração
	 * @throws EncomendaTemComponentesIncompativeis Se a configuração tem componentes
	 * incompatíveis
	 */
	private void temIncompativeis(Map<Integer, Componente> componentes) throws EncomendaTemComponentesIncompativeis {
		Map<Integer, Componente> incompativeis = new HashMap<>();

		componentes.forEach((k, c) ->
				incompativeis.putAll(
						c.getIncompativeis()
				)
		);

		for (Componente componente : componentes.values()) {
			if (incompativeis.containsKey(componente.getId())) {
				throw new EncomendaTemComponentesIncompativeis(
						componente.getFullName() + " é incompatível com outros componentes."
				);
			}
		}
	}

	/**
	 * Verifica se estão presentes na configuração todos os componentes
	 * que os componentes que fazem parte da configuração requerem.
	 * Em caso afirmativo, lança a devida exceção.
	 *
	 * @param componentes Componentes presentes na configuração
	 * @throws EncomendaRequerOutrosComponentes Se existem componentes na configuração
	 * que requerem outros componentes que não estão presentes na mesma
	 */
	private void requerOutros(Map<Integer, Componente> componentes) throws EncomendaRequerOutrosComponentes {
		Map<Integer, Componente> requeridos = new HashMap<>();
		componentes.forEach((k,c) ->
				requeridos.putAll(
						c.getRequeridos()
				)
		);

		Collection<Componente> requeridosValues = requeridos.values();

		for (Componente req : requeridosValues) {
			if (!componentes.keySet().contains(req.getId()))
				throw new EncomendaRequerOutrosComponentes();
		}
	}

	public Map<Integer, Componente> consultarComponentes() {
		return configuracoes.getComponentes(configuracaoatual.getId());
	}

	/**
	 * Indica se a configuração tem todos os componentes básicos (obrigatórios).
	 *
	 * @return boolean true se todos os componentes obrigatórios estão presentes
	 * na configuração, false caso contrário.
	 */
	public boolean temComponentesObrigatorios() {
		ClasseComponenteDAO cdao = new ClasseComponenteDAO();
		List<Integer> idsTiposObrigatorios = cdao.values().stream().map(p -> (ClasseComponente)p).
				filter(ClasseComponente :: getEObrigatorio).
				map(ClasseComponente:: getId).collect(Collectors.toList());
		Collection<Integer> idsTiposNaClasse = this.consultarComponentes().values().stream().
				map(c -> c.getClasseComponente().getId()).collect(Collectors.toSet());

		return idsTiposNaClasse.containsAll(idsTiposObrigatorios);
	}

	/**
	 * Método que adiciona um novo componente à configuração.
	 *
	 * @param componenteId Id do componente que se pretende adicionar à configuração
	 * @return Componente adicionado à configuração
	 * @throws ComponenteJaAdicionadoException Caso o componente que se pretende
	 * adicionar já esteja na configuração
	 */
	public Componente adicionarComponente(int componenteId) throws ComponenteJaAdicionadoException {
		if (configuracoes.getComponentes(configuracaoatual.getId()).containsKey(componenteId)) {
			throw new ComponenteJaAdicionadoException();
		}
		Componente componente = configuracoes.addComponente(configuracaoatual.getId(), componenteId);
		configuracaoatual.setPreco(configuracaoatual.getPreco() + componente.getPreco());
		configuracoes.put(configuracaoatual.getId(), configuracaoatual);

		return componente;
	}

	/**
	 * Devolve todos os pacotes presentes na configuração.
	 *
	 * @return Map<Integer, Pacote> com o par chave valor de cada um dos pacotes
	 * na configuração
	 */
	public Map<Integer, Pacote> consultarPacotes() {
		return configuracoes.getPacotes(configuracaoatual.getId());
	}

	/**
	 * Método que adiciona um novo pacote à configuração.
	 *
	 * @param pacoteId Id do pacote que se pretende adicionar à configuração
	 * @param replacedPacote Pacote que será substituído pelo outro pacote
	 * @return Pacote adicionado à configuração
	 * @throws PacoteJaAdicionadoException Caso o pacote que se pretende
	 * adicionar já esteja na configuração
	 */
	public Pacote adicionarPacote(int pacoteId, Pacote replacedPacote) throws PacoteJaAdicionadoException {
		Map<Integer, Pacote> pacotes = configuracoes.getPacotes(configuracaoatual.getId());
		Pacote pacote;

		if (pacotes.containsKey(pacoteId)) {
			throw new PacoteJaAdicionadoException();
		}

		if (replacedPacote == null) {
			pacote = configuracaoatual.conflito(pacotes, pacoteId);

			if (pacote != null) {
				return pacote;
			}
		} else {
			configuracaoatual.setDesconto(configuracaoatual.getDesconto() - replacedPacote.getDesconto());
			configuracoes.removePacote(configuracaoatual.getId(), replacedPacote.getId());
		}

		pacote = configuracoes.addPacote(configuracaoatual.getId(), pacoteId);
		configuracaoatual.setDesconto(configuracaoatual.getDesconto() + pacote.getDesconto());

		Set<Integer> componentes = consultarComponentes().keySet();
		pacote.getComponentes().forEach((k,c) -> {
			if (!componentes.contains(c.getId())) {
				configuracoes.addComponente(configuracaoatual.getId(), c.getId());
				configuracaoatual.setPreco(configuracaoatual.getPreco() + c.getPreco());
			}
		});

		configuracoes.update(configuracaoatual.getId(), configuracaoatual);

		return null;
	}

	/**
	 * Averigua se os componentes presentes na configuração constituem um
	 * pacote. Em caso afirmativo, substitui os componentes isolados pelo
	 * pacote.
	 */
	public boolean checkforPacotesInConfiguration(){
		Collection<RemoteClass<Integer>> pacotes = configuracaoatual.getPacotes();
		Map<Integer,Componente> compsNotInPacotes = this.componentesNotInPacotes();
		for (RemoteClass r:pacotes) {
			Pacote p = (Pacote)r;
			Collection<Componente> comps = p.getComponentes().values();
			boolean containsPacote = true;
			for (Componente c : comps) {
				containsPacote = containsPacote && compsNotInPacotes.containsKey(c.getId());
			}

			if (containsPacote && comps.size()!=0) {
				try {
					for (Componente c : comps)
						compsNotInPacotes.remove(c.getId());
					this.adicionarPacote(p.getId(),null);
					return true;
				} catch (PacoteJaAdicionadoException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	/**
	 * Método que devolve os componentes que fazem parte da configuração mas
	 * que não fazem parte de nenhum pacote contido na configuração.
	 *
	 * @return Map<Integer, Componente> componentes
	 */
	public Map<Integer, Componente> componentesNotInPacotes() {
		Map<Integer, Componente> componentes = this.consultarComponentes();
		Map<Integer, Pacote> pacotes = this.consultarPacotes();

		for (Pacote p : pacotes.values()) {
			for (Componente c : p.getComponentes().values()) {
				componentes.remove(c.getId());
			}
		}

		return componentes;
	}

	/**
	 * Construtor de Configuração que tem como objetivo construir uma nova configuração
	 * a partir da configuração ótima gerada mais rapidamente
	 *
	 * @param pacotesAceites Lista dos pacotes presentes na configuração
	 * @param componentesAceites Lista dos componentes presentes na configuração
	 */
	public Configuracao newConfiguracao(List<Pacote> pacotesAceites, List<Componente> componentesAceites) {
		Configuracao configuracao = new Configuracao();
		configuracoes.put(configuracao.getId(), configuracao);

		try {
			for (Pacote p : pacotesAceites) {
				adicionarPacote(p.getId(), null);
			}
			for (Componente c : componentesAceites) {
				adicionarComponente(c.getId());
			}
		} catch (ComponenteJaAdicionadoException | PacoteJaAdicionadoException e) {
		}

		return configuracao;
	}
}
