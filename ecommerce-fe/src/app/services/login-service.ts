import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({ providedIn: "root" })
export class LoginService {
	private readonly url = "http://localhost:8080";
	private authorized = new BehaviorSubject<boolean>(true);

	constructor(private http: HttpClient) {}

	getAuthorization() {
		return this.authorized.asObservable();
	}

	loginUser(creds: any) {
		return this.http.post(this.url + "/login", creds.form.value, {
			headers: {
				"Content-Type": "application/json",
			},
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