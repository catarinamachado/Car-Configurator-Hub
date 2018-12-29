package CCH.business;

import CCH.dataaccess.ClasseComponenteDAO;
import CCH.dataaccess.ConfiguracaoDAO;
import CCH.dataaccess.PacoteDAO;
import CCH.exception.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ilog.concert.IloException;

import java.util.*;
import java.util.stream.Collectors;


public class Configuracao {
	private int id;
	private double preco;
	private double desconto;

	private PacoteDAO pacoteDAO = new PacoteDAO();
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

	public Configuracao(int id, double preco, double desconto) {
		this.id = id;
		this.preco = preco;
		this.desconto = desconto;
	}

	public Configuracao() {
		this.id = configuracaoDAO.getNextId();
		this.preco = 0;
		this.desconto = 0;
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
		configuracaoDAO.put(this.id, this);

		try {
			for (Pacote p : pacotesAceitados) {
				adicionarPacote(p.getId(), null);
			}
			for (Componente c : componentesAceitados) {
				adicionarComponente(c.getId());
			}
		} catch (ComponenteJaAdicionadoException | PacoteJaAdicionadoException e) {}
	}

	public Map<Integer, Componente> consultarComponentes() {
		return configuracaoDAO.getComponentes(id);
	}

	/**
	 *
	 * @param componenteId
	 */
	public Componente adicionarComponente(int componenteId) throws ComponenteJaAdicionadoException {
		if (configuracaoDAO.getComponentes(id).containsKey(componenteId)) {
			throw new ComponenteJaAdicionadoException();
		}
		Componente componente = configuracaoDAO.addComponente(id, componenteId);
		this.preco += componente.getPreco();
		configuracaoDAO.put(id, this);
		return componente;
	}

	/**
	 *
	 * @param componenteId
	 */
	public void removerComponente(int componenteId) {
		Componente componente = configuracaoDAO.removeComponente(id, componenteId);

		this.preco -= componente.getPreco();
		for (Pacote pacote : configuracaoDAO.getPacotes(id).values()) {
			if (pacote.getComponentes().containsKey(componenteId)) {
				this.desconto -= pacote.getDesconto();
				configuracaoDAO.removePacote(id, pacote.getId());
			}
		}

		configuracaoDAO.put(id, this);
	}

	public Map<Integer, Pacote> consultarPacotes() {
		return configuracaoDAO.getPacotes(id);
	}

	/**
	 *
	 * @param pacoteId
	 */
	public Pacote adicionarPacote(int pacoteId, Pacote replacedPacote) throws PacoteJaAdicionadoException {
		Map<Integer, Pacote> pacotes = configuracaoDAO.getPacotes(id);
		Pacote pacote;

		if (pacotes.containsKey(pacoteId)) {
			throw new PacoteJaAdicionadoException();
		}

		if (replacedPacote == null) {
			pacote = conflito(pacotes, pacoteId);

			if (pacote != null) {
				return pacote;
			}
		} else {
			this.desconto -= replacedPacote.getDesconto();
			configuracaoDAO.removePacote(id, replacedPacote.getId());
		}

		pacote = configuracaoDAO.addPacote(id, pacoteId);
		this.desconto += pacote.getDesconto();

		Set<Integer> componentes = consultarComponentes().keySet();
		pacote.getComponentes().forEach((k,c) -> {
			if (!componentes.contains(c.getId())) {
				configuracaoDAO.addComponente(id, c.getId());
				this.preco += c.getPreco();
			}
		});

		configuracaoDAO.put(id, this);

		return null;
	}

	private Pacote conflito(Map<Integer, Pacote> pacotes, int pacoteId) {
		Set<Integer> componentesDoPacote = pacoteDAO.get(pacoteId).getComponentes().keySet();

		for (int i : componentesDoPacote) {
			for (Pacote pacote : pacotes.values()) {
				if (pacote.getComponentes().containsKey(i)) {
					return pacote;
				}
			}
		}

		return null;
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

	public Map<Integer, Componente> verificaValidade() throws EncomendaTemComponentesIncompativeis, EncomendaRequerOutrosComponentes, EncomendaRequerObrigatoriosException {
		Map<Integer, Componente> componentes = configuracaoDAO.getComponentes(id);
		temIncompativeis(componentes);
		requerOutros(componentes);
		if(!this.temComponentesObrigatorios())
			throw new EncomendaRequerObrigatoriosException();
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

		for (Componente req : requeridosValues) {
			if (!componentes.keySet().contains(req.getId()))
				throw new EncomendaRequerOutrosComponentes();
		}
	}

	public double getPrecoFinal() {
		return preco-desconto;
	}

	public List<Componente> componentesIncompativeisNaConfig (Componente componente) {
		List<Componente> incompativeis = new ArrayList<>();

		for(Componente componenteIncomp : componente.getIncompativeis().values()) {
			if (this.consultarComponentes().containsKey(componenteIncomp.getId())) {
				incompativeis.add(componenteIncomp);
			}
		}

		return incompativeis;
	}

	public List<Componente> componentesRequeridosQueNaoEstaoConfig (Componente componente) {
		List<Componente> requeridos = new ArrayList<>();

		for(Componente componenteRequerido : componente.getRequeridos().values()) {
			if (!this.consultarComponentes().containsKey(componenteRequerido.getId())) {
				requeridos.add(componenteRequerido);
			}
		}

		return requeridos;
	}

	public void checkforPacotesInConfiguration(){
		Collection<Pacote> pacotes = this.pacoteDAO.values();
		Map<Integer,Componente> compsNotInPacotes = this.componentesNotInPacotes();
		for (Pacote p:pacotes) {
			Collection<Componente> comps = p.getComponentes().values();
			boolean containsPacote = true;
			for (Componente c:comps) {
				containsPacote = containsPacote && compsNotInPacotes.containsKey(c.getId());
			}
			if(containsPacote && comps.size()!=0){
				try {
					for (Componente c:comps) compsNotInPacotes.remove(c.getId());
					this.adicionarPacote(p.getId(),null);
				} catch (PacoteJaAdicionadoException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Map<Integer, Componente> componentesNotInPacotes(){
		Map<Integer,Componente> componentes = this.consultarComponentes();
		Map<Integer,Pacote> pacotes = this.consultarPacotes();
		for (Pacote p:pacotes.values()) {
			for (Componente c:p.getComponentes().values()) {
				componentes.remove(c.getId());
			}
		}
		return componentes;
	}

	public void removerPacote(int pacoteId){
		if (pacoteDAO.containsKey(pacoteId)){
			Pacote p = pacoteDAO.get(pacoteId);
			this.preco += p.getDesconto();
			configuracaoDAO.removePacote(this.id,pacoteId);
			configuracaoDAO.put(this.id,this);
		}
	}

	public boolean temComponentesObrigatorios() {
		ClasseComponenteDAO cdao = new ClasseComponenteDAO();
		List<Integer> idsTiposObrigatorios = cdao.values().stream().filter(p -> p.getEObrigatorio()).map(p -> p.getId()).collect(Collectors.toList());
		Collection<Integer> idsTiposNaClasse = this.consultarComponentes().values().stream().map(c -> c.getClasseComponente().getId()).collect(Collectors.toSet());
		return idsTiposNaClasse.containsAll(idsTiposObrigatorios);
	}
}
