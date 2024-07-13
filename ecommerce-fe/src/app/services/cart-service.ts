import { Injectable } from "@angular/core";
import {
	AddCartItem,
	CartItemsContents,
	SingleCartItem,
} from "../helpers/constants";
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({ providedIn: "root" })
export class CartService {
	private readonly url = "http://localhost:8080";

	constructor(private http: HttpClient) {}

	getCartItems(cart: CartItemsContents) {
		return this.http.post<SingleCartItem[]>(this.url + "/cart", cart, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}

	addItemToCart(cart: AddCartItem) {
		return this.http.post(this.url + "/cartproduct", cart, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}

	deleteItemFromCart(cart: AddCartItem) {
		return this.http.post(this.url + "/cartproduct", cart, {
			params: new HttpParams().set("remove", true),
			headers: {
				"Content-Type": "application/json",
			},
		});
	}

	payForItems(creds: CartItemsContents, sum: number) {
		return this.http.post(this.url + "/cart", creds, {
			headers: {
				"Content-Type": "application/json",
			},
			params: new HttpParams().set("sum", sum),
		});
	}

	getOwnedProducts(creds: CartItemsContents) {
		return this.http.post(this.url + "/owned", creds, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}
}
