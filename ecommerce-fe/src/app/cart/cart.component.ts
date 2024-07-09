import { Component, OnInit } from "@angular/core";
import { CartService } from "../services/cart-service";
import { CartItemsContents, SingleCartItem } from "../helpers/constants";
import { Router } from "@angular/router";
import { BehaviorSubject } from "rxjs";

@Component({
	selector: "app-cart",
	templateUrl: "./cart.component.html",
	styleUrl: "./cart.component.css",
})
export class CartComponent implements OnInit {
	protected cartItems = new BehaviorSubject<SingleCartItem[]>([]);
	protected errorMessage = "";
	constructor(private cartService: CartService, private router: Router) {}
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

	proceed() {}
}
