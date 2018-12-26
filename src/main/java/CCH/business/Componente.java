package CCH.business;

import CCH.dataaccess.ComponenteDAO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Componente {

	private ClasseComponente classeComponente;
	private Collection<Integer> requeridos;
	private Collection<Integer> incompativeis;
	private int id;
	private int stock;
	private double preco;
	private String nome;

	private static int nextId = 1;

	private ComponenteDAO componenteDAO = new ComponenteDAO();

	public Componente(int id, int stock, double preco, String nome, ClasseComponente classeComponente) {
		this.requeridos = null;
		this.incompativeis = null;
		this.id = id;
		this.stock = stock;
		this.preco = preco;
		this.nome = nome;
		this.classeComponente = classeComponente;
	}

	public static int getNextId() {
		return nextId++;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getPreco() {
		return this.preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ClasseComponente getClasseComponente() {
		return classeComponente;
	}

	public void setClasseComponente(ClasseComponente classeComponente) {
		this.classeComponente = classeComponente;
	}

	public Map<Integer, Componente> getRequeridos() {
		return componenteDAO.getComponentesRequeridos(id);
	}

	public void setRequeridos(Collection<Integer> requeridos) {
		this.requeridos = requeridos;
	}

	public Map<Integer, Componente> getIncompativeis() {
		return componenteDAO.getComponentesIncompativeis(id);
	}

	public void setIncompativeis(Collection<Integer> incompativeis) {
		this.incompativeis = incompativeis;
	}


	public String getFullName() {
		return classeComponente.getNome() + " " + nome;
	}

	public String getStockString() {
		return Integer.toString(stock);
	}

	public String getStockAvailable() {
		if (stock > 20) {
			return "Disponível";
		} else if (stock > 0) {
			return "Limitado";
		}

		return "Indisponível";
	}

	public List<Integer> getIdNecessarios() {
		return requeridos.stream().collect(Collectors.toList());
	}

	public List<Integer> getIdIncompativeis() {
		return incompativeis.stream().collect(Collectors.toList());
	}

}