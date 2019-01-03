package CCH.business;

import CCH.dataaccess.ClasseComponenteDAO;
import CCH.dataaccess.ConfiguracaoDAO;
import CCH.dataaccess.PacoteDAO;
import CCH.exception.ComponenteJaAdicionadoException;
import CCH.exception.PacoteJaAdicionadoException;
import CCH.exception.EncomendaTemComponentesIncompativeis;
import CCH.exception.EncomendaRequerOutrosComponentes;
import CCH.exception.EncomendaRequerObrigatoriosException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ilog.concert.IloException;

/**
 * Classe que caracteriza uma configuração, com todas as informações da mesma.
 *
 * @version 20181229
 */

public class Configuracao {
	private int id;
	private double preco;
	private double desconto;

	private PacoteDAO pacoteDAO = new PacoteDAO();
	private ConfiguracaoDAO configuracaoDAO = new ConfiguracaoDAO();

    /**
     * Construtor por omissão da Configuração.
     */
    public Configuracao() {
        this.id = configuracaoDAO.getNextId();
        this.preco = 0;
        this.desconto = 0;
    }

    /**
     * Construtor parametrizado da Configuração.
     *
     * @param id Id da configuração
     * @param preco Indica o preço da configuração
     * @param desconto Indica o desconto que a configuração irá usufruir
     */
    public Configuracao(int id, double preco, double desconto) {
        this.id = id;
        this.preco = preco;
        this.desconto = desconto;
    }

    /**
     * Construtor de Configuração que tem como objetivo construir uma nova configuração
     * a partir da configuração ótima gerada mais rapidamente
     *
     * @param pacotesAceites Lista dos pacotes presentes na configuração
     * @param componentesAceites Lista dos componentes presentes na configuração
     */
    public Configuracao(List<Pacote> pacotesAceites, List<Componente> componentesAceites) {
        this();
        configuracaoDAO.put(this.id, this);

        try {
            for (Pacote p : pacotesAceites) {
                adicionarPacote(p.getId(), null);
            }
            for (Componente c : componentesAceites) {
                adicionarComponente(c.getId());
            }
        } catch (ComponenteJaAdicionadoException | PacoteJaAdicionadoException e) {
        }
    }

    /**
     * Devolve o id da configuração.
     *
     * @return id
     */
	public int getId() {
		return this.id;
	}


    /**
     * Atualiza o id da configuração.
     *
     * @param id Id da configuração
     */
	public void setId(int id) {
		this.id = id;
	}

    /**
     * Devolve o preço da configuração.
     *
     * @return preço
     */
	public double getPreco() {
		return this.preco;
	}

    /**
     * Atualiza o preço da configuração.
     *
     * @param preco Preço da configuração
     */
	public void setPreco(double preco) {
		this.preco = preco;
	}

    /**
     * Devolve o desconto da configuração.
     *
     * @return desconto
     */
	public double getDesconto() {
		return this.desconto;
	}

    /**
     * Atualiza o desconto da configuração.
     *
     * @param desconto Desconto da configuração
     */
	public void setDesconto(double desconto) {
		this.desconto = desconto;
	}

    /**
     * Devolve o preço final da configuração.
     *
     * @return preço
     */
    public double getPrecoFinal() {
        return preco-desconto;
    }

    /**
     * Devolve a configuração gerada através da configuração ótima.
     *
     * @return Configuracao
     */
	public Configuracao gerarConfiguracaoOtima(
			List<Componente> componentesObrigatorios,
		   	List<Componente> componentes,
		   	List<Pacote> pacotes,
		   	double money
	) throws IloException {
		ConfiguracaoOtima configuracaoOtima = new ConfiguracaoOtima();
		return configuracaoOtima.configuracaoOtima(componentesObrigatorios, componentes, pacotes, money);
	}

	public Map<Integer, Componente> consultarComponentes() {
		return configuracaoDAO.getComponentes(id);
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
		if (configuracaoDAO.getComponentes(id).containsKey(componenteId)) {
			throw new ComponenteJaAdicionadoException();
		}
		Componente componente = configuracaoDAO.addComponente(id, componenteId);
		this.preco += componente.getPreco();
		configuracaoDAO.put(id, this);
		return componente;
	}

