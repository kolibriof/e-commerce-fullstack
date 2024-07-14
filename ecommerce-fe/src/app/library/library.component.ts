import { Component, OnInit } from "@angular/core";
import { LoginService } from "../services/login-service";
import { CartService } from "../services/cart-service";
import { Router } from "@angular/router";
import { BehaviorSubject, catchError, of, take } from "rxjs";
import { CartItemsContents } from "../helpers/constants";

@Component({
	selector: "app-library",
	templateUrl: "./library.component.html",
	styleUrl: "./library.component.css",
})
export class LibraryComponent implements OnInit {
	protected loggedInUser!: CartItemsContents;
	protected ownedProducts = new BehaviorSubject<any[]>([]);
	protected errorMessage = "";
	protected collectionCost = new BehaviorSubject<string>("");

	constructor(private cartService: CartService, private router: Router) {}

	ngOnInit(): void {
		const userFromLs = JSON.parse(
			localStorage.getItem("ecommerce-loggedin-user") || "",
		);

		if (userFromLs) {
			this.loggedInUser = { id: userFromLs.id, username: userFromLs.login };
		} else {
			this.router.navigate(["/login"]);
		}

		this.updateOwnedProducts();
	}

	updateOwnedProducts() {
		this.cartService
			.getOwnedProducts(this.loggedInUser)
			.pipe(
				take(1),
				catchError((data) => {
					this.collectionCost.next("0");
					this.errorMessage = data.error.message;
					return of();
				}),
			)
			.subscribe((data: any) => {
				const collectionSum = (data as []).reduce((acc, value: any, index) => {
					acc += value.product.price;
					return acc;
				}, 0);
				console.log(data);

				this.collectionCost.next(collectionSum.toFixed(2));
				this.ownedProducts.next(data);
			});
	}

	itemIsSold(confirmation: boolean) {
		if (confirmation) {
			this.updateOwnedProducts();
		}
	}
}
