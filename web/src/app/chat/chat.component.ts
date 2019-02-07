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

import { Component, OnInit } from '@angular/core';
import { ChatService } from '../chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  chatLog = new Array<ChatMessage>();

  constructor(public chatService: ChatService) { }

  async ngOnInit() {
    try {
      await this.chatService.subscribeTopicUpdates((topic, specification, newValue, oldValue) => {
        const msg = new ChatMessage(
          newValue.value.get().id,
          newValue.timestamp,
          newValue.value.get().content
        );
        this.chatLog.push(msg);
      });
    } catch (error) {
      console.log(error);
    }
  }

  async sendMessage(message: string) {
    try {
      await this.chatService.sendMessage(message);
    } catch (error) {
      console.error(error);
    }
  }
}

class ChatMessage {
  constructor(public id: string, public timestamp: number, public content: string) { }
}
