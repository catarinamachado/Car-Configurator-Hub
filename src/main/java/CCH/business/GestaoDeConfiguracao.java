package CCH.business;

import CCH.dataaccess.ConfiguracaoDAO;

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
		// TODO - implement GestaoDeConfiguracao.criarConfiguracao
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param configuracaoId
	 */
	public void removerConfiguracao(int configuracaoId) {
		// TODO - implement GestaoDeConfiguracao.removerConfiguracao
		throw new UnsupportedOperationException();
	}

}