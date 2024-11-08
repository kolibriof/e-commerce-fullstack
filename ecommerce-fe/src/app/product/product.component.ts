import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { CartService } from "../services/cart-service";
import { AddCartItem, SingleCartItem } from "../helpers/constants";
import { ToastrService } from "ngx-toastr";
import { Observable, catchError, of, take } from "rxjs";
import { Router } from "@angular/router";
import { PaymentComponent } from "../payment/payment.component";

interface Product {
	date: string;
}

@Component({
	selector: "app-product",
	templateUrl: "./product.component.html",
	styleUrl: "./product.component.css",
})
export class ProductComponent implements OnInit {
	@Input("id") id!: number;
	@Input("name") name: string = "";
	@Input("desc") desc: string = "";
	@Input("img") img: string = "";
	@Input("cartItems") cartItems: Observable<SingleCartItem[]> = of([]);
	@Input("price") price: number = 0;
	@Input("owned") owned: boolean = false;
	@Output("sold") sold = new EventEmitter<boolean>();
	protected inCart: boolean = true;
	protected product: { date: string } = {
		date: "12/09/2024",
	};
	protected dateBought: any;

	constructor(
		private cartService: CartService,
		private toastr: ToastrService,
		private router: Router,
	) {}

	ngOnInit(): void {
		this.cartItems.subscribe((items: any) => {
			if (items.message) {
				this.inCart = false;
			} else {
				this.inCart = items.some((item: any) => item.name === this.name);
			}
		});
		this.dateBought = this.getDate(this.product);
	}

	addItemToCart(name: string): void {
		let userFromLS: any;
		let itemToAdd: AddCartItem;

		if (localStorage.getItem("ecommerce-loggedin-user")) {
			userFromLS = JSON.parse(
				localStorage.getItem("ecommerce-loggedin-user") || "",
			);

			itemToAdd = {
				id: userFromLS.id,
				username: userFromLS.login,
				product_name: name,
			};

			if (!this.inCart) {
				this.cartService.addItemToCart(itemToAdd).subscribe((data: any) => {
					this.inCart = true;
					this.loadLatestItems(userFromLS.id, userFromLS.login);
					this.toastr.success(data.message, data.cause);
				});
				return;
			}
			this.cartService
				.deleteItemFromCart(itemToAdd)
				.pipe(
					catchError((err) => {
						this.toastr.error(err.error.message);
						return of();
					}),
				)
				.subscribe((data: any) => {
					this.inCart = false;

					this.toastr.info(data.message, data.cause);
					this.loadLatestItems(userFromLS.id, userFromLS.login);
				});
		} else {
			this.router.navigate(["/login"]);
		}
	}

	private loadLatestItems(id: any, login: any) {
		this.cartService
			.getCartItems({
				id: id,
				username: login,
			})
			.pipe(take(1))
			.subscribe((data) => {
				console.log(data);
				localStorage.setItem("ecommerce-cart-items", JSON.stringify(data));
			});
	}

	private getDate(product: Product) {
		const options: Intl.DateTimeFormatOptions = {
			weekday: "short",
			month: "short",
			day: "numeric",
			hour: "2-digit",
			minute: "2-digit",
			hour12: true,
		};

		return new Date(product.date).toLocaleString("en-US", options);
	}

	sellProductById(id: number) {
		const userFromLS = JSON.parse(
			localStorage.getItem("ecommerce-loggedin-user") || "",
		);
		if (userFromLS) {
			this.cartService
				.sellOwnedProduct(id, userFromLS.id, {
					id: userFromLS.id,
					username: userFromLS.login,
				})
				.pipe(
					take(1),
					catchError((data) => {
						this.toastr.error(data.error.cause, data.error.message);
						return of();
					}),
				)
				.subscribe((data: any) => {
					this.sold.emit(true);
					this.toastr.success(data.cause, data.message);
				});
		} else {
			this.router.navigate(["/login"]);
		}
	}
}
