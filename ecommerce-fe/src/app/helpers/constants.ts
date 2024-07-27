export interface Product {
	id?: number;
	name: string;
	description: string;
	price: number;
	imglink: string;
}

export const LinksConst = {
	HOME: "HOME",
	PRODUCTS: "PRODUCTS",
	CART: "CART",
	COLLECTION: "COLLECTION",
};

export interface CartItemsContents {
	id: number;
	username: string;
}

interface PersonResponse {
	id: number;
	login: string;
}
export interface SingleCartItem {
	id: number;
	name: string;
	person: PersonResponse;
	price: number;
}

export interface AddCartItem extends CartItemsContents {
	product_name: string;
}
