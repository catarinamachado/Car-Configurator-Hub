package CCH.business;

import CCH.dataaccess.ConfiguracaoDAO;
import CCH.exception.EncomendaRequerOutrosComponentes;
import CCH.exception.EncomendaTemComponentesIncompativeis;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilog.concert.IloException;

public class Configuracao {

	private int id;
	private double preco;
	private double desconto;
	private Map<Integer, Componente> componentes;
	private Map<Integer, Pacote> pacotes;

	private ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPreco() {
		return this.preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public double getDesconto() {
		return this.desconto;
	}

	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

	public Map<Integer, Componente> getComponentes() {
		return configuracaoDAO.getComponentes(id);
	}

	public void setComponentes(Map<Integer, Componente> componentes) {
		this.componentes = componentes;
	}

	public Map<Integer, Pacote> getPacotes() {
		return configuracaoDAO.getPacotes(id);
	}

	public void setPacotes(Map<Integer, Pacote> pacotes) {
		this.pacotes = pacotes;
	}

	public Configuracao(int id, double preco, double desconto) {
		this.id = id;
		this.preco = preco;
		this.desconto = desconto;
	}

	public Configuracao() {
		this.id = configuracaoDAO.getNextId();
		this.preco = 0;
		this.desconto = 0;
		componentes = new HashMap<>();
		pacotes = new HashMap<>();
	}

	public Configuracao gerarConfiguracaoOtima(
			List<Componente> componentesObrigatorios,
		   	List<Componente> componentes,
		   	List<Pacote> pacotes,
		   	double money
	) throws IloException {
		ConfiguracaoOtima configuracaoOtima = new ConfiguracaoOtima();
		return configuracaoOtima.configuracaoOtima(componentesObrigatorios, componentes, pacotes, money);
	}

	//Para criar Configuração a partir da configuração otima mais rapidamente
	public Configuracao(List<Pacote> pacotesAceitados, List<Componente> componentesAceitados) {
		this();
		for (Pacote p:pacotesAceitados) {
			desconto += p.getDesconto();
			pacotes.put(p.getId(),p);
		}
		for (Componente c:componentesAceitados) {
			preco += c.getPreco();
			componentes.put(c.getId(),c);
		}
	}

	public Map<Integer, Componente> consultarComponentes() {
		return configuracaoDAO.getComponentes(id);
	}

	/**
	 *
	 * @param componenteId
	 */
	public Componente adiconarComponente(int componenteId) {
		return configuracaoDAO.addComponente(id, componenteId);
	}

	/**
	 *
	 * @param componenteId
	 */
	public void removerComponente(int componenteId) {
		configuracaoDAO.removeComponente(id, componenteId);
	}

	public Map<Integer, Pacote> consultarPacotes() {
		return configuracaoDAO.getPacotes(id);
	}

	/**
	 *
	 * @param pacoteId
	 */
	public void adicionarPacote(int pacoteId) {
		configuracaoDAO.addPacote(id, pacoteId);
	}


	/**
	 *
	 * @param componentes
	 */
	public void aceitarConfiguracaoOtima(List<Componente> componentes) {
		// TODO - implement Configuracao.aceitarConfiguracaoOtima
		throw new UnsupportedOperationException();
	}

	public String getNome() {
		return "Configuração " + id;
	}

	@Override
	public String toString() {
		return "Configuracao{" +
				"id=" + id +
				", preco=" + preco +
				", desconto=" + desconto +
				'}';
	}

	public Map<Integer, Componente> verificaValidade() throws EncomendaTemComponentesIncompativeis, EncomendaRequerOutrosComponentes {
		Map<Integer, Componente> componentes = configuracaoDAO.getComponentes(id);
		temIncompativeis(componentes);
		requerOutros(componentes);

		return componentes;
	}

	private void temIncompativeis(Map<Integer, Componente> componentes) throws EncomendaTemComponentesIncompativeis {
		Map<Integer, Componente> incompativeis = new HashMap<>();

		componentes.forEach((k,c) ->
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

	private void requerOutros(Map<Integer, Componente> componentes) throws EncomendaRequerOutrosComponentes {
		Map<Integer, Componente> requeridos = new HashMap<>();
		componentes.forEach((k,c) ->
				requeridos.putAll(
						c.getRequeridos()
				)
		);

		Collection<Componente> requeridosValues = requeridos.values();

		if(!componentes.values().containsAll(requeridosValues)) {
			throw new EncomendaRequerOutrosComponentes();
		}
	}
}