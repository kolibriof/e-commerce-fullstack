import { NgModule } from "@angular/core";
import { BrowserModule, DomSanitizer } from "@angular/platform-browser";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
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
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { ToastrModule } from "ngx-toastr";
import { ProductComponent } from "./product/product.component";
import { LibraryComponent } from "./library/library.component";
import { AuthInterceptor } from "./interceptors/auth-interceptor";
import { HeaderComponent } from './header/header.component';

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
		path: "collection",
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
  HeaderComponent,
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
		ReactiveFormsModule,
		ToastrModule.forRoot(),
	],
	providers: [
		AuthGuard,
		provideAnimations(),
		{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
	],
	bootstrap: [AppComponent],
})
export class AppModule {}
