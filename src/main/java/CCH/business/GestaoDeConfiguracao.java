package CCH.business;

import CCH.dataaccess.ConfiguracaoDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GestaoDeConfiguracao {

	private ConfiguracaoDAO configuracoes;

	public GestaoDeConfiguracao() {
		this.configuracoes = new ConfiguracaoDAO();
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
}