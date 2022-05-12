package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Data {

	private ArrayList<Category> categories;
	private ArrayList<Photo> photos;

	private ArrayList<Item> items;
	private ArrayList<Item> user_items;
	private ArrayList<News> news;

	@JsonProperty("item_details")
	private Item item_details;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("count")
	private int count;

	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}

	public void setNews(ArrayList<News> news) {
		this.news = news;
	}
	public ArrayList<News> getNews() {
		return news;
	}

	public void setUser_items(ArrayList<Item> user_items) {
		this.user_items = user_items;
	}
	public ArrayList<Item> getUser_items() {
		return user_items;
	}

	public void setItem_details(Item item_details) {
		this.item_details = item_details;
	}
	public Item getItem_details() {
		return item_details;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setCategories(ArrayList<Category> s) {
		categories = s;
	}
	public ArrayList<Category> getCategories() {
	    return categories;
	}

	public void setPhotos(ArrayList<Photo> s) {
	    photos = s;
	}
	public ArrayList<Photo> getPhotos() {
	    return photos;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}
}
