import { Injectable } from "@angular/core";
import {
	HttpEvent,
	HttpInterceptor,
	HttpHandler,
	HttpRequest,
	HttpErrorResponse,
} from "@angular/common/http";
import { Observable, catchError, take, throwError } from "rxjs";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { LoginService } from "../services/login-service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
	constructor(
		private router: Router,
		private toastr: ToastrService,
		private loginService: LoginService,
	) {}

	intercept(
		req: HttpRequest<any>,
		next: HttpHandler,
	): Observable<HttpEvent<any>> {
		let userToken;

		this.loginService._userToken.pipe(take(1)).subscribe((token) => {
			console.log(token);
			userToken = token;
		});

		const authReq = req.clone({
			withCredentials: true,
			headers: userToken
				? req.headers.set("Authorization", "Bearer " + userToken)
				: req.headers,
		});
		return next.handle(authReq).pipe(
			catchError((error: HttpErrorResponse) => {
				if (error.status === 403) {
					this.toastr.error("Your token has expired!");
					this.router.navigate(["/login"]);
				}
				return throwError(() => error);
			}),
		);
	}
}