	/**
     * Método que remove um determinado componente da configuração.
     *
     * @param componenteId Id do componente que se pretende remover da configuração
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

    /**
     * Devolve todos os pacotes presentes na configuração.
     *
     * @return Map<Integer, Pacote> com o par chave valor de cada um dos pacotes
     * na configuração
     */
	public Map<Integer, Pacote> consultarPacotes() {
		return configuracaoDAO.getPacotes(id);
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

	/**
	 * Método que averigua se os pacotes já presentes na configuração têm algum
	 * componente em comum com o pacote que se pretende adicionar.
	 *
	 * @param pacotes Pacotes já presentes na configuração
	 * @param pacoteId Id do pacote que se pretende averiguar se causa conflito
	 * @return null caso não haja componentes comuns ou o Pacote que gera
	 * conflito com o pacote que se pretende adicionar
	 */
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

    /**
     * Método que remove um determinado pacote da configuração.
     *
     * @param pacoteId Id do pacote que se pretende remover da configuração
     */
    public void removerPacote(int pacoteId){
        if (pacoteDAO.containsKey(pacoteId)) {
            Pacote p = pacoteDAO.get(pacoteId);
            this.preco += p.getDesconto();
            configuracaoDAO.removePacote(this.id,pacoteId);
            configuracaoDAO.put(this.id,this);
        }
    }

    /**
     * Devolve o nome da configuração (Configuração + o número do seu id).
     *
     * @return nome completo da configuração
     */
	public String getNome() {
		return "Configuração " + id;
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
		Map<Integer, Componente> componentes = configuracaoDAO.getComponentes(id);
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

	/**
	 * Devolve a lista dos componentes presentes na configuração que são
	 * incompatíveis com o componente passado como parâmetro.
	 *
	 * @param componente Componente que se pretende analisar
	 */
	public List<Componente> componentesIncompativeisNaConfig(Map<Integer,Componente> componentes) {
		List<Componente> incompativeis = new ArrayList<>();

		for (Componente c : componentes.values()) {
			for (Componente componenteIncomp : c.getIncompativeis().values()) {
				if (this.consultarComponentes().containsKey(componenteIncomp.getId())) {
					incompativeis.add(componenteIncomp);
				}
			}
		}
		return incompativeis;
	}

	/**
	 * Devolve a lista dos componentes que ainda não estão na configuração
	 * mas que o componente passado como parâmetro também requer.
	 *
	 * @param componente Componente que se pretende analisar
	 */
	public List<Componente> componentesRequeridosQueNaoEstaoConfig(Map<Integer,Componente> componentes) {
		List<Componente> requeridos = new ArrayList<>();

		for (Componente c : componentes.values()) {
			for (Componente componenteRequerido : c.getRequeridos().values()) {
				if (!this.consultarComponentes().containsKey(componenteRequerido.getId())) {
					requeridos.add(componenteRequerido);
				}
			}
		}

		return requeridos;
	}

  /**
	 * Devolve a lista dos componentes que estão na configuração que requerem
   * o componente passado como parâmetro.
	 *
	 * @param componenteId Id do componente que se pretende analisar
	 */
	public List<Componente> componentesRequeremMeNaConfig(int componenteId) {
		List<Componente> requeridos = new ArrayList<>();

		for (Componente componenteRequerMe : this.consultarComponentes().values()) {
			if (componenteRequerMe.getRequeridos().containsKey(componenteId))
				requeridos.add(componenteRequerMe);
		}

		return requeridos;
	}

	/**
	 * Averigua se os componentes presentes na configuração constituem um
	 * pacote. Em caso afirmativo, substitui os componentes isolados pelo
	 * pacote.
	 */
	public boolean checkforPacotesInConfiguration() {
		Collection<Pacote> pacotes = this.pacoteDAO.values();
		Map<Integer,Componente> compsNotInPacotes = this.componentesNotInPacotes();
		
    for (Pacote p : pacotes) {
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
		Map<Integer,Componente> componentes = this.consultarComponentes();
		Map<Integer,Pacote> pacotes = this.consultarPacotes();

		for (Pacote p : pacotes.values()) {
			for (Componente c : p.getComponentes().values()) {
				componentes.remove(c.getId());
			}
		}

		return componentes;
	}

    /**
     * Indica se a configuração tem todos os componentes básicos (obrigatórios).
     *
     * @return boolean true se todos os componentes obrigatórios estão presentes
     * na configuração, false caso contrário.
     */
	public boolean temComponentesObrigatorios() {
		ClasseComponenteDAO cdao = new ClasseComponenteDAO();
		List<Integer> idsTiposObrigatorios =
                cdao.values().stream().
                        filter(p -> p.getEObrigatorio()).map(p -> p.getId()).collect(Collectors.toList());
		Collection<Integer> idsTiposNaClasse =
                this.consultarComponentes().values().stream().
                        map(c -> c.getClasseComponente().getId()).collect(Collectors.toSet());

		return idsTiposNaClasse.containsAll(idsTiposObrigatorios);
	}
}
