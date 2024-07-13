import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({ providedIn: "root" })
export class LoginService {
	private readonly url = "http://localhost:8080";
	private authorized = new BehaviorSubject<boolean>(false);

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
}
