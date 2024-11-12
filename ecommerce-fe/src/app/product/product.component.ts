import {
	Component,
	EventEmitter,
	Input,
	OnInit,
	Output,
	OnDestroy,
} from "@angular/core";
import { Observable, Subject, catchError, of, take, takeUntil } from "rxjs";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import _ from "lodash";

// Services
import { CartService } from "../services/cart-service";

// Models
import { AddCartItem, SingleCartItem } from "../helpers/constants";

interface ProductDetails {
	date: string;
	formattedDate?: string;
}

interface StoredUser {
	id: number;
	login: string;
}

@Component({
	selector: "app-product",
	templateUrl: "./product.component.html",
	styleUrls: ["./product.component.css"],
})
export class ProductComponent implements OnInit, OnDestroy {
	@Input() id!: number;
	@Input() name: string = "";
	@Input() desc: string = "";
	@Input() img: string = "";
	@Input() cartItems: Observable<SingleCartItem[]> = of([]);
	@Input() price: number = 0;
	@Input() owned: boolean = false;
	@Output() sold = new EventEmitter<boolean>();

	protected inCart: boolean = false;
	protected product: ProductDetails = {
		date: "12/09/2024",
	};
	protected dateBought: string = "";

	private destroy$ = new Subject<void>();
	private readonly USER_STORAGE_KEY = "ecommerce-loggedin-user";
	private readonly CART_STORAGE_KEY = "ecommerce-cart-items";

	private readonly dateOptions: Intl.DateTimeFormatOptions = {
		weekday: "short",
		month: "short",
		day: "numeric",
		hour: "2-digit",
		minute: "2-digit",
		hour12: true,
	};

	constructor(
		private cartService: CartService,
		private toastr: ToastrService,
		private router: Router,
	) {}

	ngOnInit(): void {
		this.initializeComponent();
	}

	ngOnDestroy(): void {
		this.destroy$.next();
		this.destroy$.complete();
	}

	addItemToCart(name: string): void {
		const storedUser = this.getStoredUser();

		if (!storedUser) {
			this.router.navigate(["/login"]);
			return;
		}

		const itemToAdd = this.createCartItem(storedUser, name);

		if (!this.inCart) {
			this.addItem(itemToAdd);
		} else {
			this.removeItem(itemToAdd);
		}
	}

	sellProductById(id: number): void {
		const storedUser = this.getStoredUser();

		if (!storedUser) {
			this.router.navigate(["/login"]);
			return;
		}

		this.sellProduct(id, storedUser);
	}

	private initializeComponent(): void {
		this.setupCartItemsSubscription();
		this.formatProductDate();
	}

	private setupCartItemsSubscription(): void {
		this.cartItems
			.pipe(takeUntil(this.destroy$))
			.subscribe((items: SingleCartItem[] | any) => {
				this.inCart = items.message
					? false
					: _.some(items, { name: this.name });
			});
	}

	private formatProductDate(): void {
		this.dateBought = new Date(this.product.date).toLocaleString(
			"en-US",
			this.dateOptions,
		);
	}

	private getStoredUser(): StoredUser | null {
		try {
			const userStr = localStorage.getItem(this.USER_STORAGE_KEY);
			return userStr ? JSON.parse(userStr) : null;
		} catch (error) {
			console.error("Error parsing stored user:", error);
			return null;
		}
	}

	private createCartItem(user: StoredUser, productName: string): AddCartItem {
		return {
			id: user.id,
			username: user.login,
			product_name: productName,
		};
	}

	private addItem(item: AddCartItem): void {
		this.cartService
			.addItemToCart(item)
			.pipe(
				take(1),
				takeUntil(this.destroy$),
				catchError(this.handleError.bind(this)),
			)
			.subscribe((response: any) => {
				this.handleCartSuccess(response, true);
				this.loadLatestItems(item.id, item.username);
			});
	}

	private removeItem(item: AddCartItem): void {
		this.cartService
			.deleteItemFromCart(item)
			.pipe(
				take(1),
				takeUntil(this.destroy$),
				catchError(this.handleError.bind(this)),
			)
			.subscribe((response: any) => {
				this.handleCartSuccess(response, false);
				this.loadLatestItems(item.id, item.username);
			});
	}

	private sellProduct(productId: number, user: StoredUser): void {
		this.cartService
			.sellOwnedProduct(productId, user.id, {
				id: user.id,
				username: user.login,
			})
			.pipe(
				take(1),
				takeUntil(this.destroy$),
				catchError(this.handleError.bind(this)),
			)
			.subscribe((response: any) => {
				this.sold.emit(true);
				this.toastr.success(response.cause, response.message);
			});
	}

	private loadLatestItems(userId: number, username: string): void {
		this.cartService
			.getCartItems({
				id: userId,
				username: username,
			})
			.pipe(take(1), takeUntil(this.destroy$))
			.subscribe((data: SingleCartItem[]) => {
				localStorage.setItem(this.CART_STORAGE_KEY, JSON.stringify(data));
			});
	}

	private handleCartSuccess(response: any, isAdding: boolean): void {
		this.inCart = isAdding;
		const toastrMethod = isAdding ? "success" : "info";
		this.toastr[toastrMethod](response.message, response.cause);
	}

	private handleError(error: any) {
		this.toastr.error(error.error?.message || "An error occurred");
		return of(null);
	}
}
