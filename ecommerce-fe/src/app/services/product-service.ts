import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Product } from "../helpers/constants";

@Injectable({ providedIn: "root" })
export class ProductService {
	private readonly url = "http://localhost:8080";

	constructor(private http: HttpClient) {}

	getAllProducts() {
		return this.http.get<Product[]>(this.url + "/products");
	}
}
