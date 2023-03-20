import { Component } from '@angular/core';
import { environment } from '../environments/environment';
import { CommonHttpService } from './services/common-http-service';
import { AdvanceCryptoService } from './services/adv-crypto-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private http: CommonHttpService, private enc: AdvanceCryptoService) {

  }

  user = {
    username: '',
    password: ''
  };

  submitDetail(): void {

    let encUser = {
      field: '',
      username: this.user.username,
      password: this.user.password
    };

    let encObj: any = this.enc.encryptObject(encUser);

    encUser = {
      field: encObj.encKey,
      username: encObj.object.username,
      password: encObj.object.password
    };

    this.http.postApi(environment.decrypt, encUser)
      .subscribe(
        res => {
          if (res.data.length == 0) {
            console.log(res.data);
          }
        },
        err => {
          console.log(err);
        }
      );
  }
}
