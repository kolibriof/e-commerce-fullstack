import {
	ActivatedRouteSnapshot,
	CanActivate,
	GuardResult,
	MaybeAsync,
	Router,
	RouterStateSnapshot,
} from "@angular/router";
import { LoginService } from "../services/login-service";
import { Injectable } from "@angular/core";
import { map } from "rxjs";

@Injectable({ providedIn: "root" })
export class AuthGuard implements CanActivate {
	constructor(private loginService: LoginService, private router: Router) {}

	canActivate(
		route: ActivatedRouteSnapshot,
		state: RouterStateSnapshot,
	): MaybeAsync<GuardResult> {
		return this.loginService.getAuthorization().pipe(
			map((authorized) => {
				if (!authorized) {
					this.router.navigate([""]);
					return authorized;
				}
				return authorized;
			}),
		);
	}
}
