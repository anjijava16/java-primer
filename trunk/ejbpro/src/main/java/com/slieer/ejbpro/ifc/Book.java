package com.slieer.ejbpro.ifc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Book implements Serializable{
	private static final long serialVersionUID = -2742011205617133280L;
	
	public enum Category {
		LITERATURE, PHILOSOPHY, PROGRAMMING
	}
	
	private String title;
	private double price;
	private Author author;
	private String isbn;
	private Category category;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", price=" + price + ", author="
				+ author + ", category=" + category + "]";
	}	
}
