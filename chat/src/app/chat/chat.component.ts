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
  chatSession: diffusion.Session;
  chatLog: ChatMessage[] = new Array<ChatMessage>();
  chatSpecification: diffusion.TopicSpecification;

  ngOnInit() {
    window.addEventListener('unload', () => {
      if (this.chatSession !== null) {
        this.chatSession.close();
      }
    });

    const connectionResult = diffusion.connect({
      host: 'localhost',
      port: 8080,
      principal: 'control',
      credentials: 'password'
    });

    connectionResult.then((session) => {
      this.chatSession = session;
      this.chatSpecification = new diffusion.topics.TopicSpecification(diffusion.topics.TopicType.TIME_SERIES)
        .withProperty('TIME_SERIES_EVENT_VALUE_TYPE', 'json')
        .withProperty('REMOVAL', 'when subscriptions < 1 for 10m');

      const topicResult = this.chatSession.topics.add(
        'Demos/Chat/Channel',
        this.chatSpecification
      );
      topicResult.then(() => {
        session.addStream('Demos/Chat/Channel', diffusion.datatypes.json())
          .on('value', (topic, specification, newValue, oldValue) => {
            const msg = new ChatMessage(
              newValue.value.get().generatedId,
              newValue.timestamp,
              newValue.value.get().content
            );
            this.chatLog.push(msg);
          });
        this.chatSession.select('Demos/Chat/Channel');
      }, ((error) => { console.log(error); }));
    }, ((error) => { console.log(error); }));
  }

  sendMessage(message: string) {
    const msg = { content: message, generatedId: this.chatSession.sessionID };
    this.chatSession.timeseries.append('Demos/Chat/Channel', msg);
  }
}

class ChatMessage {
  constructor(public id: string, public timestamp: number, public content: string) { }
}
