/**
 * Copyright © 2019 Push Technology Ltd.
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
  public signedIn: boolean;

  private async getSession(): Promise<diffusion.Session> {
    if (!this.chatSessionPromise) {
      this.chatSessionPromise = new Promise<diffusion.Session>(async (resolve, reject) => {
        try {
          // The host can be left out if it is the same as the web content providers. The same with the port number,
          // where in this case angular ng serve uses the port 4200 by default.
          const chatSession = await diffusion.connect({ port: 8080 });
          resolve(chatSession);
        } catch (error) {
          reject(error);
        }
      });
    }
    return this.chatSessionPromise;
  }

  public async subscribeTopicUpdates(callback: (topic: string, specification: any, newValue: any, oldValue: any) => any)
    : Promise<diffusion.Stream> {
    return new Promise<diffusion.Stream>(async (resolve, reject) => {
      try {
        const session = await this.getSession();
        const stream = session.addStream('Demos/Chat/Channel', diffusion.datatypes.json()).on('value', callback);
        session.select('Demos/Chat/Channel');
        resolve(stream);
      } catch (error) {
        reject(error);
      }
    });
  }

  public async signInRequest(username: string, password: string): Promise<Object> {
    return new Promise<Object>(async (resolve, reject) => {
      try {
        const session = await this.getSession();
        resolve(session.messages.sendRequest('Demos/Chat/Messages/ClientJoin', { 'username': username, 'password': password },
          diffusion.datatypes.json(), diffusion.datatypes.json()));
      } catch (error) {
        reject(error);
      }
    });
  }

  public async sendMessage(message: string): Promise<Object> {
    return new Promise(async (resolve, reject) => {
      try {
        const session = await this.getSession();
        const msg = { content: message, id: this.username };
        resolve(session.timeseries.append('Demos/Chat/Channel', msg));
      } catch (error) {
        reject(error);
      }
    });
  }
}
