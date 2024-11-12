export interface User {
	id: number;
	login: string;
	email?: string;
	createdAt?: Date;
}

export interface BaseCartItem {
	id: number;
	username: string;
}

export interface AddCartItem extends BaseCartItem {
	product_name: string;
}

export interface SingleCartItem extends BaseCartItem {
	name: string;
	price: number;
	description?: string;
	quantity?: number;
	addedAt?: Date;
}

export interface ApiResponse {
	success: boolean;
	message: string;
	cause: string;
	timestamp?: Date;
}

export interface CartResponse extends ApiResponse {
	items?: SingleCartItem[];
	totalItems?: number;
	totalPrice?: number;
}

export enum CartActionType {
	ADD = "ADD",
	REMOVE = "REMOVE",
	UPDATE = "UPDATE",
	CLEAR = "CLEAR",
}

export function isCartResponse(response: any): response is CartResponse {
	return (
		response &&
		typeof response === "object" &&
		"success" in response &&
		"message" in response &&
		"cause" in response
	);
}

export const CART_STORAGE_KEY = "ecommerce-cart-items";
export const USER_STORAGE_KEY = "ecommerce-loggedin-user";

export type CartItemId = number | string;
export type CartOperationResult = Promise<CartResponse>;

export interface CartState {
	items: SingleCartItem[];
	isLoading: boolean;
	error: string | null;
	lastUpdated: Date;
}

export type CartAction = {
	type: CartActionType;
	payload: {
		item?: SingleCartItem;
		id?: CartItemId;
		quantity?: number;
	};
};
