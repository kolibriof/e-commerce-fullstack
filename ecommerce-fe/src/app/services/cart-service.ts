import { Injectable } from "@angular/core";
import {
	AddCartItem,
	CartItemsContents,
	SingleCartItem,
} from "../helpers/constants";
import { HttpClient } from "@angular/common/http";
import { take } from "rxjs";

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
		return this.http.post(this.url + "/removeproduct", cart, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}
}
