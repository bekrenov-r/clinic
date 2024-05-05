export class Address {
    id?: number;
    city: string;
    street: string;
    building: string;
    flat?: string;
    zipCode: string;

    static toSimpleString(address: Address): string {
      let flat: string = address.flat ? `/${address.flat}` : '';
      return `${address.street} ${address.building}${flat} `;
    }
}
