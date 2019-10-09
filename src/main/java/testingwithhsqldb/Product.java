/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testingwithhsqldb;

/**
 *
 * @author pedago
 */

public class Product {
	// TODO : ajouter les autres propriétés
	private int productId;
	private String name;
	private int prix;

	public Product(int productId, String name, int prix) {
		this.productId = productId;
		this.name = name;
		this.prix = prix;
	}

	/**
	 * Get the value of productId
	 *
	 * @return the value of productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Get the value of name
	 *
	 * @return the value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the value of prix
	 *
	 * @return the value of prix
	 */
	public int getPrix() {
		return prix;
	}
}