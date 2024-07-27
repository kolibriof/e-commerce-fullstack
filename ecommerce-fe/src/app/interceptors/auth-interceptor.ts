import { Injectable } from "@angular/core";
import {
	HttpEvent,
	HttpInterceptor,
	HttpHandler,
	HttpRequest,
	HttpErrorResponse,
} from "@angular/common/http";
import { Observable, catchError, throwError } from "rxjs";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
	constructor(private router: Router, private toastr: ToastrService) {}
	intercept(
		req: HttpRequest<any>,
		next: HttpHandler,
	): Observable<HttpEvent<any>> {
		const authReq = req.clone({
			withCredentials: true,
		});
		return next.handle(authReq).pipe(
			catchError((error: HttpErrorResponse) => {
				if (error.status === 403) {
					this.toastr.error("The access is forbidden!");
					this.router.navigate(["/login"]);
				}
				return throwError(error);
			}),
		);
	}
}
