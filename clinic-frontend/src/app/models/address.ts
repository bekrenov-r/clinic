export class Address {
    id?: number;
    city: string;
    street: string;
    building: string;
    flat?: string;
    zipCode: string;

  constructor(city: string, street: string, building: string, flat: string, zipCode: string) {
    this.city = city;
    this.street = street;
    this.building = building;
    this.flat = flat;
    this.zipCode = zipCode;
  }

  toSimpleString(): string {
    let flat: string = this.flat ? `/${this.flat}` : '';
    return `ul. ${this.street} ${this.building}${flat}`;
  }
}
