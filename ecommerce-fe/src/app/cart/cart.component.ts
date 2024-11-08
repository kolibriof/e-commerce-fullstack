import { Component, OnInit } from "@angular/core";
import { CartService } from "../services/cart-service";
import {
	AddCartItem,
	CartItemsContents,
	SingleCartItem,
} from "../helpers/constants";
import { Router } from "@angular/router";
import { BehaviorSubject, catchError, of, take, tap } from "rxjs";
import { ToastrService } from "ngx-toastr";

@Component({
	selector: "app-cart",
	templateUrl: "./cart.component.html",
	styleUrl: "./cart.component.css",
})
export class CartComponent implements OnInit {
	protected cartItems = new BehaviorSubject<SingleCartItem[]>([]);
	protected errorMessage = "";
	constructor(
		private cartService: CartService,
		private router: Router,
		private toastr: ToastrService,
	) {}
	ngOnInit(): void {
		let cartCreds: CartItemsContents;
		const storedUser = localStorage.getItem("ecommerce-loggedin-user");

		if (!storedUser) {
			this.router.navigate(["/login"]);
			return;
		}

		try {
			const creds = JSON.parse(storedUser) as { id: number; login: string };
			cartCreds = {
				id: creds.id,
				username: creds.login,
			};

			this.cartService.getCartItems(cartCreds).subscribe({
				next: (data: any) => {
					if (data.message) {
						console.error(data.message);
						this.errorMessage = data.message;
					} else {
						this.cartItems.next(data);
					}
				},
				error: (err) => {
					console.error("Failed to fetch cart items", err);
					this.errorMessage = "Unable to fetch cart items at the moment.";
				},
			});
		} catch (error) {
			console.error("Failed to parse user data from localStorage", error);
			this.router.navigate(["/login"]);
		}
	}

	back() {
		this.router.navigate(["/home"]);
	}

	removeFromCart(item: SingleCartItem) {
		const storedUser = localStorage.getItem("ecommerce-loggedin-user");

		if (storedUser) {
			const itemToAdd: AddCartItem = {
				id: item.person.id,
				username: item.person.login,
				product_name: item.name,
			};

			this.cartService
				.deleteItemFromCart(itemToAdd)
				.pipe(
					catchError((err) => {
						this.toastr.error(err.error.message);
						return of();
					}),
				)
				.subscribe((data: any) => {
					this.toastr.info(data.message, data.cause);

					this.cartService
						.getCartItems({
							id: item.person.id,
							username: item.person.login,
						})
						.pipe(take(1))
						.subscribe((data: any) => {
							localStorage.setItem(
								"ecommerce-cart-items",
								JSON.stringify(data),
							);
							if (data.message) {
								this.cartItems.next([]);
								this.errorMessage = data.message;
							} else {
								this.cartItems.next(data);
							}
						});
				});
		}
	}

	proceed() {
		if (this.errorMessage) {
			this.toastr.error("Add something to your cart first.");
			return;
		}
		this.router.navigate(["/payment"]);
	}
}
