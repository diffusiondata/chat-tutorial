/**
 * Copyright © 2018 Push Technology Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, OnInit } from '@angular/core';

import * as diffusion from 'diffusion';
import { ChatService } from '../chat.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  public signedIn: boolean;
  constructor(private chatService: ChatService) { }

  ngOnInit() { }

  async signIn(username: any, password: any) {
    try {
      const session = await this.chatService.getSession();
      const data = await session.messages.sendRequest('ClientJoin', { 'username': username.value, 'password': password.value },
        diffusion.datatypes.json(), diffusion.datatypes.json()
      );

      const response = (<any>data).get();
      if (response.status === 'OK') {
        this.signedIn = true;
        this.chatService.setUsernameAndPassword(username.value, password.value);
      } else {
        alert(response.message);
      }
    } catch (error) {
      console.error(error);
      alert(error);
    }
  }
}
