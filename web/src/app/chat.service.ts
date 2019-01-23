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
  private chatSessionPromise: Promise<diffusion.Session>;
  public username: string;

  public setUsername(username: string) {
    this.username = username;
  }

  public async getSession(): Promise<diffusion.Session> {
    if (!this.chatSessionPromise) {
      this.chatSessionPromise = new Promise(async (resolve, reject) => {
        try {
          // leaving the host out is a short-hand to connect to where the angular project is being served from.
          const chatSession = await diffusion.connect({ host: window.location.host, port: 8080 });
          resolve(chatSession);
        } catch (error) {
          reject(error);
        }
      });
    }
    return this.chatSessionPromise;
  }

  public async signInRequest(username: string, password: string): Promise<Object> {
    return new Promise(async (resolve, reject) => {
      try {
        const session = await this.getSession();
        resolve(session.messages.sendRequest('ClientJoin', { 'username': username, 'password': password },
          diffusion.datatypes.json(), diffusion.datatypes.json()));
      } catch (error) {
        reject(error);
      }
    });
  }
}
