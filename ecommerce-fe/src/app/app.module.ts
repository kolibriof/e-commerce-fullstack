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
import {
	HttpClient,
	HttpClientModule,
	provideHttpClient,
} from "@angular/common/http";
import { ProductComponent } from "./product/product.component";

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
	],
	providers: [AuthGuard, provideAnimations()],
	bootstrap: [AppComponent],
})
export class AppModule {}
