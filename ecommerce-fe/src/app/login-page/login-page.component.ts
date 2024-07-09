import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { LoginService } from "../services/login-service";
import { Router } from "@angular/router";

@Component({
	selector: "app-login-page",
	templateUrl: "./login-page.component.html",
	styleUrl: "./login-page.component.css",
})
export class LoginPageComponent {
	protected isLogin: boolean = true;

	constructor(
		private http: HttpClient,
		private loginService: LoginService,
		private router: Router,
	) {}

	submitForm(form: NgForm) {
		let lcUserObj;
		if (form.form.value.passwordRepeat) {
			this.loginService.createUser(form).subscribe((data) => {
				lcUserObj = {
					id: data,
					login: form.form.value.login,
				};
				localStorage.setItem(
					"ecommerce-loggedin-user",
					JSON.stringify(lcUserObj),
				);
			});
		} else {
			this.loginService.loginUser(form).subscribe((data) => {
				lcUserObj = {
					id: data,
					login: form.form.value.login,
				};
				localStorage.setItem(
					"ecommerce-loggedin-user",
					JSON.stringify(lcUserObj),
				);

				this.router.navigate(["home"]);
			});
		}
	}
}
