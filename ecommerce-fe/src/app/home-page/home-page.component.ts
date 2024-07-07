import { Component, OnInit } from "@angular/core";
import { ProductService } from "../services/product-service";
import { BehaviorSubject, take } from "rxjs";
import { Product } from "../helpers/constants";

@Component({
	selector: "app-home-page",
	templateUrl: "./home-page.component.html",
	styleUrl: "./home-page.component.css",
})
export class HomePageComponent implements OnInit {
	protected receivedProducts = new BehaviorSubject<Product[]>([]);

	constructor(private productService: ProductService) {}

	ngOnInit(): void {
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
}
