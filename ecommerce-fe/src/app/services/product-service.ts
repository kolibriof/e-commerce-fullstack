import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Product } from "../helpers/constants";
import { CartService } from "./cart-service";

@Injectable({ providedIn: "root" })
export class ProductService extends CartService {
	constructor(protected override http: HttpClient) {
		super(http);
	}

	getAllProducts() {
		return this.http.get<Product[]>(this.url + "/products");
	}
}
