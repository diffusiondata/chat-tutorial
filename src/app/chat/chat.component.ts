/**
 * Copyright © 2018 Push Technology Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, OnInit } from '@angular/core';
import * as diffusion from 'diffusion';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  private chatSession: diffusion.Session;

  /**
   * Lists the chat messages.
   */
  public chatLog: ChatMessage[] = new Array<ChatMessage>();

  async ngOnInit() {
    window.addEventListener('unload', () => {
      if (this.chatSession) {
        this.chatSession.close();
      }
    });

    try {
      this.chatSession = await diffusion.connect({
        host: 'localhost',
        port: 8080,
        principal: 'control',
        credentials: 'password'
      });
      await this.chatSession.topics.add(
        'Demos/Chat/Channel',
        new diffusion.topics.TopicSpecification(diffusion.topics.TopicType.TIME_SERIES)
          .withProperty('TIME_SERIES_EVENT_VALUE_TYPE', 'json')
          .withProperty('REMOVAL', 'when subscriptions < 1 for 10m')
      );

      this.chatSession.addStream('Demos/Chat/Channel', diffusion.datatypes.json())
        .on('value', (
          topic: string,
          specification: diffusion.TopicSpecification,
          newValue: diffusion.Event,
          oldValue: diffusion.Event
        ) => {
          this.chatLog.push(new ChatMessage(
            newValue.value.get().author,
            newValue.timestamp,
            newValue.value.get().content
          ));
        });
      this.chatSession.select('Demos/Chat/Channel');
    } catch (error) {
      if (error.id && error.message) {
        console.error(`Error: ID ${error.id} - ${error.message} - More: %o`, error);
      } else {
        console.error(error);
      }
    }
  }

  /**
   * Creates a timeseries update with the value that was passed in message.
   * @param message The value that is to be sent.
   */
  sendMessage(message: string) {
    try {
      this.chatSession.timeseries.append('Demos/Chat/Channel', {
        content: message,
        author: this.chatSession.sessionID
      });
    } catch (error) {
      if (error.id && error.message) {
        console.error(`Error: ID ${error.id} - ${error.message} - More: %o`, error);
      } else {
        console.error(error);
      }
    }
  }
}

/**
 * The type to be used for listing the time series updates.
 */
class ChatMessage {
  constructor(public readonly author: string, public readonly timestamp: number, public readonly content: string) { }
}
