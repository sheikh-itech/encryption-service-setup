import { Injectable } from "@angular/core";
import * as CryptoJS from 'crypto-js';

@Injectable({ providedIn: 'root' })
export class AdvanceCryptoService {

  private key: any;
  private secreteKey: any;
  private encryptkey: any;
  private encryptor: any;

  constructor() {
    this.encryptor = CryptoJS;
    this.secreteKey = "encryption-service";
    this.encryptkey = this.encryptor.enc.Hex.parse(new Array(32).fill(0).toString().replaceAll(",", ""));
    this.key = this.encryptor.PBKDF2(this.secreteKey, "salt", { keySize: 256 / 32, iterations: 1000, hasher: this.encryptor.algo.SHA512 });
  }

  public encrypt(plainText: any): string {
    let temp = this.encryptor.AES.encrypt(plainText, this.key, { iv: this.encryptkey });
    return temp.toString();
  }

  public decrypt(encryptedText: any): string {

    return this.encryptor.AES.decrypt(encryptedText, this.key, { iv: this.encryptkey })
      .toString(this.encryptor.enc.Utf8);
  }

  public encryptObject(object: any): Object {

    let encryptkey = this.init();
    Object.keys(object).forEach(key => {
      object[key] = this.customEncrypt(object[key], encryptkey);
    });

    return {
      encKey: encryptkey.toString(),
      object: object
    };
  }

  public decryptObject(object: any, encKey: any): Object {

    Object.keys(object).forEach(key => {
      object[key] = this.customDecrypt(object[key], encKey);
    });
    return object;
  }

  public customEncrypt(plainText: any, encKey: string): string {

    return this.encryptor.AES.encrypt(plainText, this.key, { iv: encKey }).toString();
  }

  private customDecrypt(encText: any, encKey: string): string {

    return this.encryptor.AES.decrypt(encText, this.key, { iv: encKey })
      .toString(this.encryptor.enc.Utf8);
  }

  private init(): string {

    return this.encryptor.enc.Hex.parse(Array.from(Array(32),
      () => Math.floor(Math.random() * 36).toString(36)).join(''));
  }
}
