import { Injectable } from "@angular/core";
import * as CryptoJS from 'crypto-js';

@Injectable({providedIn: 'root'})
export class CryptoService {

  private key: any;
  private encKey: any;
  private encryptor: any;
  private constant: any;
  
  constructor() {
    this.encryptor = CryptoJS;
    this.encKey = "encryption-service";
    this.constant = this.encryptor.enc.Hex.parse("00000000000000000000000000000000");
    this.key = this.encryptor.PBKDF2(this.encKey, "salt", { keySize: 256 / 32, iterations: 1000, hasher: this.encryptor.algo.SHA512 });
    let text = this.encrypt('Arham');
    console.log(text);
    console.log(this.decrypt(text));
  }

  public encrypt(text: any): void {
    
    return this.encryptor.AES.encrypt(text, this.key, { iv: this.constant }).toString();
  }

  public decrypt(encText: any): string {

    return this.encryptor.AES.decrypt(encText, this.key, { iv: this.constant })
      .toString(this.encryptor.enc.Utf8);
  }
}
