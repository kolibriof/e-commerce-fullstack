import { Component, Input } from "@angular/core";

@Component({
	selector: "app-product",
	templateUrl: "./product.component.html",
	styleUrl: "./product.component.css",
})
export class ProductComponent {
	@Input("name") name: string = "";
	@Input("desc") desc: string = "";
	@Input("img") img: string = "";
}
