import { Component } from "@angular/core";
import { Product, SingleCartItem } from "../helpers/constants";
import { BehaviorSubject } from "rxjs/internal/BehaviorSubject";

@Component({
	selector: "app-home-page",
	templateUrl: "./home-page.component.html",
	styleUrl: "./home-page.component.css",
})
export class HomePageComponent {
	protected receivedProducts: Product[] = [];
	protected cartItems = new BehaviorSubject<SingleCartItem[]>([]);

	receiveProducts(data: Product[]) {
		if (data) this.receivedProducts = data;
	}
	receiveCartItems(data: SingleCartItem[]) {
		if (data) this.cartItems.next(data);
	}
}
