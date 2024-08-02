import { Component, OnInit } from "@angular/core";
import { ProductService } from "../services/product-service";
import { BehaviorSubject, take, tap } from "rxjs";
import { LinksConst, Product, SingleCartItem } from "../helpers/constants";
import { Router } from "@angular/router";
import { CartService } from "../services/cart-service";
import { LoginService } from "../services/login-service";

const NavLinks = {
	[LinksConst.HOME]: "home",
	[LinksConst.CART]: "cart",
	[LinksConst.COLLECTION]: "collection",
};

@Component({
	selector: "app-home-page",
	templateUrl: "./home-page.component.html",
	styleUrl: "./home-page.component.css",
})
export class HomePageComponent implements OnInit {
	protected navHeaders = Object.keys(NavLinks);
	protected userLogin: string = "";
	protected balance = new BehaviorSubject<String>("");
	protected navLinks = NavLinks;
	protected cartItems = new BehaviorSubject<SingleCartItem[]>([]);
	protected receivedProducts = new BehaviorSubject<Product[]>([]);

	constructor(
		private productService: ProductService,
		private loginService: LoginService,
		private router: Router,
		private cartService: CartService,
	) {}

	ngOnInit(): void {
		if (localStorage.getItem("ecommerce-loggedin-user")) {
			const user = JSON.parse(
				localStorage.getItem("ecommerce-loggedin-user") || "",
			);
			this.userLogin = user.login;

			this.loginService
				.getUserBalance({ id: user.id, login: user.login })
				.pipe(take(1))
				.subscribe((data: any) => {
					this.balance.next((data as number).toFixed(2));
				});

			if (localStorage.getItem("ecommerce-cart-items")) {
				const itemsForUser = JSON.parse(
					localStorage.getItem("ecommerce-cart-items") || "{}",
				);
				if (itemsForUser[0]?.person.login != this.userLogin) {
					this.cartService
						.getCartItems({ id: user.id, username: user.login })
						.subscribe((data) => {
							localStorage.setItem(
								"ecommerce-cart-items",
								JSON.stringify(data),
							);
							this.cartItems.next(data);
						});
				} else {
					this.cartItems.next(itemsForUser);
				}
			} else {
				this.cartService
					.getCartItems({ id: user.id, username: user.login })
					.subscribe((data) => {
						localStorage.setItem("ecommerce-cart-items", JSON.stringify(data));
						this.cartItems.next(data);
					});
			}
		} else {
			this.router.navigate(["/login"]);
		}

		if (localStorage.getItem("ecommerce-products-fe")) {
			const lsData = JSON.parse(
				localStorage.getItem("ecommerce-products-fe") || "",
			);
			this.receivedProducts.next(lsData);
		} else {
			this.updateProducts();
		}
	}

	updateProducts() {
		this.productService
			.getAllProducts()
			.pipe(take(1))
			.subscribe((data) => {
				localStorage.setItem("ecommerce-products-fe", JSON.stringify(data));
				this.receivedProducts.next(data);
			});
	}

	navigate(path: string) {
		if (path) {
			this.router.navigate(["/" + path]);
		}
	}

	logout() {
		[
			"ecommerce-products-fe",
			"ecommerce-user-token",
			"ecommerce-cart-items",
			"ecommerce-loggedin-user",
		].forEach((item) => localStorage.removeItem(item));
		this.loginService.logout();
		this.router.navigate(["/login"]);
	}
}
