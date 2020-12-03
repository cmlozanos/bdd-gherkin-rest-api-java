package com.example.gherkin.server;

import java.util.List;

public class Pagination {
	private final int page;
	private final int total;
	private final List<User> data;

	public Pagination(final int page, final int total, final List<User> usersLimited) {
		super();
		this.page = page;
		this.total = total;
		data = usersLimited;
	}

	public int getPage() {
		return page;
	}

	public int getTotal() {
		return total;
	}

	public List<User> getData() {
		return data;
	}

}
