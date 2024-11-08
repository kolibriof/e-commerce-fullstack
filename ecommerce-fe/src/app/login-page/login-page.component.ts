import { HttpClient } from "@angular/common/http";
import { Component, SecurityContext } from "@angular/core";
import { FormBuilder, FormGroup, NgForm, Validators } from "@angular/forms";
import { LoginService } from "../services/login-service";
import { Router } from "@angular/router";
import { DomSanitizer } from "@angular/platform-browser";

@Component({
	selector: "app-login-page",
	templateUrl: "./login-page.component.html",
	styleUrl: "./login-page.component.css",
})
export class LoginPageComponent {
	protected isLogin: boolean = true;
	protected loginForm!: FormGroup;
	protected signupForm!: FormGroup;
	protected lcUserObj: any;

	constructor(
		private loginService: LoginService,
		private router: Router,
		private fb: FormBuilder,
	) {
		this.loginForm = this.fb.group({
			login: ["", Validators.required],
			password: ["", Validators.required],
		});

		this.signupForm = this.fb.group({
			login: ["", [Validators.required]],
			password: ["", [Validators.required, Validators.minLength(4)]],
			passwordRepeat: ["", [Validators.required, Validators.minLength(4)]],
		});
	}

	submitLoginForm() {
		if (this.loginForm.valid) {
			this.loginService.loginUser(this.loginForm).subscribe((data: any) => {
				this.lcUserObj = {
					id: data.message,
					login: this.loginForm.value.login,
				};
				localStorage.setItem(
					"ecommerce-loggedin-user",
					JSON.stringify(this.lcUserObj),
				);

				this.loginService.userToken = data.login;
				this.router.navigate(["home"]);
			});
		}
	}

	submitSignupForm() {
		if (
			this.signupForm.valid &&
			this.signupForm.value.password === this.signupForm.value.passwordRepeat
		) {
			this.loginService.createUser(this.signupForm).subscribe((data: any) => {
				this.lcUserObj = {
					id: data.message,
					login: this.signupForm.value.login,
					balance: 0,
				};
				localStorage.setItem(
					"ecommerce-loggedin-user",
					JSON.stringify(this.lcUserObj),
				);
				this.loginService.userToken = data.login;
				this.router.navigate(["home"]);
			});
		}
	}
}
