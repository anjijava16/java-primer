package com.sc.ch02.stax;

import java.io.File;

public class Book implements Comparable<Book> {
	public final static File file = new File("src/java/com/sc/ch02/Catalog.xml");

	enum BookField {
		sku, title, author, price, category
	}

	String sku = null;
	String title = null;
	String author = null;
	String price = null;
	String category = null;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "Book [sku=" + sku + ", title=" + title + ", author=" + author + ", price=" + price + ", category=" + category + "]";
	}

	@Override
	public int compareTo(Book o) {
		return this.hashCode() - o.hashCode();
	}
}
