package CCH.business;

import java.util.*;

public class Componente {

	private ClasseComponente classeComponente;
	private Collection<Integer> requeridos;
	private Collection<Integer> incompativeis;
	private int id;
	private int stock;
	private double preco;
	private String nome;

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

	public Collection<Integer> getRequeridos() {
		return requeridos;
	}

	public void setRequeridos(Collection<Integer> requeridos) {
		this.requeridos = requeridos;
	}

	public Collection<Integer> getIncompativeis() {
		return incompativeis;
	}

	public void setIncompativeis(Collection<Integer> incompativeis) {
		this.incompativeis = incompativeis;
	}

	public Componente(int id, int stock, double preco, String nome, ClasseComponente classeComponente) {
		this.id = id;
		this.stock = stock;
		this.preco = preco;
		this.nome = nome;
		this.classeComponente = classeComponente;
	}
}