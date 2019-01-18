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

import { Injectable } from '@angular/core';
import * as diffusion from 'diffusion';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private chatSession: diffusion.Session;
  private chatSessionPromise: Promise<diffusion.Session>;
  public username: string;
  private password: string;

  public setUsernameAndPassword(username: string, password: string) {
    this.username = username;
    this.password = password;
  }

  public getSession(): Promise<diffusion.Session> {
    if (!this.chatSessionPromise) {
      this.chatSessionPromise = new Promise((resolve, reject) => {
        diffusion.connect({
          // The host that serves the angular pages is the same as the host which serves diffusion.
          host: window.location.host,
          port: 8080
        }).then((session) => {
          this.chatSession = session;
          resolve(this.chatSession);
        }, (error) => reject(error));
      });
    }
    return this.chatSessionPromise;
  }
}
