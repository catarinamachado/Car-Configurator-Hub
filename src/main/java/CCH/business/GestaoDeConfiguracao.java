package CCH.business;

import CCH.dataaccess.ConfiguracaoDAO;
import CCH.dataaccess.EncomendaDAO;
import CCH.exception.EncomendaRequerOutrosComponentes;
import CCH.exception.EncomendaTemComponentesIncompativeis;
import CCH.exception.NoOptimalConfigurationException;
import ilog.concert.IloException;

import java.util.*;
import java.util.stream.Collectors;

public class GestaoDeConfiguracao {
	private Configuracao confatual;
	private ConfiguracaoDAO configuracoes;
	private EncomendaDAO encomendas;

	public GestaoDeConfiguracao() {
		this.configuracoes = new ConfiguracaoDAO();
		this.encomendas = new EncomendaDAO();
	}

	public ConfiguracaoDAO getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(ConfiguracaoDAO configuracoes) {
		this.configuracoes = configuracoes;
	}

	public void criarConfiguracao() {
		Configuracao configuracao = new Configuracao(configuracoes.getNextId(), 0, 0);
		this.confatual = configuracao;
	}

	/**
	 *
	 * @param configuracaoId
	 */
	public void removerConfiguracao(int configuracaoId) {
		configuracoes.remove(configuracaoId);
	}

	public List<Configuracao> consultarConfiguracoes() {
		return new ArrayList<>(configuracoes.values());
	}

	public Collection<Componente> getComponentes(int configuracaoId) {
		return configuracoes.getComponentes(configuracaoId).values();
	}

	public void removerComponente(int configuracaoId, int componenteId) {
		configuracoes.removeComponente(configuracaoId, componenteId);
	}

	public void criarEncomenda(
				Configuracao configuracao,
				String nomeCliente,
				String numeroDeIdentificacaoCliente,
				String moradaCliente,
				String paisCliente,
				String emailCliente
	) throws EncomendaRequerOutrosComponentes, EncomendaTemComponentesIncompativeis {
		Map<Integer, Componente> componentes = configuracao.verificaValidade();

		int id = encomendas.getNextId();
		Encomenda encomenda = new Encomenda(componentes, id, configuracao.getPreco(), nomeCliente, numeroDeIdentificacaoCliente, moradaCliente, paisCliente, emailCliente);
		encomendas.put(id, encomenda);
	}

	public Configuracao configuracaoOtima(Collection<Pacote> pacs, Collection<Componente> comps, double valor) throws NoOptimalConfigurationException {
		if (valor<0)
			throw new NoOptimalConfigurationException("Negative Value");
		ConfiguracaoOtima c = new ConfiguracaoOtima();
		Collection<Componente> componentesObrigatorios = confatual.getComponentes().values();
		try {
			return c.configuracaoOtima(componentesObrigatorios,comps,pacs,valor);
		} catch (IloException e) {
			e.printStackTrace();
			throw new NoOptimalConfigurationException();
		}
	}

	public void guardarConfiguracao() {
		int id = confatual.getId();
		configuracoes.put(id,confatual);
	}


	public Configuracao getConfiguracaoAtual() {
		return confatual;
	}

	public void updateConfiguracao(Configuracao c) {
		confatual = c;
	}

}