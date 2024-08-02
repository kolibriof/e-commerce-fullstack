import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, Subject, take } from "rxjs";

@Injectable({ providedIn: "root" })
export class LoginService {
	private readonly url = "http://localhost:8080";
	private authorized = new BehaviorSubject<boolean>(false);
	_userToken = new BehaviorSubject<string>("");

	set userToken(token: string) {
		this._userToken.next(token);
		localStorage.setItem("ecommerce-user-token", token);
	}

	constructor(private http: HttpClient) {}

	getAuthorization() {
		this.authorized.next(!!localStorage.getItem("ecommerce-loggedin-user"));
		return this.authorized.asObservable();
	}

	loginUser(creds: any) {
		return this.http.post(this.url + "/login", creds.form.value, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}

	getUserBalance(creds: any) {
		return this.http.post(this.url + "/users", creds, {
			headers: {
				"Content-Type": "application/json",
			},
			params: new HttpParams().set("balance", true),
		});
	}

	createUser(creds: any) {
		return this.http.post(this.url + "/users", creds.form.value, {
			headers: {
				"Content-Type": "application/json",
			},
		});
	}
	logout() {
		this.http
			.get(this.url + "/login", {
				params: new HttpParams().set("logout", true),
			})
			.subscribe();
	}
}
