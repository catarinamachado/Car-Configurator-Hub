package CCH.business;
import CCH.dataaccess.RemoteClass;

import java.util.LinkedList;
import java.util.List;


/**
 * ClasseComponente simboliza as características básicas do componente, como
 * por exemplo o seu tipo e se se trata de um componente obrigatório ou não.
 *
 * @version 20181229
 */

public class ClasseComponente implements RemoteClass<Integer> {
	private TipoComponente tipoComponente;
	private int id;
	private boolean eObrigatorio;
	private String nome;


	/**
	 * Construtor parametrizado da ClasseComponente.
	 *
	 * @param id Id da Classe Componente
	 * @param eObrigatorio Indica se se trata de um componente obrigatório ou não
	 * @param nome Nome da Classe a que o Componente pertence
	 * @param tipoComponente Tipo em que se insere o componente
	 */
	public ClasseComponente(int id, boolean eObrigatorio, String nome, TipoComponente tipoComponente) {
		this.id = id;
		this.eObrigatorio = eObrigatorio;
		this.nome = nome;
		this.tipoComponente = tipoComponente;
	}
  
  	public ClasseComponente(List<String> rs){
		this.id = Integer.valueOf(rs.get(0));
		this.eObrigatorio = Integer.valueOf(rs.get(1)).equals(1);
		this.nome= rs.get(2);
		this.tipoComponente = TipoComponente.withValue(Integer.valueOf(rs.get(3)));
	}
    

	/**
	 * Devolve o id da classe do componente.
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
	public ClasseComponente fromRow(List<String> row) {
		return new ClasseComponente(row);
	}

	@Override
	public List<String> toRow() {
		List<String> l = new LinkedList<String>();
		l.add(String.valueOf(this.id));
		if(this.eObrigatorio) l.add("1"); else l.add("0");
		l.add(this.nome);
		l.add(String.valueOf(this.tipoComponente.getValue()));
		return l;
	}

	/**
	 * Atualiza o id da classe do componente.
	 *
	 * @param id Id da ClasseComponente
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Devolve o nome da classe do componente.
	 *
	 * @return nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * Atualiza o nome da classe do componente.
	 *
	 * @param nome Nome da ClasseComponente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Devolve o tipo do componente.
	 *
	 * @return TipoComponente
	 */
	public TipoComponente getTipoComponente() {
		return tipoComponente;
	}

	/**
	 * Atualiza o tipo do componente.
	 *
	 * @param tipoComponente Tipo do componente
	 */
	public void setTipoComponente(TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	/**
	 * Devolve true se os componentes que sejam desta classe são obrigatórios, false caso contrário.
	 *
	 * @return boolean
	 */
	public boolean getEObrigatorio() {
		return eObrigatorio;
	}

	/**
	 * Atualiza o campo que guarda a informação se os componentes que forem desta
	 * classe serão componentes obrigatórios ou não.
	 *
	 * @param eObrigatorio True se os componentes forem obrigatórios, false caso contrário
	 */
	public void setEObrigatorio(boolean eObrigatorio) {
		this.eObrigatorio = eObrigatorio;
	}
}