import { Component, OnInit } from "@angular/core";
import { CartItemsContents, SingleCartItem } from "../helpers/constants";
import { LoginService } from "../services/login-service";
import { BehaviorSubject, catchError, of, take } from "rxjs";
import { Router } from "@angular/router";
import { CartService } from "../services/cart-service";
import { ToastrService } from "ngx-toastr";

@Component({
	selector: "app-payment",
	templateUrl: "./payment.component.html",
	styleUrl: "./payment.component.css",
})
export class PaymentComponent implements OnInit {
	protected cartItems: SingleCartItem[] = [];
	protected userBalance = new BehaviorSubject<string>("");
	protected summary: string = "";
	protected loggedInUser!: CartItemsContents;

	constructor(
		private loginService: LoginService,
		private router: Router,
		private cartService: CartService,
		private toastrService: ToastrService,
	) {}

	ngOnInit(): void {
		const itemsFromLs = localStorage.getItem("ecommerce-cart-items");
		const userFromLs = JSON.parse(
			localStorage.getItem("ecommerce-loggedin-user") || "",
		);
		this.loggedInUser = { id: userFromLs.id, username: userFromLs.login };
		this.refreshUserBalance(userFromLs);
		this.cartItems = itemsFromLs ? JSON.parse(itemsFromLs) : [];

		if (this.cartItems.length > 0) {
			this.summary = this.cartItems
				.reduce((acc, value) => {
					acc += value.price;
					return acc;
				}, 0)
				.toFixed(1);
		}
	}

	back() {
		this.router.navigate(["/cart"]);
	}
	pay() {
		if (this.loggedInUser) {
			this.cartService
				.payForItems(this.loggedInUser, Number(this.summary))
				.pipe(take(1))
				.pipe(
					catchError((data) => {
						console.log(data);
						this.toastrService.error(data.error.cause, data.error.message);
						return of();
					}),
				)
				.subscribe((data: any) => {
					console.log(data);
					localStorage.setItem("ecommerce-cart-items", "");
					this.toastrService.success(data.message);
					this.refreshUserBalance({
						id: this.loggedInUser.id,
						login: this.loggedInUser.username,
					});
					this.updateCartItems();
					this.router.navigate(["/home"]);
				});
		}
	}

	updateCartItems() {
		this.cartService
			.getCartItems(this.loggedInUser)
			.pipe(take(1))
			.subscribe((data) => {
				localStorage.setItem("ecommerce-cart-items", JSON.stringify(data));
			});
	}

	refreshUserBalance(userFromLs: any) {
		this.loginService
			.getUserBalance({ id: userFromLs.id, login: userFromLs.login })
			.pipe(take(1))
			.subscribe((data: any) => {
				this.userBalance.next(data);
			});
	}
}
