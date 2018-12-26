package CCH.business;

import CCH.dataaccess.ConfiguracaoDAO;
import CCH.dataaccess.EncomendaDAO;
import CCH.exception.EncomendaRequerOutrosComponentes;
import CCH.exception.EncomendaTemComponentesIncompativeis;

import java.util.*;

public class GestaoDeConfiguracao {

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

	public Configuracao criarConfiguracao() {
		Configuracao configuracao = new Configuracao(configuracoes.getNextId(), 0, 0);
		configuracao = configuracoes.put(configuracao.getId(), configuracao);
		return configuracao;
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
}