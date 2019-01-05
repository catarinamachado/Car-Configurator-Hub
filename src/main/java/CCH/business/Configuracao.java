package CCH.business;

import CCH.dataaccess.PacoteDAO;
import CCH.dataaccess.RemoteClass;
import CCH.exception.*;
import ilog.concert.IloException;

import java.util.*;

/**
 * Classe que caracteriza uma configuração, com todas as informações da mesma.
 *
 * @version 20181229
 */

public class Configuracao implements RemoteClass<Integer> {
	private int id;
	private double preco;
	private double desconto;

	private PacoteDAO pacoteDAO = new PacoteDAO();

    /**
     * Construtor por omissão da Configuração.
     */
    public Configuracao() {
        this.id = 0;
        this.preco = 0;
        this.desconto = 0;
    }
  
  	public Configuracao(List<String> rs){
	    this.id =Integer.valueOf(rs.get(0));
	    this.preco=Double.valueOf(rs.get(1));
	    this.desconto=Double.valueOf(rs.get(2));
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
	 * Devolve o nome da configuração (Configuração + o número do seu id).
	 *
	 * @return nome completo da configuração
	 */
	public String getNome() {
		return "Configuração " + id;
	}

    /**
     * Devolve o id da configuração.
     *
     * @return id
     */
	public int getId() {
		return this.id;
	}
  
	public Integer key(){return this.id; }

    public Integer key(String k) {
        return Integer.valueOf(k);
    }

	@Override
	public List<String> toRow() {
		List<String> l = new LinkedList<>();
		l.add(String.valueOf(this.id));
        l.add(String.valueOf(this.preco));
        l.add(String.valueOf(this.desconto));
        return l;
	}

	public Configuracao fromRow(List<String> rs){
	    return new Configuracao(rs);
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

	/**
	 * Método que averigua se os pacotes já presentes na configuração têm algum
	 * componente em comum com o pacote que se pretende adicionar.
	 *
	 * @param pacotes Pacotes já presentes na configuração
	 * @param pacoteId Id do pacote que se pretende averiguar se causa conflito
	 * @return null caso não haja componentes comuns ou o Pacote que gera
	 * conflito com o pacote que se pretende adicionar
	 */
	public Pacote conflito(Map<Integer, Pacote> pacotes, int pacoteId) {
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
    public boolean updateOnPacoteRemove(int pacoteId){
        if (pacoteDAO.containsKey(pacoteId)) {
            Pacote p = pacoteDAO.get(pacoteId);
            this.preco += p.getDesconto();
            return true;
        }

        return false;
    }

    public Collection<RemoteClass<Integer>> getPacotes() {
		return this.pacoteDAO.values();
	}
}
