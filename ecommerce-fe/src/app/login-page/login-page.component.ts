import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { NgForm } from "@angular/forms";
import { LoginService } from "../services/login-service";

@Component({
	selector: "app-login-page",
	templateUrl: "./login-page.component.html",
	styleUrl: "./login-page.component.css",
})
export class LoginPageComponent {
	protected isLogin: boolean = true;

	constructor(private http: HttpClient, private loginService: LoginService) {}

	submitForm(form: NgForm) {
		if (form.form.value.passwordRepeat) {
			this.loginService.createUser(form).subscribe((data) => {
				console.log(data);
			});
		} else {
			this.loginService.loginUser(form).subscribe((data) => {
				console.log(data);
			});
		}
	}
}
