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
		if (localStorage.getItem("ecommerce-loggedin-user")) {
			const creds = JSON.parse(
				localStorage.getItem("ecommerce-loggedin-user") || "",
			);
			cartCreds = {
				id: creds.id,
				username: creds.login,
			};
			this.cartService.getCartItems(cartCreds).subscribe((data: any) => {
				if (data.message) {
					console.log(data);
					this.errorMessage = data.message;
				} else {
					this.cartItems.next(data);
				}
			});
		} else {
			this.router.navigate(["/login"]);
		}
	}

	back() {
		this.router.navigate(["/home"]);
	}

	removeFromCart(item: SingleCartItem) {
		if (localStorage.getItem("ecommerce-loggedin-user")) {
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
