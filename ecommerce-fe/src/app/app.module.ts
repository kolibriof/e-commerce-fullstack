import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
import { MatFormField, MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { LoginPageComponent } from "./login-page/login-page.component";
import { CartComponent } from "./cart/cart.component";
import { PaymentComponent } from "./payment/payment.component";
import { HomePageComponent } from "./home-page/home-page.component";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./guards/AuthGuard";
import { provideAnimations } from "@angular/platform-browser/animations";
import { MatIconModule } from "@angular/material/icon";
import {
	HttpClient,
	HttpClientModule,
	provideHttpClient,
} from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { ProductComponent } from "./product/product.component";
import { LibraryComponent } from "./library/library.component";

const routes: Routes = [
	{
		path: "",
		pathMatch: "full",
		redirectTo: "login",
	},
	{
		path: "login",
		component: LoginPageComponent,
	},
	{
		path: "home",
		component: HomePageComponent,
		canActivate: [AuthGuard],
	},
	{
		path: "cart",
		component: CartComponent,
		canActivate: [AuthGuard],
	},
	{
		path: "payment",
		component: PaymentComponent,
		canActivate: [AuthGuard],
	},
	{
		path: "library",
		component: LibraryComponent,
		canActivate: [AuthGuard],
	},
	{
		path: "**",
		redirectTo: "login",
	},
];

@NgModule({
	declarations: [
		AppComponent,
		LoginPageComponent,
		CartComponent,
		PaymentComponent,
		HomePageComponent,
		ProductComponent,
  LibraryComponent,
	],
	imports: [
		MatButtonModule,
		MatFormField,
		MatFormFieldModule,
		BrowserModule,
		AppRoutingModule,
		RouterModule.forRoot(routes),
		FormsModule,
		MatInputModule,
		MatCardModule,
		HttpClientModule,
		MatIconModule,
		ToastrModule.forRoot(),
	],
	providers: [AuthGuard, provideAnimations()],
	bootstrap: [AppComponent],
})
export class AppModule {}
